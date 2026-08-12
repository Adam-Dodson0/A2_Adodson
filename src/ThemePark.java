
/**
 * Represents the central Theme Park system managing park
 * wide visitor counts and registered attractions
 *
 * @author Adam Dodson
 * @version 1
 *
 * @param ThemePark
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ThemePark {

    private final String parkName;
    private int totalVisitorCount = 0;
    private final Map<String, Attraction> attractions = new HashMap<>();

    /**
     * Constructs a ThemePark instance
     *
     * @param parkName The official name of the theme park
     */
    public ThemePark(String parkName) {
        this.parkName = Objects.requireNonNull(parkName, "Park name can not be null.");
    }

    public String getParkName() {
        return parkName;
    }

    public synchronized void registerAtrraction(Attraction a) {
        Objects.requireNonNull(a, "Attraction ID can not be null");
        if (attractions.containsKey(a.getId())) {
            throw new IllegalArgumentException("No attraction is registered with ID '" + a.getId() + "'.");
        }
        attractions.put(a.getId(), a);
        System.out.println("[" + parkName + "] registed " + a.getClass().getSimpleName() + " '" + a.getName() + "' under ID " + a.getId() + ".");
    }

    public synchronized Attraction getAttractionById(String id) {
        Objects.requireNonNull(id, "Attraction ID can not be null.");
        Attraction a = attractions.get(id);
        if (a == null) {
            throw new IllegalArgumentException("No attraction is registered with ID '" + id + "'.");
        }
        System.out.println("[" + parkName + "] lookup for ID " + id + ": found " + a.getClass().getSimpleName() + " '" + a.getName() + "'.");
        return a;
    }

    /**
     * Returns a copy of all attractions registered in the Theme park.
     *
     * @return A list copy of attractions
     */
    public synchronized Map<String, Attraction> getAttractions() {
        return new HashMap<>(attractions);
    }

    public synchronized void reportSeatsServedPerAttraction() {
        System.out.println("[" + parkName + "] Seats served per attraction:");
        if (attractions.isEmpty()) {
            System.out.println("    <no attractions registered>");
            return;
        }
        for (Attraction a : attractions.values()) {
            System.out.println("    " + a.getName() + " (ID " + a.getId() + "): " + a.getSeatsServed() + "seat(s) served over " + a.getCyclesRan() + "cycle(s).");
        }
    }

    public synchronized int reportDistinctVisitors() {
        Set<Visitor> distinct = new HashSet<>();
        for (Attraction a : attractions.values()) {
            distinct.addAll(a.getHistorySnapshot());
        }
        System.out.println("[" + parkName + "] Distinct visitors admitted across the park today: " + distinct.size());
        return distinct.size();
    }

    /**
     * increase visitor counter using java's intrinsic lock.
     */
    public synchronized void increaseVisitorCount() {
        this.totalVisitorCount++;
    }

    /**
     * Retrieve the total count of visitors to the Themepark.
     *
     * @return Total visitor count
     */
    public synchronized int getTotalVisitorCount() {
        return this.totalVisitorCount;
    }

    @Override
    public String toString() {
        return "ThemePark {Name = '" + getParkName()
                + "'. Total Visitors = " + totalVisitorCount
                + ", Attractions = " + attractions.size() + "}";
    }

}
