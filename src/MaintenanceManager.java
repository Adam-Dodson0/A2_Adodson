import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * MaintenanceManager
 */

public class MaintenanceManager {

    private final List<Maintainable> tracked = new ArrayList<>();

    public synchronized void register(Maintainable item) {
        Objects.requireNonNull(item, "Can not register a null item for maintenance tracking.");
        tracked.add(item);
        System.out.println("[MAINTENANCE] Registered " + item.getMaintainableName() + " for tracking.");
    }

    public synchronized void reportStatus() {
        System.out.println("[MAINTENANCE] status report for " + tracked.size() + "tracked item(s):");
        if (tracked.isEmpty()) {
            System.out.println("  <none registered>");
            return;
        }
    
    for (Maintainable m : tracked) {
        System.out.println("    " + m.getMaintainableName() + " -- "
                    + (m.isUnderMaintenance() ? "UNDER MAINTENANCE" : "operational")
                    + ", " + m.getMaintenanceHistory().size() + " past record(s).");
        }
    }

    public synchronized int getTrackedCount() {
        return tracked.size();
    }

    public synchronized List<Attraction> findOverdue(List<Attraction> attractions, int cycleThreshold) {
        Objects.requireNonNull(attractions, "Attraction list can not be null.");
        List<Attraction> overdue = new ArrayList<>();
        for (Attraction a : attractions) {
            if (a.getCyclesSinceLastMaintenance() >= cycleThreshold) {
                overdue.add(a);
            }
        }
        return overdue;
    }
}
