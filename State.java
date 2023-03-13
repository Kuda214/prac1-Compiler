import java.util.ArrayList;

public class State {
     
    //class varaibles
    private String stateLabel; //A,B
    private String stateType; // 'start', 'final', 'normal
    private int numberOfTransitions = 0;
    ArrayList<Transition> transitions = new ArrayList<Transition>();

    public State(String stateLabel, String stateType) {
        this.stateLabel = stateLabel;
        this.stateType = stateType;
        numberOfTransitions = numberOfTransitions + 1;
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
             
            transitions.add(transition);
            return true;
        } else {
            return false;
        }
    }

    

    public String toString() {
        String output =  
         "State Label: " + stateLabel + " State Type: " + stateType + " Number of Transitions: " + numberOfTransitions + "";
        
        // for (Transition transition : transitions) {
        //     output += transition.toString() + "\n";
        // }

        

        return output;
    }

    public void removeTransition(Transition transition) {
        transitions.remove(transition);
        
    }

    


}
