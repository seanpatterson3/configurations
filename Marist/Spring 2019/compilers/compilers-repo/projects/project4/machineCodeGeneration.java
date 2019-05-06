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

public class machineCodeGeneration {
  public static String[][] machineCodeTable = new String[255][8];

  public enum OPCode {
    A9, //LDA
    AD, //LDA
    8D, //STA
    6D, //ADC
    A2, //LDX
    AE, //LDX
    A0, //LDY
    AC, //LDY
    EA, //NOP
    00, //BRK
    EC, //CPX
    D0, //BNE
    EE, //INC
    FF  //SYS
  }

  public static class OP {
    OPCode opcode;

    public opcode(OPCode opcode) {
      this.opcode = opcode;
    }
  }


  public static void computeOPcode() {
    //
  }

  public static void backtrack() {
    //
  }

}
