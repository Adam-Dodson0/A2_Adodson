/**
 * 
 * People
 */

public abstract class People {

private String id;
private String name;
private int age;

public People(String id, String name, int age) {
    setId(id);
    setName(name);
    setAge(age);
}

public String getId() {
    return id;
}

public void setId(String id) {
    if (id == null || !id.matches("\\d+")) {
        throw new IllegalArgumentException ("ID must consist of only numbers: " + id);
    }
}

public String getName() {
    return name;
}

public void setName(String name) {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Name can not be empty.");
    }
}

public int getAge() {
    return age;
}

public void setAge(int age) {
    if (age < 0 || age > 100) {
        throw new IllegalArgumentException("Age must be within 0 and 100" + age);        
    }
}

@Override
public String toString() {
    return "ID = " + id + ", Name = " + name + ", Age = " + age;
}

}
