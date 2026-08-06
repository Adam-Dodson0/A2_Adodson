
import java.util.HashMap;

/**
 * 
 * ThemePark
 */



public class ThemePark {
    
    private String parkName;
    private Map<String, Attraction> attractions = new HashMap();

    public ThemePark(String parkName) {
        this.parkName = parkName;
    }

    public String getParkName() {
        return parkName;
    }

    public Map<String, Attraction> getAttractions() {
        return new HashMap<>(attractions);
    }
}
