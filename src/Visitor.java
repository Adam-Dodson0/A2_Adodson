
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

    private String ticketNumber;

    /**
     *
     * @param id
     * @param name
     * @param age
     * @param ticketNumber
     */
    public Visitor(String id, String name, int age, String ticketNumber) {
        super(id, name, age);
        this.ticketNumber = (ticketNumber == null || ticketNumber.isBlank()) ? "Normal" : ticketNumber;
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

    public String getticketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public int compareTo(Visitor other) {
        return Integer.compare(this.getAge(), other.getAge());
    }

    @Override
    public String toString() {
        return "Visitor{ " + super.toString() + ", Ticket = " + getticketNumber() + "}";
    }

}
