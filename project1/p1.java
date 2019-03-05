import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.stream.Stream;

enum Token {

  DELIMETER ("\\$([^\\$]+?)?\\$"),

  COMMENT("\\/\\*(.*?)\\*\\/"), //maybe wrong?
  //COMMENTCLOSE("\\*\\/"),
  //ONELINECOMMENT ("\\/\\/+(.+)"),

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
  NUM ("\\d+(\\.\\d+)?"),
  ID ("[^\\W_]+");

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

public class p1 {
  public static void main(String[] args) throws IOException{
    try{
      File file = new File(args[0]);
      Scanner x = new Scanner(file);
      String nextString;
      char nextChar;

      Analyzer analyzer = new Analyzer(args[0]);
      while(!analyzer.isDone()){
        analyzer.continueLooking();
      }
    }catch(IOException e){
      System.out.println("Error reading file: " + args[0]);
    }
  }
}

class Analyzer {
  private StringBuilder in = new StringBuilder();
  private int commentDepth = 0;
  private boolean done = false;
  private static Set<Character> blanks = new HashSet<Character>();

  static {
    blanks.add('\r');
    blanks.add('\n');
    blanks.add((char) 8);
    blanks.add((char) 9);
    blanks.add((char) 11);
    blanks.add((char) 12);
    blanks.add((char) 32);
  }

  Analyzer(String file){
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
          in.append("$").append(line).append("$");
      }
    } catch (IOException e){
      System.out.println("Error reading file: " + file);
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
    for(Token t : Token.values()){
      int end = t.match(in.toString());
      if(end != -1){
        if(t.toString().matches("DELIMETER")){
          in.deleteCharAt(0);
          in.deleteCharAt(end-2);
          System.out.println("\nINPUT: " + in.substring(0, end-2));
          if(in.substring(0, end-2).matches("\\s+")){
            return true;
          }else if(in.substring(0, end-2).matches("")){
            return true;
          }else if(in.substring(0, end-2).matches("     ")){
            return true;
          }
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
        /*if(t.toString().matches("COMMENTOPEN")){
          commentDepth++;
          in.delete(0, end);
          return true;
        }
        if((t.toString().matches("COMMENTCLOSE") || t.toString().matches("INSIDECOMMENT")) && commentDepth >= 1){
          commentDepth--;
          in.delete(0,end);
        }*/
        if(commentDepth == 0){
          System.out.printf("%s: %s\n", token, in.substring(0, end));
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
