import java.lang.reflect.Array;
import java.util.ArrayList;

public class DFA {
    
    public State startState = new State("Q0", "start");
    public int numberOfStates = 1;
    public ArrayList<String> charList = new ArrayList<>();
    public ArrayList<State> states = new ArrayList<>();
    public ArrayList<State> finalStates = new ArrayList<>();
    public ArrayList<Transition> transitions = new ArrayList<>();
    public ArrayList<ArrayList<State>> dfaStates = new ArrayList<>();
    public NFA nfa;


    public DFA(NFA nfa) {
        this.nfa = nfa;
        this.startState = nfa.getStartState();
        this.charList = nfa.getAlphabetList();
        this.states = nfa.getStates();
        this.transitions = nfa.getTransitions();
    }

    public DFA to_DFA(NFA nfa)
    {
        DFA dfa = new DFA(nfa);
        ArrayList<State> e_cloStates = new ArrayList<>();

        //get the e-closure of the start state
        dfa.e_closure(nfa.getStartState(), null);


        return null;
    }




    public ArrayList<State> e_closure(State state, String onInput) {
        
        NFA toLoopThrough = nfa ;
        ArrayList<State> eClosure = new ArrayList<>();
        ArrayList<State> isVisited = new ArrayList<>();
        State nfaStarState = toLoopThrough.getStartState();

        //find states I can get to from the start state with null transitions

        // eClosure of a state is the state itself plus all states that can be reached from it with null transitions
        eClosure.add(state);
        isVisited.add(state);

        for(Transition t: state.getTransitions())
        {
            if(t.getTransitionValue() == onInput)
            {
                eClosure.add(t.getTransitionTo());
            }
        }

        if(eClosure.size() == 1)
        {
            dfaStates.add(eClosure);
            return eClosure;
        }
        else
        {
            ArrayList<State> temp = new ArrayList<>();
            temp.addAll(eClosure);

            for(State s: temp)
            {
                if(isVisited.contains(s))
                    continue;
                else{
                    
                    eClosure = e_closureRecursive(eClosure, isVisited, onInput);
                    System.out.println("eClosure: of  " + state.getStateLabel() + "is " );
                    //loop through eClosure and print out the states

                    for(State ss: eClosure)
                    {
                        System.out.println(ss.getStateLabel() + " ,");
                    }

                }
            }
            dfaStates.add(eClosure);

        }

       
        return eClosure;
    }

    public ArrayList<State> e_closureRecursive(ArrayList<State> eClosure, ArrayList<State> isVisited, String onInput)
    {

        ArrayList<State> temp = new ArrayList<>();
        temp.addAll(eClosure);

        for(State s: temp)
        {
            if(isVisited.contains(s))
                continue;
            else{
                isVisited.add(s);

                for(Transition t: s.getTransitions())
                {
                    if(t.getTransitionValue() == onInput)
                    {
                        eClosure.add(t.getTransitionTo());
                    }
                }

                eClosure = e_closureRecursive(eClosure, isVisited, onInput);
            }
        }
        
        return eClosure;
    }
    {

    }




    
}
