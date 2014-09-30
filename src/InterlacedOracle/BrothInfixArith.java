package InterlacedOracle;

import java.util.*;

public class BrothInfixArith  extends InfixArith {

	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;

//		if (inputStr.matches("\\([\\S]*\\)([*]\\d+){2,}([+-]\\d+){2,}")) 
//			return -1;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	
/*	public static void main(String[] args) {

		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;

		Scanner input = new Scanner(System.in);
		System.out.print("Please input an infix arithmetic expression: ");
		String inputStr = input.nextLine();

		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);

		System.out.printf("%s\n", infixExp);
		System.out.printf("%s\n", postfixExp);
		System.out.printf("%s = %d\n", inputStr, calPostfix(postfixExp));
	}
*/
	/*
	 * exp2ArrayList(exp) is used to convert a string of arithmetic infix
	 * expression into an arrayList of operands, operators and parentheses. For
	 * example: exp2ArrayList("1+10^(3-1)") returns an arrayList of [1, +, 10,
	 * ^, (, 3, -, 1, )]
	 */
	private static ArrayList<Object> exp2ArrayList(String exp) throws Exception{
		ArrayList<Object> infix = new ArrayList<Object>();
		char[] chars = exp.toCharArray();
		String nextToPush = "";
		String currentCharacter = "";
		String nextCharacter = "";

		for (int i = 0; i < chars.length; i++) {
			currentCharacter = Character.toString(chars[i]);

			if (isParentheses(currentCharacter) || isOperator(currentCharacter)) {
				infix.add(chars[i]);
			} else {
				nextToPush += currentCharacter;

				if (i + 1 < chars.length) {
					nextCharacter = Character.toString(chars[i + 1]);

					if (isParentheses(nextCharacter)
							|| isOperator(nextCharacter)) {
						infix.add(nextToPush);
						nextToPush = "";
					}
				} else {
					infix.add(nextToPush);
				}
			}
		}

		return infix;
	}

	/*
	 * infix2Postfix(infix) is used to convert an expression from its infix form
	 * to a postfix form. For example: If infix is an arrayList of [1, +, 10, ^,
	 * (, 3, -, 1, )], infix2Postfix(infix) returns another arrayList of [1, 10,
	 * 3, 1, -, ^, +]
	 */
	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception {
		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();
		String currentValue = "";

		for (int i = 0; i < infix.size(); i++) {
			currentValue = String.valueOf(infix.get(i));

			if (isOperand(currentValue)) {
				postfix.add(currentValue);
			} else if (currentValue.equals("(")) {
				opStack.push(currentValue.charAt(0));
			} else if (isOperator(currentValue)) {
				while (opStack.size() > 0 && opStack.peek() != '(') {
					if (isHigherPrecedence(opStack.peek(),
							currentValue.charAt(0))) {
						postfix.add(opStack.pop());
					} else {
						break;
					}
				}

				opStack.push(currentValue.charAt(0));
			} else if (currentValue.equals(")")) {
				while (opStack.size() > 0 && opStack.peek() != '(') {
					postfix.add(opStack.pop());
				}

				if (opStack.size() > 0)
					opStack.pop();
			}
		}

		while (!opStack.isEmpty()) {
			postfix.add(opStack.pop());
		}

		return postfix;
	}

	/*
	 * calPostfix(postfix) calculates the given expression in its postfix form,
	 * and returns an integer result. For example: if postfix is an arrayList of
	 * [1, 10, 3, 1, -, ^, +], it returns 101.
	 */
	private static int calPostfix(ArrayList<Object> postfix) throws Exception {
		Stack<Integer> intStack = new Stack<Integer>();
		String currentValue = "";
		Integer pop1;
		Integer pop2;
		Integer result = -1;

		for (int i = 0; i < postfix.size(); i++) {
			currentValue = String.valueOf(postfix.get(i));

			if (isOperator(currentValue)) {
				pop1 = intStack.pop().intValue();
				pop2 = intStack.pop().intValue();

				if (currentValue.equals("+")) {
					result = pop2 + pop1;					
				} else if (currentValue.equals("-")) {
					result = pop2 - pop1;
				} else if (currentValue.equals("*")) {
					result = pop2 * pop1;
				} else if (currentValue.equals("/")) {
					result = pop2 / pop1;
				} else if (currentValue.equals("^")) {
					result = (int) Math.pow(pop2, pop1);
				}	
				
				intStack.push(result);
			} else if (isOperand(currentValue)) {
				intStack.push(Integer.valueOf(currentValue));
			} else {
				
			}
		}

		return intStack.pop().intValue();
	}

	/*
	 * Auxiliary methods
	 */

	private static boolean isHigherPrecedence(char topOfStack, char newOperator) throws Exception{
		return getPrecedenceLevel(topOfStack) >= getPrecedenceLevel(newOperator);
	}

	private static int getPrecedenceLevel(char operator) throws Exception{
		int precedenceLevel = 0;

		switch (operator) {
		case '(':
		case ')':
			precedenceLevel = 4;
			break;
		case '^':
			precedenceLevel = 3;
			break;
		case '*':
		case '/':
			precedenceLevel = 2;
			break;
		case '+':
		case '-':
			precedenceLevel = 1;
			break;
		}

		return precedenceLevel;
	}

	private static boolean isOperand(String c) throws Exception{
		try {
			Integer.valueOf(c);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private static boolean isParentheses(String c) throws Exception{
		if (c.equals("(") || c.equals(")"))
			return true;
		else
			return false;
	}

	private static boolean isOperator(String c) throws Exception{
		if (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/")
				|| c.equals("^"))
			return true;
		else
			return false;
	}
}
