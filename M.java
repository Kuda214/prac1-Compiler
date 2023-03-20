import java.io.File;
import java.io.FileWriter;
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
        System.out.println("Enter a regex: ");
        String stringToTest = scanner.nextLine();

        if(stringToTest.isEmpty()) {
            return;
        }

        isPureRegex =  ifPureRegex(stringToTest);

        if( isPureRegex ) {
            //convert to nfa
            
            createNFA(stringToTest);
            

        } else {
            System.out.println("NOT a Pure regex.");
        }
    }

    private static void createNFA(String stringToTest) {

        //call the function to connvert to NFA nn
        System.out.println("Converting to NFA...");
        to_NFA nfa = new to_NFA();

        NFA nfaObj = nfa.regex_to_NFA(stringToTest);
        nfaObj = nfaObj.reFormatNFA();

        System.out.println(nfaObj);

        DFA dfa = new DFA(nfaObj);

        System.out.println("NFA is: " + nfaObj.toString());

        dfa = dfa.to_DFA(nfaObj );

        minDFA minDFA = new minDFA(dfa);

        DFA minimizedDFA = new DFA(nfaObj);

        minimizedDFA = minDFA.minimizeDFA();

        exportMinimizedDFAToXMLFile(minimizedDFA);



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

    private static void exportMinimizedDFAToXMLFile(DFA minimizedDFA) {
        // TODO Auto-generated method stub

        //color the output in the console green

        System.out.println("  Exporting to XML file...  ");
        // System.out.println(minimizedDFA);

        try {
            //create a new xml file
            FileWriter file = new FileWriter("minimizedDFA.xml");
            
            file.write("<MinimizedDFA>\n");
            file.write("<States>\n");

            for(int y=0; y<minimizedDFA.states.size() ; y++)
            {
                file.write("<"+minimizedDFA.states.get(y).getStateLabel() +" ");
                if(minimizedDFA.states.get(y).getStateType().equals("start"))
                {
                    file.write("type=\"start\" ");
                }
                else if(minimizedDFA.states.get(y).getStateType().equals("final"))
                {
                    file.write("type=\"final\" ");
                }
                else if(minimizedDFA.states.get(y).getStateType().equals("start &&final"))
                {
                    file.write("type=\"start && final\" ");
                }
                file.write(">\n");
              
            }

            file.write("</States>\n");
            file.write("<Transitions>\n");
            for(int r=0; r<minimizedDFA.states.size() ; r++)
            {
                for(int t=0; t<minimizedDFA.states.get(r).getTransitions().size() ; t++)
                {
                    file.write("\t<" + minimizedDFA.states.get(r).getTransitions().get(t).getTransitionFrom().getStateLabel() + "> \n");
                    file.write("\t\t<" + minimizedDFA.states.get(r).getTransitions().get(t).getTransitionTo().getStateLabel() + "> " + 
                                minimizedDFA.states.get(r).getTransitions().get(t).getTransitionValue()  +" </"+ minimizedDFA.states.get(r).getTransitions().get(t).getTransitionTo().getStateLabel() + ">\n");
                    file.write("\t</" + minimizedDFA.states.get(r).getTransitions().get(t).getTransitionFrom().getStateLabel() + ">\n");
                }
            }

            file.write("</Transitions>\n");
            file.write("</MinimizedDFA>\n");
            file.close();

            System.out.println("\033[32m XML file created successfully. Check the project folder for the file.  \033[0m ");

            //save the xml file
        } catch (Exception e) {

        
        }
    }

  
}

