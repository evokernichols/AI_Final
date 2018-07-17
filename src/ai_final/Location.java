package ai_final;

/**
 * This class defines a location, which ,for this problem only, has a name
 * @author Ryan Nichols
 */
public class Location {
    String name = "";

    public Location() {
    }
    

    public Location(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
    public boolean equals(Location other) {
        return name.equals(other.getName());
    }
    
    public String toString() {
        return name;
    }
}
