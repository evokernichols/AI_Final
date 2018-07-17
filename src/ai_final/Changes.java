package ai_final;

import java.util.ArrayList;

/**
 * This class defines a set of changes which can be applied to a world (list of
 * predicates). It consists of a list of predicates to be removed from the world
 * and a list of predicates to be added to the world.
 * @author Ryan Nichols
 */
public class Changes {
    ArrayList<Predicate> removed;   //List of predicates to be removed
    ArrayList<Predicate> added;     //List of predicates to be added

    public Changes() {
    }
    
    public Changes(ArrayList<Predicate> removed, ArrayList<Predicate> added) {
        this.removed = removed;
        this.added = added;
    }

    public ArrayList<Predicate> getRemoved() {
        return removed;
    }

    public void setRemoved(ArrayList<Predicate> removed) {
        this.removed = removed;
    }

    public ArrayList<Predicate> getAdded() {
        return added;
    }

    public void setAdded(ArrayList<Predicate> added) {
        this.added = added;
    }
}
