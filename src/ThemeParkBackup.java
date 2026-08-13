
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class ThemeParkBackup {

    private static final String SEP = ",";

    /**
     * Utility class: never instantiated
     */
    private ThemeParkBackup() {
    }

    /**
     *
     * @param park
     * @param filename
     * @return
     */
    public static boolean saveParkToFile(ThemePark park, String filename) {
        Objects.requireNonNull(park, "Theme park can not be null.");
        Objects.requireNonNull(filename, "Filename can not be null.");
        System.out.println("[BACKUP] Saving park '" + park.getParkName() + "' to " + filename + " ...");

        try (PrintWriter out = new PrintWriter(filename)) {
            for (Attraction a : park.getAttractions().values()) {
                //ATTRACTION, <type>,<id>,<name>,<perCycle>,<cyclesRun>
                out.println(String.join(SEP, "ATTRACTION",
                        a.getClass().getSimpleName(), a.getId(), a.getName(),
                        String.valueOf(a.getVisitorsPerCycle()),
                        String.valueOf(a.getCyclesRan())));

                Staff op = a.getOperator();
                if (op != null) {
                    //OPERATOR, <attractionId>, <staffId>, <name>, <age>, <role>
                    out.println(String.join(SEP, "OPERATOR", a.getId(), op.getId(),
                            op.getName(), String.valueOf(op.getAge()), op.getRole()));
                }

                for (Visitor v : a.getWaitingLine()) {
                    //WAITING, <attractionId>, <visitorId>, <name>, <age>, <ticket>
                    out.println(String.join(SEP, "WAITING", a.getId(), v.getId(), v.getName(),
                            String.valueOf(v.getAge()), v.getTicketType()));
                }

                for (Visitor v : a.getVisitHistory()) {
                    //SERVED, <attractionId>,<visitorId>,<name>,<age>,<ticket>
                    out.println(String.join(SEP, "SERVED", a.getId(), v.getId(), v.getName(),
                            String.valueOf(v.getAge()), v.getTicketType()));
                }

                for (MaintenanceRecord m : a.getMaintenanceHistory()) {
                    //MAINTENANCE,<attractionId>,<type>,<staffId>,<staffName>,<staffAge>,<staffRole>,<downtime>,<notes>
                    Staff tech = m.getTechnician();
                    out.println(String.join(SEP, "MAINTENANCE", a.getId(), m.getType().getCode(),
                            tech.getId(), tech.getName(),
                            String.valueOf(tech.getAge()), tech.getRole(),
                            String.valueOf(m.getDowntimeMinutes()), m.getNotes()));
                }
            }

            System.out.println("[BACKUP] Save completed successfully: " + filename);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[BACKUP] FAILED to save -- the file could ne be created or opened: " + e.getMessage());
            return false;
        }
    }

    public static ThemePark loadParkFromFile(String parkName, String filename) {
        Objects.requireNonNull(parkName, "Park name can not be null.");
        Objects.requireNonNull(filename, "Filename can not be null.");
        System.out.println("[RESTORE] Loading park from " + filename + "...");

        ThemePark park = new ThemePark(parkName);
        int lineNo = 0, bad = 0;

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = in.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    parseLine(park, line, lineNo);
                } catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException e) {
                    bad++;
                    System.out.println("[RESTORE] Line " + lineNo + " skipped (malformed): " + e.getMessage());
                }
            }

            System.out.println("[RESTORE] load completed: " + lineNo + " line(s) read, "
                    + bad + "malformed line(s) skipped.");
            return park;

        } catch (FileNotFoundException e) {
            System.out.println("[RESTORE] FAILED -- the backup file does not exist: " + filename);
            return null;
        } catch (IOException e) {
            System.out.println("[RESTORE] FAILED -- the backup file could not be read: " + e.getMessage());
            return null;
        }
    }

    private static void parseLine(ThemePark park, String line, int lineNo) {
        String[] parts = line.split(SEP, -1);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Insufficient fields on line " + lineNo);
        }

        String recordType = parts[0].trim();

        switch (recordType) {
            case "ATTRACTION" -> {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Malformed ATTRACTION record");
                }
                String type = parts[1];
                String id = parts[2];
                String name = parts[3];
                int perCycle = Integer.parseInt(parts[4]);
                int cyclesRun = Integer.parseInt(parts[5]);

                Attraction attraction;
                if ("Ride".equalsIgnoreCase(type)) {
                    attraction = new Ride(id, name, perCycle, 0);
                } else if ("Show".equalsIgnoreCase(type)) {
                    attraction = new Show(id, name, perCycle, 0, new ArrayList<>());
                } else {
                    throw new IllegalArgumentException("Unknown attraction type: " + type);
                }
                attraction.setCyclesRan(cyclesRun);
                park.registerAtrraction(attraction);
                break;
            }

            case "OPERATOR" -> {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Malformed OPERATOR record");
                }
                String opAttrId = parts[1];
                Staff operator = new Staff(parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);

                Attraction opTarget = requireAttraction(park, opAttrId, lineNo);
                if (opTarget != null) {
                    opTarget.setOperator(operator);
                }
                break;
            }

            case "WAITING" -> {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Malformed WAITING record");
                }
                String waitAttrId = parts[1];
                Visitor waitingVisitor = new Visitor(parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);

                Attraction waitTarget = requireAttraction(park, waitAttrId, lineNo);
                if (waitTarget != null) {
                    waitTarget.getWaitingLine().add(waitingVisitor);
                }
                break;
            }

            case "SERVED" -> {
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Malformed SERVED record");
                }
                String servedAttrId = parts[1];
                Visitor servedVisitor = new Visitor(parts[2], parts[3], Integer.parseInt(parts[4]), parts[5]);

                Attraction servedTarget = requireAttraction(park, servedAttrId, lineNo);
                if (servedTarget != null) {
                    servedTarget.getVisitHistory().add(servedVisitor);
                }
                break;
            }

            case "MAINTENANCE" -> {
                if (parts.length < 9) {
                    throw new IllegalArgumentException("Malformed MAINTENANCE record");
                }
                String maintAttrId = parts[1];
                String maintCode = parts[2].trim();

                MaintenanceType maintType = switch (maintCode.toUpperCase()) {
                    case "CLEAN" ->
                        MaintenanceType.CLEAN;
                    case "ROUTINE" ->
                        MaintenanceType.ROUTINE;
                    case "REPAIR" ->
                        MaintenanceType.REPAIR;
                    case "EMERGENCY" ->
                        MaintenanceType.EMERGENCY;
                    case "SAFETY_UPGRADE" ->
                        MaintenanceType.SAFETY_UPGRADE;
                    default ->
                        throw new IllegalArgumentException("Unknown maintenance type code: " + maintCode);
                };

                Staff tech = new Staff(parts[3], parts[4], Integer.parseInt(parts[5]), parts[6]);
                int downtime = Integer.parseInt(parts[7]);
                String notes = parts[8];

                MaintenanceRecord record = new MaintenanceRecord(maintType, notes, tech, downtime);
                Attraction maintTarget = requireAttraction(park, maintAttrId, lineNo);
                if (maintTarget != null) {
                    maintTarget.getMaintenanceHistory().add(record);
                }
                break;
            }

            default ->
                throw new IllegalArgumentException("Unknown record type:" + recordType);
        }

    }

    private static Attraction requireAttraction(ThemePark park, String id, int lineNo) {
        Attraction a = park.getAttractions().get(id);
        if (a == null) {
            throw new IllegalArgumentException("Line " + lineNo + ", references unknown attraction ID:" + id);
        }
        return a;
    }
}
