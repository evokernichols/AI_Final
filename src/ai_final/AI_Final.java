/*
* Program for Final Project of CS4850
* This program acts a states based solver for the World of Blocks problem.
* It uses a modified version of A* that finds a solution as quickly as possible
* at the expense of no longer being the optimal solution. This is needed for most
* problems larger than 6 blocks. If desired, the algorithm can be changed back
* to traditional A* by commenting State.java: line 109 and uncommenting line 110
*/
package ai_final;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * This main class contains methods to allow the user to test the solver by 
 * specifying a initial and goal state via command line. Upon defining the 
 * states, the solver will find and display a solution. 
 * @author Ryan Nichols
 */
public class AI_Final {
    //Global vars
    static Block A = new Block("A");
    static Block B = new Block("B");
    static Block C = new Block("C");
    static Block D = new Block("D");
    static Block E = new Block("E");
    static Block F = new Block("F");
    static Block G = new Block("G");
    static Block H = new Block("H");
    static Block I = new Block("I");
    static Block J = new Block("J");

    static Location L1 = new Location("L1");
    static Location L2 = new Location("L2");
    static Location L3 = new Location("L3");
    static Location L4 = new Location("L4");

    /**
     * Main Method
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Predicate> initialWorld;
        ArrayList<Predicate> goalWorld;
        ArrayList<State> states = new ArrayList<>();

        //Get initial state
        System.out.println("Define the initial state.");
        initialWorld = defineWorld();

        //Get goal state
        System.out.println("Define the goal state.");
        goalWorld = defineWorld();

        //Display init world desciption in console
        System.out.println("Init State:");
        for (Predicate p : initialWorld) {
            System.out.println(p.toString());
        }
        System.out.println("");

        //Display goal world desciption in console
        System.out.println("Goal State:");
        for (Predicate p : goalWorld) {
            System.out.println(p.toString());
        }
        System.out.println("");

        //Solve
        ResolutionEngine res = new ResolutionEngine(initialWorld, goalWorld);
        long start = System.currentTimeMillis();
        states = res.solve();
        long end = System.currentTimeMillis();
        long total = end - start;   //Calculate runtime


        //Display states in console
        ArrayList<Stack<String>> stringStacks = new ArrayList<>();              //Description of a world
        ArrayList<ArrayList<Stack<String>>> allStateStrings = new ArrayList<>();//List of world descriptions

        for (int i = 0; i < states.size(); i++) {
            stringStacks = states.get(i).toStringStacks();      //Convert each state in the path to a printable form
            allStateStrings.add(stringStacks);                  //Add it to an iterable list
        }

        for (int i = 0; i < allStateStrings.size(); i++) {      //For each state
            System.out.println("State " + i + ": ");            //Display the state number
            System.out.println("_____________________");
            for (int k = 0; k < 10; k++) {                      //For each row
                System.out.print("|   ");
                for (int j = 0; j < allStateStrings.get(i).size(); j++) {   //For each stack
                    String temp = allStateStrings.get(i).get(j).pop();      //Print the block name or empty space
                    System.out.print(temp + "   "); //Spacing
                }
                System.out.println("|");
            }
            System.out.println("    L1  L2  L3  L4\n\n");
        }
        System.out.println("Solution found in " + total/1000.0 + " seconds.");
    }//end main

    /**
     * Get user input to define the world
     * @return A list of predicates describing the world defined by the user
     */
    public static ArrayList<Predicate> defineWorld() {
        ArrayList<Predicate> predicates = new ArrayList<>();
        predicates.addAll(defineStack(L1));
        predicates.addAll(defineStack(L2));
        predicates.addAll(defineStack(L3));
        predicates.addAll(defineStack(L4));
        return predicates;
    }

    /**
     * Get user input to define one stack
     * @param loc Location/Block on which the stack is to be placed
     * @return A list of predicates describing the stack defined by the user
     */
    public static ArrayList<Predicate> defineStack(Object loc) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        Scanner s = new Scanner(System.in);
        String ans = "";
        Object top = loc;

        while (!ans.equals("CLEAR")) {
            System.out.print("Enter a block to stack on " + top.toString() + " (or clear): ");
            ans = s.next().toUpperCase();
            switch(ans) {
                case "A":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, A, (Location) top));
                        top = A;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, A, (Block) top));
                        top = A;
                    }
                    break;
                case "B":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, B, (Location) top));
                        top = B;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, B, (Block) top));
                        top = B;
                    }
                    break;
                case "C":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, C, (Location) top));
                        top = C;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, C, (Block) top));
                        top = C;
                    }
                    break;
                case "D":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, D, (Location) top));
                        top = D;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, D, (Block) top));
                        top = D;
                    }
                    break;
                case "E":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, E, (Location) top));
                        top = E;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, E, (Block) top));
                        top = E;
                    }
                    break;
                case "F":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, F, (Location) top));
                        top = F;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, F, (Block) top));
                        top = F;
                    }
                    break;
                case "G":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, G, (Location) top));
                        top = G;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, G, (Block) top));
                        top = G;
                    }
                    break;
                case "H":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, H, (Location) top));
                        top = H;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, H, (Block) top));
                        top = H;
                    }
                    break;
                case "I":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, I, (Location) top));
                        top = I;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, I, (Block) top));
                        top = I;
                    }
                    break;
                case "J":
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.ONTABLE, J, (Location) top));
                        top = J;
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.ON, J, (Block) top));
                        top = J;
                    }
                    break;
                default:
                    ans = "CLEAR";   //Break loop
                    if (top instanceof Location) {
                        predicates.add(new Predicate(Predicate.CLEARLOC, (Location) top));
                    }
                    else if (top instanceof Block) {
                        predicates.add(new Predicate(Predicate.CLEAR, (Block) top));
                    }
                    break;
            } //End Switch
        }//End while
        return predicates;
    }

}//end class