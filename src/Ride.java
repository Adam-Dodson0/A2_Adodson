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

public class Ride extends Attraction implements Inspectable {

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

    @Override
    public void assignOperator(Staff operator) {
        setOperator(operator);
    }

    // ==========================================
    //  ----------- RIDES OWN RULES -------------
    // ------------- (POLYMORPHISM) -------------
    // ==========================================

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
    
    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    @Override
    protected boolean canRunCycle() {
        if (closedForInspection) {
            System.out.println("[" + getName() + "] REFUSED to run: closed for inspection.");
            return false;
        }
        if (getOperator() == null) {
            System.out.println("[" + getName() + "] REFUSED to run: no operator assigned.");
            return false;
        }
        if (getWaitingLineSize() == 0) {
            System.out.println("[" + getName() + "] REFUSED to run: the waiting line is empty.");
            return false;
        }
        return true;
    }

    // ==========================================
    // --- REQUIRED FOR INSPECTABLE INTERFACE ---
    // ==========================================

    @Override
    public String getInspectableName() {
        return getName();
    }

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

    public String getLastInspectionOutcome() {
        return lastInspectionOutcome;
    }

    @Override
    public boolean isClosedForInspection() {
        return closedForInspection;
    }

        @Override
    public synchronized void reopen() {
        this.closedForInspection = false;
        this.inService = true;
        setUnderMaintenance(false);
        System.out.println("[" + getName() + "] REOPENED after inspection.");
    }

    @Override
    public synchronized void closeForInspection() {
        this.closedForInspection = true;
        this.inService = false;
        System.out.println("[" + getName() + "] CLOSED for inspection -- can not be used.");
    }

    @Override
    public synchronized String getInspectionStatus() {
        return getInspectableName() + " is " + (closedForInspection ? "CLOSED (inspection underway)" : "OPEN") + "; last outcome: " + lastInspectionOutcome;
    }

    @Override
    public synchronized  void recordInspection(String outcome) {
        Objects.requireNonNull(outcome, "Inspection outcome can not be null.");
        lastInspectionOutcome = outcome;
        System.out.println("[" + getName() + "] Inspection outcome recorded:" + outcome);
        if (outcome.isEmpty() || !outcome.isBlank()) {
            this.lastInspectionOutcome = outcome.trim();
        }
    }

    // ==========================================
    //  -- REQUIRED FOR MAINTAINABLE INTERFACE --
    // ==========================================

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
}
