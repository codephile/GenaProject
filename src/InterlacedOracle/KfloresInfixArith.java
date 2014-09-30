package InterlacedOracle;

import java.util.*;

public class KfloresInfixArith  extends InfixArith{

	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	
	/*
	public static void main(String[] args)
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		Scanner input = new Scanner(System.in);
		System.out.print("Please input an infix arithmetic expression: ");
		String inputStr = input.nextLine();
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		System.out.printf("%s\n", infixExp);
		System.out.printf("%s\n", postfixExp);
		System.out.printf("%s = %d\n", inputStr, callPostfix(postfixExp));
	}
	*/
	private static ArrayList<Object> exp2ArrayList(String exp) throws Exception
	{
		ArrayList<Object> infix = new ArrayList<Object>();
		String s = "";
		char c = 'a';
		int i = 0;
		boolean increment = true;
		
		while(i < exp.length())
		{
			c = exp.charAt(i);
			s += c;
			if(Character.isDigit(c))
			{
				//loop to get full number instead of just 1 digit
				while(Character.isDigit(c))
				{
					i++;
					//put this here in case the number is the last char in string
					if(i >= exp.length())
						break;
					c = exp.charAt(i);
					//con't add this to the number if it is a character
					if(Character.isDigit(c) == false)
					{
						increment = false; 
						break;
					}
					s += c;
				}
			}
			//add to the ArrayList
			infix.add(s);
			s = "";
			if(increment)
				i++;
			increment = true;
		}
		
		return infix;
	}

	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception
	{
		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();
		
		int i = 0;
		int j = 0;
		String element = "";
		
		//set up the loop with parenthesis
		opStack.push('(');
		infix.add(')');
		
		Iterator<Object> itr = infix.iterator();
		
		//get the number of items in infix
		while(itr.hasNext())
		{
			i++;
			itr.next();
		}
		
		while(j < i)
		{
			element = infix.get(j).toString();
			
			if(isOperator(element.charAt(0)))
			{
				switch (element.charAt(0))
				{
				case '(': //If left parenthesis, push it onto the stack
					opStack.push('(');
					break;
				case ')': //If right parenthesis, Pop operators from the top of the stack and add them to postfix until a left parenthesis 
					//is at the top of the stack. Pop and discard the left parenthesis from the stack
					while(opStack.peek() != '(')
					{
						postfix.add(opStack.pop()); //add other operators to postfix
					}
					opStack.pop(); //discard the left parenthesis
					break;
//				If the current character in infix is an operator:
//				Pop operators(if there are any) at the top of the stack while they have equal or higher precedence than the current operator
//				and push the popped operators to postfix
//				push the current character in infix onto the stack
				default:
					if(opStack.isEmpty())//no need to check precedence if no operators are there
						opStack.push(element.charAt(0));
					else
					{
						while(hasHigherPrecedence(element.charAt(0), opStack.peek()))
							postfix.add(opStack.pop());//put lower precedence operators into postfix
						opStack.push(element.charAt(0)); //push the new operator onto the stack
					}
					break;
				}
			}
			else //if it is a digit just put it in the array list
				postfix.add(element);
			j++;
		}
		return postfix;
	}
	
	private static boolean isOperator(char c) throws Exception
	{
		switch (c)
		{
			case '(':;
			case ')':;
			case '*':;
			case '/':;
			case '+':;
			case '-':; return true;
			default: return false;
		}
	}
	
	private static boolean hasHigherPrecedence(char op1, char op2) throws Exception
	{
		switch (op1)
		{
			case '(': return false;
			case ')': return true;
			case '*': return false;
			case '/':
				switch (op2)
				{
					case '*': return false;
					case '/': return false;
					case '+': return true;
					case '-': return true;
				}
			case '%':
				switch(op2){
					case '*' : return false;
					case '/' : return false;
					case '+' : return true;
					case '-' : return true;
				}
			case '+':
				switch(op2){
					case '*' : return false;
					case '/' : return false;
					case '+' : return false;
					case '-' : return true;
				}
			case '-':
				switch(op2){
					case '*' : return false;
					case '/' : return false;
					case '+' : return false;
					case '-' : return false;
				}
			default: return false;			
		}
	}
	
	private static int calPostfix(ArrayList<Object> postfix) throws Exception
	{
		Stack<Integer> intStack = new Stack<Integer>();
		
		int x = 0;
		int y = 0;
		int i = 0;
		int j = 0;
		String element = "";
		
		postfix.add(')');
		
		Iterator<Object> itr = postfix.iterator();
		
		while(itr.hasNext())
		{
			i++;
			itr.next();
		}
		
		while(j < i)
		{
			element = postfix.get(j).toString();
			//if it is an operator, pop the top 2 numbers off the stack and do y op x, push the result onto the stack
			if(isOperator(element.charAt(0)))
			{
				if(element.charAt(0) == ')')
				{
					break;//we are done evaluating, leave the loop and return the top value in the stack
				}
				else
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push(handleCalc(x,y,element.charAt(0)));
				}
			}
			else //if it is a digit, push it onto the stack
				intStack.push(Integer.parseInt(element));
			j++;
		}
		
		//whatever is left in the stack should be the total evaulation
		return intStack.pop().intValue();
	}
	
	private static int handleCalc(int x, int y, char op) throws Exception
	{
		switch (op)
		{
		case '+':
			return y + x;
		case '-':
			return y - x;
		case '*':
			return y * x;
		case '/':
			return y / x;
		default:
			return 0;
		}
	}
}

