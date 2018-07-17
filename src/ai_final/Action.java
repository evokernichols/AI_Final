package ai_final;

import java.util.ArrayList;

/**
 * This class defines a single action that can be performed upon a world (list
 * of predicates). Possible actions include:
 * pickup a block from a location,
 * putdown a block onto a location,
 * unstack a block from another block
 * stack a block onto another block
 * 
 * Call constructors for each of these actions as follows:
 * pickup: new Action(Action.PICKUP, BLOCK, LOCATION);
 * putdown: new Action(Action.PUTDOWN, BLOCK, LOCATION);
 * unstack: new Action(Action.UNSTACK, BLOCK, BLOCK);
 * stack: new Action(Action.STACK, BLOCK, BLOCK);
 * 
 * @author Ryan Nichols
 */
public class Action {
    //Fields
    String name;
    Location location;  //For certain actions like pickup(A, L1) or putdown(A, L1)
    Block block1;
    Block block2;       //For certain actions like unstack(A, B) or stack(A, B)
    
    //String constants
    final public static String PICKUP = "PICKUP";
    final public static String PUTDOWN = "PUTDOWN";
    final public static String UNSTACK = "UNSTACK";
    final public static String STACK = "STACK";
    final public static String NOOP = "NOOP";  

    /**
     * Constructor for pickup/putdown actions
     * @param name
     * @param location
     * @param block1 
     */
    public Action(String name, Block block1, Location location) {
        this.name = name;
        this.location = location;
        this.block1 = block1;
        this.block2 = null;
    }

    /**
     * Constructor stack/unstack actions
     * @param name
     * @param block1 top block
     * @param block2 bottom block
     */
    public Action(String name, Block block1, Block block2) {
        this.name = name;
        this.location = null;
        this.block1 = block1;
        this.block2 = block2;
    }       
    
    /*****************************************************
     *                     Core Methods                  *
     *****************************************************/
    /**
     * Generates a list of predicates that should be added and removed from a 
     * world to which this action is applied. 
     * @return a Changes object, which contains a list of Predicates to be 
     * removed and a list of Predicates to be added
     */
    public Changes getChanges() {
        Changes changes;
        ArrayList<Predicate> removed = new ArrayList<>();
        ArrayList<Predicate> added = new ArrayList<>();
        
        switch (name) {
            case PICKUP:                                                //Pickup block1 from location
                removed.add(new Predicate(Predicate.ONTABLE, block1, location));    //Remove ONTABLE(block1, location)
                removed.add(new Predicate(Predicate.CLEAR, block1));                //Remove CLEAR(block1). This will be added back as soon as it is placed somewhere
                added.add(new Predicate(Predicate.CLEARLOC, location));             //Add CLEARLOC(location)
                added.add(new Predicate(Predicate.HOLDING, block1));                //Add HOLDING(block1)
                break;
            case PUTDOWN:                                               //Putdown block1 on location
                added.add(new Predicate(Predicate.ONTABLE, block1, location));      //Add ONTABLE(block1, location)
                added.add(new Predicate(Predicate.CLEAR, block1));                  //Add CLEAR(block1)
                removed.add(new Predicate(Predicate.CLEARLOC, location));           //Remove CLEARLOC(location)
                removed.add(new Predicate(Predicate.HOLDING, block1));              //Remove HOLDING(block1)
                break;
            case UNSTACK:                                               //Unstack block1 from block2
                removed.add(new Predicate(Predicate.ON, block1, block2));           //Remove ON(block1, block2)
                removed.add(new Predicate(Predicate.CLEAR, block1));                //Remove CLEAR(block1). This will be added back as soon as it is placed somewhere
                added.add(new Predicate(Predicate.CLEAR, block2));                  //Add CLEAR(block2)
                added.add(new Predicate(Predicate.HOLDING, block1));                //Add HOLDING(block1)
                break;
            case STACK:                                                 //Stack block1 on block2
                added.add(new Predicate(Predicate.ON, block1, block2));             //Add ON(block1, block2)
                added.add(new Predicate(Predicate.CLEAR, block1));                  //Add CLEAR(block1). This will be added back as soon as it is placed somewhere
                removed.add(new Predicate(Predicate.CLEAR, block2));                //Remove CLEAR(block2)
                removed.add(new Predicate(Predicate.HOLDING, block1));              //Remove HOLDING(block1)
                break;
            case NOOP:
                break;
        }
        
        changes = new Changes(removed, added);
        return changes;
    }
    
    /**
     * Modifies and returns the provided world by applying the changes that 
     * occur upon performing this action.
     * @param world
     * @return 
     */
    public ArrayList<Predicate> applyAction(ArrayList<Predicate> world) {
        ArrayList<Predicate> modifiedWorld = new ArrayList<>(world);    //Copy the given world
        Changes predicateChanges = getChanges();                        //Calculate the changes caused by this action
        ArrayList<Predicate> removals = new ArrayList<>();              //Predicates marked for removal
        //Apply changes
        for (Predicate p : predicateChanges.getRemoved()) {     //For each predicate that is to be removed
            for(Predicate p2 : modifiedWorld) {                 //For each predicate in the world
                if (p.equals(p2))                               //If the predicates match
                    removals.add(p2);                           //Mark the predicate for removal 
            }
        }
        modifiedWorld.removeAll(removals);                      //Delete all predicates that are removed as a result of this action
        modifiedWorld.addAll(predicateChanges.getAdded());      //Add all predicates that are added as a result of this action
               
        return modifiedWorld;
    }
    
    /*****************************************************
     *                 Getters and Setters               *
     *****************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Block getBlock1() {
        return block1;
    }

    public void setBlock1(Block block1) {
        this.block1 = block1;
    }

    public Block getBlock2() {
        return block2;
    }

    public void setBlock2(Block block2) {
        this.block2 = block2;
    }
    
    /****************************************************
    *                  Utility Methods                  *
    *****************************************************/
    public String toString() {
        String output = name + "(";
        if(block1 != null) {                        //If there is an block1               
            output += block1.getName();             
            if(block2 != null) {                    //And block2
                output += ", " + block2.getName();  //e.g. ON(A, B)
            }
            if(location != null) {                  //And location
                output += ", " + location.getName();//e.g. ONTABLE(A, L1)
            }
        }
        output += ")";
        return output;
    }
    
    public void print() {
        String output = toString();
        System.out.print(output);
    }
    
    public boolean equals(Action other) {
        boolean eq = false;     
        if (name.equals(other.getName())) {                         //If the predicates are of the same type
            eq = true;                                              //True so far
            if(block1 != null) {                                 
               eq = eq && block1.equals(other.getBlock1());         //Check if block1 matches
            }
            if(block2 != null) {
               eq = eq && block2.equals(other.getBlock2());         //Check if block2 matches
            }
            if(location != null) {
               eq = eq && location.equals(other.getLocation());     //Check if location matches
            }
        }
        return eq;
    }
}
