/**
 * Represents the central Theme Park system managing park
 * wide visitor counts and registered attractions
 * 
 * @author Adam Dodson
 * @version 1
 * 
 * @param ThemePark
 */

import java.util.ArrayList;
import java.util.List;

public class ThemePark {
    
    private final String parkName;
    private int totalVisitorCount = 0;
    private final List<Attraction> attractions = new ArrayList<>();

    /**
     * Constructs a ThemePark instance
     * 
     * @param parkName The official name of the theme park
     */
    public ThemePark(String parkName) {
        if (parkName == null || parkName.isBlank()) {
            throw new IllegalArgumentException(" Park name can not be null or blank.");
        }
        this.parkName = parkName.trim();
    }

    
    public synchronized void addAttraction(Attraction attraction) {
        if (attraction == null) {
            throw new IllegalArgumentException("Can not add a null attraction");
        }
        this.attractions.add(attraction);
    }

    /**
     * increase visitor counter using java's intrinsic lock.
    */
    public synchronized void increaseVisitorCount() {
        this.totalVisitorCount++;
    }

    public String getParkName() {
        return parkName;
    }

    /**
     * Retrieve the total count of visitors to the Themepark.
     * 
     * @return Total visitor count
     */
    public synchronized int getTotalVisitorCount() {
        return this.totalVisitorCount;
    }

    /**
     * Returns a copy of all attractions registered
     * in the Theme park.
     * 
     * @return A list copy of attractions
     */
    public List<Attraction> getAttractions() {
        return new ArrayList<>(attractions);
    }

    @Override
    public String toString() {
        return "ThemePark {Name = '" + getParkName() +
            "'. Total Visitors = " + totalVisitorCount +
            ", Attractions = " + attractions.size() + "}";
    }
   
}
