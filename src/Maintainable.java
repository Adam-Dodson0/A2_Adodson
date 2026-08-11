import java.util.List;

/**
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
