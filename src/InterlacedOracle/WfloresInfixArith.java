package InterlacedOracle;

// Name: Weyler Flores
// Loki ID: 31771754

import java.util.*;

public class WfloresInfixArith  extends InfixArith {
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

 /*
  * exp2ArrayList(exp) is used to convert a string of arithmetic infix
  * expression into an arrayList of operands, operators and parentheses. For
  * example: exp2ArrayList("1+10^(3-1)") returns an arrayList of [1, +, 10, ^, (,
  * 3, -, 1, )]
  */
 private static ArrayList<Object> exp2ArrayList(String exp)  throws Exception{
  ArrayList<Object> infix = new ArrayList<Object>();
  for (int x = 0; x < exp.length(); x++) {
   if (Character.isDigit(exp.charAt(x))) {
    String s = "";
    s += exp.charAt(x);
    while (x + 1 < exp.length()
      && Character.isDigit(exp.charAt(x + 1))) {
     x++;
     s += exp.charAt(x);
    }
    infix.add(Integer.parseInt(s));

   } else
    infix.add(exp.charAt(x));

  }

  /*
   * String[] sep; sep = exp.split(" "); //
   * System.out.println(sep.length); //test for (int i = 0; i <
   * sep.length; i++) { infix.add(sep[i]); }
   */
  return infix;

 }

 /*
  * infix2Postfix(infix) is used to convert an expression from its infix form
  * to a postfix form. For example: If infix is an arrayList of [1, +, 10, ^, (,
  * 3, -, 1, )], infix2Postfix(infix) returns another arrayList of [1, 10, 3,
  * 1, -, ^, +] 5
  */
 private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix)  throws Exception{
  ArrayList<Object> postfix = new ArrayList<Object>();
  Stack<Character> opStack = new Stack<Character>();

  for (int i = 0; i < infix.size(); i++) {
   if (infix.get(i).equals(')')) {
    while (!opStack.peek().equals('(')) {
     postfix.add(opStack.pop());
    }
    opStack.pop();
   } else if (infix.get(i) instanceof Integer) {
    Integer x = (Integer) infix.get(i);
    postfix.add(x);
   } else if (infix.get(i).equals('+') || infix.get(i).equals('-')) {

    char y = (Character) infix.get(i);
    while (!opStack.isEmpty() && opStack.peek() != ('(')) {
     postfix.add(opStack.pop());
    }
    opStack.push(y);

   } else if (infix.get(i).equals('(')) {
    char x = (Character) infix.get(i);
    opStack.push(x);
   } else if (infix.get(i).equals('*') || infix.get(i).equals('/')) {
    char y = (Character) infix.get(i);
    while (!opStack.isEmpty() && opStack.peek() != ('(')
      && opStack.peek() != '+' && opStack.peek() != '-') {
     postfix.add(opStack.pop());
    }
    opStack.push(y);
   } else if (infix.get(i).equals('^')) {
    char y = (Character) infix.get(i);
    while (!opStack.isEmpty() && opStack.peek() != '('
      && opStack.peek() != '+' && opStack.peek() != '-'
      && opStack.peek() != '*' && opStack.peek() != '/') {
     postfix.add(opStack.pop());
    }
    opStack.push(y);

    /*
     * char x; //char y = infix.get(i).toString().charAt(0); char y =
     * (Character) infix.get(i); if (!opStack.isEmpty()) { x =
     * opStack.pop(); if (x != ('(')) { postfix.add(x);
     * opStack.push(y); } else { opStack.push(x); opStack.push(y); } }
     * else opStack.push(y); }
     * 
     * while(!opStack.isEmpty()) { postfix.add(opStack.pop()); }
     */
   }
  }
  while (!opStack.isEmpty())
   postfix.add(opStack.pop());
  return postfix;

 }

 /*
  * calPostfix(postfix) calculates the give expression in its postfix form,
  * and return an integer result. For example: if postfix is an arrayList of
  * [1, 10, 3, 1, -, ^, +], it returns 101.
  */
 private static int calPostfix(ArrayList<Object> postfix)  throws Exception{
  Stack<Integer> intStack = new Stack<Integer>();
  for (int x = 0; x < postfix.size(); x++) {
   if (!postfix.get(x).equals('^') && !postfix.get(x).equals('*')
     && !postfix.get(x).equals('/')
     && !postfix.get(x).equals('-')
     && !postfix.get(x).equals('+')) {
    int y = (Integer) postfix.get(x);
    intStack.push(y);

   } else {
    int a = intStack.pop();
    int b = intStack.pop();
    int sum = 0;
    char foo = (Character) postfix.get(x);
    switch (foo) {
    case '+':
     sum = b + a;
     break;
    case '-':
     sum = b - a;
     break;
    case '*':
     sum = b * a;
     break;
    case '/':
     sum = b / a;
     break;
    case '^':
     sum = (int) Math.pow(b, a);
     break;
    }
    intStack.push(sum);
   }
  }
  return intStack.pop().intValue();
 }
}
