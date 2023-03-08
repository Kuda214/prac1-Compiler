import java.util.ArrayList;

public class State {
     
    //class varaibles
    private String nodeLabel; //A,B
    private String nodeType; // 'start', 'final', 'normal
    private int numberOfTransitions = 0;
    ArrayList<Transition> transitions = new ArrayList<Transition>();

    public State(String nodeLabel, String nodeType) {
        this.nodeLabel = nodeLabel;
        this.nodeType = nodeType;
        numberOfTransitions = numberOfTransitions + 1;
        transitions = new ArrayList<Transition>(numberOfTransitions );
    }   

    public String getNodeLabel() {
        return nodeLabel;
    }

    public String getNodeType() {
        return nodeType;
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

    


}
