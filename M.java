import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Stack;
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
            
           System.out.println("Char in question: " + stringToTest.charAt(i) + "");
            if(stringToTest.charAt(i) == '(' || stringToTest.charAt(i) == ')') {
                stack.add(stringToTest.charAt(i));
                System.out.println("Stack Now : " + stack + " size:" + stack.size());
                
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
                if(stack.size() > 0){
                    System.out.println(stringToTest.charAt(i) + " is a character");

                    if(stack.get(stack.size()-1) instanceof Character)
                    {
                        //concatenate
                        NFA tempNfa = new NFA();
                        State fromState = new State("q"+numOfState, "normal");
                        numOfState++;
                        State toState = new State("q"+ numOfState, "normal");
                        numOfState++;

                      
                        tempNfa = tempNfa.alphanumericNFA(null, fromState, toState, stringToTest.charAt(i)+ "");
                    

                        stack.add(tempNfa);
                        // System.out.mprimmntln("Stack:mm " + stackn + " size:" + stack.size());

                    }
                    else if(stack.get(stack.size()-1) instanceof NFA)
                    {
                        //concatenate
                        NFA prevNFA = (NFA) stack.get(stack.size()-1);
                        NFA tempNfa = new NFA();
                        State fromState = prevNFA.getExitingState() ;
                        // State fromState = new State("q"+numOfState, "normal");

                        State toState = new State("q"+ numOfState, "normal");
                        numOfState++;

                        tempNfa = tempNfa.alphanumericNFA(prevNFA , fromState, toState, stringToTest.charAt(i)+ "");
                        // tempNfa = tempNfa.concatenation(prevNFA, toState, stringToTest.charAt(i)+ "");
                        stack.remove(stack.size()-1);//remove previous item in stack 

                        // stack.add(tempNfa);

                        stack.add(tempNfa);

                    }
                }

            }
            else if(stringToTest.charAt(i) == '|')
            {
                stack.add(stringToTest.charAt(i));

            }  
            else if(stringToTest.charAt(i) == '*')
            {
                stack.add(stringToTest.charAt(i));
            }
            
        }

        System.out.println("=================Final Stack Stack size:" + stack.size() + " =======================");

        System.out.println("Stack to String:  " + stack + " " + stack.size());
    };
    

    private static NFA instanceOf(Object object) {
        NFA nfa = new NFA();
        return  nfa;
    }

    private static boolean isLetterOrDigit(char charAt) {
        return false;

    }

  

}