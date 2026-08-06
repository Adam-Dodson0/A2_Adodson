
/**
 *
 * Attraction
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Collections;

public abstract class Attraction {

    private static int parkWideTotalServed = 0;

    private String id;
    private String name;
    private Queue<Visitor> waitingLine = new LinkedList<>();
    private List<Visitor> previousVisits = new ArrayList<>();
    private Staff operator;
    private int visitorsPerCycle;
    private int cyclesRan = 0;

    protected Atttraction(String id, String name, int visitorsPerCycle) {
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

    public int getCyclesRan() {
        return cyclesRan;
    }

    public Staff getOperator() {
        return operator;
    }

    public static int getParkWideTotalServed() {
        return parkWideTotalServed.get();
    }

    public static void resetParkWideTotal() {
        parkWideTotalServed.set(0);
    }

}
