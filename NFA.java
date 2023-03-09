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
        if (state.getStateType().equals("final")) {
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

    public NFA alphanumericNFA(NFA nfa , State from , State to , String transitionValue)
    {
        System.out.println("Prev-NFA: "  + nfa );
        if(nfa != null && nfa.states != null) 
        System.out.println("States of nfa: "  + nfa.states.toString() + "\n");

        if(nfa != null)
        System.out.println("prevNFA - exiting state: "  + nfa.getExitingState() );
        
        NFA alNfa = new NFA();

        
        if(nfa != null)
        {
            alNfa.setStartState(nfa.getStartState());
            // alNfa.addState(nfa.getStartState());
   
            //loop through all states of nfa
            for (State state : nfa.states) {
                if(state != alNfa.getStartState())
                alNfa.addState(state);
            }
        }
        else
        {
            alNfa.setStartState(from);
            alNfa.addState(from);
        }
        // alNfa.addState(from);

        alNfa.addState(to);
        alNfa.setExitingState(to);

        alNfa.addTransition(from, to, transitionValue);
       
        if(nfa != null && nfa.getStartState() != null)
        System.out.println("nfa here: " + nfa.getStartState().toString());

        if(alNfa != null && alNfa.getStartState() != null)
        System.out.println("alNfa here: " + alNfa.getStartState().toString());



        System.out.println("min nfa here: " + alNfa.toString());
        if(alNfa != null && alNfa.states != null)
        System.out.println("States of ALNFA: "  + alNfa.states.toString() + "\n");

        // if(nfa != null)
        // {
        //     alNfa = AddTwoNFAs(nfa, alNfa);
        // }
        // else
        // {
            
        // }
        return alNfa;
    }

    public NFA AddTwoNFAs(NFA one , NFA two) {

        NFA nfa = one;

        nfa.setStartState(one.getStartState());
        nfa.setExitingState(two.getExitingState());

        nfa.addTransition(one.getExitingState(), two.getStartState(), null);

        return nfa;
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

    @Override
    public String toString() {

        String statesString  = "";
        //q0(a)-q1(b)-q2(c)-q3
        System.out.println("states size: " + states.size());
        if(states.size() > 0)
        {
            statesString = states.get(0).getTransitions().toString();
            System.out.println("State0 : " + states.get(0).getTransitions().toString());
            for(int i = 1; i < states.size()-1; i++)
            {
                System.out.println("innnnnnnnnnnnn: " + i);
                statesString += ", " + states.get(i).getTransitions().toString();
            }
        }
        else
        {
            statesString = "null";
        }
        return statesString;

    }


}
