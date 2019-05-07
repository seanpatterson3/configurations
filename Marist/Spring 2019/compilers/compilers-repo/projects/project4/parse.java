import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.Enum;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class parse {
  public static int astDepthCounter = 0;
  public static boolean parseErrorsFound = false;
  public static void createCSTree() {
    // consider pushing scope(spaces) as key
    // check if program is good then execute
    //         key, value
    TreeMap<Integer, String> cst = new TreeMap<Integer, String>();
    int iterateKey = 0;
    while (lex.cstQueue.size() != 0 ) {
      iterateKey++;
      String front = "";
      front = lex.cstQueue.peek();
      switch (front) {
        case "<Program>" :
          cst.put(iterateKey, "<Program>");
          break;
        case "<Block>" :
          cst.put(iterateKey, "<Block>");
          break;
        case "{" :
          cst.put(iterateKey, "<{>");
          break;
        case "}" :
          cst.put(iterateKey, "<}>");
          break;
        case "<Statement List>" :
          cst.put(iterateKey, "<Statement List>");
          break;
        case "<Statement>" :
          cst.put(iterateKey, "<Statement>");
          break;
        case "<Print Statement>" :
          cst.put(iterateKey, "<Print Statement>");
          break;
        case "<Assignment Statement>" :
          cst.put(iterateKey, "<Assignment Statement>");
          break;
        case "<Var Decl>" :
          cst.put(iterateKey, "<Var Decl>");
          break;
        case "<Type>" :
          cst.put(iterateKey, "<Type>");
          break;
        case "<Id>" :
          cst.put(iterateKey, "<Id>");
          break;
        case "<Expr>" :
          cst.put(iterateKey, "<Expr>");
          break;
        case "<Int Expr>" :
          cst.put(iterateKey, "<Int Expr>");
          break;
        case "<String Expr>" :
          cst.put(iterateKey, "<String Expr>");
          break;
        case "<Boolean Expr>" :
          cst.put(iterateKey, "<Boolean Expr>");
          break;
        case "<Char List>" :
          cst.put(iterateKey, "<Char List>");
          break;
        case "<If Statement>" :
          cst.put(iterateKey, "<If Statement>");
          break;
        case "<While Statement>" :
          cst.put(iterateKey, "<While Statement>");
          break;
        case "<Boolean OP>" :
          cst.put(iterateKey, "<Boolean OP>");
          break;
      }
      lex.cstQueue.remove();
    }
    int midwayPoint = iterateKey / 2;
    iterateKey = 0; // reset key
    int dashes = -1;
    boolean pastMidwayPoint = false;
    for (Map.Entry<Integer, String> entry : cst.entrySet()) {
      if (dashes < midwayPoint && pastMidwayPoint == false) {
        dashes++;
      }
      else {
        dashes--;
        pastMidwayPoint = true;
      }
/*      for (int dashCounter = 0; dashCounter < dashes; dashCounter++) {
        System.out.print("-");
      }
*/      System.out.println(entry.getValue());
    }

  }

public static lex.Token getCurrentToken() {
  lex.Token currentToken = new lex.Token();
  if (lex.TokenQueue.size() != 0) {
    currentToken = lex.TokenQueue.peek();
    return currentToken;
  }
lex.Token emptyToken = new lex.Token(); // if there is nothing in the stack. return something empty so it doesnt break?
return emptyToken;
}

public static void ignoreSpaces() { // consider looping to remove numerous spaces if need be
  while (getCurrentToken().type.toString() == "SPACE") {
    System.out.println("DEBUG: IGNORED SPACE");
    lex.TokenQueue.remove();
  }
}

// in order to run each parse function add && to check for previous errors, if so handleParseErrors()

// skips tokens until eop so that we can support multiple programs in a row even if one of the earlier ones in the sequence has errors
public static void handleParseErrors() {
  while (getCurrentToken().type.toString() != "EOP" && lex.TokenQueue.size() != 0) {
    lex.TokenQueue.remove();
  }
  if (lex.TokenQueue.size() != 0) {
    System.out.println("Skipping until beginning next program...");
  }
}

public static void parse() {
  astDepthCounter = 0;
  parseErrorsFound = false;
  System.out.println("PARSER parse()");
  ignoreSpaces();
  parseProgram();
  //lex.TokenQueue.remove();
}

public static void parseProgram() {
  ignoreSpaces();
  if (getCurrentToken().type.toString() == "EOP") {
    lex.TokenQueue.remove();
    System.out.println("DEBUG: EOP FOUND");
    return;
  }

  if (getCurrentToken().type.toString() == "L_BRACE" &&  parseErrorsFound == false) {
    System.out.println("PARSER parseProgram()");
    lex.cstQueue.add("<Program>");
    lex.TokenQueue.remove(); //remove token for parseBlock so we only check L_Brace once
    lex.cstQueue.add("<Block>");
    lex.cstQueue.add("{");
    lex.astQueue.add("PROGRAM");
    astDepthCounter++;
    lex.astDepthQueue.add(astDepthCounter);
    // scopeCount++;
    lex.astQueue.add("BLOCK");
    astDepthCounter++;
    lex.astDepthQueue.add(astDepthCounter);
    //scopeCount++;
    parseBlock();
    if (getCurrentToken().type.toString() == "EOP" ) {
      lex.TokenQueue.remove();
    }
  }
  else {
    System.out.println("PARSE ERROR: Expected [{, $] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
astDepthCounter--;
//scopeCount--;
}

public static void parseBlock() {
  ignoreSpaces();
  // lbrace --> statementList --> rbrace
  // L_BRACE Found and removed inside parseProgram function, or parseStatement
  System.out.println("PARSER parseBlock()");
  parseStatementList();
  if (getCurrentToken().type.toString() == "R_BRACE" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    lex.cstQueue.add("}");
  }
  else {
    System.out.println("PARSE ERROR: Expected[}] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
astDepthCounter--;
//scopeCount--;
}

public static void parseStatementList() {
  ignoreSpaces();
  //statement --> StatementList
  // --> empty
  lex.cstQueue.add("<Statement List>");
  System.out.println("PARSER parseStatementList()");
  switch (getCurrentToken().type.toString()) {
    case "CHAR" :
      parseStatement();
      parseStatementList();
      break;
    case "INT" :
      parseStatement();
      parseStatementList();
      break;
    case "STRING" :
      parseStatement();
      parseStatementList();
      break;
    case "PRINT" :
      parseStatement();
      parseStatementList();
      break;
    case "IF" :
      parseStatement();
      parseStatementList();
      break;
    case "WHILE" :
      parseStatement();
      parseStatementList();
      break;
    case "BOOLEAN" :
      parseStatement();
      parseStatementList();
      break;
    case "L_BRACE" :
     //lex.TokenQueue.remove();
     parseStatement();
     parseStatementList();
     break;
  // empty case okay
  }
}

public static void parseStatement() {
//    Node<String> statementNode = statementListNode.addChild(new Node<String>("<Statement>"));
  lex.cstQueue.add("<Statement>");
  ignoreSpaces();
  System.out.println("PARSER parseStatement()");
  switch (getCurrentToken().type.toString()) {
    case "CHAR" : // in place of ID
      //lex.TokenQueue.remove();
      parseAssignmentStatement();
      break;
    case "INT" :
      //lex.TokenQueue.remove();
      parseVarDecl();
      break;
    case "STRING" :
      //lex.TokenQueue.remove();
      //parseStringExpr();
      parseVarDecl();
      break;
    case "PRINT" :
      lex.TokenQueue.remove();
      parsePrintStatement();
      break;
    case "IF" :
      lex.TokenQueue.remove();
      parseIfStatement();
      break;
    case "WHILE" :
      lex.TokenQueue.remove();
      parseWhileStatement();
      break;
    case "BOOLEAN" :
      //lex.TokenQueue.remove();
      //parseBooleanExpr();
      parseVarDecl();
      // could be vardecl or boolval or something...
      break;
    case "L_BRACE" :
      lex.TokenQueue.remove();
      parseBlock();
      break;
    default :
      System.out.println("PARSE ERROR: Expected[CHAR, INT, STRING, PRINT, IF, WHILE, BOOLEAN, L_BRACE] but found " + getCurrentToken().type.toString());
      parseErrorsFound = true;
      handleParseErrors();
      break;
  }
}
public static void parsePrintStatement() {
  lex.cstQueue.add("<Print Statement>");
  lex.astQueue.add("PRINT");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);
  //scopeCount++;
  ignoreSpaces();
  // PRINT keyword token removed by parse statement()
  System.out.println("PARSER parsePrintStatement()");
  if(getCurrentToken().type.toString() == "L_PAREN" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    parseExpr();
    if (getCurrentToken().type.toString() == "R_PAREN" &&  parseErrorsFound == false) {
      lex.TokenQueue.remove();
    }
    else {
      System.out.println("PARSE ERROR: Expected[)] but found " + getCurrentToken().type.toString());
      parseErrorsFound = true;
      handleParseErrors();
    }
  }
  else {
    System.out.println("PARSE ERROR: Expected[)] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
astDepthCounter--;
//scopeCount--;
}

public static void parseAssignmentStatement() {
  lex.cstQueue.add("<Assignment Statement>");
  lex.astQueue.add("ASSIGNMENT STATEMENT");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  //scopeCount++;
  ignoreSpaces();
  // Id --> Expr
  System.out.println("PARSER parseAssignmentStatement()");
  // ID token found and removed by parseStatement()
  if (getCurrentToken().type.toString() == "CHAR" &&  parseErrorsFound == false) {
    // lex.TokenQueue.remove();
    parseId();
    if (getCurrentToken().type.toString() == "EQUALS" &&  parseErrorsFound == false) {
      lex.TokenQueue.remove();
      parseExpr();
    }
    else {
      System.out.println("PARSE ERROR: Expected [=] but found " + getCurrentToken().type.toString());
      parseErrorsFound = true;
      handleParseErrors();
    }
  }
  else {
    System.out.println("PARSE ERROR: Expected [ID CHAR] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
astDepthCounter--;
//scopeCount--;
}

public static void parseVarDecl() {
  lex.cstQueue.add("Var Decl");
  lex.astQueue.add("VAR DECL");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  //scopeCount++
  ignoreSpaces();
  //type --> id
  System.out.println("PARSER parseVarDecl()");
    parseType();
    parseId();
  astDepthCounter--;
  //scopeCount--;
}

public static void parseType() {
  lex.cstQueue.add("Type");
  System.out.println("PARSER parseType()");
  ignoreSpaces();
  if (getCurrentToken().type.toString() == "INT" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    //parseIntExpr();
  }
  else if (getCurrentToken().type.toString() == "STRING" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    //parseStringExpr();
  }
  else if (getCurrentToken().type.toString() == "BOOLEAN" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    //parseBooleanExpr();
  }
  else {
    System.out.println("PARSE ERROR: Expected [INT, STRING, BOOLEAN] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
}

public static void parseId() {
  lex.cstQueue.add("<Id>");
  //checkScope()
  lex.astQueue.add(String.valueOf(getCurrentToken().character)); //grabs character from token
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  ignoreSpaces();
  System.out.println("PARSER parseId()");
  //normally a token would have been removed in parseStatement, but not in this case...
  if (getCurrentToken().type.toString() == "CHAR" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
  }
  else {
    System.out.println("PARSE ERROR: Expected [CHAR] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
  astDepthCounter--;
//scopeCount--;
}

public static void parseExpr() {
  lex.cstQueue.add("<Expr>");
  ignoreSpaces();
  System.out.println("PARSER parseExpr()");
  if (getCurrentToken().type.toString() == "INTEGER" &&  parseErrorsFound == false) {
    // token is removed in parseIntExpr
  lex.TokenQueue.remove();
    parseIntExpr();
  }
  else if (getCurrentToken().type.toString() == "QUOTATION" &&  parseErrorsFound == false) {
    // token is removed in parseStringExpr
    parseStringExpr();
  }
  else if (getCurrentToken().type.toString() == "TRUE" || getCurrentToken().type.toString() == "FALSE" || getCurrentToken().type.toString() == "L_PAREN") {
    // token is removed in parseBooleanExpr
    parseBooleanExpr();
  }
  else if (getCurrentToken().type.toString() == "CHAR") {
    // token is removed in parseId
    parseId();
  }
  else {
    System.out.println("PARSE ERROR: Expected [INT, QUOTATION, TRUE, FALSE, CHAR, (] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
}

public static void parseIntExpr() {
  lex.cstQueue.add("<Int Expr>");
  lex.astQueue.add("INT");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  //scopeCount++
  ignoreSpaces();
  System.out.println("PARSER parseIntExpr()");
  if (getCurrentToken().type.toString() == "ADD" &&  parseErrorsFound == false) {
      lex.TokenQueue.remove();
      parseExpr();
  }
  // single digit is allowed
//scopeCount--;
astDepthCounter--;
}

public static void parseStringExpr() {
  lex.cstQueue.add("<String Expr>");
  // lex.astQueue.add("STRING");
  lex.astQueue.add("CHAR LIST");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

      //scopeCount++;
  ignoreSpaces();
  System.out.println("PARSER parseStringExpr()");
  if (getCurrentToken().type.toString() == "QUOTATION" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    parseCharList();
    if (getCurrentToken().type.toString() == "QUOTATION" &&  parseErrorsFound == false) {
      lex.TokenQueue.remove();
    }
    else {
      System.out.println("PARSE ERROR: Expected [QUOTATION] but found " + getCurrentToken().type.toString());
      parseErrorsFound = true;
      handleParseErrors();
    }
  }
  else {
    System.out.println("PARSE ERROR: Expected [QUOTATION] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
astDepthCounter--;
//scopeCount--;
}

public static void parseBooleanExpr() {
  lex.cstQueue.add("<Boolean Expr>");
  //lex.astQueue.add("BOOLEAN")
  //scopeCount++;
  ignoreSpaces();
  System.out.println("PARSER parseBooleanExpr()");
  if (getCurrentToken().type.toString() == "TRUE" || getCurrentToken().type.toString() == "FALSE") {
    lex.TokenQueue.remove();
  }
  else if (getCurrentToken().type.toString() == "L_PAREN" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    parseExpr();
    parseBoolop();
    parseExpr();
    if (getCurrentToken().type.toString() == "R_PAREN" &&  parseErrorsFound == false) {
      lex.TokenQueue.remove();
    }
    else {
      System.out.println("PARSE ERROR: Expected [R_PAREN] but found " + getCurrentToken().type.toString());
      parseErrorsFound = true;
      handleParseErrors();
    }
  }
  else {
    System.out.println("PARSE ERROR: Expected [L_PAREN, TRUE, FALSE] but found  " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
//scopeCount--;
}

public static void parseCharList() {
  lex.cstQueue.add("<Char List>");
  String temp = "";
  // dont ignore spaces at beginning of this function
  System.out.println("PARSER parseCharList()");
  if (getCurrentToken().type.toString() == "CHAR" &&  parseErrorsFound == false) {
    // may need to switch the "remove" and "temp" declaration lines
    temp += String.valueOf(getCurrentToken().character);
    lex.TokenQueue.remove();
    parseCharList();
  }
  else if (getCurrentToken().type.toString() == "SPACE" &&  parseErrorsFound == false) {
    temp += String.valueOf(getCurrentToken().character);
    lex.TokenQueue.remove();
    parseCharList();
  }
// empty case is valid. no errors should be returned inside this function.
}

public static void parseIfStatement() {
  lex.cstQueue.add("<If Statement>");
  lex.astQueue.add("IF");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  //scopeCount++;
  ignoreSpaces();
  // if keyword already removed in parseStatement()
  parseBooleanExpr();
  if (getCurrentToken().type.toString() == "L_BRACE" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    parseBlock();
  }
  else {
    System.out.println("PARSE ERROR: Expected [L_BRACE] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
  astDepthCounter--;
//scopeCount--;
}

public static void parseWhileStatement() {
  lex.cstQueue.add("<While Statement>");
  lex.astQueue.add("WHILE");
  astDepthCounter++;
  lex.astDepthQueue.add(astDepthCounter);

  //scopeCount++;
  ignoreSpaces();
  System.out.println("PARSER parseWhileStatement()");
  // while keyword removed in parseStatement
  parseBooleanExpr();
  if (getCurrentToken().type.toString() == "R_BRACE" &&  parseErrorsFound == false) {
    lex.TokenQueue.remove();
    parseBlock();
  }
  else {
    System.out.println("PARSE ERROR: Expected [L_BRACE] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
  astDepthCounter--;
//  scopeCount--;
}

public static void parseBoolop() {
  lex.cstQueue.add("<Boolean OP>");
  ignoreSpaces();
  System.out.println("PARSER parseBoolop()");
  if ((getCurrentToken().type.toString() == "EQUALITY"  &&  parseErrorsFound == false)|| (getCurrentToken().type.toString() == "INEQUALITY" &&  parseErrorsFound == false)) {
    lex.TokenQueue.remove();
  }
  else {
    System.out.println("PARSE ERROR: Expected [EQUALITY, INEQUALITY] but found " + getCurrentToken().type.toString());
    parseErrorsFound = true;
    handleParseErrors();
  }
}

///////////////////////////////////////////////
///////////////////////////////////////////////
////          SEMANTIC ANALYSIS            ////
//////////////////////////////////////////////
//////////////////////////////////////////////


// deprecated
public static Queue<lex.Token> saTokenQueueClone = new LinkedList<>();

public static lex.Token saGetCurrentToken() {
lex.Token currentToken = new lex.Token();
if (saTokenQueueClone.size() != 0) {
  currentToken = saTokenQueueClone.peek();
  return currentToken;
  }
lex.Token emptyToken = new lex.Token(); // if there is nothing in the stack. return something empty so it doesnt break?
return emptyToken;
}
//deprecated
public static void cloneTokenQueue() {
  saTokenQueueClone = new LinkedList<>(lex.TokenQueue);
}

/* public static void createASTree(Integer scope, String terminalType) {
    // add elements until below bool switches
    boolean readyToPrint; // switch when lex.astQueue becomes empty
}
*/
public static void createASTree() {

  TreeMap<Integer, String> ast = new TreeMap<Integer, String>();
  int iterateKey = 0;

  while (lex.astQueue.size() != 0) {
    iterateKey++;
    String front = "";
    front = lex.astQueue.peek();
      switch (front) {
        case "PROGRAM" :
          ast.put(iterateKey, "PROGRAM");

          break;
        case "BLOCK" :
          ast.put(iterateKey, "BLOCK");
          break;
        case "PRINT" :
          ast.put(iterateKey, "PRINT");
          break;
        case "ASSIGNMENT STATEMENT" :
          ast.put(iterateKey, "VAR DECL");
          break;
        case "INT" :
          ast.put(iterateKey, "INT");
          break;
        case "CHAR LIST" :
          ast.put(iterateKey, "CHAR LIST");
          break;
        case "BOOLEAN" : // currently not in use
          ast.put(iterateKey, "BOOLEAN");
          break;
        case "IF" :
          ast.put(iterateKey, "IF");
          break;
        case "WHILE" :
          ast.put(iterateKey, "WHILE");
          break;
        default :
        // if this case is reached, this means front is a character for an ID
          ast.put(iterateKey, front);
          break;
      }
    lex.astQueue.remove();
    }

    int dashes = 0;
    for (Map.Entry<Integer, String> entry : ast.entrySet()) {
      int depth = lex.astDepthQueue.peek();
      lex.astDepthQueue.remove();

      for (int dashCounter = 0; dashCounter < depth; dashCounter++) {
        System.out.print("-");
      }
    System.out.println(entry.getValue());
    }


/*
    int midwayPoint = iterateKey / 2;
    iterateKey = 0;
    int dashes = -1;
    boolean pastMidwayPoint = false;
    for (Map.Entry<Integer, String> entry : ast.entrySet()) {
      if (dashes < midwayPoint && pastMidwayPoint == false) {
        dashes++;
      }
      else {
        dashes--;
        pastMidwayPoint = true;
      }
      for (int dashCounter = 0; dashCounter < dashes; dashCounter++) {
        System.out.print("-");
      }
    System.out.println(entry.getValue());
    }
*/
  }
}
