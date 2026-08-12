
public class MaintenanceType {

    public static final MaintenanceType CLEAN = new MaintenanceType("CLEAN", "Clean up service.");
    public static final MaintenanceType ROUTINE = new MaintenanceType("ROUTINE", "Routine inspection/Service");
    public static final MaintenanceType REPAIR = new MaintenanceType("REPAIR", "Repair of a known fault.");
    public static final MaintenanceType EMERGENCY = new MaintenanceType("EMERGENCY", "Emergency shutdown repair.");
    public static final MaintenanceType SAFETY_UPGRADE = new MaintenanceType("SAFETY_UPGRADE", "Safety upgrade/Replacment");

    private final String code;
    private final String description;

    private MaintenanceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + " (" + description + ")";
    }
}
