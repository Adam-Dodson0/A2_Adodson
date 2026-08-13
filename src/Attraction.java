
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Abstract base class for every attraction in the Theme Park (Ride, Show).
 * Holds all shared state and behaviour: the waiting line, visit history,
 * assigned operator, and cycle-running logic (template method pattern —
 * {@link #runCycle()} is fixed here, subclasses only supply their own
 * {@link #canRunCycle()} rule).
 */
public abstract class Attraction implements Maintainable {

    private final String id;
    private final String name;
    private Staff operator;

    private final Queue<Visitor> waitingLine = new LinkedList<>();
    private final List<Visitor> visitHistory = new ArrayList<>();

    private static int parkWideTotalServed = 0;
    private int maxCapacityPerCycle;
    private int visitorsPerCycle;
    private int cyclesRun = 0;

    private boolean underMaintenance = false;
    private int cyclesRunAtLastMaintenance = 0;
    private MaintenanceType pendingMaintenanceType;
    private final List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();

    /**
     * Constructs an Attraction. Subclasses call this via super().
     *
     * @param id unique identifier for the attraction
     * @param name display name; can not be null or blank
     * @param visitorsPerCycle how many visitors are served each time
     * {@link #runCycle()} runs; must be at least 1
     * @param maxCapacityPerCycle hard ceiling on visitors per cycle
     * @throws IllegalArgumentException if name is blank or visitorsPerCycle < 1
     */
    protected Attraction(String id, String name, int visitorsPerCycle, int maxCapacityPerCycle) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Attractions can not be nameless");
        }
        if (visitorsPerCycle < 1) {
            throw new IllegalArgumentException("Visitors per cycle must be atleast 1.");
        }
        this.id = id;
        this.name = name;
        this.visitorsPerCycle = visitorsPerCycle;
        this.maxCapacityPerCycle = Math.max(1, maxCapacityPerCycle);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVisitorsPerCycle() {
        return visitorsPerCycle;
    }

    // ==========================================
    //  ----------- VISITOR SERVED  -------------
    // ==========================================
    public static synchronized int getParkWideTotalServed() {
        return parkWideTotalServed;
    }

    public void incrementVisitorsServed(int count) {
        if (count > 0) {
            parkWideTotalServed += count;
        }
    }

    public static void resetParkWideTotal() {
        parkWideTotalServed = 0;
    }

    private static synchronized void addToParkWideTotal(int amount) {
        parkWideTotalServed += amount;
    }

    // ==========================================
    //  --------- OPERATOR MANAGEMENT -----------
    // ==========================================
    public synchronized Staff getOperator() {
        return operator;
    }

    // setOperator is the silent internal setter (used by subclasses' own
    public void setOperator(Staff operator) {
        this.operator = operator;
    }
    // assignOperator overrides); assignOperator is the public, logged entry point
    public synchronized void assignOperator(Staff staff) {
        Objects.requireNonNull(staff, "Can not assing a null operator.");
        this.operator = staff;
        System.out.println("[" + name + "] Operator assigned: " + staff.getName() + "(Staff " + staff.getId() + ")");
    }

    public synchronized void removeOperator() {
        if (operator == null) {
            System.out.println("[" + name + "] No operator to remove.");
            return;
        }
        System.out.println("[" + name + "] Operator removed: " + operator.getName());
        this.operator = null;
    }

    // ==========================================
    //  ----------- THE WAITING LINE ------------
    // ==========================================
    public Queue<Visitor> getWaitingLine() {
        return waitingLine;
    }

    public synchronized void addVisitorToLine(Visitor v) {
        Objects.requireNonNull(v, "Can not add a null visitor to the line");
        waitingLine.offer(v);
        System.out.println("[" + name + "] " + v.getName() + "(ID " + getId() + ") joined the waiting line. Line length: " + waitingLine.size());
    }

    public synchronized Visitor serveNextVisitor() {
        Visitor v = waitingLine.poll();
        if (v == null) {
            System.out.println("[" + name + "] The waiting line is empty -- no visitor to serve.");
        } else {
            System.out.println("[" + name + "] Serving next visitor from the front of the line : " + v.getName() + "(ID " + v.getId() + ")");
        }
        return v;
    }

    public synchronized void printWaitingLine() {
        System.out.println("[" + name + "] Waiting line (" + waitingLine.size() + " visitor(s), front first):");
        if (waitingLine.isEmpty()) {
            System.out.println("    <empty>");
            return;
        }
        int pos = 1;
        for (Visitor v : waitingLine) {
            System.out.println("    " + (pos++) + ". " + v);
        }
    }

    public synchronized int getWaitingLineSize() {
        return waitingLine.size();
    }

    public synchronized List<Visitor> getWaitingLineSnapShot() {
        return new ArrayList<>(waitingLine);
    }

    // ==========================================
    //  -------------VISIT HISTORY---------------
    // ==========================================
    public List<Visitor> getVisitHistory() {
        return visitHistory;
    }

    public synchronized void recordVisitorInHistory(Visitor v) {
        Objects.requireNonNull(v, "Can not record a null visitor.");
        visitHistory.add(v);
        System.out.println("[" + name + "] Recorded in visit history: " + v.getName() + " (ID " + v.getId() + ")");
    }

    public synchronized boolean hasServed(Visitor v) {
        Objects.requireNonNull(v, "Can not check history for a null visitor.");
        boolean found = visitHistory.contains(v);
        System.out.println("[" + name + "] Has visitor "
                + v.getName() + " (ID " + v.getId()
                + ") been served here? " + (found ? "YES" : "NO"));
        return found;
    }

    public int getHistoryCount() {
        System.out.println("[" + name + "] Visit history contains " + visitHistory.size() + " entry/entries.");
        return visitHistory.size();
    }

    public synchronized int getSeatsServed() {
        return visitHistory.size();
    }

    public synchronized List<Visitor> getHistorySnapshot() {
        return new ArrayList<>(visitHistory);
    }

    public synchronized void printHistory() {
        System.out.println("[" + name + "] Visit history (" + visitHistory.size() + " entry/entries, served order):");
        if (visitHistory.isEmpty()) {
            System.out.println("<Empty>");
            return;
        }
        Iterator<Visitor> it = visitHistory.iterator();
        int pos = 1;
        while (it.hasNext()) {
            System.out.println("    " + (pos++) + ". " + it.next());
        }
    }

    public synchronized void printHistoryByAge() {
        System.out.println("[" + name + "] Visit history sorted by AGE (natural order):");
        List<Visitor> sorted = new ArrayList<>(visitHistory);
        Collections.sort(sorted);
        for (Visitor v : sorted) {
            System.out.println("" + v);
        }
    }

    public synchronized void printHistoryByNameThenTicket() {
        System.out.println("[" + name + "] Visit history sorted by Name then Ticket type(Comparator):");
        List<Visitor> sorted = new ArrayList<>(visitHistory);
        sorted.sort((Comparator.comparing(Visitor::getName).thenComparing(Visitor::getTicketType)));
        for (Visitor v : sorted) {
            System.out.println("" + v);
        }
    }

    // ==========================================
    //  ----------- RUNNING A CYCLE -------------
    // ==========================================
    public synchronized int getCyclesRan() {
        return cyclesRun;
    }

    protected abstract boolean canRunCycle();

    public void incrementCyclesRan() {
        this.cyclesRun++;
    }

    public int getMaxCapacityPerCycle() {
        return maxCapacityPerCycle;
    }

    public synchronized void setCyclesRan(int cyclesRun) {
        if (cyclesRun < 0) {
            throw new IllegalArgumentException("Cycle count can not be negative.");
        }
        this.cyclesRun = cyclesRun;
    }

    public synchronized void runCycle() {
        System.out.println("[" + name + "] Attempting to run cycle...");
        if (underMaintenance) {
            System.out.println("[" + name + "] REFUSED to run. currently under maintenance.");
            return;
        }
        if (!canRunCycle()) {
            return;
        }
        int served = 0;
        while (served < visitorsPerCycle && !waitingLine.isEmpty()) {
            Visitor v = waitingLine.poll();
            System.out.println("[" + name + "] Took " + v.getName() + " (ID " + v.getId() + ") from the front of the line.");
            visitHistory.add(v);
            served++;
        }
        // Serve up to visitorsPerCycle visitors, or fewer if the line runs out
        cyclesRun++;
        addToParkWideTotal(served);
        System.out.println("[" + name + "] Cycle #" + cyclesRun + " completed -- served " + served + " visitor(s) this cycle");
    }

    // ==========================================
    //  -- REQUIRED FOR MAINTAINABLE INTERFACE --
    // ==========================================
    @Override
    public String getMaintainableName() {
        return getClass().getSimpleName() + " '" + name + "'(ID " + id + ")";
    }

    @Override
    public synchronized void beginMaintenance(MaintenanceType type) {
        Objects.requireNonNull(type, "Maintenance type can not be null");
        throw new IllegalArgumentException("Not supported yet.");
    }

    @Override
    public synchronized void completeMaintenance(String notes, Staff technician, int downtimeMinutes) {
        if (!underMaintenance) {
            throw new IllegalArgumentException(getMaintainableName() + " is not under maintenance -- nothing to complete");
        }
        MaintenanceRecord record = new MaintenanceRecord(pendingMaintenanceType, notes, technician, downtimeMinutes);
        maintenanceHistory.add(record);
        underMaintenance = false;
        pendingMaintenanceType = null;
        cyclesRunAtLastMaintenance = cyclesRun;
        System.out.println("[" + name + "] Maintenance COMPLETED: " + record);
    }

    @Override
    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    @Override
    public synchronized List<MaintenanceRecord> getMaintenanceHistory() {
        return new ArrayList<>(maintenanceHistory);
    }

    public synchronized int getCyclesSinceLastMaintenance() {
        return cyclesRun - cyclesRunAtLastMaintenance;
    }

    //toString
    @Override
    public synchronized String toString() {
        return getClass().getSimpleName() + "{ID = " + id + ", Name = " + name
                + ", PerCycle = " + visitorsPerCycle + ", CyclesRan = " + cyclesRun
                + ", Waiting = " + waitingLine.size() + ", Served=" + visitHistory.size()
                + ", Operator = " + (operator == null ? "none" : operator.getName())
                + ", Maintenance = " + (underMaintenance ? "IN PROGRESS" : maintenanceHistory.size() + " record(s)") + "}";
    }
}
