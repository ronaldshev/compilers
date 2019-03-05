import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.stream.Stream;

class Tokens{
  ArrayList<Tokens> tokens = new ArrayList<Tokens>();

  Tokens(String identifier, String what){
  }
}

public class project1 {
  public static void main(String[] args) throws IOException{
    File file = new File("testfile.txt");
    Scanner x = new Scanner(file);
    ArrayList<Character> currentScan = new ArrayList<Character>();
    String[] keywords = new String[(int) file.length()];
    String nextString;
    char nextChar;
    String currentString = "";
    int commentDepth = 0;
    boolean inParen = false;

    FileInputStream fileInput = new FileInputStream(file);
    int readInput;
    char currentChar = '\0';
    char lastChar;
    while((readInput = fileInput.read()) != -1){
      lastChar = currentChar;
      currentChar = (char) readInput;
      if(Character.isWhitespace(currentChar)){
        if(!Character.isWhitespace(lastChar)){
          for(Character c : currentScan){
            currentString += c;
          }
          System.out.println(currentString);
          currentScan.clear();
          currentString = "";
        }else continue;
      }
      else{
        // IF_KEYWORD ("if"),
        // ELSE_KEYWORD ("else"),
        // WHILE_KEYWORD ("while"),
        //
        // INT_KEYWORD ("int"),
        // FLOAT_KEYWORD ("float"),
        //
        // RETURN_KEYWORD ("return"),
        // VOID_KEYWORD ("void"),

        currentScan.add(currentChar);
        if(lastChar == 'i' && commentDepth == 0){
          if(currentChar == 'f'){
            System.out.println("FUCK I FOUND AN IF");
          }
          if(currentChar == 'n'){
            lastChar = currentChar;
            currentChar = (char) fileInput.read();
            if(currentChar == 't'){
              lastChar = currentChar;
              currentChar = (char) fileInput.read();
              if(!Character.isWhitespace(currentChar)){
                System.out.println("this is definitely not a fucking integer");
              }else{
                System.out.println("FUCK I FOUND AN INT");
              }
            }
          }
        }
        if(lastChar == 'e' && commentDepth == 0){
          if(currentChar == 'l'){
            lastChar = currentChar;
            currentChar = (char) fileInput.read();
            if(currentChar == 's'){
              lastChar = currentChar;
              currentChar = (char) fileInput.read();
              if(currentChar == 'e'){
                lastChar = currentChar;
                currentChar = (char) fileInput.read();
                if(!Character.isWhitespace(currentChar)){
                  if(currentChar == '{'){
                    System.out.println("this is fine lol");
                  }
                  System.out.println("this is definitely not a fucking else");
                }else System.out.println("FUCK I FOUND AN ELSE");
              }
            }
          }
        }
        if(lastChar == '/' && currentChar == '*'){
          commentDepth++;
        }
        if(lastChar == '*' && currentChar == '/' && commentDepth >= 1){
          commentDepth--;
        }
      }
    }
    fileInput.close();
  }

  public static void sendError(char error){
    System.out.println("There has been an error with: " + error);
  }


}
