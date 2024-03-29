import java.util.ArrayList;

public class to_NFA {

    public  ArrayList<Object> stack = new ArrayList<Object>();
    public NFA nfa = new NFA();
    public State startState = new State("A", "start");
    public State finalState = new State("B", "final");
    public int numOfState = 0;
    
    public NFA regex_to_NFA(String stringToTest) {
        for(int i =0; i < stringToTest.length(); i++) {
            
            if(stringToTest.charAt(i) == '(' || stringToTest.charAt(i) == ')') {                
                if(stringToTest.charAt(i) == '(')
                {
                    //just add 
                    stack.add(stringToTest.charAt(i));
                }
                else if(stringToTest.charAt(i) == ')' )
                {
                    int tempIndex = 0;
                    ArrayList<Object> tempStack = new ArrayList<Object>();
                    NFA tempNFA = new NFA();
                  
                    //find nearest (
                    for (int j = stack.size()-1; j >= 0; j--)
                    {
                        if(stack.get(j) instanceof Character)
                        {
                            char c2 = (char) stack.get(j);
                            if(c2 == '(')
                            {
                                //find nearest NFA
                                tempIndex = j;
                                break;
                            }
                        }
                    }

                    //create a subset of the stack from the nearest '(' to the nearest ')'
                    for (int j = tempIndex; j < stack.size(); j++)
                    {
                        tempStack.add(stack.get(j));

                    }

                    tempNFA = resolveObjectsToNFA(tempStack);

                    //remove the subset from the stack
                    
                    ArrayList<Object> tempStack2 = new ArrayList<Object>();

                    for (int j = 0; j < tempIndex; j++)
                    {
                        tempStack2.add(stack.get(j));
                    }

                    stack = tempStack2;

                    stack.add(tempNFA);

                }
            }
            else if(isAlphanumeric(stringToTest.charAt(i))) {
                
                char c = ' ';
                
                if(stack.size() > 0 &&  stack.get(stack.size()-1) instanceof Character)
                    c = (char) stack.get(stack.size()-1);

                if(stack.size() > 0) //starts with (
                {
                    // //m.out.println(stringToTest.charAt(i) + " is a character");

                    if(stack.get(stack.size()-1) instanceof Character)
                    {

                        char cc = ' ';
                        if((i+1) < stringToTest.length())
                          cc = stringToTest.charAt(i+1);

                        if(cc != ' ') // not done traversing through the stringToTest
                        {
                            //check if next char is a special char

                            if(cc == '*' || cc == '+' || cc == '?' )
                            {
                                if(cc == '*')
                                {
                                    //m.out.println( "prev chaar is: " + c + "and next char is character is: *");


                                    NFA tempNfa = new NFA();
                                    NFA asteriskNFA = new NFA();
                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    asteriskNFA = tempNfa.asteriskNFA(fromState2, tempNfa);

                                    // tempNfa = asteriskNFA.alphanumericNFA(asteriskNFA, fromState2, toState, stringToTest);

                                    tempNfa = asteriskNFA;
                                    i++;
                                
                                    stack.add(tempNfa);
                                }
                                else if (cc == '+')
                                {
                                    //m.out.println( "prev char is: " + c + "and next char is character is: +");

                                    NFA tempNfa = new NFA();
                                    NFA plusNFA = new NFA();

                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;

                                    State fromState3 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    plusNFA = tempNfa.oneOrMore_PlusNFA(fromState2, fromState3 , tempNfa);

                                    tempNfa = plusNFA;
                                    i++;
                                    //m.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);

                                }
                                else if (cc == '?')
                                {
                                    //m.out.println( "prev char is: " + c + "and next char is character is: ?");

                                    NFA tempNfa = new NFA();
                                    NFA optionalNFA = new NFA();

                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;

                                    State fromState3 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    optionalNFA = tempNfa.optionalNFA(fromState2, fromState3 , tempNfa);

                                    tempNfa = optionalNFA;
                                    i++;
                                    //m.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);
                                }
                            }
                            else if(cc == '(' || cc == ')' || isAlphanumeric(cc) || cc == '|'  )
                            {
                                //m.out.println( "prev char is: " + c + "and next char is character is: (<), (|), (char), ()) ");

                                NFA tempNfa = new NFA();

                                State fromState = new State("q"+numOfState, "normal");
                                numOfState++;
                                State toState = new State("q"+ numOfState, "normal");
                                numOfState++;
                                
                                tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");
                            
                                stack.add(tempNfa);

                            }
                        }
                        else // no next thing to check 
                        {
                            //m.out.println( "prev char is: " + c + "and no NEXT cgar");

                            NFA tempNfa = new NFA();

                            State fromState = new State("q"+numOfState, "normal");
                            numOfState++;
                            State toState = new State("q"+ numOfState, "normal");
                            numOfState++;
                            
                            tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                            stack.add(tempNfa);
                        }
                    }
                    else if(stack.get(stack.size()-1) instanceof NFA)
                    {
                        //m.out.println(c + " prev is an NFA ");

                        char nextChar = ' ';

                        if((i+1) < stringToTest.length())
                            nextChar = stringToTest.charAt(i+1);

                        if(nextChar != ' ') // not done traversing through the stringToTest
                        {
                            if(nextChar == '*')
                            {   
                                //m.out.println( "prev char is an NFA "+ c + "and next char is character is: *");

                                NFA prevNFA = (NFA) stack.get(stack.size()-1);
                                NFA tempNfa = new NFA();
                                NFA asteriskNFA = new NFA();

                                State from = new State("q"+numOfState, "normal");
                                numOfState++;

                                State to = new State("q"+numOfState, "normal");
                                numOfState++;

                                State fromState = new State("q" + numOfState, "normal");
                                numOfState++;

                                tempNfa = tempNfa.alphanumericNFA(null, from, to, stringToTest.charAt(i)+ "");
                                asteriskNFA = tempNfa.asteriskNFA(fromState, tempNfa);
                                //m.out.println("***************** ");
                                tempNfa = tempNfa.alphanumericNFA(asteriskNFA, prevNFA.getExitingState(), fromState, null);
                                //m.out.println("Start state: " + tempNfa.getStartState());
                                //m.out.println("Exiting state: " + tempNfa.getExitingState());

                                //m.out.println("PrevNaf ss: " + prevNFA.getStartState());
                                //m.out.println("PrevNaf ee: " + prevNFA.getExitingState());
                                //m.out.println("***************** ");

                                // tempNfa.addTransition(prevNFA.getExitingState(), asteriskNFA.getStartState(), null);
                                tempNfa =   tempNfa.AddTwoNFAs(tempNfa, prevNFA);

                                //m.out.println("NFA after Adding two nfas wo: " + tempNfa);
                                //m.out.println("tempNfa start state ss: " + tempNfa.getStartState());
                                //m.out.println("tempNfa exiting state ee: " + tempNfa.getExitingState());
                                // 
                                i++; 
                                stack.remove(stack.size()-1);

                                stack.add(tempNfa);
                            }
                            else if(nextChar == '+')
                            {
                                //m.out.println( "prev char is an NFA "+ c + "and next char is character is: +");

                                NFA prevNFA = (NFA) stack.get(stack.size()-1);
                                NFA tempNfa = new NFA();
                                NFA oneOrMoreNFA = new NFA();

                                State from = new State("q"+numOfState, "normal");
                                numOfState++;

                                State to = new State("q"+numOfState, "normal");
                                numOfState++;

                                State fromState = new State("q" + numOfState, "normal");
                                numOfState++;

                                State fromState2 = new State("q" + numOfState, "normal");
                                numOfState++;

                                tempNfa = tempNfa.alphanumericNFA(null, from, to, stringToTest.charAt(i) + "");
                                tempNfa = tempNfa.oneOrMore_PlusNFA(fromState,fromState2, tempNfa);

                                //m.out.println("TavaPano: " + tempNfa);
                                //m.out.println("SS tempnfsss : " + tempNfa.getStartState());
                                //m.out.println("EE tempss: " + tempNfa.getExitingState() );

                                //m.out.println(")))))))))))))))))))))))");
                                //m.out.println("Prev info: " + prevNFA);
                                //m.out.println("ss: " + prevNFA.getStartState());
                                //m.out.println("ee: " + prevNFA.getExitingState());

                                tempNfa.addTransition(prevNFA.getExitingState(), tempNfa.getStartState(), null);
                                tempNfa.addState(fromState2);
                                tempNfa.setExitingState(fromState2);
                                tempNfa.setStartState(prevNFA.getStartState());

                                //m.out.println("TavaPannno: " + tempNfa);
                                //m.out.println("SS tempnfsss : " + tempNfa.getStartState());
                                //m.out.println("EE tempss: " + tempNfa.getExitingState() );
                    

                                tempNfa = tempNfa.AddTwoNFAsNotConcatenate(tempNfa, prevNFA);

                                //m.out.println("TavaPano after adding : " + tempNfa);
                                //m.out.println("SS tempnfsss : " + tempNfa.getStartState());
                                //m.out.println("EE tempss: " + tempNfa.getExitingState() );

                                i++;
                                stack.remove(stack.size()-1);

                                stack.add(tempNfa);

                            }
                            else if(nextChar == '?')
                            {
                                //m.out.println( "prev char is an NFA "+ c + "and next char is character is: ?");

                                NFA prevNFA = (NFA) stack.get(stack.size()-1);
                                NFA tempNfa = new NFA();
                                NFA optionalNFA = new NFA();

                                State from = new State("q"+numOfState, "normal");
                                numOfState++;

                                State to = new State("q"+numOfState, "normal");
                                numOfState++;

                                State fromState = new State("q" + numOfState, "normal");
                                numOfState++;

                                State fromState2 = new State("q" + numOfState, "normal");
                                numOfState++;

                                tempNfa = tempNfa.alphanumericNFA(null, from, to, stringToTest.charAt(i) + "");
                                optionalNFA = tempNfa.optionalNFA(fromState,fromState2, tempNfa);

                                tempNfa.addTransition(prevNFA.getExitingState(), optionalNFA.getStartState(), null);
                                tempNfa.addState(fromState2);
                                tempNfa.setExitingState(fromState2);
                                tempNfa.setStartState(prevNFA.getStartState());

                                tempNfa = tempNfa.AddTwoNFAs(tempNfa, prevNFA);
                                tempNfa.setStartState(prevNFA.getStartState());

                                i++;
                                stack.remove(stack.size()-1);

                                stack.add(tempNfa);
                            }
                            else if(nextChar == '|' ||nextChar == '(' || nextChar == ')' || isAlphanumeric(nextChar))
                            {
                                //m.out.println( "prev char is an NFA " + c + " and next char is character is: or (_) alphanumeric " + nextChar);

                                NFA prevNFA = (NFA) stack.get(stack.size()-1);
                                NFA tempNfa = new NFA();

                                State from = prevNFA.getExitingState();
                                State to = new State("q" + numOfState, "normal");
                                numOfState++;

                                tempNfa = tempNfa.alphanumericNFA(prevNFA,from,to, stringToTest.charAt(i)+ "");

                            }
                           
                        }
                        else
                        {
                            //m.out.println( "prev char is an NFA " + c + " and no next char");

                            NFA prevNFA = (NFA) stack.get(stack.size()-1);
                            NFA tempNfa = new NFA();

                            State from = prevNFA.getExitingState();
                            State to = new State("q" + numOfState, "normal");
                            numOfState++;

                            tempNfa = tempNfa.alphanumericNFA(prevNFA,from,to, stringToTest.charAt(i)+ "");
                        }
                    }
                }
                else 
                {
                    // starts with an alphanumeric

                    if(isAlphanumeric(stringToTest.charAt(i)))
                    {
                        
                        char cc = ' ';
                        if((i+1) < stringToTest.length())
                          cc = stringToTest.charAt(i+1);

                        if(cc != ' ') // not done traversing through the stringToTest
                        {
                            //check if next char is a special char

                            if(cc == '*' || cc == '+' || cc == '?' )
                            {
                                if(cc == '*')
                                {
                                    //m.out.println( "prev char is: " + c + "and next char is character is: *");


                                    NFA tempNfa = new NFA();
                                    NFA asteriskNFA = new NFA();
                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;

                                    State fromState3 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    asteriskNFA = tempNfa.asteriskNFA(fromState2, tempNfa);

                                    tempNfa = asteriskNFA.alphanumericNFA(asteriskNFA, asteriskNFA.getExitingState(), fromState3, null);

                                    tempNfa = asteriskNFA;
                                    i++;
                                
                                    stack.add(tempNfa);
                                }
                                else if (cc == '+')
                                {
                                    //m.out.println( "prev char is: " + c + "and next char is character is: +");

                                    NFA tempNfa = new NFA();
                                    NFA plusNFA = new NFA();

                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;

                                    State fromState3 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    plusNFA = tempNfa.oneOrMore_PlusNFA(fromState2, fromState3 , tempNfa);

                                    // tempNfa = asteriskNFA.alphanumericNFA(asteriskNFA, asteriskNFA.getExitingState(), fromState3, null);


                                    tempNfa = plusNFA;
                                    i++;
                                    //m.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);

                                }
                                else if (cc == '?')
                                {
                                    //m.out.println( "prev char is: " + c + "and next char is character is: ?");

                                    NFA tempNfa = new NFA();
                                    NFA optionalNFA = new NFA();

                                    State fromState = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    State toState = new State("q"+ numOfState, "normal");
                                    numOfState++;

                                    State fromState2 = new State("q"+numOfState, "normal");
                                    numOfState++;

                                    State fromState3 = new State("q"+numOfState, "normal");
                                    numOfState++;
                                    
                                    tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                                    optionalNFA = tempNfa.optionalNFA(fromState2, fromState3 , tempNfa);

                                    tempNfa = optionalNFA;
                                    i++;
                                    //m.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);
                                }
                            }
                            else if(cc == '(' || cc == ')' || isAlphanumeric(cc) || cc == '|'  )
                            {
                                //m.out.println( "prev char is: " + c + "and next char is character is: (<), (|), (char), ()) ");

                                NFA tempNfa = new NFA();

                                State fromState = new State("q"+numOfState, "normal");
                                numOfState++;
                                State toState = new State("q"+ numOfState, "normal");
                                numOfState++;
                                
                                tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");
                            
                                stack.add(tempNfa);

                            }
                        }
                        else // no next thing to check 
                        {
                            //m.out.println( "prev char is: " + c + "and no NEXT cgar");

                            NFA tempNfa = new NFA();

                            State fromState = new State("q"+numOfState, "normal");
                            numOfState++;
                            State toState = new State("q"+ numOfState, "normal");
                            numOfState++;
                            
                            tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");

                            stack.add(tempNfa);
                        }
                    }

                }
            }
            else if(stringToTest.charAt(i) == '|') //prev can be ) ,NFA, *, +, ?
            {
                stack.add(stringToTest.charAt(i));
                //m.out.println( "just added to stack");

            }
            else if(stringToTest.charAt(i) == '*') // prev element in stack can be ) or nfa 
            {
                //m.out.println(stringToTest.charAt(i) + " is a *");
                ArrayList<Object> tempStack = new ArrayList<Object>();
                int tempIndex = 0;
                NFA tempNFA = new NFA();

                if(stack.get(stack.size()-1) instanceof Character) // prev ')'
                {
                    char c = (char) stack.get(stack.size()-1);

                    if(c == ')' )
                    {
                        //find nearest (
                        for (int j = stack.size()-1; j >= 0; j--)
                        {
                            if(stack.get(j) instanceof Character)
                            {
                                char c2 = (char) stack.get(j);
                                if(c2 == '(')
                                {
                                    //find nearest NFA
                                    tempIndex = j;
                                    break;
                                }
                            }
                        }

                        //create a subset of the stack from the nearest '(' to the nearest ')'
                        for (int j = tempIndex; j < stack.size(); j++)
                        {
                            tempStack.add(stack.get(j));
                        }
                        //m.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
            
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    //m.out.println("* withh last thing NFA: " + stringToTest.charAt(i));
                    //concatenate
                    NFA prevNFA = (NFA) stack.get(stack.size()-1);
                    NFA tempNfa = new NFA();
                    State fromState = new State("q"+numOfState, "normal");

                    numOfState++;

                    tempNfa = tempNfa.asteriskNFA(fromState, prevNFA);
                    // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");
                    stack.remove(stack.size()-1);//remove previous item in stack 

                    // stack.add(tempNfa);

                    stack.add(tempNfa);

                }
            
            }
            else if(stringToTest.charAt(i) == '+') // prev element in stack can be ) or nfa 
            {
                if(stack.get(stack.size()-1) instanceof Character) // prev ')'
                {
                    char c = (char) stack.get(stack.size()-1);
                    int tempIndex = -1;
                    ArrayList<Object> tempStack = new ArrayList<Object>();
                    NFA tempNFA = new NFA();

                   
                    if(c == ')' )
                    {
                        //find nearest (
                        for (int j = stack.size()-1; j >= 0; j--)
                        {
                            if(stack.get(j) instanceof Character)
                            {
                                char c2 = (char) stack.get(j);
                                if(c2 == '(')
                                {
                                    //find nearest NFA
                                    tempIndex = j;
                                    break;
                                }
                            }
                        }

                        //create a subset of the stack from the nearest '(' to the nearest ')'
                        for (int j = tempIndex; j < stack.size(); j++)
                        {
                            tempStack.add(stack.get(j));
                        }
                        //m.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    //m.out.println("* with lasttt thing NFA: " + stringToTest.charAt(i));
                    //concatenate
                    NFA prevNFA = (NFA) stack.get(stack.size()-1);
                    NFA tempNfa = new NFA();
                    State fromState = new State("q"+numOfState, "normal");
                    numOfState++;

                    State toState = new State("q"+ numOfState, "normal");
                    numOfState++;

                    tempNfa = tempNfa.oneOrMore_PlusNFA(fromState, toState ,prevNFA);
                    stack.remove(stack.size()-1);//remove previous item in stack
                    
                    tempNfa = tempNfa.AddTwoNFAsNotConcatenate(tempNfa, prevNFA);
                    tempNfa.setStartState(fromState);
                    tempNfa.setExitingState(toState);
                  
                    stack.add(tempNfa);
                }

            }
            else if(stringToTest.charAt(i) == '?')
            {
                if(stack.get(stack.size()-1) instanceof Character) // prev ')'
                {
                    char c = (char) stack.get(stack.size()-1);
                    int tempIndex = -1;
                    ArrayList<Object> tempStack = new ArrayList<Object>();
                    NFA tempNFA = new NFA();

                   
                    if(c == ')' )
                    {
                        //find nearest (
                        for (int j = stack.size()-1; j >= 0; j--)
                        {
                            if(stack.get(j) instanceof Character)
                            {
                                char c2 = (char) stack.get(j);
                                if(c2 == '(')
                                {
                                    //find nearest NFA
                                    tempIndex = j;
                                    break;
                                }
                            }
                        }

                        //create a subset of the stack from the nearest '(' to the nearest ')'
                        for (int j = tempIndex; j < stack.size(); j++)
                        {
                            tempStack.add(stack.get(j));
                        }
                        //m.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    //m.out.println("* with last thingg NFA: " + stringToTest.charAt(i));
                    //concatenate
                    NFA prevNFA = (NFA) stack.get(stack.size()-1);
                    NFA tempNfa = new NFA();
                    State fromState = new State("q"+numOfState, "normal");
                    numOfState++;

                    State toState = new State("q"+ numOfState, "normal");
                    numOfState++;

                    //TODO: change all the other stuff

                    // tempNfa = tempNfa.asteriskNFA(fromState, prevNFA);
                    tempNfa = tempNfa.optionalNFA(fromState, toState ,prevNFA);
                    


                    stack.remove(stack.size()-1);//remove previous item in stack 

                    tempNfa = tempNfa.AddTwoNFAsNotConcatenate(tempNfa, prevNFA);
                    tempNfa.setStartState(fromState);
                    tempNfa.setExitingState(toState);

                    stack.add(tempNfa);
                }
            }
            
        }

        //m.out.println("End of string reached");
        // All is done now resolve the stack to NFA

      
            NFA finalNFA = resolveObjectsToNFA(stack);

            stack.clear();
    
            // finalNFA.removeDuplicateTransitions();
            //m.out.println("Stack cleared " + finalNFA);
    
    
            stack.add(finalNFA);
    
       
        nfa = finalNFA;

        //m.out.println("=================Final Stack Stack size:" + stack.size() + " =======================");

        //m.out.println("Stack to String:  " + stack.get(0) + " " + stack.size());
        //m.out.println("Start State : " + finalNFA.getStartState() );
        //m.out.println("Final State: " + finalNFA.getExitingState());

        return nfa; 
    }

    public  NFA resolveObjectsToNFA(ArrayList<Object> tempStack) {

        //color output in orange
        //m.out.println("\u001B[33m Stack to resolve: ---------" + tempStack +" sized: " + tempStack.size() +   " \u001B[0m");

        //remove firs element in stack
        if(tempStack.get(0) instanceof Character)
        {
            char c = (char) tempStack.get(0);
            if(c == '(')
            {
                tempStack.remove(0);
            }
        }

        if(tempStack.size() == 1) //only one nfa
        {
            if(tempStack.get(0) instanceof NFA)
            {
                //m.out.println("\u001B[30m Resolved to: ---------\n" + (NFA) tempStack.get(0) + " \u001B[0m");
                //m.out.println("\u001B[31m Start state: ---------\n" + ((NFA) tempStack.get(0)).getStartState() + " \u001B[0m");
                //m.out.println("\u001B[31m Exit state: ---------\n" + ((NFA) tempStack.get(0)).getExitingState() + " \u001B[0m");

                return (NFA) tempStack.get(0);
            }
        }
        else  //more than one nfa 
        {
            //count number off 

            int count = 0;

            for (int i = 0; i < tempStack.size(); i++)
            {
                if(tempStack.get(i) instanceof Character)
                {
                    char c = (char) tempStack.get(i);
                    if(c == '|')
                    {
                        count++;
                    }
                }
            }

            if(count > 0)
            {
                
            //if temp stack has a | as one of the elements

                ArrayList<Object> stackToAlter = new ArrayList<Object>(tempStack);
            
                for(Object obj : tempStack)
                {
                    if(obj instanceof Character)
                    {
                        char c = (char) obj;
                        if(c == '|')
                        {
                            //union operator 
                            NFA nfa = (NFA) tempStack.get(tempStack.indexOf(obj)-1); 
                            NFA nfa2 = (NFA) tempStack.get(tempStack.indexOf(obj)+1);

                            State fromState = new State("q"+numOfState, "normal");
                            numOfState++;
                            State toState = new State("q"+numOfState, "normal");
                            numOfState++;

                            //m.out.println("\u001B[31m First NFA: ---------" + nfa + " \u001B[0m");
                            //m.out.println("Frst nfa ss: " + nfa.getStartState());
                            //m.out.println("Frst nfa es: " + nfa.getExitingState());
                            //m.out.println(" --------------------------------------------------");
                            //m.out.println("\u001B[31m Second NFA: ---------" + nfa2 + " \u001B[0m");
                            //m.out.println("Second nfa ss: " + nfa2.getStartState());
                            //m.out.println("Second nfa es: " + nfa2.getExitingState());

                            NFA tempNFA = new NFA();
                            tempNFA = tempNFA.unionNFA(fromState, toState , nfa, nfa2);
                            tempNFA.addTransition(nfa.getExitingState(), toState, null);
                            tempNFA.addTransition(nfa2.getExitingState(), toState, null);
                            // unionNfa
                            //remove the two nfa andnn the | from the sttack
                            stackToAlter.remove(nfa);
                            stackToAlter.remove(stackToAlter.indexOf(obj));
                            stackToAlter.remove(nfa2);

                        

                            //m.out.println("Temp stack after removal of stuff: " + stackToAlter + " Sized: " + stackToAlter.size());

                            stackToAlter.add(0, tempNFA);

                            // recursive call to nnresolveObjectsToNFA
                            //m.out.println("recursive call to resolveObjectsToNFA with tempStack: " + stackToAlter + " Sized: " + stackToAlter.size());
                            tempNFA = resolveObjectsToNFA(stackToAlter);
                            return tempNFA;
                        }
                    }
                }
        }
        else
        {
            //many nfas next to each other 

            //check if all elements of tempStack are nfas
            //m.out.println("All elements are nfas");

            //color output in light blue
            //m.out.println("\033[94m Tempstack at the moment: " + tempStack +" sized: " + tempStack.size() +   " \033[0m");



            boolean allNFA = true;

            for (int i = 0; i < tempStack.size(); i++)
            {
                if(!(tempStack.get(i) instanceof NFA))
                {
                    allNFA = false;
                }
            }

            // ab* or (a?b*)?

            if(allNFA == true)
            {
                NFA first = (NFA) tempStack.get(0);
                NFA second = (NFA) tempStack.get(1);
                NFA newNFNfa = new NFA();

                newNFNfa = newNFNfa.joinTwoNFAs(first, second);



                tempStack.remove(0);
                tempStack.remove(0);
                tempStack.add(0, newNFNfa);

                //m.out.println("Temp stack after removal of stuff: " + tempStack + " Sized: " + tempStack.size());

                // recursive call to resolveObjectsToNFA

                //m.out.println("recursive call to resolveObjectsToNFA with tempStack: " + tempStack + " Sized: " + tempStack.size());
                newNFNfa = resolveObjectsToNFA(tempStack);

                return (NFA) tempStack.get(0);


            }


        }
    }


        return (NFA) tempStack.get(0);
    }

    private static NFA instanceOf(Object object) {
        NFA nfa = new NFA();
        return  nfa;
    }

    private static boolean isAlphanumeric(char charAt) {
        if((Character.isLowerCase(charAt)) || (Character.isDigit(charAt)) || (Character.isUpperCase(charAt))  )
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
