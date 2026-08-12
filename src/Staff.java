/**
 * Represents a Theme Park employee
 *  capable of operating attractions
 * and performing basic safety inspections.
 * 
 * Extends the People superclass and implements
 * role management and inspection workflows.
 *
 * @author Adam Dodson
 * @version 1
 * @param Staff
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
        }
        else {
            setRole(role);
        }
    }

    /**
     * 
     * @param id
     * @param name
     * @param age
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
     * safety inspection on any inspectable object in the park.
     *
     * @param target
     */
    public void performInspection(Inspectable target) {
        if (target == null) {
            throw new IllegalArgumentException(" target for inspection can not be null or empty");
        }

        System.out.println("[STAFF LOG] Staff " + getName() + "started inspecting" + target.getInspectableName());
        
        target.closeForInspection();

        if (target instanceof Ride rideTarget) {
            boolean passed = rideTarget.inspect();

        if (!passed) {
            System.out.println("[STAFF LOG] Inspection failed." + target.getInspectableName() + " Has closed for maintenance.");
            rideTarget.Maintenance();
        } else {
        target.recordInspection("Routine inspection passed");
        target.reopen();
        System.out.println("[STAFF LOG] Staff " + getName() 
                            + "has finished inspection on " 
                            + target.getInspectableName());
            }
        } else {
            target.recordInspection("Routine inspection passed");
            target.reopen();
            System.out.println("[STAFF LOG] Staff " + getName() 
                                + "has finished inspection on " 
                                + target.getInspectableName());
        }
    }

    @Override
    public String toString() {
        return "Staff{" + super.toString() + ", Role = " + role + "}";
    }
}