/**
 * A Show Attraction within the Theme Park
 * Show Attraction is different to the Ride Attraction Class
 * 
 * @author Adam Dodson
 * @version 1
 * @param Show
 * @see Attraction
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show extends Attraction {
    
    private final Map<String, List<String>> showTypeSchedules = new HashMap<>();

    public Show (String id, String name, int visitorsPerCycle,
         int maxCapacityPerCycle, List<String>showTimes) {
        super(id, name, visitorsPerCycle, maxCapacityPerCycle);
    }

    public void assignOperator(Staff operator) {
        setOperator(operator);
    }

    public void addShowTypeSchedule(String showType, List<String> times) {
        if (showType == null || showType.isBlank()) {
            throw new IllegalArgumentException("Show type can not be null or Empty.");
        }
        if (times == null || times.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one showtime.");
        }
        showTypeSchedules.put(showType.trim(), new ArrayList<>(times));
    }

    public void runShow(String showType, String time) {
        if (isUnderMaintenance()) {
            throw new IllegalStateException("Can not start show '" + getName() + "': is Currently under Maintenance");
        }
        if (getOperator() == null) {
            throw new IllegalStateException("Can not start show '" + getName() + "': No staff assigned to show.");
        }

        incrementCyclesRan();
        System.out.println("[SHOW LOG] Show: '" + getName() + "' starts at " + time + "!");
    }

    public Map<String, List<String>> getShowTypeSchedules() {
        return new HashMap<>(showTypeSchedules);
    }

    @Override
    public String toString() {
        return "Show{ID='" + getId() + "', Name='" + getName()
        + "', Scheduled show time: '" + showTypeSchedules + "'}";
    }
}
