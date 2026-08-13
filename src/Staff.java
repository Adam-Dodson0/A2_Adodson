
import java.util.Objects;

/**
 * Represents a Theme Park employee capable of operating attractions and
 * performing basic safety inspections.
 *
 * Extends the People superclass and implements role management and inspection
 * workflows.
 *
 * @author Adam Dodson
 * @version 1
 */
public class Staff extends People {

    private String role;

    /**
     * Constructs a Staff member with an explicit role.
     *
     * @param id
     * @param name
     * @param age
     * @param role
     */
    public Staff(String id, String name, int age, String role) {
        super(id, name, age);
        if (role == null || role.isBlank()) {
            this.role = "General Staff";
        } else {
            setRole(role);
        }
    }

    /**
     * Constructs a Staff member with the default role of "Staff Member".
     *
     * @param id numeric-only unique identifier
     * @param name the staff member's display name
     * @param age the staff member's age
     */
    public Staff(String id, String name, int age) {
        this(id, name, age, "Staff Member");
    }

    public String getRole() {
        return role;
    }

    public final void setRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Staff can not have no role");
        }
        this.role = role.trim();
    }

    /**
     * Runs a safety inspection on any Inspectable target, closing it first and
     * reopening it if the inspection passes.
     *
     * @param target the item being inspected (Ride, Toilet)
     * @param notes inspection notes to record if it passes; falls back to a
     * default message if blank
     * @throws IllegalArgumentException if target is null
     */
    public void performInspection(Inspectable target, String notes) {
        if (target == null) {
            throw new IllegalArgumentException(" target for inspection can not be null or empty");
        }

        System.out.println("[STAFF LOG] Staff " + getName() + " started inspecting " + target.getInspectableName());

        target.closeForInspection();

        boolean passed;
        if (target instanceof Ride rideTarget) {
            passed = rideTarget.inspect();
        } else {
            passed = true;
        }

        if (!passed) {
            System.out.println("[STAFF LOG] Inspection failed. " + target.getInspectableName() + " Has closed for maintenance.");
            if (target instanceof Ride rideTarget) {
                rideTarget.maintenance();
            }
        } else {
            String recordNotes;
            if (notes != null && !notes.isBlank()) {
                recordNotes = notes;
            } else {
                recordNotes = "Routine inspection passed";
            }

            target.recordInspection(recordNotes);
            target.reopen();
            System.out.println("[STAFF LOG] Staff " + getName() + " has finished inspection on " + target.getInspectableName());
        }
    }

    public void performInspection(Inspectable target) {
        performInspection(target, "Routine inpsection passed");
    }

    public void performMaintenance(Maintainable item, MaintenanceType type, String notes, int downtimeMinutes) {
        Objects.requireNonNull(item, "Can not perform maintenance on a null item.");
        System.out.println("[MAINTENANCE] " + getName() + " (Staff " + getId() + ") begins servicing: " + item.getMaintainableName());
        item.beginMaintenance(type);
        item.completeMaintenance(notes, this, downtimeMinutes);
    }

    @Override
    public String toString() {
        return "Staff {" + super.toString() + ", Role = " + role + "}";
    }
}
