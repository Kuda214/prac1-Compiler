import java.util.ArrayList;
import java.util.Collection;

public class State {
     
    //class varaibles
    private String stateLabel; //A,B
    private String stateType; // 'start', 'final', 'normal
    private int numberOfTransitions = 0;
    ArrayList<Transition> transitions = new ArrayList<Transition>();

    public State(String stateLabel, String stateType) {
        this.stateLabel = stateLabel;
        this.stateType = stateType;
        transitions = new ArrayList<Transition>(numberOfTransitions );
    }   

    public String getStateLabel() {
        return stateLabel;
    }

    public String getStateType() {
        return stateType;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public int getNumberOfTransitions( )
    {
        return this.numberOfTransitions;
    }

    public void setNumberOfTransitions(int num)
    {
        this.numberOfTransitions = num;
    }

    public boolean addTransition(Transition transition) {
        if (transition != null) {
            
            //check if transition that has the same state from and to already exist if not add else add
            if(isTransitionPresent(transition, transitions)) {
                return false;
            }

            transitions.add(transition);
            numberOfTransitions++;
            return true;
        } else {
            return false;
        }
    }

    public boolean isTransitionPresent(Transition transition, Collection<Transition> transitions) {
        for (Transition t : transitions) {
            if (t.getTransitionFrom().equals(transition.getTransitionFrom()) &&
                t.getTransitionTo().equals(transition.getTransitionTo())
               ) {
                return true;
            }
        }
        return false;
    }

    

    public String toString() {
        String output =  
         "State Label: " + stateLabel + " State Type: " + stateType + " Number of Transitions: " + numberOfTransitions + ""+ 
         " Transitions: \n" + transitions + "\n";
    
        return output;
    }

    public void removeTransition(Transition transition) {
        transitions.remove(transition);
        
    }

    


}
