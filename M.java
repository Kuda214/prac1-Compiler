import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Stack;
import java.util.regex.PatternSyntaxException;

import javax.lang.model.util.ElementScanner14;


public class M {
     
    static Scanner scanner = new Scanner(System.in);
 

    // ab* | (a?b*)+


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

    private static void createNFA(String stringToTest) {

        //call the function to convert to NFA nn
        System.out.println("Converting to NFA...");
        to_NFA nfa = new to_NFA();

        NFA nfaObj = nfa.regex_to_NFA(stringToTest);
        nfaObj = nfaObj.reFormatNFA();

        System.out.println(nfaObj);

        DFA dfa = new DFA(nfaObj);

        System.out.println("NFA is: " + nfaObj.toString());

        dfa = dfa.to_DFA(nfaObj );

        minDFA minDFA = new minDFA(dfa);

        minDFA = minDFA.minimizeDFA();



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
  
}

