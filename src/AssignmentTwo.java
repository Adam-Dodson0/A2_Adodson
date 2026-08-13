
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * PROG2004 Assessment 2 -- Theme Park Management System. The main method
 * demonstrates each part of the system independently, including proper
 * checked/unchecked exception handling and the maintenance subsystem.
 *
 * @author Adam Dodson
 * @version 1
 */
public class AssignmentTwo {

    public static void main(String[] args) throws InterruptedException {

        // =======================================
        // PART 1 Modelling the Theme Parks People 
        // =======================================
        //Staff and Visitors to the Theme park
        Staff staffA = new Staff("15", "Tim Berners Lee", 51, "Ride Operator");
        Staff staffB = new Staff("245", "Alan Turing", 32, "Maintenance");
        Staff staffC = new Staff("193", "Ada Lovelace", 28, "Cleaner");
        Staff staffD = new Staff("562", "Margaret Hamilton", 41, "Manager");
        Staff staffE = new Staff("349", "Edsger Dijkstra", 39, "Handler");
        Staff staffF = new Staff("85", "Robert Kahn", 21);           // no role Defaults to staff member
        Visitor visA = new Visitor("7", "Linus Torvalds", 25, "VIP");
        Visitor visB = new Visitor("284", "Ken Thompson", 31, "ADULT");
        Visitor visC = new Visitor("916", "Grace Hooper", 19, "STUDENT");
        Visitor visD = new Visitor("405", "Vint Cerf", 45, "CONCESSION");
        Visitor visE = new Visitor("351", "James Gosling", 36);      //no ticketType Defaults to "Normal Ticket"

        System.out.println(staffA);
        System.out.println(staffB);
        System.out.println(staffC);
        System.out.println(staffD);
        System.out.println(staffE);
        System.out.println(staffF);
        System.out.println(visA);
        System.out.println(visB);
        System.out.println(visC);
        System.out.println(visD);
        System.out.println(visE);

        // Visitors ordered by age
        List<Visitor> byAge = new ArrayList<>(List.of(visA, visB, visC));
        Collections.sort(byAge);
        System.out.println("Visitor(s) ordered by age (comparable natural order:");
        for (Visitor v : byAge) {
            System.out.println("    " + v);
        }

        // =============================================
        // PART 2 Modelling the Parks People Attractions
        // =============================================
        Ride coaster = new Ride("R728", "Thunder Coaster", 23, 47);
        Show animal = new Show("S391", "Animal Feeding", 27, 83, List.of("10:15", "13:40", "15:25"));
        Toilet blockA = new Toilet("TB614", "Near the main entrance");

        System.out.println(coaster);
        System.out.println(animal);
        System.out.println(blockA);

        // Assign and remove an operator
        coaster.assignOperator(staffA);
        animal.assignOperator(staffE);
        animal.removeOperator();
        animal.assignOperator(staffE);

        staffA.performInspection(coaster, "All restraints and brakes PASSED safety check.");
        staffC.performInspection(blockA, "Cleaned and fully stocked -- PASSED");

        // ==================================
        // PART 3 The Attractions Waitingline
        // ==================================
        coaster.addVisitorToLine(visD);
        coaster.addVisitorToLine(visC);
        coaster.addVisitorToLine(visA);
        Visitor visF = new Visitor("601", "Dennis Richie", 43, "ADULT");
        Visitor visG = new Visitor("602", "Bjarne Stroustrup Richie", 8, "CHILD");
        coaster.addVisitorToLine(visE);
        coaster.addVisitorToLine(visB);
        System.out.println(visF);
        System.out.println(visG);

        coaster.printWaitingLine();
        coaster.serveNextVisitor();            //removes the visitor 
        coaster.printWaitingLine();

        // ========================
        // PART 4 The visit History
        // ========================
        animal.recordVisitorInHistory(visA);
        animal.recordVisitorInHistory(visC);
        animal.recordVisitorInHistory(visF);
        animal.recordVisitorInHistory(visE);
        animal.recordVisitorInHistory(visD);
        animal.recordVisitorInHistory(visG);

        animal.hasServed(visD);
        animal.hasServed(visF);
        animal.getHistoryCount();
        animal.printHistory();
        animal.printHistoryByAge();
        animal.printHistoryByNameThenTicket();

        // ==============================
        // PART 5 Operating an attraction
        // ==============================
        System.out.println("---  Ride runs successfully (2 visitors per cycle) ---");
        System.out.println("[Thunder Coaster] Cycles run so far: " + coaster.getCyclesRan());
        coaster.printWaitingLine();
        coaster.runCycle();
        coaster.printWaitingLine();
        coaster.printHistory();
        System.out.println("[Thunder Coaster] Cycles run so far: " + coaster.getCyclesRan());

        System.out.println("--- Ride refuses: closed for inspected ---");
        coaster.closeForInspection();
        coaster.runCycle();
        coaster.reopen();

        System.out.println("--- Ride refuses: empty queue ---");
        coaster.runCycle();
        coaster.runCycle();

        System.out.println("--- Feeding goes ahead to an empty house ---");
        System.out.println("[Animal Feeding Show] Cycles run so far: " + animal.getCyclesRan());
        animal.printWaitingLine();
        animal.runCycle();
        System.out.println("[Animal Feeding Show] Cycles run so far: " + animal.getCyclesRan());

        System.out.println("--- Show with waiting visitors ---");
        animal.addVisitorToLine(visB);
        animal.addVisitorToLine(visE);
        animal.runCycle();
        animal.printHistory();

        // ========================
        // PART 6 Managing the park
        // ========================
        ThemePark park = new ThemePark("Gold Coast Adventure World");
        Ride ferris = new Ride("18", "Giant ferris Wheel", 9, 12);
        ferris.assignOperator(staffB);
        ferris.addVisitorToLine(visA);
        ferris.addVisitorToLine(visE);
        ferris.runCycle();

        park.registerAtrraction(coaster);
        park.registerAtrraction(animal);
        park.registerAtrraction(ferris);

        park.reportSeatsServedPerAttraction();
        park.reportDistinctVisitors();

        // ========================================
        // PART 7 Backing up and restoring the park
        // ========================================
        coaster.addVisitorToLine(new Visitor("421584", "Ivan Sutherland", 16, "NORMAL"));
        coaster.addVisitorToLine(new Visitor("841358", "John von Neumann", 48, "VIP"));

        String backUpFile = "park_backup.txt";
        ThemeParkBackup.saveParkToFile(park, backUpFile);

        System.out.println("--- Restoring into a fresh park ---");
        ThemePark restored = ThemeParkBackup.loadParkFromFile("Gold Coast Adventure world (restored)", backUpFile);

        System.out.println(" --- Verifying the restored park matches the original park ---");
        for (Attraction original : park.getAttractions().values()) {
            Attraction copy = restored.getAttractions().get(original.getId());
            boolean match = copy != null && copy.getName().equals(original.getName())
                    && copy.getCyclesRan() == original.getCyclesRan()
                    && copy.getWaitingLineSize() == original.getWaitingLineSize()
                    && copy.getSeatsServed() == original.getSeatsServed()
                    && ((copy.getOperator() == null) == (original.getOperator() == null));
            System.out.println("   " + original.getName() + ": " + (match ? "MATCH (OK)" : "MISMATCH (!!)"));
            if (copy != null) {
                copy.printWaitingLine();
                copy.printHistory();
            }
        }

        System.out.println("--- Attempting to restore from a missing file ---");
        ThemeParkBackup.loadParkFromFile("Ghost Park", "No_File_Exists.txt");

        System.out.println("--- Attempting to restore a corrupted file ---");
        try (PrintWriter out = new PrintWriter("corrupted_backup.txt")) {
            out.println("ATTRACTION, Ride, 684 Splash falls, 3, 0");
            out.println("WAITING,684, garbage-line-not-enough-fields");
            out.println("BANANA, this,is,not,a,record");
            out.println("WAITING, 684, 2010, Marcus Ranum, 26, NORMAL");
        } catch (FileNotFoundException e) {
            System.out.println("Could not create corrupted test file: " + e.getMessage());
        }
        ThemePark partly = ThemeParkBackup.loadParkFromFile("Partly Restored Park", "corrupted_backup.txt");
        if (partly != null) {
            partly.reportSeatsServedPerAttraction();
        }

        // ==================
        // PART 8 Maintenance
        // ==================
        MaintenanceManager maintenanceManager = new MaintenanceManager();
        maintenanceManager.register(coaster);
        maintenanceManager.register(ferris);
        maintenanceManager.register(blockA);
        maintenanceManager.reportStatus();

        System.out.println("--- A ride is serviced from start to finish ---");
        try {
            staffA.performMaintenance(coaster, MaintenanceType.ROUTINE, "Lubricated track, tightened bolts", 20);
        } catch (Exception e) {
            System.out.println("Maintenance failed: " + e.getMessage());
        }

        // ===================================
        // PART 9  Exception Handling Showcase
        // ===================================
        System.out.println("--- Catching a nullPointerException ---");
        try {
            coaster.addVisitorToLine(null);
        } catch (NullPointerException e) {
            System.out.println("Caught expected NullPointerException: " + e.getMessage());
        }

        System.out.println("--- Catching an IllegalArgumentException ---");
        try {
            new Visitor("Not-a-number", "Bad ID Visitor", 30);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected IllegalArgumentException" + e.getMessage());
        }

        System.out.println("--- Catching an IOException error while backing up to an invalid path ---");
        boolean saved = ThemeParkBackup.saveParkToFile(park, "/no/such/directory/park_backup.txt");
        System.out.println("Save to an invalid path succeeded? " + saved);

        // ======================================
        // PART 10 Running the park (concurrency)
        // ======================================
        Attraction.resetParkWideTotal();

        Ride rapids = new Ride("925", "River Rapids", 6, 10);
        Show parade = new Show("645", "Park Parade", 200, 500, List.of("09:30", "13:45", "16:30"));

        rapids.assignOperator(staffA);
        parade.assignOperator(staffD);
        for (int i = 1; i <= 6; i++) {
            rapids.addVisitorToLine(new Visitor("70" + String.format("%02d", i), "RapidsGuest" + i, 18 + i));
            parade.addVisitorToLine(new Visitor("71" + String.format("%02d", i), "ParadeGuest" + i, 20 + i));
        }

        ExecutorService pool = Executors.newFixedThreadPool(2);
        Runnable runRapids = () -> {
            for (int i = 0; i < 10; i++) {
                rapids.runCycle();
            }
        };
        Runnable runParade = () -> {
            for (int i = 0; i < 3; i++) {
                parade.runCycle();
            }
        };
        pool.submit(runRapids);
        pool.submit(runParade);

        pool.shutdown();
        if (pool.awaitTermination(30, TimeUnit.SECONDS)) {
            System.out.println("All attractions have finished running!");
        } else {
            System.out.println("Timed out waiting for attractions to finish");
        }

        System.out.println("FINAL PARK-WIDE TOTAL of visitors served: " + Attraction.getParkWideTotalServed());

        System.out.println();
        System.out.println("--- End of demonstrating all parts ---");

    }
}
