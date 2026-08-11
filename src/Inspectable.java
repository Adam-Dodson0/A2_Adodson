public interface Inspectable {

    String getInspectableName();

    void closeForInspection();

    void reopen();

    boolean isClosedForInspection();

    void recordInspection(String outcome);

    String getInspectionStatus();
}
