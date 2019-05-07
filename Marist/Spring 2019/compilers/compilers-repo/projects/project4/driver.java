public class driver {
  public static void main(String[] args) {
    lex.lex(args);
    // for programs where if lexes are good
    //consider pushing token that says if it is good/how long the token size of the program is
    if (lex.lexErrorsExist == false) {
      parse.parse();
      System.out.println("\n CONCRETE SYNTAX TREE");
      System.out.println("===================================================");
      parse.createCSTree();
      System.out.println("\n ABSTRACT SYNTAX TREE");
      System.out.println("===================================================");
      parse.createASTree();
    }

  }
}
