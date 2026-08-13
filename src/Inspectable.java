
/**
 * Contract for anything in the park that can be closed, inspected, and
 * reopened by Staff — Rides and Toilets implement this.
 *
 * @author Adam Dodson
 * @version 1
 */

public interface Inspectable {

    String getInspectableName();

    void closeForInspection();

    void reopen();

    boolean isClosedForInspection();

    void recordInspection(String outcome);

    String getInspectionStatus();
}
