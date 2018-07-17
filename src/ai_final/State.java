package ai_final;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class defines a State object, which consists of: 
 * 1) a list of predicates that describe a world
 * 2) a parent state, from which this state was derived
 * 3) the action that was applied to the parent state to obtain this state
 * 4) the cost of reaching this state from the initial state (used for A*)
 * 5) the estimated cheapest cost of reaching the goal state from this state
 * @author Ryan Nichols
 */
public class State implements Comparable {
    ArrayList<Predicate> predicate;
    Action action;
    State parent;
    int gn; //Cost from start to here
    int hn; //Estimated cheapest cost from here to goal
    

    /**
     * Default constructor
     */
    public State() {
        this.predicate = null;
        this.action = null;
        this.parent = null;
    }

    /**
     * Complete constructor
     * @param predicate the list of predicates that define the world of this 
     * state
     * @param action the action applied to obtain this state from its parent
     * @param parent this states parent
     * @param gn distance from initial state to this state
     * @param hn estimated distance from this state to the goal state
     */
    public State(ArrayList<Predicate> predicate, Action action, State parent, int gn, int hn) {
        this.predicate = predicate;
        this.action = action;
        this.parent = parent;
        this.gn = gn;
        this.hn = hn;
    }
   
    
    /****************************************************
    *               Getters and Setters                 *
    *****************************************************/
    public ArrayList<Predicate> getPredicate() {
        return predicate;
    }

    public void setPredicate(ArrayList<Predicate> predicate) {
        this.predicate = predicate;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public int getGn() {
        return gn;
    }

    public void setGn(int gn) {
        this.gn = gn;
    }

    public int getHn() {
        return hn;
    }

    public void setHn(int hn) {
        this.hn = hn;
    }
    
      
    
    /****************************************************
    *                  Utility Methods                  *
    *****************************************************/
    /**
     * This method was originally used in the A* implementation to find the 
     * total estimated length of the path from the start state -> this state ->
     * goal state. By changing this method to return hn instead, it returns the 
     * length of the path from just this state -> goal state. This means that 
     * the solver is no longer technically an A* implementation, and does not 
     * find the optimal path, however it does find an answer many orders of 
     * magnitude faster, which is absolutely necessary for non trivial problems 
     * with more than 7 blocks.
     */
    public int calcFn() {
        return hn;          //Find an answer as quickly as possible, technically no longer A*
        //return gn + hn;   //Find the answer with the shortest possible path (original A* method)
    }
    
    /**
     * This method generates and returns a list of Action objects that can be
     * validly applied to this state.
     * @return ArrayList of Action objects that can be validly applied to this 
     * state.
     */
    public ArrayList<Action> getPossibleActions() {
        ArrayList<Action> actions = new ArrayList<>();
        Block holding = null;
        
        //Check if there is a block being held
        for (Predicate p: predicate) { //Scan all predicates
            if(p.getName().equals(Predicate.HOLDING)) {
                holding = p.getArgument1();
            }
        }
        
        //Pickup
        if (holding == null) {                              //Can only pick up if not already holding a block
            for (Predicate p : predicate) {                 //Scan all predicates
                if(p.getName().equals(Predicate.ONTABLE)) { //If there's an ONTABLE predicate
                    Block blockOnTable = p.getArgument1();  //Get the block on the table
                    for(Predicate p2 : predicate) {         //Scan through predicates again
                        if(p2.getName().equals(Predicate.CLEAR) && p2.getArgument1() != null && p2.getArgument1().equals(blockOnTable)) { //If the block on the table is found to be clear
                            actions.add(new Action(Action.PICKUP, blockOnTable, p.getLocation()));        //Add pickup block as a possible action
                            break;
                        }
                    }                
                }
            }
        }        
        //Putdown
        if (holding != null) {                              //Can only put down if already holding a block
            for (Predicate p : predicate) {                 //Scan all predicates
                if(p.getName().equals(Predicate.CLEARLOC)) {//If there's a clear location
                    if (p.getLocation() != null) {          //If the location is not null, then it is a clear location
                        actions.add(new Action(Action.PUTDOWN, holding, p.getLocation()));   //Add putdown block on location as a possible action
                    }                
                }
            }
        }        
        //Unstack
        if (holding == null) {                              //Can only unstack if not already holding a block
            for (Predicate p : predicate) {                 //Scan all predicates
                if(p.getName().equals(Predicate.ON)) {      //If there's an ON predicate
                    Block topBlock = p.getArgument1();      //Get the block on top
                    Block bottomBlock = p.getArgument2();   //Get the block on bottom
                    for(Predicate p2 : predicate) {         //Scan through predicates again
                        if(p2.getName().equals(Predicate.CLEAR) && p2.getArgument1() != null && p2.getArgument1().equals(topBlock)) {    //If the top block is found to be clear
                            actions.add(new Action(Action.UNSTACK, topBlock, bottomBlock));              //Add unstack top block as a possible action
                            break;
                        }
                    }                
                }
            }
        }        
        //Stack
        if (holding != null) {                              //Can only stack if already holding a block
            for (Predicate p : predicate) {                 //Scan all predicates
                if(p.getName().equals(Predicate.CLEAR)) {   //If there's a clear location
                    if (p.getArgument1() != null) {         //If the argument1 is not null, then it is a clear block
                        actions.add(new Action(Action.STACK, holding, p.getArgument1()));   //Add putdown held block on clear block as a possible action                       
                    }                
                }
            }
        }
                
        return actions;
    }
    
    
    
    /**
     * Simple print method that shows all predicates
     */
    public void print() {
        for(Predicate p : predicate) {
            p.print(); 
            System.out.print(" ");
        }
        System.out.println("");
    }
    
    /**
     * Converts the predicate calculus statements of this state to a list of 
     * four Stacks of Strings (one each for L1, L2, L3, and L4).
     * @return an ArrayList of four Stacks of Strings
     */
    public ArrayList<Stack<String>> toStringStacks() {
        ArrayList<Stack<String>> stacks = new ArrayList<Stack<String>>();
        Stack<String> L1 = new Stack<>();
        Stack<String> L2 = new Stack<>();
        Stack<String> L3 = new Stack<>();
        Stack<String> L4 = new Stack<>();
        
        ArrayList<Predicate> copy = new ArrayList<>(predicate);
        ArrayList<Predicate> toRemove = new ArrayList<>();
        
        //L1 first pass
        for (Predicate p : copy) {
            if (p.getLocation() != null) {
                if (p.getLocation().getName().equals("L1")) {
                    if(p.getName().equals("CLEARLOC")){            //L1 is clear
                        for(int i = 0; i < 10; i++){
                            L1.push("_");                        //Fill stack with 10 empty strings
                        }                        
                    }
                    else if(p.getName().equals("ONTABLE")) {    //A block is on L1
                        L1.push(p.getArgument1().getName());    //Push the name of the block
                    }
                    toRemove.add(p);
                }
            }
        }
        copy.removeAll(toRemove);
        toRemove.clear();
        
        //L2 first pass
        for (Predicate p : copy) {
            if (p.getLocation() != null) {
                if (p.getLocation().getName().equals("L2")) {
                    if(p.getName().equals("CLEARLOC")){            //L2 is clear
                        for(int i = 0; i < 10; i++){
                            L2.push("_");                        //Fill stack with 10 empty strings
                        }
                    }
                    else if(p.getName().equals("ONTABLE")) {    //A block is on L2
                        L2.push(p.getArgument1().getName());    //Push the name of the block
                    }
                    toRemove.add(p);
                }
            }
        }
        copy.removeAll(toRemove);
        toRemove.clear();
        
        //L3 first pass
        for (Predicate p : copy) {
            if (p.getLocation() != null) {
                if (p.getLocation().getName().equals("L3")) {
                    if(p.getName().equals("CLEARLOC")){            //L3 is clear
                        for(int i = 0; i < 10; i++){
                            L3.push("_");                        //Fill stack with 10 empty strings
                        }
                    }
                    else if(p.getName().equals("ONTABLE")) {    //A block is on L3
                        L3.push(p.getArgument1().getName());    //Push the name of the block
                    }
                    toRemove.add(p);
                }
            }
        }
        copy.removeAll(toRemove);
        toRemove.clear();
        
        //L4 first pass
        for (Predicate p : copy) {
            if (p.getLocation() != null) {
                if (p.getLocation().getName().equals("L4")) {
                    if(p.getName().equals("CLEARLOC")){            //L4 is clear
                        for(int i = 0; i < 10; i++){
                            L4.push("_");                        //Fill stack with 10 empty strings
                        }
                    }
                    else if(p.getName().equals("ONTABLE")) {    //A block is on L4
                        L4.push(p.getArgument1().getName());    //Push the name of the block
                    }
                    toRemove.add(p);
                }
            }
        }
        copy.removeAll(toRemove);
        toRemove.clear();
        
        L1 = toStringStackHelper(L1,copy);
        L2 = toStringStackHelper(L2,copy);
        L3 = toStringStackHelper(L3,copy);
        L4 = toStringStackHelper(L4,copy);
        
        stacks.add(L1);
        stacks.add(L2);
        stacks.add(L3);
        stacks.add(L4);
        
        return stacks;
    }
    
    /**
     * Helper method for toStringStacks. Should not be called from anywhere else
     * @param strStack The stack that is to be completed
     * @param preds The list of predicates
     * @return The complete string stack representation
     */
    private Stack<String> toStringStackHelper(Stack<String> strStack, ArrayList<Predicate> preds) {
        String top = strStack.peek();
        if(top.equals("")){
            return strStack;
        }
        else {
            for (Predicate p : preds) {
                if(p.getArgument1().getName().equals(top) && p.getName().equals(Predicate.CLEAR)){  //Top is clear
                    for(int i = strStack.size(); i<10; i++) {
                        strStack.push("_");                                                  //Fill the rest of the way with emptry strings
                    }
                }
                if(p.getArgument2() != null && p.getArgument2().getName().equals(top) && p.getName().equals(Predicate.ON)) {
                    //Arg 1 is on top
                    top = p.getArgument1().getName();
                    strStack.push(top);
                }
            }
        }
        return strStack;
    }
    
    /**
     * Compare this State to another State object
     * @param o The State object to which this State will be compared
     * @return Standard 1, 0, -1 depending on comparison result.
     */
    @Override
    public int compareTo(Object o) {
        State other = (State)o;
        int result = Integer.compare(calcFn(), other.calcFn());
        return result;
    }
}
