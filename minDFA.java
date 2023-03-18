import java.lang.reflect.Array;
import java.util.ArrayList;

public class minDFA {
    
    private ArrayList<State> states = new ArrayList<State>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();
    private ArrayList<String> alphabet = new ArrayList<String>();
    private DFA dfa;

    public minDFA(DFA dfa) {
        this.dfa = dfa;
        this.alphabet = dfa.getAlphabet();
    }

    public void addState(State state ) {
    
        if(!states.contains(state))
        {
            states.add(state);
        }

}

    public void addTransition(State from, State to, String transitionValue) {
        Transition transition = new Transition(transitionValue);
        transition.setTransitionFrom(from);
        transition.setTransitionTo(to);

        //check if from does not have the same transition before adding

        if(!from.getTransitions().contains(transition))
        {
            from.addTransition(transition);
            
        }

        //check if transition does not exist before adding
        if(!transitions.contains(transition))
        {
            //print transition in dark pin
            transitions.add(transition);
        }
    
    }

    public minDFA minimizeDFA() {
        //create a new DFA
       


        return null;
    }
}
