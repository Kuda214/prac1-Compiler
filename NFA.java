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

        // if(!states.contains(state))
        // {
            states.add(state);
            stateCount++;
            if (state.getStateType().equals("final")) {
                finalStateCount++;
                finalStates.add(state);
            }
        // }

    }
        
    public void addTransition(State from, State to, String transitionValue) {
        Transition transition = new Transition(transitionValue);
        transition.setTransitionFrom(from);
        transition.setTransitionTo(to);

        from.addTransition(transition);
        // to.addTransition(transition);
    }

    public NFA alphanumericNFA(NFA nfa , State from , State to , String transitionValue)
    {
        NFA alNfa = new NFA();

        if(nfa !=null)
        {
            alNfa = nfa;

            // alNfa.addState(from);
            alNfa.addState(to);
            alNfa.setExitingState(to);
            alNfa.addTransition(from, to, transitionValue);
        }
        else
        {
            alNfa = new NFA();

            alNfa.addState(from);
            alNfa.setStartState(from);

            alNfa.addState(to);
            alNfa.setExitingState(to);

            alNfa.addTransition(from, to, transitionValue);
        }

        System.out.println("\033[38;2;255;265;0m, alNfa: " + alNfa.toString() + "\033[0m");
        System.out.println("Num of states: " + alNfa.states);
        // System.out.println("Num of final states: " + finalStateCount);/

        return alNfa;
    }

    public NFA removeExcessTransitions(NFA alNfa) {
       
        //color output green and yellow
        // System.out.println("\033[38;2;255;265;0m, NFA to sort: "  + alNfa  + "\033[0m");
        //remove excess transitions and concatenate the nfas
        // if(alNfa..size() > 2)
        // {
        //     // connect all the mini nfas to the first nfa and remove the excess transitions

        //     for()


        // }

      

        return alNfa;
    }

    public NFA concatenation(NFA prevNFA, NFA newNFA)
    {
        NFA nfa = prevNFA;

        nfa.setStartState(prevNFA.getStartState());
        nfa.setExitingState(newNFA.getExitingState());

        nfa.addTransition(prevNFA.getExitingState(), newNFA.getStartState(), null);
        System.out.println("\033[38;2;255;265;0m, prevNFAAAAA: " + prevNFA.toString() + "\033[0m");
        System.out.println("\033[38;2;255;265;0m, newNFAAAAAA: " + newNFA.toString() + "\033[0m");

        System.out.println("\033[33m Combined NFA: "  +  nfa.toString()  + "\033[0m");

        return nfa;
    }

    public NFA AddTwoNFAs(NFA one , NFA two) {

        NFA nfa = one;

        nfa.setStartState(one.getStartState());
        nfa.setExitingState(two.getExitingState());

        nfa.addTransition(one.getExitingState(), two.getStartState(), null);

        return nfa;
    }
    
    public NFA unionNFA(NFA firstOption, NFA secondOption)
    {
        NFA nfa = new NFA();

        // nfa.addState(from);
        // nfa.setStartState(from);

        // nfa.addState(to);
        // nfa.setExitingState(to);

        // nfa.addTransition(from, firstOption.getStartState(), null);
        // nfa.addTransition(from, secondOption.getStartState(), null);

        // nfa.addTransition(firstOption.getExitingState(), to, null);
        // nfa.addTransition(secondOption.getExitingState(), to, null);

        return nfa;
    }

    public NFA asteriskNFA( State fromState, NFA substituteNfa)
    {
        NFA nfa = new NFA();


        nfa.addState(fromState);
        nfa.addTransition(substituteNfa.getExitingState(), fromState, null);


        //add all states to nfa from substitute nfa
        for(State state : substituteNfa.states)
        {
            nfa.addState(state);
        }


        nfa.setStartState(fromState);
        nfa.setExitingState(fromState);


        nfa.addTransition(fromState, substituteNfa.getStartState(), null);

        System.out.println("\033[38;2;255;265;0m, prevNFAAAAA: " + substituteNfa.getExitingState() + "\033[0m");

        System.out.println("\033[33m Combined NFA: "  +  nfa.toString()  + "\033[0m");


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
        if(states.size() > 0)
        {
            statesString = states.get(0).getTransitions().toString();
            for(int i = 1; i < states.size()-1; i++)
            {
                statesString += ", " + states.get(i).getTransitions().toString();
            }
        }
        else
        {
            statesString = "null";
        }
        return statesString;
    }

    public NFA joinAllStates( NFA tempNfa) {

        NFA nfa = new NFA();

        nfa.addState(tempNfa.getStartState());
        nfa.setStartState(tempNfa.getStartState());

        nfa.addState(tempNfa.getExitingState());
        nfa.setExitingState(tempNfa.getExitingState());

        System.out.println("tempNfa: " + tempNfa.toString());



        return nfa;

    }


}
