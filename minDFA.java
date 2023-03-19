import java.lang.reflect.Array;
import java.util.ArrayList;

public class minDFA {
    
    private ArrayList<State> states = new ArrayList<State>();
    private ArrayList<Transition> transitions = new ArrayList<Transition>();
    private ArrayList<String> alphabet = new ArrayList<String>();
    private ArrayList<State> finalStates = new ArrayList<State>();
    private ArrayList<State> nonFinalStates = new ArrayList<State>();
    private DFA dfa;
    public ArrayList<ArrayList<State>> groups = new ArrayList<ArrayList<State>>();
    public boolean hasntChanged2 = false;
    public boolean hasntChanged = false;


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
        //create a new minDFA
       
        ArrayList<State> acceptingStates = dfa.finalStates;
        ArrayList<State> nonAcceptingStates = dfa.nonFinalStates;

  
        //add accepting states to group 1
        groups.add(acceptingStates);
        //add non accepting states to group 2
        groups.add(nonAcceptingStates);

        System.out.println("*************************************************8");
        System.out.println("Accepting states: " + acceptingStates.toString());
        System.out.println("Non accepting states: " + nonAcceptingStates.toString());
        

        //loop through groups and print them
        System.out.println();
        System.out.println();

        for(int i = 0; i < groups.size(); i++)
        {
            System.out.println("Group " + i + ": "  );
            for(int t=0; t<groups.get(i).size(); t++){
                System.out.print( groups.get(i).get(t).getStateLabel() + " , ");
            }
            System.out.println();

        }

        recurisveGrouping();

        return null;
    }

    private void recurisveGrouping(){

       //check if all states are in one group
        
        ArrayList<ArrayList<State>> group0 = new ArrayList<>();
        ArrayList<ArrayList<State>> group1 = new ArrayList<>();

        ArrayList<State> g0Prev = new ArrayList<>();
        ArrayList<State> g1Prev = new ArrayList<>();

        if(groups.get(0).size() < 2)
        {
            if(groups.get(0).size() == 1)
            {
                hasntChanged = true;
                group0.add(groups.get(0));
            }        
        }
        else
        {

            group0.add(splitGroup(groups.get(0)));
            

            System.out.print("Results are: " );

            for(int e=0; e<group0.size(); e++)
            {
                for(int r=0; r<group0.get(e).size() ; r++)
                {
                    System.out.print( group0.get(e).get(r).getStateLabel() + " , ");
                }
                System.out.println();
            }
    
            System.out.println("************************************************==");
    
            //TODO: add group0 to groups
            do {
                for(int i=0; i<group0.size(); i++)
                {
                    if(group0.get(i).size() <2)
                    {
                        hasntChanged = true;
                    }
                    else if(group0.get(i).size() > 1)
                    {
                        group0.add(splitGroup(group0.get(i)));
                        hasntChanged = false;
                    }
                
                }
            } while (hasntChanged == true);

        }

        // check if group1 should be resolved

        if(groups.get(1).size() < 2)
        {
            if(groups.get(1).size() == 1)
            {
                hasntChanged2 = true;
                group1.add(groups.get(0));
            }

        }
        else
        {
            group1.add(splitGroup(groups.get(1)));
            
            do {
                for(int i=0; i<group1.size(); i++)
                {
                    if(group1.get(i).size() <2)
                    {
                        hasntChanged2 = true;
                    }
                    else if(group1.get(i).size() > 1)
                    {
                        group1.add(splitGroup(group1.get(i)));
                        hasntChanged2 = false;
                    }
                
                }
            } while (hasntChanged2 == true);

        }

        //add group0 to groups
        groups.clear();
        groups.addAll(group0);
        groups.addAll(group1);

        //loop through groups and print them
        System.out.println();
        System.out.println();

        for(ArrayList<State> gs: groups)
        {
            for(int r=0; r<gs.size() ; r++)
            {
                System.out.println("Groups: " + gs.get(r).getStateLabel());
            }
            System.out.println();
        }
       
    }

    private ArrayList<State> splitGroup(ArrayList<State> arrayList) {

        ArrayList<ArrayList<State>> newGroups = new ArrayList<>();

        // aa*|b
        System.out.println();
        System.out.println("Spliting the following:");
        for(int k=0; k<arrayList.size(); k++)
        {
            System.out.println( arrayList.get(k).getStateLabel() + " , ");
        }

        System.out.println();


        System.out.println("Our alphabet is:  " + dfa.getAlphabet());


 
                State state1 = arrayList.get(0);
                ArrayList<State> tempGroup = new ArrayList<>();


                for(int i=0; i<arrayList.size(); i++)
                {

                    if(i != 0)
                    {
                        State state2 = arrayList.get(i);
                        if(state1 == state2 )
                        {
                            continue;
                        }
                        State state1TransitionTo = null ;
                        State state2TransitionTo  = null;
                        boolean state1Found = false;
                        boolean state2Found = false;
                        boolean sameGroup = false;
                        //check if state1 and state2 are in the same group
                        for(int u=0; u<alphabet.size() ; u++)
                        {
                            System.out.println("Our alphabet is:  " + alphabet.get(u));
                            System.out.println("Our State one: " + state1.getStateLabel());
                            System.out.println("Our State two: " + state2.getStateLabel());

                            for(Transition t : state1.getTransitions())
                            {
                                if(t.getTransitionValue().equals(alphabet.get(u)))
                                {
                                    state1Found = true;
                                    state1TransitionTo = t.getTransitionTo();
                                    break;
                                }
                            }

                            for(Transition t : state2.getTransitions())
                            {
                                if(t.getTransitionValue().equals(alphabet.get(u)))
                                {
                                    state2Found = true;
                                    state2TransitionTo = t.getTransitionTo();
                                    break;
                                }
                            }

                        // if founds are not both true then add state1 to a new group
                            if((state1Found == false && state2Found == true) || (state1Found == true && state2Found == false) )
                            {
                                System.out.println("State1: " + state1.getStateLabel() + " and State2: " + state2.getStateLabel() + " are not in the same group");
                                sameGroup = false;
                                continue;
                            }
                            else if ((state1Found == false && state2Found == false) )
                            {
                                System.out.println("State1: " + state1.getStateLabel() + " - " + alphabet.get(u) + "->" + state2.getStateLabel() + " might be in the same group");
                                sameGroup = true; 
                              
                               continue;
                            }
                            else if (state1Found == true && state2Found == true)
                            {
                                // check if the transition state to is in the same group
                                if(state1TransitionTo == state2TransitionTo)
                                {
                                    System.out.println("State1: " + state1.getStateLabel() + " - " + alphabet.get(u) + "->" + state2.getStateLabel() + " might be in the same true");

                                    sameGroup = true;
                                   
                                    continue;
                                }
                                else
                                {
                                    //check if in the same group 
                                    for(int j=0; j<groups.size(); j++)
                                    {
                                        for(int k=0; k<groups.get(j).size(); k++)
                                        {
                                            if(groups.get(j).get(k).getStateLabel().equals(state1TransitionTo.getStateLabel()) && groups.get(j).get(k).getStateLabel().contains(state2TransitionTo.getStateLabel()))
                                            {
                                                //add state1 to a new group
                                                System.out.println("yeeee s1: " + state1.getStateLabel() + " - " + alphabet.get(u) + "->" + state2.getStateLabel() + " might yeyow be e true");

                                                sameGroup = true;
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //compare tempGroup to groups
                        // if(sameGroup == false)
                        // {
                        //     tempGroup.add(state1);
                            
                        // }
                        // else{

                        //     tempGroup.add(state1);
                        //     tempGroup.add(state2);
                        // }    
                        
                        if(sameGroup == true)
                        {
                            tempGroup.add(state1);
                            tempGroup.add(state2);
                        }
                        else
                        {
                            tempGroup.add(state1);
                        }
                }
            }

            //check if tempGroup is the same as arrayList
            if(tempGroup.equals(arrayList))
            {

            }
            else
            {
                newGroups.add(tempGroup);
            }

        
        return tempGroup;

    }

}
