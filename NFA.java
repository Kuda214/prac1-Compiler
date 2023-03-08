import java.util.ArrayList;

public class NFA {
    //class varaibles
    private static int stateCount = 0;
    private static int finalStateCount = 0;
    private static int alphabetCount = 0;
    private State startState ;
    private State exitingState ;
    private ArrayList<State> states = new ArrayList<State>(); //vertices
    private ArrayList<State> finalStates = new ArrayList<State>();

    public NFA() {
        //constructor

    }

    public void setStartState(State state)
    {
        this.startState = state;
    }

    public void setExitingState(State state)
    {
        this.exitingState = state;
    }

    public State getExitingState()
    {
        return this.exitingState;
    }

    public State getStartState()
    {
        return this.startState;
    }

    public void addState(State state ) {
        // State state = new State(nodeLabel, nodeType, numberOfTransitions);
        states.add(state);
        stateCount++;
        if (state.getNodeType().equals("final")) {
            finalStateCount++;
            finalStates.add(state);
        }
    }
        
    public void addTransition(State from, State to, String transitionValue) {
        Transition transition = new Transition(transitionValue);
        transition.setTransitionFrom(from);
        transition.setTransitionTo(to);

        from.addTransition(transition);
        to.addTransition(transition);

    }

    public NFA alphanumericNFA(State from , State to , String transitionValue)
    {
        //draw alphanumericNFA
        NFA alNfa = new NFA();

        alNfa.addState(from);
        alNfa.setStartState(from);

        alNfa.addState(to);
        alNfa.setExitingState(to);

        alNfa.addTransition(from, to, transitionValue);

        return alNfa;
    }
    
    public NFA unionNFA(State from , State to , NFA firstOption, NFA secondOption)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.setStartState(from);

        nfa.addState(to);
        nfa.setExitingState(to);

        nfa.addTransition(from, firstOption.getStartState(), null);
        nfa.addTransition(from, secondOption.getStartState(), null);

        nfa.addTransition(firstOption.getExitingState(), to, null);
        nfa.addTransition(secondOption.getExitingState(), to, null);

        return nfa;
    }

    public NFA zeroOrMore_AsteriskNFA(State from, NFA substituteNfa)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.setStartState(from);
        nfa.setExitingState(from);
        
        nfa.addTransition(from, substituteNfa.getStartState(), null);
        nfa.addTransition(substituteNfa.getExitingState(), from, null);

        return nfa;
    }

    public NFA oneOrMore_PlusNFA( State from , State to , NFA substituteNfa)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.setStartState(from);

        nfa.addState(to);
        nfa.setExitingState(to);

        nfa.addTransition(from, substituteNfa.getStartState(), null);
        nfa.addTransition(substituteNfa.getExitingState(), to, null);
        nfa.addTransition(to, from, null);

        return nfa;
    }

    public NFA optionalNFA(State from, State to , NFA substituteNfa)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.setStartState(from);

        nfa.addState(to);
        nfa.setExitingState(to);

        nfa.addTransition(from, substituteNfa.getStartState(), null);
        nfa.addTransition(from, to, null);
        nfa.addTransition(substituteNfa.getExitingState(), to, null);

        return nfa;
    }


}
