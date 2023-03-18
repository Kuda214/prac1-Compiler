import java.lang.reflect.Array;
import java.util.ArrayList;

public class DFA {
    
    // public State startState = new State("Q0", "start");
    public int numberOfStates = 0;
    public ArrayList<String> charList = new ArrayList<>();
    public ArrayList<State> states = new ArrayList<>();
    public ArrayList<State> finalStates = new ArrayList<>();
    public ArrayList<Transition> transitions = new ArrayList<>();
    public ArrayList<ArrayList<State>> dfaStates = new ArrayList<>();
    public NFA nfa;
    public DFA dfa ;
    public ArrayList<String> newTransitions_string = new ArrayList<>();

    public ArrayList<String> move_on_char = new ArrayList<>();


    public DFA(NFA nfa) {
        this.nfa = nfa;
        this.charList = nfa.getAlphabetList();
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


    public DFA to_DFA(NFA nfa)
    {
        DFA tempDfa = new DFA(nfa);
        ArrayList<State> moveResult = new ArrayList<>();
        ArrayList<State> e_closure_start_state = new ArrayList<>();
        State finalState = nfa.getExitingState();


        //get the e-closure of the start state
        e_closure_start_state = tempDfa.e_closure(nfa.getStartState(), null);
      
        dfaStates.add(e_closure_start_state);

        for(int i=0 ; i<dfaStates.size() ;i++)
        {
            for(int k=0; k<charList.size(); k++)
            {

                moveResult = move(dfaStates.get(i), charList.get(k));
            
                for(State sss: moveResult)
                {
                    if(moveResult != null)
                    System.out.print("moveResult state " + sss.getStateLabel() + " , on symbol + "+ charList.get(k) +" , ");
                    
                }
                System.out.println();

                if(moveResult != null && !moveResult.isEmpty())
                {
                    if(dfaStates.contains(moveResult))
                    {
                        System.out.println("\033[33m" + moveResult +"\033[0m");

                        State newState = new State("Q" + i, "normal");
                        numberOfStates++;

                        int index = dfaStates.indexOf(moveResult);
                        State state = new State("Q" + index, "normal");
                        
                        if(moveResult.contains(finalState))
                        {
                            state.setStateType("final");
                        }

                        if(i == 0 )
                        {
                            newState.setStateType("start");
                        }
                        if(index == 0 )
                        {
                            state.setStateType("start");
                        }

                        

                        
                        
                        tempDfa.addState(newState);
                        tempDfa.addState(state);
                        tempDfa.addTransition(newState, state, charList.get(k));
                    }
                    else
                    {
                        dfaStates.add(moveResult);

                        State newState = new State("Q" + i, "normal");
                        numberOfStates++;
                        State state = new State("Q" + (dfaStates.size()-1), "normal");
                        numberOfStates++;

                        if(moveResult.contains(finalState))
                        {
                            state.setStateType("final");
                        }

                        if(i == 0 )
                        {
                            newState.setStateType("start");
                        }
                        if((dfaStates.size()-1) == 0 )
                        {
                            state.setStateType("start");
                        }
                        
                        tempDfa.addState(newState);
                        tempDfa.addState(state);
                        tempDfa.addTransition(newState, state, charList.get(k));
                    }
                }
            }
        }

        System.out.println("DFAStates after moves" );


        // for(int j=0; j<dfaStates.size() ; j++)
        // {
        
        //         System.out.println("dfaState " + dfaStates.get(j));
            
        // }
        System.out.println();
        System.out.println("Done " );


        System.out.println("New Transitions: " + tempDfa);


        return null;
    }

    private ArrayList<State> move(ArrayList<State> arrayList, String string) {



        //get where one can go on input string

        ArrayList<State> isVisited = new ArrayList<>();
        ArrayList<State> input_closure = new ArrayList<>();
        ArrayList<State> e_closureOfInputSet = new ArrayList<>();
        
        for(State state: arrayList)
        {
            for(Transition t: state.getTransitions())
            {
                if(t.getTransitionValue() == null)
                    continue;
                else
                if(t.getTransitionValue().equals(string))
                {
                    input_closure.add(t.getTransitionTo());
                }
            }
        }

        // System.out.println("input_closure: " + input_closure);

       
        ArrayList<State> temp = new ArrayList<>();
        temp.addAll(input_closure);

        for(State s: temp)
        {
            if(isVisited.contains(s))
                continue;
            else{
                
                input_closure = e_closure(s, string); //q1, q7

                for(State ss: input_closure)
                {
                    e_closureOfInputSet.add(ss);
                }
            }
        }

        // e_closureOfInputSet = e_closureRecursive(input_closure, isVisited, string);

        isVisited.clear();
        ArrayList<State> resultList = new ArrayList<>();
        ArrayList<State> temp2 = new ArrayList<>();
        //get e-closure of the states in e_closureOfInputSet
        for(State s: e_closureOfInputSet)
        {
            if(isVisited.contains(s))
                continue;
            else{
                temp2 = e_closure(s, null); //q1, q7

                for(State ss: temp2)
                {
                    resultList.add(ss);
                }

            }
        }
        
        return resultList;

    }

    public ArrayList<State> e_closure(State state, String onInput) {
        
        NFA toLoopThrough = nfa ;
        ArrayList<State> eClosure = new ArrayList<>();
        ArrayList<State> isVisited = new ArrayList<>();        

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
            // dfaStates.add(eClosure);
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
                    // System.out.println("eClosure: of  " + state.getStateLabel() + "is " );
                    //loop through eClosure and print out the states

                    // for(State ss: eClosure)
                    // {
                    //     System.out.println(ss.getStateLabel() + " ,");
                    // }

                }
            }
            // dfaStates.add(eClosure);

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
    
    @Override
    public String toString() {

        String statesString  = "";

        System.out.println("--------------------------------");

        // print all states
        for(State state : states) {
            if(state.getStateType() != "normal")
            statesString += state.getStateLabel() +" - " + state.getStateType()+ "\n ";
            else
            statesString += state.getStateLabel() + "\n ";
        }

        System.out.println("--------------------------------");
        for(Transition transition : transitions) {
            statesString += transition + "\n ";
        }   

        // System.out.println("Start State : " + dfa);
        // System.out.println("Exiting State : " + exitingState);
        System.out.println("====================================");

        return statesString;
    }

    // public NFA reFormatNFA() {

    //     NFA newNFA = new NFA();

    //     //add transitions as I traverse the transitions from start state

    //     for(Transition transition: transitions )
    //     {
    //         newNFA.addState(transition.getTransitionFrom());
    //         newNFA.addState(transition.getTransitionTo());
    //     }

    //     //for each state in newNFA add transitions to it from transitions is transition.from == state

    //     for(State state : newNFA.states)
    //     {
    //         for(Transition transition : transitions)
    //         {
    //             if(transition.getTransitionFrom() == state)
    //             {
    //                 if( state.isTransitionPresent(transition, transitions) )
    //                 {
    //                     continue;
    //                 }
    //                 state.addTransition(transition);
    //             }
    //         }
    //     }

    //     newNFA.setStartState(startState);
    //     newNFA.setExitingState(exitingState);

    //     //add transitions to newNFA from state transitions 
    //     for(State state : newNFA.states)
    //     {
    //         for(Transition transition : state.getTransitions())
    //         {
    //             newNFA.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
    //         }
    //     }


    //     //add exiting state

    //     return newNFA;



    // }



    
}
