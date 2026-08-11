
/** *
 *  Abstract Class showing a single person
 *  within the Theme park can be Staff or Visitor
 *
 * @param People
 *
 * @author Adam Dodson
 * @version 1
 */
import java.util.Objects;

public abstract class People {

    private String id;
    private String name;
    private int age;

    /**
     * Constructs a new Persons Record
     *
     * @param id
     * @param name
     * @param age
     */
    public People(String id, String name, int age) {
        if (!id.matches("\\d+")) {
            throw new IllegalArgumentException("ID can only consist of numbers only: " + id);
        }
        this.id = id;
        this.name = name;
        this.age = Math.max(0, age);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name can not be empty.");
        }
    }

    public void setAge(int age) {
        if (age < 0 || age > 100) {
            throw new IllegalArgumentException("Age must be within 0 and 100" + age);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        People people = (People) o;
        return Objects.equals(id, people.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID = " + id + ", Name = " + name + ", Age = " + age;
    }

}
