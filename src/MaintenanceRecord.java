import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An imutable record of a single completed maintenance event on a
 * Maintainable attraction.
 * Every field is private and final, and there are no setters,
 *  so once created a record can never be altered - a permanent log entry
 * just like a physical maintenance logbook
 */

public final class MaintenanceRecord {

    private final MaintenanceType type;
    private final String notes;
    private final Staff technician;
    private final int downtimeMinutes;
    private final LocalDateTime performedAt;

    /**
     * Creates a new maintenance record, that is timestamped at the moment of creation
     * 
     * @param type the kind of maintenance performed.
     * @param notes text description of the maintenance completed.
     * @param technician the staff member who carried out the work.
     * @param downtimeMinutes how long the maintainable item was out of service, displayed in minutes.
     * 
     * @throws NullPointerException if Type, notes or technician is null.
     * @throws IllegalArgumentException if notes is blank or downtimeMinutes is negative
     */
    public MaintenanceRecord(MaintenanceType type, String notes, Staff technician, int downtimeMinutes) {
        this.type = Objects.requireNonNull(type, "Maintenance type can not be null.");
        this.technician = Objects.requireNonNull(technician, "technician can not be null.");
        Objects.requireNonNull(notes, "Maintenance notes can not be null.");
        
        if (notes.trim().isEmpty()) {
            throw new IllegalArgumentException("Maintenance notes can not be empty.");
        }
        if (downtimeMinutes < 0) {
            throw new IllegalArgumentException("Downtime minutes can not be negative: " + downtimeMinutes);
        }
        this.notes = notes.trim();
        this.downtimeMinutes = downtimeMinutes;
        this.performedAt = LocalDateTime.now();
    }

    public MaintenanceType getType() {
        return type;
    }

    public String getNotes() {
        return notes;
    }

    public Staff getTechnician() {
        return technician;
    }

    public int getDowntimeMinutes() {
        return downtimeMinutes;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    @Override
    public String toString() {
        return "[ " + type.getDescription() + " ] by " + technician.getName() + "--" + notes + " (" + downtimeMinutes + " min downtime)";
    }
}
