
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * Toilet
 */
public class Toilet implements Inspectable, Maintainable {

    private final String id;
    private final String location;
    private boolean closedForInspection = false;
    private String lastInspectionOutcome = "No inspection recorded yet";

    private final List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();
    private boolean underMaintenance = false;
    private MaintenanceType pendingMaintenanceType;

    public Toilet(String id, String location) {
        Objects.requireNonNull(id, "Toilet ID can not be Null.");
        Objects.requireNonNull(location, "Location can not be null.");

        if (location.trim().isEmpty()) {
            throw new IllegalArgumentException("location can not be empty.");
        }
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    // ==========================================
    // --- REQUIRED FOR INSPECTABLE INTERFACE ---
    // ==========================================
    @Override
    public String getInspectableName() {
        return "Toilet at " + location + " (ID " + id + ")";
    }

    @Override
    public synchronized void closeForInspection() {
        closedForInspection = true;
        System.out.println("[Toilet " + id + "] CLOSED for inspection -- cannot be used.");
    }

    @Override
    public synchronized void reopen() {
        closedForInspection = false;
        System.out.println("[Toilet " + id + "] REOPENED after inspection");
    }

    @Override
    public synchronized boolean isClosedForInspection() {
        return closedForInspection;
    }

    @Override
    public synchronized void recordInspection(String outcome) {
        Objects.requireNonNull(outcome, "Inspection outcome can not be null.");
        lastInspectionOutcome = outcome;
        System.out.println("[Toilet " + id + "] Inspection recorded: " + outcome);
    }

    @Override
    public synchronized String getInspectionStatus() {
        return getInspectableName() + "is " + (closedForInspection ? "CLOSED (inspection underway)" : "OPEN") + "; last outcome: " + lastInspectionOutcome;
    }

    // ==========================================
    //  -- REQUIRED FOR MAINTAINABLE INTERFACE --
    // ==========================================
    @Override
    public String getMaintainableName() {
        return "Toilet at " + location + " (ID " + id + ")";
    }

    @Override
    public synchronized void beginMaintenance(MaintenanceType type) {
        Objects.requireNonNull(type, "Maintenance type can not be null.");

        if (underMaintenance) {
            throw new IllegalArgumentException(getMaintainableName() + " is already under maintenance.");
        }
        underMaintenance = true;
        pendingMaintenanceType = type;
        System.out.println("[Toilet " + id + "] Maintenance STARTED (" + type.getDescription() + ").");
    }

    @Override
    public synchronized void completeMaintenance(String notes, Staff Cleaner, int downtimeMinutes) {
        if (!underMaintenance) {
            throw new IllegalArgumentException(getMaintainableName() + " is not under maintenance nothing to complete.");
        }

        MaintenanceRecord record = new MaintenanceRecord(pendingMaintenanceType, notes, Cleaner, downtimeMinutes);
        maintenanceHistory.add(record);
        underMaintenance = false;
        pendingMaintenanceType = null;
        System.out.println("[Toilet " + id + "] Maintenance COMPLETED: " + record);
    }

    @Override
    public synchronized boolean isUnderMaintenance() {
        return underMaintenance;
    }

    @Override
    public synchronized List<MaintenanceRecord> getMaintenanceHistory() {
        return new ArrayList<>(maintenanceHistory);
    }

    @Override
    public String toString() {
        return "Toilet{ID=" + id + ", location= " + location + "}";
    }
}
