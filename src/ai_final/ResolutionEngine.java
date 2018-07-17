package ai_final;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * This class contains methods to find a path (list of State objects) from an 
 * initial world description (list of predicates) to a goal world description.
 * To solve with this class, call the two argument constructor and provide
 * an initial world and goal world description (two ArrayList of Predicate 
 * objects). Then call the solve() method on this object to get the path.
 * @author Ryan Nichols
 */
public class ResolutionEngine {
    ArrayList<Predicate> initialWorld;
    ArrayList<Predicate> goalWorld;

    /*****************************************************
     *                   Constructors                    *
     *****************************************************/
    
    public ResolutionEngine() {
    }
    
    public ResolutionEngine(ArrayList<Predicate> initialWorld, ArrayList<Predicate> goalWorld) {
        this.initialWorld = initialWorld;
        this.goalWorld = goalWorld;
    }

    
    /*****************************************************
     *              Getters and Setters                  *
     *****************************************************/
    public ArrayList<Predicate> getInitialWorld() {
        return initialWorld;
    }

    public void setInitialWorld(ArrayList<Predicate> initialWorld) {
        this.initialWorld = initialWorld;
    }

    public ArrayList<Predicate> getGoalWorld() {
        return goalWorld;
    }

    public void setGoalWorld(ArrayList<Predicate> goalWorld) {
        this.goalWorld = goalWorld;
    }
    
    
    /*****************************************************
     *                   Core Methods                    *
     *****************************************************/
    /**
     * This method uses a modified version of the  A* algorithm to find list of 
     * states that describe a path from an initial world description to a goal 
     * world description.
     * Upon realizing that the traditional A* algorithm is infeasible for large
     * problems, such as a complex 10 block world of blocks problem, this 
     * algorithm was modified slightly by adjusting the sorting method of the 
     * priority queue.
     * 
     * @return An ArrayList of States which define a path from the initial world
     * to the goal world
     */
    public ArrayList<State> solve() {
        PriorityQueue<State> unevaluatedStates = new PriorityQueue<>();    //List of intermediate states, sorted by the State method calcFn()
        ArrayList<State> evaluatedStates = new ArrayList<>();
        ArrayList<State> stateList;
        boolean solutionFound = false;  //Loop breaker
        State finalState = null;        
        
        
        //Create a state from the initial world description
        int distanceToGoal = calcDistanceToGoal(initialWorld, goalWorld);
        State initState = new State(initialWorld,   //State using the initial world conditions
                                    null,           //No action has been performed as this is the initial state
                                    null,           //No parent exists for the 
                                    0,              //The distance from the initial state to itself is 0
                                    distanceToGoal);//The distance from the inital state to the goal as calculated by the heuristic function, calcDistanceToGoal
        
        //Add the initial state to the list of 
        unevaluatedStates.add(initState);
               
        //Iterate through the priority queue until it is empty or the solution has been found
        while(unevaluatedStates.size() > 0 && solutionFound == false) {
            //Obtain and remove the current best state in the queue
            State current = unevaluatedStates.poll(); 
            //System.out.println("Evaluating state, distance: " + calcDistanceToGoal(current.getPredicate(), goalWorld));
            
            //Get a list of possible actions to be performed on this state
            ArrayList<Action> possibleActions = current.getPossibleActions();            
            
            //Add to completed list
            evaluatedStates.add(current);
            
            //Iterate through possible actions
            for (Action a : possibleActions) {
                //Run an action on the current world to obtain a modified world
                ArrayList<Predicate> modifiedWorld = a.applyAction(current.getPredicate());
                
                boolean checked = false;
                
                //Check if new world is equivalent to that of an already evaluated state
                for(State s : evaluatedStates) {
                    if (areWorldsEqual(s.getPredicate(), modifiedWorld)){   //If match is found
                        checked = true;                                     //Mark world as already checked
                        break;                                              //Stop searching
                    }
                }
                if (checked)
                    continue;                                               //Continue to next possible world
                
                //Create a State from this modified world
                State childState = new State(modifiedWorld,                                 //The new predicate set
                                             a,                                             //The action performed to obtain the state
                                             current,                                       //The parent of the new state
                                             current.getGn() + 1,                           //Gn of the new state is 1 more than that of its parent
                                             calcDistanceToGoal(modifiedWorld, goalWorld)); //The calculated distance from the new state to the goal
                
                //Check if the modified world is the same as the goal world
                if(areWorldsEqual(modifiedWorld, goalWorld)) {  //If it matches the goal
                    solutionFound = true;                       //Mark solution as found to break out of while loop
                    finalState = childState;                    //Save the state
                    System.out.println("Solution found!\n");    
                    break;                                      //Stop searching
                }
                else { //Otherwise
                    //Add the new state to the list of unevaluated states
                    unevaluatedStates.add(childState);
                    //System.out.println("Not goal and not evaluated, adding to queue: " + calcDistanceToGoal(modifiedWorld, goalWorld)); //Debug
                }
            }
        }
        
        //Build the list of states from the initial state to the final state
        stateList = buildStateList(finalState);        
        return stateList;
    }
    
    /**
     * Heuristic function for solving algorithm. Returns the number of 
     * predicates in the given world description that is not in the target world 
     * description.
     * Lower is better.
     * @param current
     * @param goal
     * @return 
     */
    public int calcDistanceToGoal(ArrayList<Predicate> current, ArrayList<Predicate> goal) {
        int predCount = 0; //Number of predicates in current that are not in goal
        for (Predicate currP : current) {
            if (!goal.contains(currP)) 
                predCount++;
        }             
        return predCount;
    }   
    
    /**
     * Compares two lists of Predicates to determine if they describe the same 
     * world. This is necessary as the predicates may not be listed in the same 
     * order.
     * @param world First world
     * @param world2 Second world
     * @return true if the worlds are equal; false otherwise
     */
    public boolean areWorldsEqual(ArrayList<Predicate> world, ArrayList<Predicate> world2) {
        boolean equal = world2.containsAll(world); //All predicate in world are also in the goal world
        equal = equal && world.containsAll(world2);//All predicate in the goal world are also in the world
               
        return equal;
    }
    
    /**
     * Builds a list of State objects from the start State (i.e. the ancestor 
     * state that has no parent) to the provided State
     * @param state The state to which the list should be built
     * @return an ArrayList of State objects from the start State to the 
     * provided state.
     */
    public ArrayList<State> buildStateList(State state) {
        ArrayList<State> stateList = new ArrayList<>();
        State current = state;
        if(state == null) {
            return stateList;
        }
        while(current.getParent() != null) {    //While current has a parent   
            stateList.add(current);             //Add the current state
            current = current.getParent();      //Set current to its parent
        }
        stateList.add(current);
        
        //Reverse the state list
        Collections.reverse(stateList);
        
        ArrayList<State> removals = new ArrayList<>();
        for (State s : stateList) {
            for (Predicate p : s.getPredicate()) {
                if(p.getName().equals(Predicate.HOLDING)) { //If it's a holding clause
                    removals.add(s);                        //Mark for removal
                }
            }
        }
        stateList.removeAll(removals);
        
        return stateList;
    }
}