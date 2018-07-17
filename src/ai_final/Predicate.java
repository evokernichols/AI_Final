package ai_final;

/**
 * This class defines a predicate, which consists of a name and various 
 * arguments, depending on the type.
 * @author Ryan Nichols
 */
public class Predicate {
    //Fields   
    String name;
    Block argument1;
    Block argument2;
    Location location;
    
    //String constants
    final public static String ON = "ON";
    final public static String ONTABLE = "ONTABLE";
    final public static String CLEAR = "CLEAR";
    final public static String CLEARLOC = "CLEARLOC";
    final public static String HOLDING = "HOLDING";
    
    

    /*****************************************************
     *                   Constructors                    *
     *****************************************************/
    /**
     * 
     * @param name 
     */
    public Predicate(String name) {
        this.name = name;
        this.argument1 = null;
        this.argument2 = null;
        this.location = null;
    }    
    
    /**
     * Used for Holding(block), Clear(block)
     * @param name
     * @param argument1 
     */
    public Predicate(String name, Block argument1) {
        this.name = name;
        this.argument1 = argument1;
        this.argument2 = null;
        this.location = null;
    }  

    /**
     * Only used for Clear(loc)
     * @param name
     * @param location 
     */
    public Predicate(String name, Location location) {
        this.name = name;
        this.argument1 = null;
        this.argument2 = null;
        this.location = location;
    }    

    /**
     * Used for pickup(block, loc), putdown(block, loc)
     * @param name
     * @param argument1
     * @param location 
     */
    public Predicate(String name, Block argument1, Location location) {
        this.name = name;
        this.argument1 = argument1;
        this.argument2 = null;
        this.location = location;
    }
           
    /**
     * Used for stack(block, block), unstack(block, block)
     * @param name
     * @param argument1
     * @param argument2 
     */
    public Predicate(String name, Block argument1, Block argument2) {
        this.name = name;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.location = null;
    }

    
    /*****************************************************
     *              Getters and Setters                  *
     *****************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block getArgument1() {
        return argument1;
    }

    public void setArgument1(Block argument1) {
        this.argument1 = argument1;
    }

    public Block getArgument2() {
        return argument2;
    }

    public void setArgument2(Block argument2) {
        this.argument2 = argument2;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    /****************************************************
    *                  Utility Methods                  *
    *****************************************************/
    /**
     * Returns true if this predicate matches the provided Predicate
     * @param o Other Predicate to be compared
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        
        if (!(o instanceof Predicate)) {
            return super.equals(o);
        }
        Predicate other = (Predicate) o;
        
        boolean eq; 
        eq = name.equals(other.getName());  
        if (eq) {
            if(argument1 != null && other.getArgument1() != null) {                                 
               eq = eq && argument1.equals(other.getArgument1());   //Check if argument1 matches
            }
            if(argument2 != null && other.getArgument2() != null) {
               eq = eq && argument2.equals(other.getArgument2());   //Check if argument2 matches
            }
            if(location != null && other.getLocation() != null) {
               eq = eq && location.equals(other.getLocation());     //Check if location matches
            } 
        }
               
        return eq;
    }
    
    public String toString() {
        String output = name + "(";
        if(argument1 != null) {                     //If there is an argument1               
            output += argument1.getName();          //e.g clear(A) or the following
            if(argument2 != null) {                 //And argument 2
                output += ", " + argument2.getName();//e.g. on(A, B)
            }
            if(location != null) {                  //And location
                output += ", " + location.getName();//e.g. onTable(A, L1)
            }
        }
        else if (location != null) {                //If there is a location but not argument 1
            output += location.getName();           //e.g. clear(L1)
        }
        output += ")";
        return output;
    }
    
    public void print() {
        String output = toString();
        System.out.print(output);
    }
}
