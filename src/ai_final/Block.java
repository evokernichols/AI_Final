package ai_final;

/**
 * This class defines a block, which, for this problem, only has a name
 * @author Ryan Nichols
 */
public class Block {
    String name = "";

    public Block() {
    }

    public Block(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
    
    public boolean equals(Block other) {
        return name.equals(other.getName());
    }
    
    public String toString() {
        return name;
    }
}
