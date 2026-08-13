
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

    public Show(String id, String name, int visitorsPerCycle,
            int maxCapacityPerCycle, List<String> showTimes) {
        super(id, name, visitorsPerCycle, maxCapacityPerCycle);
        if (showTimes != null && !showTimes.isEmpty()) {
            showTypeSchedules.put("Default", new ArrayList<>(showTimes));
        }
    }

    @Override
    public void assignOperator(Staff operator) {
        setOperator(operator);
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

    public void addShowTypeSchedule(String showType, List<String> times) {
        if (showType == null || showType.isBlank()) {
            throw new IllegalArgumentException("Show type can not be null or Empty.");
        }
        if (times == null || times.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one showtime.");
        }
        showTypeSchedules.put(showType.trim(), new ArrayList<>(times));
    }

    public Map<String, List<String>> getShowTypeSchedules() {
        return new HashMap<>(showTypeSchedules);
    }

    // Unlike a Ride, an empty waiting line doesn't stop a Show — it
    // still runs to schedule, just serves 0 visitors
    @Override
    protected boolean canRunCycle() {
        if (getOperator() == null) {
            System.out.println("[" + getName() + "] REFUSED to run: no operator assigned.");
            return false;
        }
        if (getWaitingLineSize() == 0) {
            System.out.println("[" + getName() + "] The house is empty, but the show must go on!");
        }
        return true;
    }

    @Override
    public String toString() {
        return "Show{ID='" + getId() + "', Name='" + getName()
                + "', Scheduled show time: '" + showTypeSchedules + "'}";
    }
}
