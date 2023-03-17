import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Stack;
import java.util.regex.PatternSyntaxException;

import javax.lang.model.util.ElementScanner14;


public class M {
     
    static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Object> stack = new ArrayList<Object>();
    NFA nfa = new NFA();
    State startState = new State("A", "start");
    State finalState = new State("B", "final");
    static int numOfState = 0;


    public static void main(String[] args) {

        boolean isPureRegex = false;
        String stringToTest = scanner.nextLine();

        isPureRegex =  ifPureRegex(stringToTest);

        if( isPureRegex ) {
            //convert to nfa
            
            createNFA(stringToTest);
            

        } else {
            System.out.println("NOT a Pure regex.");
        }
    }

    public static boolean ifPureRegex(String stringToTest) {
        boolean isSyntacticallyCorrect = false;

        isSyntacticallyCorrect =  ifSyntacticallyCorrect(stringToTest);

        if (isSyntacticallyCorrect) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean ifSyntacticallyCorrect(String stringToTest) {
        try {
            // Attempt to compile the given regex string
            java.util.regex.Pattern.compile(stringToTest);
            return true;
        } catch (PatternSyntaxException e) {
            // The given string is not a valid regular expression
            return false;
        }
    }

    public static void createNFA(String stringToTest) {
        //create NFA
        boolean isStart = true;

        collectSubstrings(stringToTest);
   
    }

    public static void collectSubstrings(String stringToTest )
    {        
        for(int i =0; i < stringToTest.length(); i++) {

            // String color = "\u001B[32m"; //light green

            //text in light green 
            System.out.println( "\u001B[32m" +stringToTest.charAt(i) + " is at index: " + i + " and stack is: " + stack + "\u001B[0m");
            
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

                    System.out.println( "tempIndex is:  " + tempIndex);

                    //create a subset of the stack from the nearest '(' to the nearest ')'
                    for (int j = tempIndex; j < stack.size(); j++)
                    {
                        tempStack.add(stack.get(j));

                    }
                    System.out.println( "substring is:  " + tempStack);

                    tempNFA = resolveObjectsToNFA(tempStack);

                    //remove the subset from the stack
                    System.out.println("temp index: " + tempIndex + " thats: " + stack.get(tempIndex) );
                    
                    ArrayList<Object> tempStack2 = new ArrayList<Object>();

                    for (int j = 0; j < tempIndex; j++)
                    {
                        tempStack2.add(stack.get(j));
                    }

                    stack = tempStack2;

                    System.out.println("After removing: " + stack + " size: " + stack.size());
                    stack.add(tempNFA);
                    System.out.println("After Adding combined nfa: " + stack + " size: " + stack.size());

                }
            }
            else if(isAlphanumeric(stringToTest.charAt(i))) {
                
                char c = ' ';
                
                if(stack.size() > 0 &&  stack.get(stack.size()-1) instanceof Character)
                    c = (char) stack.get(stack.size()-1);

                if(stack.size() > 0) //starts with (
                {
                    System.out.println(stringToTest.charAt(i) + " is a character");

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
                                    System.out.println( "prev chaar is: " + c + "and next char is character is: *");


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
                                    System.out.println( "prev char is: " + c + "and next char is character is: +");

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
                                    System.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);

                                }
                                else if (cc == '?')
                                {
                                    System.out.println( "prev char is: " + c + "and next char is character is: ?");

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
                                    System.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);
                                }
                            }
                            else if(cc == '(' || cc == ')' || isAlphanumeric(cc) || cc == '|'  )
                            {
                                System.out.println( "prev char is: " + c + "and next char is character is: (<), (|), (char), ()) ");

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
                            System.out.println( "prev char is: " + c + "and no NEXT cgar");

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
                        System.out.println(c + " prev is an NFA ");

                        char nextChar = ' ';

                        if((i+1) < stringToTest.length())
                            nextChar = stringToTest.charAt(i+1);

                        if(nextChar != ' ') // not done traversing through the stringToTest
                        {
                            if(nextChar == '*')
                            {   
                                System.out.println( "prev char is an NFA "+ c + "and next char is character is: *");

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
                                tempNfa = tempNfa.alphanumericNFA(asteriskNFA, prevNFA.getExitingState(), fromState, null);
                                tempNfa =   tempNfa.AddTwoNFAs(tempNfa, prevNFA);

                                i++; 
                                stack.remove(stack.size()-1);

                                stack.add(tempNfa);
                            }
                            else if(nextChar == '+')
                            {
                                System.out.println( "prev char is an NFA "+ c + "and next char is character is: +");

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
                                oneOrMoreNFA = tempNfa.oneOrMore_PlusNFA(fromState,fromState2, tempNfa);

                                tempNfa.addTransition(prevNFA.getExitingState(), oneOrMoreNFA.getStartState(), null);
                                tempNfa.addState(fromState2);
                                tempNfa.setExitingState(fromState2);
                                tempNfa.setStartState(prevNFA.getStartState());
                    

                                tempNfa = tempNfa.AddTwoNFAs(tempNfa, prevNFA);

                                i++;
                                stack.remove(stack.size()-1);

                                stack.add(tempNfa);

                            }
                            else if(nextChar == '?')
                            {
                                System.out.println( "prev char is an NFA "+ c + "and next char is character is: ?");

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
                                System.out.println( "prev char is an NFA " + c + " and next char is character is: or (_) alphanumeric " + nextChar);

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
                            System.out.println( "prev char is an NFA " + c + " and no next char");

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
                                    System.out.println( "prev char is: " + c + "and next char is character is: *");


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
                                    System.out.println( "prev char is: " + c + "and next char is character is: +");

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
                                    System.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);

                                }
                                else if (cc == '?')
                                {
                                    System.out.println( "prev char is: " + c + "and next char is character is: ?");

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
                                    System.out.println("NFA after plus operator: " + tempNfa);
                                
                                    stack.add(tempNfa);
                                }
                            }
                            else if(cc == '(' || cc == ')' || isAlphanumeric(cc) || cc == '|'  )
                            {
                                System.out.println( "prev char is: " + c + "and next char is character is: (<), (|), (char), ()) ");

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
                            System.out.println( "prev char is: " + c + "and no NEXT cgar");

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
                System.out.println( "just added to stack");

            }
            else if(stringToTest.charAt(i) == '*') // prev element in stack can be ) or nfa 
            {
                System.out.println(stringToTest.charAt(i) + " is a *");
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
                        System.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
            
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    System.out.println("* withh last thing NFA: " + stringToTest.charAt(i));
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
                        System.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    System.out.println("* with last thing NFA: " + stringToTest.charAt(i));
                    //concatenate
                    NFA prevNFA = (NFA) stack.get(stack.size()-1);
                    NFA tempNfa = new NFA();
                    State fromState = new State("q"+numOfState, "normal");
                    numOfState++;

                    State toState = new State("q"+ numOfState, "normal");
                    numOfState++;

                    // tempNfa = tempNfa.asteriskNFA(fromState, prevNFA);
                    tempNfa = tempNfa.oneOrMore_PlusNFA(fromState, toState ,prevNFA);
                    // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");

                    tempNfa.addTransition(tempNfa.getExitingState() , fromState, null);
                    stack.remove(stack.size()-1);//remove previous item in stack 

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
                        System.out.println( "substring is:  " + tempStack);


                        tempNFA = resolveObjectsToNFA(tempStack);
                    }
                }
                else if(stack.get(stack.size()-1) instanceof NFA) //prev is a NFA
                {
                    System.out.println("* with last thingg NFA: " + stringToTest.charAt(i));
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
                    // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");
                    // tempNfa.addTransition( fromState, toState, null);

                    System.out.println(" )))))) tmempNFA in this sunshine of ray : " + tempNfa);

                    stack.remove(stack.size()-1);//remove previous item in stack 

                    stack.add(tempNfa);
                }
            }
            
        }

        System.out.println("End of string reached");
        // All is done now resolve the stack to NFA

      
            NFA finalNFA = resolveObjectsToNFA(stack);

            stack.clear();
    
            // finalNFA.removeDuplicateTransitions();
            System.out.println("Stack cleared " + finalNFA);
    
    
            stack.add(finalNFA);
    
       
      

        System.out.println("=================Final Stack Stack size:" + stack.size() + " =======================");

        System.out.println("Stack to String:  " + stack.get(0) + " " + stack.size());
        System.out.println("Start State : " + finalNFA.getStartState() );
        System.out.println("Final State: " + finalNFA.getExitingState());

        return;

  

    };

    private static NFA resolveObjectsToNFA(ArrayList<Object> tempStack) {

        

        //color output in orange
        System.out.println("\u001B[33m Stack to resolve: ---------" + tempStack +" sized: " + tempStack.size() +   " \u001B[0m");

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
                System.out.println("\u001B[30m Resolved to: ---------\n" + (NFA) tempStack.get(0) + " \u001B[0m");
                System.out.println("\u001B[31m Start state: ---------\n" + ((NFA) tempStack.get(0)).getStartState() + " \u001B[0m");
                System.out.println("\u001B[31m Exit state: ---------\n" + ((NFA) tempStack.get(0)).getExitingState() + " \u001B[0m");

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

                            System.out.println("\u001B[31m First NFA: ---------" + nfa + " \u001B[0m");
                            System.out.println("\u001B[31m Second NFA: ---------" + nfa2 + " \u001B[0m");

                            NFA tempNFA = new NFA();
                            tempNFA = tempNFA.unionNFA(fromState, toState , nfa, nfa2);
                            tempNFA.addTransition(nfa.getExitingState(), toState, null);
                            tempNFA.addTransition(nfa2.getExitingState(), toState, null);
                            
                            //remove the two nfa andnn the | from the sttack
                            stackToAlter.remove(nfa);
                            stackToAlter.remove(stackToAlter.indexOf(obj));
                            stackToAlter.remove(nfa2);

                        

                            System.out.println("Temp stack after removal of stuff: " + stackToAlter + " Sized: " + stackToAlter.size());

                            stackToAlter.add(0, tempNFA);

                            // recursive call to nnresolveObjectsToNFA
                            System.out.println("recursive call to resolveObjectsToNFA with tempStack: " + stackToAlter + " Sized: " + stackToAlter.size());
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
            System.out.println("All elements are nfas");

            //color output in light blue
            System.out.println("\033[94m Tempstack at the moment: " + tempStack +" sized: " + tempStack.size() +   " \033[0m");



            boolean allNFA = true;

            for (int i = 0; i < tempStack.size(); i++)
            {
                if(!(tempStack.get(i) instanceof NFA))
                {
                    allNFA = false;
                }
            }

            if(allNFA == true)
            {
                NFA first = (NFA) tempStack.get(0);
                NFA second = (NFA) tempStack.get(1);
                NFA newNFNfa = new NFA();

                newNFNfa = newNFNfa.joinTwoNFAs(first, second);



                tempStack.remove(0);
                tempStack.remove(0);
                tempStack.add(0, newNFNfa);

                System.out.println("Temp stack after removal of stuff: " + tempStack + " Sized: " + tempStack.size());

                // recursive call to resolveObjectsToNFA

                System.out.println("recursive call to resolveObjectsToNFA with tempStack: " + tempStack + " Sized: " + tempStack.size());
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


  // System.out.println("\u001B[39m prev is NFA " + stringToTest.charAt(i) + " \u001B[0m");
                        // //concatenate
                        // NFA prevNFA = (NFA) stack.get(stack.size()-1);
                        // NFA tempNfa = new NFA();
                        // State fromState = prevNFA.getExitingState() ;
                        // // State fromState = new State("q"+numOfState, "normal");

                        // State toState = new State("q"+ numOfState, "normal");
                        // numOfState++;

                        // tempNfa = tempNfa.alphanumericNFA(prevNFA , fromState, toState, stringToTest.charAt(i)+ "");
                        // // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");
                        // stack.remove(stack.size()-1);//remove previous item in stack 

                        // // stack.add(tempNfa);

                        // stack.add(tempNfa);