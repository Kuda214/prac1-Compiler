import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.PatternSyntaxException;


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
        System.out.println(stringToTest + " " + stringToTest);
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

        collectSubstrings(stringToTest, isStart);


        
    }

    public static void collectSubstrings(String stringToTest, boolean isStart )
    {
        
        for(int i =0; i < stringToTest.length(); i++) {
            
           
            if(stringToTest.charAt(i) == '(' || stringToTest.charAt(i) == ')') {
                stack.add(stringToTest.charAt(i));
                
                if(stringToTest.charAt(i) == '(')
                {
                    //find nearest )
                  
                }
                else if(stringToTest.charAt(i) == ')' )
                {
                    //find nearest (
                    for (int j = i; j >= 0; j--)
                    {

                    }
                }
            }
            else if((Character.isLowerCase(stringToTest.charAt(i))) || (Character.isDigit(stringToTest.charAt(i)))  ) {
                    //alphabet or digit

                    // stack.add(stringToTest.charAt(i));

                    //concatenate
                    if(stack.get(stack.size()-1) instanceof Character)
                    {
                        //concatenate
                        NFA tempNfa = new NFA();
                        State fromState = new State("q"+numOfState, "normal");
                        numOfState++;
                        State toState = new State("q"+ numOfState, "normal");
                        numOfState++;

                        tempNfa.alphanumericNFA(fromState, toState, stringToTest.charAt(i)+ "");

                        stack.add(tempNfa);
                    }
                    else if(stack.get(stack.size()-1) instanceof NFA)
                    {
                        //concatenate
                        NFA prevNFA = (NFA) stack.get(stack.size()-1);
                        NFA tempNfa = new NFA();
                        State fromState = prevNFA.getExitingState() ;
                        State toState = new State("q"+ numOfState, "normal");
                        numOfState++;

                        tempNfa.alphanumericNFA(fromState, toState, stringToTest.charAt(i)+ "");
                        stack.remove(stack.size()-1);
                        stack.add(tempNfa);
                    }
            }
            if(stringToTest.charAt(i) == '|')
            {
                stack.add(stringToTest.charAt(i));

            }              
        }
    }

    private static boolean isLetterOrDigit(char charAt) {
        return false;
    }

  

}