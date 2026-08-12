import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Abstract class of a Theme parks Attractions
 * ABSTRACT base class for every attraction in the park. All shared state and
 * behaviour lives here, written once:
 *   - unique numeric identifier + name
 *   - the waiting line   (Queue/LinkedList -- strict FIFO, unbounded)
 *   - the visit history  (ArrayList -- ordered, growable, duplicates allowed)
 *   - the operator       (association with Staff)
 *   - visitors served per cycle + cycles run
 *   - runCycle()         (TEMPLATE METHOD -- shared machinery here, each
 *                         subclass supplies its own canRunCycle() rule)
 *   - the maintenance workflow (implements {@link Maintainable})
 *  * Being abstract stops anyone creating a "generic attraction" that is none
 * of the specific kinds (ABSTRACTION). Every field stays private, with
 * validated access only through methods (ENCAPSULATION). Concrete
 * subclasses supply their own {@link #canRunCycle()} rule (POLYMORPHISM),
 * and every attraction automatically INHERITS every method below.
 */

public abstract class Attraction implements Maintainable {

    private String id;
    private String name;
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
     * 
     * @param id
     * @param name
     * @param visitorsPerCycle
     */
    protected Attraction(String id, String name, int visitorsPerCycle, int maxCapacityPerCycle) {
        if (id == null || !id.matches("\\d+")) {
            throw new IllegalArgumentException("Attraction ID can only be numeric: " + id);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Attractions can not be nameless");
        }
        if (visitorsPerCycle < 1) {
            throw new IllegalArgumentException("Visitors per cycle must be atleast 1.");
        }
        this.id = id;
        this.name = name;
        this.visitorsPerCycle = visitorsPerCycle;
        this.maxCapacityPerCycle = Math.max (1, maxCapacityPerCycle);
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public void incrementVisitorsServed(int count) {
        if (count > 0) {
            parkWideTotalServed += count;
        }
    }

    public void incrementCyclesRan() {
        this.cyclesRun++;
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

    public int getMaxCapacityPerCycle() {
        return maxCapacityPerCycle;
    }

    public Queue<Visitor> getWaitingLine() {
        return waitingLine;
    }

    public List<Visitor> getVisitHistory() {
        return visitHistory;
    }

    public int getCyclesRan() {
        return cyclesRun;
    }

    public synchronized void setCyclesRan(int cyclesRun) {
        if (cyclesRun < 0) 
            throw new IllegalArgumentException("Cycle count can not be negative.");
                this.cyclesRun = cyclesRun;
    }

    public Staff getOperator() {
        return operator;
    }

    public void setOperator(Staff  operator) {
        this.operator = operator;
    }

    public static int getParkWideTotalServed() {
        return parkWideTotalServed;
    }

    public static void resetParkWideTotal() {
        parkWideTotalServed = 0;
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
    public boolean isUnderMaintenance() {
        return underMaintenance;
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
    public synchronized List<MaintenanceRecord> getMaintenanceHistory() { 
        return new ArrayList<>(maintenanceHistory);
    }

    public synchronized int getCyclesSinceLastMaintenance() {
        return cyclesRun - cyclesRunAtLastMaintenance;
    }

    @Override
    public synchronized String toString() {
        return getClass().getSimpleName() + "{ID = " + id + ", Name = " + name
                + ", PerCycle = " + visitorsPerCycle + ", CyclesRan = " + cyclesRun
                + ", Waiting = " + waitingLine.size() + ", Served=" + visitHistory.size()
                + ", Operator = " + (operator == null ? "none" : operator.getName())
                + ", Maintenance = " + (underMaintenance ? "IN PROGRESS" : maintenanceHistory.size() + " record(s)") + "}";
    }
}
