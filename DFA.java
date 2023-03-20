import java.lang.reflect.Array;
import java.util.ArrayList;

public class DFA {
    
    // public State startState = new State("Q0", "start");
    public int numberOfStates = 0;
    public ArrayList<String> charList = new ArrayList<>();
    public ArrayList<State> states = new ArrayList<>();
    public ArrayList<State> finalStates = new ArrayList<>();
    public ArrayList<State> nonFinalStates = new ArrayList<>();
    public ArrayList<Transition> transitions = new ArrayList<>();
    public ArrayList<ArrayList<State>> dfaStates = new ArrayList<>();
    public State startState;
    public NFA nfa;
    public DFA dfa ;
    public ArrayList<String> newTransitions_string = new ArrayList<>();

    public ArrayList<String> move_on_char = new ArrayList<>();


    public DFA(NFA nfa) {
        this.nfa = nfa;
        this.charList = nfa.getAlphabetList();
    }

    public DFA() {
    }

    public ArrayList<String> getAlphabet()
    {
        System.out.println("Alphabet: " + charList);
        return this.charList;
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

        boolean aplhabetAlreadyExists = false;

        for (int i = 0; i < charList.size(); i++) {
            if (charList.get(i).equals(transitionValue)) {
                aplhabetAlreadyExists = true;
            }
        }


        if(aplhabetAlreadyExists)
        {
            //do nothing
        }
        else
        {
            System.out.println("adding " + transitionValue + " to alphabet");
            charList.add(transitionValue);
        }
        
        //loop through charList and remove null
        for (int i = 0; i < charList.size(); i++) {
            if (charList.get(i) == null) {
                charList.remove(i);
            }
        }

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
        // System.out.println("dfaStates: " + dfaStates);

        printDfaStates(dfaStates);

        System.out.println("dsssssssssssssssssssssssssssssssss");

        for(int i=0 ; i<dfaStates.size() ;i++)
        // for(int i=0 ; i<2 ;i++)

        {
            for(int k=0; k<charList.size(); k++)
            {

                moveResult = move(dfaStates.get(i), charList.get(k));
                System.out.println("moveResult size: " + moveResult.size() + "MoveResult state: " );

                     // remove duplicate state in moveResult
                for(int j=0; j<moveResult.size(); j++)
                {
                    for(int l=0; l<moveResult.size(); l++)
                    {
                        if(moveResult.get(j).getStateLabel().equals(moveResult.get(l).getStateLabel()) && j!=l)
                        {
                            moveResult.remove(l);
                        }
                    }
                }
            
                for(State sss: moveResult)
                {
                    if(moveResult != null && !moveResult.isEmpty())
                    System.out.print( sss.getStateLabel() + " on symbol + "+ charList.get(k) +" , ");
                }
                System.out.println();
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
                            startState = newState;
                            if(moveResult.contains(finalState))
                            {
                                newState.setStateType("start && final");
                                finalStates.add(newState);
                            }
                        }
                        if(index == 0 )
                        {
                            state.setStateType("start");
                            startState = state;
                            if(moveResult.contains(finalState))
                            {
                                newState.setStateType("start && final");
                                finalStates.add(newState);
                            }
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
                            finalStates.add(state);

                        }

                        if(i == 0 )
                        {
                            newState.setStateType("start");
                            startState = newState;
                            if(moveResult.contains(finalState))
                            {
                                newState.setStateType("start && final");
                                finalStates.add(newState);
                            }
                        }
                        if((dfaStates.size()-1) == 0 )
                        {
                            state.setStateType("start");
                            startState = state;
                            if(moveResult.contains(finalState))
                            {
                                newState.setStateType("start && final");
                                finalStates.add(newState);
                            }
                        }
                        
                        tempDfa.addState(newState);
                        tempDfa.addState(state);
                        tempDfa.addTransition(newState, state, charList.get(k));
                    }
                }
            }
        }

        System.out.println("DFAStates after moves" );


       
        System.out.println();
        System.out.println("Done " );

        tempDfa = tempDfa.reFormatNFA();

        System.out.println("DFA: " + tempDfa);


        return tempDfa;
    }

    private void printDfaStates(ArrayList<ArrayList<State>> dfaStates2) {
        for(int y= 0; y< dfaStates.size(); y++)
        {
            System.out.println("dfaStates["+ y +"]" + " : " );
            for(int u=0; u<dfaStates.get(y).size(); u++)
            {
                System.out.print(dfaStates.get(y).get(u).getStateLabel() + " , ");
            }
            System.out.println();
        }
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

        statesString += "--------------------------------\n";

        // print all states
        for(State state : states) {
            if(state.getStateType() != "normal")
            statesString += state.getStateLabel() +" - " + state.getStateType()+ "\n ";
            else
            statesString += state.getStateLabel() + "\n ";
        }

    

        statesString += "--------------------------------";
        for(Transition transition : transitions) {
            statesString += transition + "\n ";
        }   

        statesString += "====================================\n";
        statesString += "Final States: \n";

        statesString += finalStates + "\n";

        statesString += "====================================\n";
        statesString += "Non accepting States: \n";
        // print non accepting states by looping through all states and checking if they are not in the final states list
        statesString += nonFinalStates + "\n";


        return statesString;
    }

    public DFA reFormatNFA() {

        DFA newDFA = new DFA();

        //add this states to the new DFA whilst removing duplicates
        newDFA.states.clear();
        for(State state: states)
        {
            if(!newDFA.states.contains(state))
            {
                newDFA.addState(state);
            }
        }
     
        //remove duplicate states

        for (int i = 0; i < newDFA.states.size(); i++) {
            for (int j = i + 1; j < newDFA.states.size(); j++) {
                if (newDFA.states.get(i).getStateLabel().equals(newDFA.states.get(j).getStateLabel())) {
                    newDFA.states.remove(j);
                    j--;
                }
            }
        }


        //print out the states 
        ArrayList<State> temp = new ArrayList<>();

        for(State state: newDFA.states)
        {
            State s = new State(state.getStateLabel(), state.getStateType());
            temp.add(s);

        }

        newDFA.states.clear();
        newDFA.states.addAll(temp);

        //add transitions to the new DFA
        for(State state : newDFA.states)
        {
            for(Transition transition : transitions)
            {

                if(transition.getTransitionFrom().getStateLabel().equals(state.getStateLabel()))
                {

                    if( state.isTransitionPresent(transition, newDFA.transitions) )
                    {
                    }
                    else
                    {
                        state.addTransition(transition);
                    }
                }
            }
        }

        

        for(State state : newDFA.states)
        {
            for(Transition transition : state.getTransitions())
            {
                newDFA.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
            }
        }

        //set the start state
        newDFA.setStartState(startState);
        
        for(State state: newDFA.states)
        {
            if((state.getStateType().equals("final")) || (state.getStateType().equals("start and final"))   )
            {
                newDFA.finalStates.add(state);
            }
        }

        for(State state: newDFA.states)
        {
            if(!state.getStateType().equals("final") && !state.getStateType().equals("start and final"))
            {
                newDFA.nonFinalStates.add(state);
            }
        }



        return newDFA;
    }

    private void setStartState(State startState2) {
        this.startState = startState2;
    }



    
}
