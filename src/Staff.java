public class Staff extends People {

private String role;

    public Staff(String id, String name, int age, String role) {
        super(id, name, age);
        setRole(role);
    }

    public Staff(String id, String name, int age) {
        this(id, name, age, "Staff Member");
    }

    public String getrole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.isEmpty()){
            throw new IllegalArgumentException("Staff can not have no role");
        }
        this.role = role;    
    }
    @Override
    public String toString() {
        return "Staff{" + super.toString() + ", Role = " + role + "}";
    }

}