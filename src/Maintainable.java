import java.util.List;

/**
 * A reusable contract for anything that requires preiodic maintenance in the Theme park
 * rides, shows, toilets with the option to implement to other sections of the Theme park
 * 
 * Maintainable
 */
public interface Maintainable {

    String getMaintainableName();

    void beginMaintenance(MaintenanceType type);

    void completeMaintenance(String notes, Staff technician, int downtimeMinutes);

    boolean isUnderMaintenance();

    List<MaintenanceRecord> getMaintenanceHistory();
}
