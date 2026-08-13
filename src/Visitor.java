
/**
 * A park visitor. Adds a ticket type on top of the common People data.
 * Implements Comparable so visitors can be sorted by age.
 *
 * @author Adam Dodson
 * @version 1
 * @see People
 */
public class Visitor extends People implements Comparable<Visitor> {

    private String ticketType;

    /**
     * Constructs a Visitor with an explicit ticket type.
     *
     * @param id numeric-only unique identifier
     * @param name the visitor's display name
     * @param age the visitor's age
     * @param ticketType ticket category (e.g. VIP, ADULT, CHILD); defaults to
     * "NORMAL" if null/blank
     */
    public Visitor(String id, String name, int age, String ticketType) {
        super(id, name, age);
        this.ticketType = (ticketType == null || ticketType.isBlank()) ? "NORMAL" : ticketType;
    }

    /**
     * Constructs a Visitor with the default "NORMAL" ticket type.
     *
     * @param id numeric-only unique identifier
     * @param name the visitor's display name
     * @param age the visitor's age
     */
    public Visitor(String id, String name, int age) {
        this(id, name, age, "NORMAL");
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
