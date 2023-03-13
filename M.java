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
                    System.out.println( "substring is:  " + tempStack);


                    tempNFA = resolveObjectsToNFA(tempStack);
                

                }
            }
            else if(isAlphanumeric(stringToTest.charAt(i))) {
                
                char c = ' ';
                if(stack.get(stack.size()-1) instanceof Character)
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
                                    System.out.println( "prev char is: " + c + "and next char is character is: *");


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

                                    tempNfa = asteriskNFA.alphanumericNFA(asteriskNFA, fromState2, toState, stringToTest);

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

                                tempNfa = tempNfa.alphanumericNFA(null, from, to, stringToTest.charAt(i) + "");
                                asteriskNFA = tempNfa.asteriskNFA(fromState, tempNfa);

                                asteriskNFA.addTransition(prevNFA.getExitingState(), fromState, null);

                                i++;

                                stack.add(asteriskNFA);

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

                                oneOrMoreNFA.addTransition(prevNFA.getExitingState(), fromState, null);

                                i++;

                                stack.add(oneOrMoreNFA);
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

                                optionalNFA.addTransition(fromState, prevNFA.getExitingState(), null);
                                i++;

                                stack.add(optionalNFA);
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
            }
            else if(stringToTest.charAt(i) == '|') //prev can be ) ,NFA, *, +, ?
            {
                stack.add(stringToTest.charAt(i));
                System.out.println( "just added | to stack");

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
                    System.out.println("* with last thing NFA: " + stringToTest.charAt(i));
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

                    tempNfa = tempNfa.asteriskNFA(fromState, prevNFA);
                    // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");
                    stack.remove(stack.size()-1);//remove previous item in stack 

                    // stack.add(tempNfa);

                    stack.add(tempNfa);

                }

            }
            else if(stringToTest.charAt(i) == '?')
            {

            }
            
        }

        System.out.println("=================Final Stack Stack size:" + stack.size() + " =======================");

        System.out.println("Stack to String:  " + stack + " " + stack.size());

  

    };

    private static NFA resolveObjectsToNFA(ArrayList<Object> tempStack) {

        System.out.println("Stack to resolve: ---------" + tempStack   );

        NFA tempNFA = new NFA();


        

        return null;
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