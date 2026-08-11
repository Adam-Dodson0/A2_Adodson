import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 
 * MaintenanceRecord
 */

public final class MaintenanceRecord {

    private final MaintenanceType type;
    private final String notes;
    private final Staff technician;
    private final int downtimeMinutes;
    private final LocalDateTime performedAt;

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
