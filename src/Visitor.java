
/**
 *  A park visitor, Adds a ticket type on top
 *  of the common Person Data. Implements Comparable to
 * be able to order visitors by age.
 *
 * @author Adam Dodson
 * @version 1
 * @see People
 * @param Visitor
 */
public class Visitor extends People implements Comparable<Visitor> {

    private String ticketType;

    /**
     *
     * @param id
     * @param name
     * @param age
     * @param ticketType
     */
    public Visitor(String id, String name, int age, String ticketType) {
        super(id, name, age);
        this.ticketType = (ticketType == null || ticketType.isBlank()) ? "Normal" : ticketType;
    }

    /**
     *
     * @param id
     * @param name
     * @param age
     */
    public Visitor(String id, String name, int age) {
        this(id, name, age, "Normal");
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public int compareTo(Visitor other) {
        return Integer.compare(this.getAge(), other.getAge());
    }

    @Override
    public String toString() {
        return "Visitor{ " + super.toString() + ", Ticket = " + getTicketType() + "}";
    }

}
