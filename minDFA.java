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
    public boolean hasChanged = true;


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
        ArrayList<ArrayList<State>> groupPrev = new ArrayList<>();
        ArrayList<ArrayList<State>> groupCurrent = new ArrayList<>();
        ArrayList<ArrayList<State>> tempGroup = new ArrayList<>();

        groupPrev.addAll(groups);
        groupCurrent.addAll(groups);
        
        //loop through groups and call splitGroup

        
            for(int i=0; i<groups.size(); i++)
            {
                if((groups.get(i).size() == 1) || (groups.get(i).size() == 0))
                {
                    tempGroup.add(groups.get(i));
                    continue;
                }

                ArrayList<State> group = groups.get(i);
                groupCurrent = splitGroup(group);

                for(int j=0; j<groupCurrent.size(); j++)
                {
                    System.out.println("Group Current " + j + ": "  );
                    for(int k=0; k<groupCurrent.get(j).size(); k++)
                    {
                        System.out.println(  groupCurrent.get(j).get(k).getStateLabel());
                    }
                    System.out.println();
            
                }

                //check if group has change

                    // groups.remove(i);
                    //add new groups
                    tempGroup.addAll(groupCurrent);
                    groupPrev = groupCurrent;
                
            }

        

            if(tempGroup.size() != groupPrev.size())
            {
                System.out.println("Groups have changed");
                groups.clear();
                groups.addAll(tempGroup);
                printGroups(tempGroup);
                // printGroups(groupPrev);
                return;
            }
            else
            {
                printGroups(groups);
            }
              
    

        //loop through groups and print them
       
    }

    private ArrayList<ArrayList<State>> splitGroup(ArrayList<State> arrayList) {

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
                newGroups.add(tempGroup);
            }
            else
            {
                //remove TEMPGROUP from arrayList
                for(int i=0; i<tempGroup.size(); i++)
                {
                    for(int j=0; j<arrayList.size(); j++)
                    {
                        if(tempGroup.get(i).getStateLabel().equals(arrayList.get(j).getStateLabel()))
                        {
                            arrayList.remove(j);
                        }
                    }
                }

                //add arrayList to newGroups
                newGroups.add(arrayList);
                //add tempGroup to newGroups
                newGroups.add(tempGroup);
            }

        

        //Remove duplicates states from newGroups
        for(int i=0; i<newGroups.size(); i++)
        {
            for(int j=0; j<newGroups.get(i).size(); j++)
            {
                for(int k=0; k<newGroups.get(i).size(); k++)
                {
                    if(newGroups.get(i).get(j).getStateLabel().equals(newGroups.get(i).get(k).getStateLabel()) && j != k)
                    {
                        newGroups.get(i).remove(k);
                    }
                }
            }
        }
        
        // print newGroups

        System.out.println("New Groups are: ");
        
        for(int r=0; r< newGroups.size() ; r++)
        {
            System.out.println("Group: " + r);
            for(int q=0; q< newGroups.get(r).size(); q++)
            {
                System.out.print(newGroups.get(r).get(q).getStateLabel() + " , ");
            }
            System.out.println();
        }

        System.out.println("End of Split group function");

        
        return newGroups;

    }

    public void printGroups(ArrayList<ArrayList<State>> groups)
    {
        System.out.println();
        System.out.println();

        for(ArrayList<State> gs: groups)
        {
            System.out.println("Group: " + groups.indexOf(gs)  );
            for(int r=0; r<gs.size() ; r++)
            {
                System.out.println(gs.get(r).getStateLabel() + " , ");
            }
            System.out.println();
        }
    }

}
