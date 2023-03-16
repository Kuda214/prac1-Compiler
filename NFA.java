import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NFA {
    //class varaibles
    private static int stateCount = 0;
    private static int finalStateCount = 0;
    private static int alphabetCount = 0;
    private State startState ;
    private State exitingState ;
    private ArrayList<State> states = new ArrayList<State>(); //vertices
    private ArrayList<State> finalStates = new ArrayList<State>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>(); //edges

    public NFA() {
        //constructor

    }

    public ArrayList<Transition> geTransitions() {
        return this.transitions;
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

    public NFA AddTwoNFAs( NFA two, NFA one) {

        // NFA nfa = one;
        // nfa.setStartState(one.getStartState());
        // nfa.setExitingState(two.getExitingState());
        // nfa.addTransition(one.getExitingState(), two.getStartState(), null);
        // return nfa;

        NFA newNFA = new NFA();

        // add states from one nfa 

        for(State state : one.states)
        {
            newNFA.addState(state);
        }

        // add states from two nfa

        for(State state : two.states)
        {
            newNFA.addState(state);
        }

        //add all transitions to nfa from substitute nfa
     
        for(Transition transition : one.transitions)
        {
            newNFA.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
        }

        for(Transition transition : two.transitions)
        {
            newNFA.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
        }

        newNFA.setStartState(one.getStartState());
        newNFA.setExitingState(two.getExitingState());

        return newNFA;
        
    }
    
    public NFA unionNFA (State from, State to  ,NFA firstOption, NFA secondOption)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.setStartState(from);

        nfa.addState(to);
        nfa.setExitingState(to);


        for(State state : firstOption.states)
        {
            nfa.addState(state);
        }

        for(State state : secondOption.states)
        {

            nfa.addState(state);
        }

        //add all transitions to nfa from substitute nfa
        // ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA
        // for(State state : firstOption.states) {
        //     for(Transition transition : state.getTransitions()) {
        //         transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
        //     }
        // }

        // for(State state : secondOption.states) {
        //     for(Transition transition : state.getTransitions()) {
        //         transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
        //     }
        // }
        //remove duplicate transitions from transitions

        //ADD first Option transitions to nfa
        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA
        for(Transition transition : firstOption.transitions)
        {
            transitionsToAdd.add(transition);
        }

        for (Transition transition : secondOption.transitions)
        {
            transitionsToAdd.add(transition);
        }
    
        System.out.println("first NFA: " + firstOption);
        System.out.println("Second NFA: " + secondOption);

        System.out.println("$4$4e%5rbjhikmdlemdlmdme  -  " + transitionsToAdd );

        nfa.transitions.clear();
        for(Transition transition : transitionsToAdd)
        {
            if(!nfa.transitions.contains(transition))
            {
                nfa.transitions.add(transition);
            }
        }

        nfa.addTransition(from, firstOption.getStartState(), null);
        nfa.addTransition(from, secondOption.getStartState(), null);

        nfa.addTransition(firstOption.getExitingState(), to, null);
        nfa.addTransition(secondOption.getExitingState(), to, null);

        System.out.println("\033[38;2;255;265;0m, unionNFA: " + nfa.toString() + "\033[0m");


        return nfa;
    }

    public NFA asteriskNFA( State fromState, NFA substituteNfa)
    {
        NFA nfa = new NFA();

        nfa.addState(fromState);
        nfa.addState(substituteNfa.getExitingState());

        for(State state : substituteNfa.states)
        {
            nfa.addState(state);
        }

        //add all transitions to nfa from substitute nfa
        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA
        for(State state : substituteNfa.states) {
            for(Transition transition : state.getTransitions()) {
                transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
            }
        }

        //remove duplicate transitions from transitions

        nfa.transitions.clear();
        for(Transition transition : transitionsToAdd)
        {
            if(!nfa.transitions.contains(transition))
            {
                nfa.transitions.add(transition);
            }
        }

        nfa.setStartState(fromState);
        nfa.setExitingState(fromState);

        nfa.addTransition(fromState, substituteNfa.getStartState(), null);
        nfa.addTransition(substituteNfa.getExitingState(), fromState, null);

        // remove duplicate transitions from nfa
      
        System.out.println("\033[33m Asterisk NFA: "  +  nfa.toString()  + "\033[0m");


        return nfa;
    }

    public NFA oneOrMore_PlusNFA( State from , State to , NFA substituteNfa)
    {
        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.addState(substituteNfa.getExitingState());

        for(State state : substituteNfa.states)
        {
            nfa.addState(state);
        }

        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA
        for(State state : substituteNfa.states) {
            for(Transition transition : state.getTransitions()) {
                transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
            }
        }

        //remove duplicate transitions from transitions

        nfa.transitions.clear();
        for(Transition transition : transitionsToAdd)
        {
            if(!nfa.transitions.contains(transition))
            {
                nfa.transitions.add(transition);
            }
        }

        nfa.setStartState(from);
        nfa.setExitingState(to);

        nfa.addTransition(from, substituteNfa.getStartState(), null);
        nfa.addTransition(substituteNfa.getExitingState(), to, null);
        nfa.addTransition(to, from, null);



        return nfa;
    }

    public NFA optionalNFA(State from, State to , NFA substituteNfa)
    {

        System.out.println("\033[38;2;255;265;0m, optionalNFA: " + substituteNfa.toString() + "\033[0m");
        System.out.println("\033[38;2;255;265;0m, StartState : " + substituteNfa.getStartState() + "\033[0m");
        System.out.println("\033[38;2;255;265;0m, ExitingState : " + substituteNfa.getExitingState() + "\033[0m");

        NFA nfa = new NFA();

        nfa.addState(from);
        nfa.addState(substituteNfa.getExitingState());

        for(State state : substituteNfa.states)
        {
            nfa.addState(state);
        }

        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA

        for(Transition transition  : substituteNfa.transitions) {
            transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
        }

        // make transitionsToAdd transitions 
        nfa.transitions.clear();
        for(Transition transition : transitionsToAdd)
        {
            nfa.transitions.add(transition);
        }

        System.out.println("\033[38;2;255;265;0m, ========== nfa Updated: " + nfa.toString() + "\033[0m");



        nfa.setStartState(from);
        nfa.setExitingState(to);

        nfa.addTransition(from, substituteNfa.getStartState(), null);
        nfa.addTransition(substituteNfa.getExitingState(), to, null);
        nfa.addTransition(from, to, null);

        System.out.println("\033[38;2;255;265;0m,******** nfa Updated: " + nfa.toString() + "\033[0m");

        


        return nfa;
    }

    @Override
    public String toString() {

        String statesString  = "";

        //print all states
        // for(State state : states) {
        //     System.out.println("\u001B[34m " + state+ "\u001B[0m");
        // }

        //q0(a)-q1(b)-q2(c)-q3
        // if(transitions.size() > 0)
        // {
            for(Transition transition : transitions) {
                statesString += transition + "\n ";
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

    public NFA removeDuplicateTransitions() {

        //remove duplicate transitions from nfa
        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA

        for(State state : states) {
            for(Transition transition : state.getTransitions()) {
                transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
            }
        }

        //remove duplicate transitions from transitions
        transitions.clear();
        for(Transition transition : transitionsToAdd)
        {
            if(!transitions.contains(transition))
            {
                transitions.add(transition);
            }
        }


        return this;
    }

    public NFA addTransitionsfromPrevNFA(NFA prevNFA) {

        // this.addTransition(startState, exitingState, null);

        //ADD states from prevNFA to this NFA
        this.setStartState(prevNFA.getStartState());
        for(State state : prevNFA.states) {
            this.addState(state);
        }

        //add states from this to this NFA

        //add all transitions to nfa from substitute nfa
        ArrayList<Transition> transitionsToAdd = new ArrayList<>(); // Create a new list to hold transitions to add to NFA

        for(State state : states) {
            for(Transition transition : state.getTransitions()) {
                transitionsToAdd.add(transition); // Add transition to the new list instead of modifying NFA transitions
            }
        }

        //remove duplicate transitions from transitionsToAdd

        // transitions.clear();    
        // for(Transition transition : transitionsToAdd) {
        //     // System.out.println("\u001B[34m " + transition+ "\u001B[0m");
        //     if(transitions.contains(transition)) continue;
        //     else // Skip if transition already exists in new list
        //     addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
        // }

        this.addTransition(prevNFA.getExitingState(), this.getStartState(), null);

        this.setStartState(prevNFA.startState);
        this.setExitingState(this.getExitingState());

        transitions = transitionsToAdd;


        return this;

        // return null;
    }

    public NFA joinTwoNFAs(NFA first, NFA second) {

        NFA nfa = new NFA();

        //add all states from first nfa to nfa
        for(State state : first.states) {
            nfa.addState(state);
        }

        //add all states from second nfa to nfa that are not already in nfa
        for(State state : second.states) {
            if(!nfa.states.contains(state)) {
                nfa.addState(state);
            }
        }
        // (0|(1(01*0)*1))*

        //add all transitions to nfa from first nfa
        for(Transition transition : first.transitions) {
            nfa.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
        }

        //add all transitions to nfa from second nfa that are not already in nfa
        for(Transition transition : second.transitions) {
            if(!nfa.transitions.contains(transition)) {
                nfa.addTransition(transition.getTransitionFrom(), transition.getTransitionTo(), transition.getTransitionValue());
            }
        }

        //add start state
        nfa.setStartState(first.getStartState());

        //add exiting state
        nfa.setExitingState(second.getExitingState());
        nfa.addTransition(first.getExitingState(), second.getStartState(), null);
        nfa = nfa.removeDuplicateTransitions();

        System.out.println("\033[92m NFA POPO : " + nfa + "\033[0m");


        return nfa;
        // (0|(1(01*0)*1))*
    }

    public NFA removeDuplicateStatesAndTransitions() {

    //    // delete duplicate states
    //     Iterator<State> stateIter = this.states.iterator();
    //     while(stateIter.hasNext()) {
    //         State state = stateIter.next();
    //         if(Collections.frequency(this.states, state) > 1) {
    //             stateIter.remove();
    //         }
    //     }

        // delete duplicate transitions
        Iterator<Transition> transitionIter = this.transitions.iterator();
        while(transitionIter.hasNext()) {
            Transition transition = transitionIter.next();
            if(Collections.frequency(this.transitions, transition) > 1) {
                transitionIter.remove();
            }
        }
        

     
        // delete duplicate transitions
        Set<Transition> uniqueTransitions = new HashSet<>(this.transitions);
        this.transitions.clear();
        this.transitions.addAll(uniqueTransitions);


        

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        //print all states in a line , comma seperated , the output shoud be in red
        for(State state : states) {
            System.out.print("\u001B[31m " + state+ "\u001B[0m");
        }

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        //print all transitions in a line , comma seperated , the output shoud be in peach
        for(Transition transition : transitions) {
            System.out.print("\u001B[35m " + transition+ "\n" + "\u001B[0m");
        }


        return this;

        


    }
}
