import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

enum Token {

    DELIMETER ("\\$([^\\$]+?)?\\$"),

    COMMENT ("\\/\\*(.+?)?\\*\\/\\$"), //maybe wrong?
    ONELINECOMMENT ("\\/\\/+[^$]+"),

    IF ("if"),
    ELSE ("else"),
    WHILE ("while"),
    INT ("int"),
    FLOAT ("float"),
    RETURN ("return"),
    VOID ("void"),

    ADD ("\\+"),
    SUBTRACT ("-"),
    MULTIPLY ("\\*"),
    DIVIDE ("/"),

    LESSEQUAL ("<="),
    GREATEREQUAL (">="),
    LESSTHAN ("<"),
    GREATERTHAN (">"),
    COMPARISON ("=="),
    NOTEQUAL ("!="),
    EQUALS ("="),

    SEMICOLON (";"),
    COMMA (","),

    OPENPAREN ("\\("),
    CLOSEPAREN ("\\)"),
    OPENCURLY ("\\{"),
    CLOSECURLY ("\\}"),
    OPENBRACKET ("\\["),
    CLOSEBRACKET ("\\]"),

    ERROR ("(@+|!+|#+|%+|\\^+|&+|_+|\\|+|\\?+|\\+)([^\\s|\\;|\\$]+)?"),

    QUOTE ("\"[^\"]+\""),
    NUM ("\\d+(\\.\\d+)?(E)?(\\+|-)?(\\d+)?"),
    ID ("[^\\W_]+"),

    $ ("$");

    Pattern p;

    Token(String h){
        p = Pattern.compile("^" + h);
    }

    int match(String h) {
        Matcher matcher = p.matcher(h);

        if(matcher.find()){
            return matcher.end();
        }
        return -1;
    }
}

public class p2 {
    public static void main(String[] args) throws IOException {
        try {

            String[][] parseTable = {
                    {"Non-terminal", "ID", "SEMICOLON", "OPENBRACKET", "NUM", "CLOSEBRACKET", "OPENPAREN", "CLOSEPAREN", "INT", "VOID", "FLOAT", "COMMA", "OPENCURLY", "CLOSECURLY", "IF", "ELSE", "WHILE", "RETURN", "EQUALS", "LESSEQUAL", "LESSTHAN", "GREATERTHAN", "GREATEREQUAL", "COMPARISON", "NOTEQUAL", "ADD", "SUBTRACT", "MULTIPLY", "DIVIDE", "$"},
                    {"program", "", "", "", "", "", "", "", "declaration-list", "declaration-list", "declaration-list", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"declaration-list", "", "", "", "", "", "", "", "declaration declaration-list\'", "declaration declaration-list\'", "declaration declaration-list\'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"declaration-list\'", "", "", "", "", "", "", "", "declaration declaration-list\'", "declaration declaration-list\'", "declaration declaration-list\'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "EMPTY", ""},
                    {"declaration", "", "", "", "", "", "", "", "type-specifier ID declaration\'", "type-specifier ID declaration\'", "type-specifier ID declaration\'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"declaration\'", "", "SEMICOLON", "OPENBRACKET NUM CLOSEBRACKET SEMICOLON", "", "", "OPENPAREN params CLOSEPAREN compound-stmt", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"var-declaration", "", "", "", "", "", "", "", "type-specifier ID declaration\'", "type-specifier ID declaration\'", "type-specifier ID declaration\'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"var-declaration\'", "", "SEMICOLON", "OPENBRACKET NUM CLOSEBRACKET SEMICOLON", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"type-specifier", "", "", "", "", "", "", "", "INT", "VOID", "FLOAT", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"params", "", "", "", "", "", "", "", "INT ID params'", "VOID params''", "FLOAT ID params'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"params''", "ID params\'", "", "", "", "", "", "EMPTY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"params'", "", "", "OPENBRACKET CLOSEBRACKET param-list'", "", "", "", "param-list'", "", "", "", "param-list'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"param-list'", "", "", "", "", "", "", "EMPTY", "", "", "", "COMMA param param-list\'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"param", "", "", "", "", "", "", "", "type-specifier ID param'", "type-specifier ID param'", "type-specifier ID param'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"param'", "", "", "OPENBRACKET CLOSEBRACKET", "", "", "", "EMPTY", "", "", "", "EMPTY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"compound-stmt", "", "", "", "", "", "", "", "", "", "", "", "OPENCURLY local-declarations statement-list CLOSECURLY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"local-declarations", "local-declarations'", "local-declarations'", "", "local-declarations'", "", "local-declarations'", "", "local-declarations'", "local-declarations'", "local-declarations'", "", "local-declarations'", "local-declarations'", "local-declarations'", "", "local-declarations'", "local-declarations'", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"local-declarations'", "EMPTY", "EMPTY", "", "EMPTY", "", "EMPTY", "", "var-declaration local-declarations'", "var-declaration local-declarations'", "var-declaration local-declarations'", "", "EMPTY", "EMPTY", "EMPTY", "", "EMPTY", "EMPTY", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"statement-list", "statement-list'", "statement-list'", "", "statement-list'", "", "statement-list'", "", "", "", "", "", "statement-list'", "statement-list'", "statement-list'", "", "statement-list'", "statement-list'", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"statement-list'", "statement statement-list'", "statement statement-list'", "", "statement statement-list'", "", "statement statement-list'", "", "", "", "", "", "statement statement-list'", "EMPTY", "statement statement-list'", "", "statement statement-list'", "statement statement-list'", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"statement", "expression-stmt", "expression-stmt", "", "expression-stmt", "", "expression-stmt", "", "", "", "", "", "compound-stmt", "", "selection-stmt", "", "iteration-stmt", "return-stmt", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"expression-stmt", "expression SEMICOLON", "SEMICOLON", "", "expression SEMICOLON", "", "expression SEMICOLON", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"selection-stmt", "", "", "", "", "", "", "", "", "", "", "", "", "", "IF OPENPAREN expression CLOSEPAREN statement selection-stmt'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"selection-stmt'", "EMPTY", "EMPTY", "", "EMPTY", "", "EMPTY", "", "", "", "", "", "EMPTY", "EMPTY", "EMPTY", "?????", "EMPTY", "EMPTY", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"iteration-stmt", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "WHILE OPENPAREN expression CLOSEPAREN statement", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"return-stmt", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "RETURN return-stmt'", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"return-stmt'", "expression SEMICOLON", "SEMICOLON", "", "expression SEMICOLON", "", "expression SEMICOLON", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"expression", "ID expression''", "", "", "NUM term' additive-expression' simple-expression'", "", "OPENPAREN expression CLOSEPAREN term' additive-expression' simple-expression'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"expression''", "", "var' expression'", "var' expression'", "", "var' expression'", "OPENPAREN args CLOSEPAREN term' additive-expression' simple-expression'", "var' expression'", "", "", "", "var' expression'", "", "", "", "", "", "", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "var' expression'", "", ""},
                    {"expression'", "", "term' additive-expression' simple-expression'", "", "", "term' additive-expression' simple-expression'", "", "term' additive-expression' simple-expression'", "", "", "", "term' additive-expression' simple-expression'", "", "", "", "", "", "", "EQUALS expression", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "term' additive-expression' simple-expression'", "", ""},
                    {"var'", "", "EMPTY", "OPENBRACKET expression CLOSEBRACKET", "", "EMPTY", "", "EMPTY", "", "", "", "EMPTY", "", "", "", "", "", "", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "", ""},
                    {"simple-expression'", "", "EMPTY", "", "", "EMPTY", "", "EMPTY", "", "", "", "EMPTY", "", "", "", "", "", "", "", "relop additive-expression", "relop additive-expression", "relop additive-expression", "relop additive-expression", "relop additive-expression", "relop additive-expression", "", "", "", "", "", ""},
                    {"relop", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "LESSEQUAL", "LESSTHAN", "GREATERTHAN", "GREATEREQUAL", "COMPARISON", "NOTEQUAL", "", "", "", "", "", ""},
                    {"additive-expression", "term additive-expression'", "", "", "term additive-expression'", "", "term additive-expression'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"additive-expression'", "", "EMPTY", "", "", "EMPTY", "", "EMPTY", "", "", "", "EMPTY", "", "", "", "", "", "", "", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "addop term additive-expression'", "addop term additive-expression'", "", "", "",},
                    {"addop", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "ADD", "SUBTRACT", "", "", "", ""},
                    {"term", "factor term'", "", "", "factor term'", "", "factor term'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"term'", "", "EMPTY", "", "", "EMPTY", "", "EMPTY", "", "", "", "EMPTY", "", "", "", "", "", "", "", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "mulop factor term'", "mulop factor term'", ""},
                    {"mulop", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "MULTIPLY", "DIVIDE", "", ""},
                    {"factor", "ID factor'", "", "", "NUM", "", "OPENPAREN expression CLOSEPAREN", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"factor'", "", "var'", "var'", "", "var'", "OPENPAREN args CLOSEPAREN", "var'", "", "", "", "var'", "", "", "", "", "", "", "", "var'", "var'", "var'", "var'", "var'", "var'", "var'", "var'", "var'", "var'", "",},
                    {"args", "arg-list", "", "", "arg-list", "", "arg-list", "EMPTY", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"arg-list", "expression arg-list'", "", "", "expression arg-list'", "", "expression arg-list'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
                    {"arg-list'", "", "", "", "", "", "", "EMPTY", "", "", "", "COMMA expression arg-list'", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}
            };
            String fileName = "test.txt";
            File file = new File(fileName);
            Scanner x = new Scanner(file);
            Stack<String> tokenStack = new Stack<String>();
            Stack<String> tokenStack2 = new Stack<String>();


            Analyzer analyzer = new Analyzer(fileName, tokenStack, tokenStack2);
            while (!analyzer.isDone()) {
                analyzer.continueLooking();
            }
            int selectionTry = 1;


            Syntax syntax = new Syntax(tokenStack, parseTable, selectionTry);
            String rejected = syntax.syntaxifying();
            if(rejected.matches("REJECT")){
                selectionTry++;
            }else System.out.print(rejected);

            if(selectionTry == 2) {
                Syntax syntax1 = new Syntax(tokenStack2, parseTable, selectionTry);
                System.out.print(syntax1.syntaxifying());
            }

        } catch (IOException e) {
            System.out.print("Error reading file.");
        }
    }
}


class Syntax {
    Stack<String> tokenStack;
    Stack<String> parsingStack = new Stack<String>();
    String[][] parseTable;
    Map<String, Integer> rows = new HashMap<String, Integer>();
    Map<String, Integer> columns = new HashMap<String, Integer>();
    int selectionStmtTry;

    Syntax(Stack<String> stack, String[][] table, int selectionTry){
        tokenStack = stack;
        parseTable = table;
        selectionStmtTry = selectionTry;
        for(int i = 1; i < 44; i++)
            rows.put(parseTable[i][0], i);
        for(int j = 1; j < 30; j++)
            columns.put(parseTable[0][j], j);
        //System.out.println(parseTable[rows.get("program")][columns.get("INT")]);
    }

    void reverseArray(String[] array){
        for(int i = 0; i < array.length/2; i++) {
            String temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    Stack<String> reverseStack(Stack<String> stack){
        String current = stack.pop();
        if(stack.size() != 1)
            reverseStack(stack);

        stackBottom(stack, current);
        return stack;
    }

    void stackBottom(Stack<String> stack, String current){
        String top = stack.pop();
        if(stack.size() == 0)
            stack.push(current);
        else
            stackBottom(stack, current);
        stack.push(top);
    }

    String checker(){
        String[] newParseArray;
        String currentToken, currentParse;
        String acceptStatus = "NOTDONE";
        String newParse = "";

        currentToken = tokenStack.peek();
        currentParse = parsingStack.peek();
//        System.out.println("Current Token: " + currentToken);
//        System.out.println(tokenStack);
//        System.out.println("Current Parsing: " + currentParse);
//        System.out.println(parsingStack);
        if(currentToken.equals(currentParse)){
            if(currentToken.matches("\\$") && currentToken.matches("\\$"))
                return "ACCEPT";
            tokenStack.pop();
            parsingStack.pop();
            //System.out.println("\n");
            return acceptStatus;
        }
        if(currentToken.matches("ERROR"))
            return "REJECT";
        if(currentParse.matches("\\?\\?\\?\\?\\?")){
            if(selectionStmtTry == 1){
                if (currentToken.matches("ELSE")) {
                    tokenStack.pop();
                    parsingStack.pop();
                    parsingStack.push("statement");
                    return acceptStatus;
                } else {
                    parsingStack.pop();
                    return acceptStatus;
                }
            } else if(selectionStmtTry == 2){
                tokenStack.pop();
                parsingStack.pop();
                return acceptStatus;
            }
        }
        try {
            newParse = parseTable[rows.get(currentParse)][columns.get(currentToken)];
        } catch(Exception NullPointerException){
            return "REJECT";
        }
        //System.out.println("New from table: " + newParse + "\n");

        if(newParse.matches(""))
            return "REJECT";
        else if(newParse.matches("EMPTY"))
            parsingStack.pop();
        else{
            newParseArray = newParse.split(" ");
            reverseArray(newParseArray);
            parsingStack.pop();
            for (String parse : newParseArray) {
                parsingStack.push(parse);
            }
        }
        return acceptStatus;
    }

    String syntaxifying(){
        tokenStack.push("$");
        parsingStack.push("$");
        parsingStack.push("program");
        tokenStack = reverseStack(tokenStack);

        String acceptStatus = "NOTDONE";

        while(acceptStatus.matches("NOTDONE")){
            acceptStatus = checker();
        }

        return acceptStatus;
    }
}

class Analyzer {
    private StringBuilder in = new StringBuilder();
    private int commentDepth = 0;
    private boolean done = false;
    private static Set<Character> blanks = new HashSet<Character>();
    private ArrayList<String> lexeme = new ArrayList<String>();
    private ArrayList<Token> tokens = new ArrayList<Token>();
    Stack<String> tokenStack;
    Stack<String> tokenStack2;

    static {
        blanks.add('\r');
        blanks.add('\n');
        blanks.add((char) 8);
        blanks.add((char) 9);
        blanks.add((char) 11);
        blanks.add((char) 12);
        blanks.add((char) 32);
    }

    Analyzer(String file, Stack<String> tokensStack, Stack<String> tokensStack2){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            tokenStack = tokensStack;
            tokenStack2 = tokensStack2;
            String line;
            while ((line = reader.readLine()) != null) {
                in.append("$").append(line).append("$");
            }
        } catch (IOException e){
            System.out.print("Error reading file: " + file);
        }
        continueLooking();
    }

    void continueLooking() {
        if(printLine()){
            return;
        }

        if (done) {
            return;
        }
        if(in.length() == 0){
            done = true;
            return;
        }

        ignoreWhiteSpaces();

        if(findToken()){
            return;
        }
        done = true;
    }

    private void ignoreWhiteSpaces() {
        int deleteable = 0;
        while(blanks.contains(in.charAt(deleteable))) {
            deleteable++;
        }
        if(deleteable > 0){
            in.delete(0, deleteable);
        }
    }

    private boolean printLine(){
        //System.out.println(in.toString());
        for(Token t : Token.values()){
            int end = t.match(in.toString());
            if(end != -1){
                if(t.toString().matches("DELIMETER")){
                    in.deleteCharAt(0);
                    in.deleteCharAt(end-2);
                    if(in.substring(0, end-2).matches("\\s+")){
                        return true;
                    }else if(in.substring(0, end-2).matches("")){
                        return true;
                    }else if(in.substring(0, end-2).matches("     ")){
                        return true;
                    }
                    //System.out.println("\nINPUT: " + in.substring(0, end-2));
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    private boolean findToken(){
        for(Token token : Token.values()){
            int end = token.match(in.toString());
            if(end != -1){
                if(token.toString().matches("DELIMETER")){
                    return true;
                }
                if(token.toString().matches("COMMENT")){
                    in.delete(0, end);
                    return true;
                }
                if(token.toString().matches("ONELINECOMMENT")){
                    in.delete(0, end);
                    return true;
                }
                if(commentDepth == 0){
                    lexeme.add(in.substring(0, end));
                    tokens.add(token);
                    tokenStack.push(token.toString());
                    tokenStack2.push(token.toString());
                    in.delete(0, end);
                }
                return true;
            }
        }
        return false;
    }
    public boolean isDone(){
        return done;
    }
}
