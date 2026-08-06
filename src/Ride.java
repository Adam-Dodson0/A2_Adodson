/**
 * 
 * Ride
 */

public class Ride extends Attraction {

    private Boolean closedForInspection = false;
    private String lastInspectionOutcome = "No Previous Records";


    public Ride (String id. String name, int visitorsPerCycle) {
        super(id, name, visitorsPerCycle);
    }
}
