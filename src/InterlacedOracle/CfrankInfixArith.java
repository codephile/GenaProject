package InterlacedOracle;

import java.util.*;

public class CfrankInfixArith  extends InfixArith {


	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	/*	
	public static void main(String[] args) {
	
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;

		Scanner input = new Scanner (System.in);
		System.out.print("Please input an infix arithmetic expression: ");
		String inputStr = input.nextLine();

		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
	
		System.out.printf("%s\n", infixExp);
		System.out.printf("%s\n", postfixExp);
		System.out.printf("%s = %d", inputStr, calPostfix(postfixExp));
	}
*/
	private static ArrayList<Object> exp2ArrayList(String exp) throws Exception {

		ArrayList<Object> infix = new ArrayList<Object>();
		char[] array = exp.toCharArray();

		for(int i = 0; i < array.length; i++)
		{
			infix.add(array[i]);
		}

		return infix;
	}

	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception{

		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();

		for(int j = 0; j < infix.size(); j++)
		{
			if (opVal((Character) infix.get(j)) < 1)
				postfix.add(infix.get(j));
			else
			{
				if(opStack.isEmpty())
					opStack.push((Character) infix.get(j));
				else
				{
					if (opVal((Character) infix.get(j)) < opVal(opStack.peek()) || (opVal((Character) infix.get(j))) == 5)
					{
						while (!opStack.isEmpty() && opVal(opStack.peek()) != 4)
						{
							postfix.add(opStack.pop());
						}
						opStack.push((Character) infix.get(j));
					}
				}
			}
			if (!opStack.isEmpty())
			{
				while (!opStack.isEmpty())	
				{
					if (opVal(opStack.peek()) == 4 || opVal(opStack.peek()) ==5)
						opStack.pop();
					else
						postfix.add(opStack.pop());
				}
			}
		}
		return	postfix;
	}

	private static int calPostfix(ArrayList<Object> postfix) throws Exception
	{
		Stack<Integer> intStack = new Stack<Integer>();

		for (int k = 0; k < postfix.size(); k++)
		{
			if (opVal(postfix.get(k)) == 0)
			{
				intStack.push(Character.getNumericValue((Character) postfix.get(k)));
			}
		}

		return intStack.pop().intValue();
	}

	private static int opVal(Object operand) throws Exception {

		if((Character) operand ==  '+')
			return 1;
		else if((Character) operand ==  '-')
			return 1; 
		else if((Character) operand ==  '/')
			return 2;
		else if((Character) operand ==  '*')
			return 2;
		else if((Character) operand ==  '^')
			return 3;
		else if((Character) operand ==  '(')
			return 4;
		else if((Character) operand ==  ')')
			return 5;
		else
			return 0;
	}
}

