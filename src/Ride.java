import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represemts a Ride Attraction in the Theme Park.
 * has concurrency with waiting lines and assigned operators.
 *
 * @author Adam Dodson
 * @version 1
 * @param Ride
 * @param Attraction
 * @param Inspectable
 */

public class Ride extends Attraction implements Inspectable, Maintainable{

    private boolean inService;
    private boolean closedForInspection = false;
    private String lastInspectionOutcome = "No inspection recorded yet";

    private final List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();
    private boolean underMaintenance = false;
    private MaintenanceType pendingMaintenanceType;

    /**
     * Constructs a Ride Attraction.
     *
     * @param name Ride name
     */
    public Ride(String id, String name, int visitorsPerCycle, int maxCapacityPerCycle) {
        super(id, name, visitorsPerCycle, maxCapacityPerCycle);
        this.inService = true;
        this.closedForInspection = false;
        this.lastInspectionOutcome = "No previous Inspections";
    }

    /**
     * Assigns a staff member to operate the attraction
     *
     *
     * @param operator Staff member assigned operators
     */
    public void assignOperator(Staff operator) {
        setOperator(operator);
    }

    public void run() {
        if (!inService || closedForInspection) {
            throw new IllegalStateException("Can not run ride '" + getName()
                    + "': Out of Service, closed for inspection.");
        }
        if (getOperator() == null) {
            throw new IllegalStateException("Cannot run ride '" + getName()
                    + "': No operator assigned.");
        }

        runOneCycle();
    }

    private void runOneCycle() {
        incrementCyclesRan();
    }

    public void Maintenance() {
        if (this.inService == true) {
            this.inService = false;
            this.closedForInspection = true;
            this.lastInspectionOutcome = "UnderGoing Maintenace";
        } else {
            this.inService = true;
            this.closedForInspection = false;
            this.lastInspectionOutcome = "Maintenance complete - Passed inspection";
        }
    }

    // ==========================================
    // --- REQUIRED FOR INSPECTABLE INTERFACE ---
    // ==========================================

        public boolean inspect() {
        if (inService && !closedForInspection) {
            this.lastInspectionOutcome = "Passed inspection";
            return true;
        } else {
            this.lastInspectionOutcome = "Failed inspection - Maintenance required";
            this.closedForInspection = true;
            return false;
        }
    }

    @Override
    public void closeForInspection() {
        this.closedForInspection = true;
        this.inService = false;
    }

    @Override
    public void reopen() {
        this.closedForInspection = false;
        this.inService = true;
        setUnderMaintenance(false);
    }

    @Override
    public String getInspectableName() {
        return getName();
    }

    @Override
    public String getInspectionStatus() {
        return lastInspectionOutcome;
    }

    @Override
    public void recordInspection(String outcome) {
        if (outcome != null && !outcome.isBlank()) {
            this.lastInspectionOutcome = outcome.trim();
        }
    }

    // ==========================================
    //  -- REQUIRED FOR MAINTAINABLE INTERFACE --
    // ==========================================

    @Override
    public String getMaintainableName() {
        return "[Ride = " + getName() + "]"; 
    }

    @Override
    public synchronized void beginMaintenance(MaintenanceType type) {
        Objects.requireNonNull(type, "Maintenance type can not be null.");
        
        if (underMaintenance) {
            throw new IllegalArgumentException(getMaintainableName() + " is already under maintenance.");
            }
            underMaintenance = true;
            pendingMaintenanceType = type;
            System.out.println("[Ride: " + getName() + "] Maintenance STARTED (" + type.getDescription() + ").");
    }

    @Override
    public synchronized List<MaintenanceRecord> getMaintenanceHistory() {
        return new ArrayList<> (maintenanceHistory);
    }

    @Override
    public synchronized void completeMaintenance(String notes, Staff Cleaner, int downtimeMinutes) {
        if (!underMaintenance) {
            throw new IllegalArgumentException(getMaintainableName() + " is not under maintenance nothing to complete.");
        }

        MaintenanceRecord record = new MaintenanceRecord(pendingMaintenanceType, notes, Cleaner, downtimeMinutes);
        maintenanceHistory.add(record);
        underMaintenance = false;
        pendingMaintenanceType = null;
        System.out.println("[Ride " + getId() + "] Maintenance COMPLETED: " + record);
    }

    // ==========================================
    // ------ ADDITIONAL GETTERS & SETTERS ------
    // ==========================================

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    @Override
    public boolean isClosedForInspection() {
        return closedForInspection;
    }

    public String getLastInspectionOutcome() {
        return lastInspectionOutcome;
    }
}
