
/**
 * Abstract class of a Theme parks Attractions
 * The Base class for Rides, Shows and Facilities classes
 *
 * @author Adam Dodson
 * @version 1
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Attraction {

    private String id;
    private String name;
    private Staff operator;

    private final Queue<Visitor> waitingLine = new LinkedList<>();
    private final List<Visitor> previousVisits = new ArrayList<>();

    private static int parkWideTotalServed = 0;
    private int maxCapacityPerCycle;
    private int visitorsPerCycle;
    private int cyclesRan = 0;
    private boolean underMaintenance = false;

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

    public boolean isUnderMaintenance() {
        return underMaintenance;
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
        this.cyclesRan++;
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

    public List<Visitor> getPreviousVisits() {
        return previousVisits;
    }

    public int getCyclesRan() {
        return cyclesRan;
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

}
