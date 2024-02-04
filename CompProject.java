import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class comp {
    // Regular expressions for token categories
    private static String identifierRegularExpression = "[?][a-zA-Z]+";
    private static String integer = "[0-9]+";
    private static String doublenum = "[#][0-9]+.[0-9]+";
    private static String comment = "[$][a-zA-Z@$#]+";
    private static String output = "\'[a-zA-Z]+\'";


    // Lists to hold keywords, symbols, operations, and logical operations
    private static List<String> keywords = new ArrayList<>();
    private static List<String> symbols = new ArrayList<>();
    private static List<String> operations = new ArrayList<>();
    private static List<String> logicalOps = new ArrayList<>();

    // Additional variables
    private static ArrayList<String> fileLines = new ArrayList<>();
    private static ArrayList<String> operators = new ArrayList<>();
    private static String endOfLine;
    private static String endOfWord;
    private static String filename = "C:\\Users\\b-6r\\OneDrive\\Desktop\\test1.txt";

    // DFA states
    enum State { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, QE }

    // Input source file and line counter
    private static int line = 1;

    static {
        // Initialize lists with keywords, symbols, operations, and logical operations
        keywords.add("if");
        keywords.add("else");
        keywords.add("elseif");
        keywords.add("for");
        keywords.add("while");
        keywords.add("int");
        keywords.add("char");
        keywords.add("double");
        keywords.add("string");

        symbols.add("!");
        symbols.add("&");
        symbols.add(";");

        operations.add("+");
        operations.add("-");
        operations.add("*");
        operations.add("/");
        operations.add("=");

        logicalOps.add("||");
        logicalOps.add("&&");
    }

    public static void main(String[] args) {
        // Code application logic here
        scan();
    }

    public static void scan() {
        int lineNumber = 1;
        try {
            Path filePath = Paths.get(filename);
            Files.lines(filePath).forEach(fileLines::add);
    
            for (String line : fileLines) {
                String[] lexemes = getLexemes(line);
                for (String lexeme : lexemes) {
                    String tokenCategory = evaluate(lexeme);
                    System.out.println("<" + lexeme + "," + tokenCategory + ">");
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found!");
        }
    }

    public static String[] getLexemes(String line) {
        return line.split(" ");
    }

    public static boolean isKeyword(String lex) {
        return keywords.contains(lex);
    }

    public static boolean isOperator(String lex) {
        return operations.contains(lex);
    }

    public static boolean isLetter(char entry) {
        return (entry >= 'a' && entry <= 'z') || (entry >= 'A' && entry <= 'Z');
    }

    public static State executeTransition(State currentState, char entry) {
        switch (currentState) {
            case Q0:
                if (isLetter(entry))
                    return State.Q7;
                else if (entry == '0')
                    return State.Q2;
                else if (entry >= '1' && entry <= '9')
                    return State.Q1;
                else
                    return State.QE;
            case Q1:
                if (entry == '.')
                    return State.Q5;
                else if (Character.isDigit(entry))
                    return State.Q1;
                else
                    return State.QE;
            case Q2:
                if (entry == '.')
                    return State.Q3;
                else
                    return State.QE;
            case Q3:
                if (Character.isDigit(entry))
                    return State.Q4;
                else
                    return State.QE;
            case Q4:
                if (Character.isDigit(entry))
                    return State.Q4;
                else
                    return State.QE;
            case Q5:
                if (Character.isDigit(entry))
                    return State.Q6;
                else
                    return State.QE;
            case Q6:
                if (Character.isDigit(entry))
                    return State.Q6;
                else
                    return State.QE;
            default:
                return State.QE;
        }
    }

    public static String evaluate(String str) {
        if (isKeyword(str)) {
            return "keyword";
        } else if (symbols.contains(str)) {
            return "Symbol";
        } else if (isOperator(str)) {
            return "operation";
        } else if (logicalOps.contains(str)) {
            return "Logical Operation";
        }
    
        /*Here's the continuation of the modified code:*/


        else if (Pattern.matches(identifierRegularExpression, str)) {
            return "identifier";
        } else if (Pattern.matches(comment, str)) {
            return "comment";
        } else if (Pattern.matches(integer, str)) {
            return "integer";
        } else if (Pattern.matches(doublenum, str)) {
            return "double";
        } else if (Pattern.matches(output, str)) {
            return "Output to the user";
        } else {
            return "error in token !!! " ;
        }
    }
}