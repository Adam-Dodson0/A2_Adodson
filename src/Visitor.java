public class Visitor extends People implements Comparable<Visitor>{

private String ticketType;

    public Visitor (String id, String name, int age, String ticketType) {
        super(id, name, age);
        setTicketType(ticketType);
    }

    public Visitor(String id, String name, int age) {
        this(id, name, age, "Normal");
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        if (ticketType == null || ticketType.isEmpty()) {
            throw new IllegalArgumentException("Ticket type is invalid.");
        }
    }

    @Override
    public int compareTo(Visitor other) {
        return Integer.compare(this.getAge(), other.getAge());
    }

    @Override
    public String toString() {
        return "Visitor{ " + super.toString() + ", Ticket = " + ticketType + "}";
    }

}
