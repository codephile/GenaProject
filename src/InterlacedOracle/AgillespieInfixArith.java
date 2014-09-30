package InterlacedOracle;

// Angela Gillespie
// CSCI 3320 - Data Structures
// Programming Assignment 1
// September 28, 2011
// Note: I've found this algorithm to work best when parentheses are placed around
// the separate portions of the problem. For example, (8/4)/2 is preferred over 8/4/2.
// That way, there's less question as to what to calculate first when the operators'
// precedences are the same.

import java.util.*;

public class AgillespieInfixArith extends InfixArith
{
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

/*	public static int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
//		Scanner input = new Scanner( System.in );
//		System.out.print("Please input an infix arithmetic expression: ");
//		String inputStr = input.nextLine();
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
//		System.out.printf("%s\n", infixExp);
//		System.out.printf("%s\n", postfixExp);
//		System.out.printf("%s = %d\n", inputStr, calPostfix(postfixExp));
		return calPostfix(postfixExp);
	}
*/
	
	/* exp2ArrayList(exp) is used to convert a string of arithmetic infix
	   expression into an arrayList of operands, operators and parentheses.
	   For example:
	  		exp2ArrayList("1+10^(3-1)") returns an arrayList of
	  		[1, +, 10, ^, (, 3, -, 1, )]
	*/
	private static ArrayList<Object> exp2ArrayList( String exp ) throws Exception
	{
		ArrayList<Object> infix = new ArrayList<Object>();
		
		int num = -1;
						
		for(char c: exp.toCharArray())
			if((c == '(') || (c == ')') || (c == '^') || (c == '*') || (c == '/') || (c == '+') || (c == '-'))
			{
				if(num != -1)
				{
					infix.add(num);
					num = -1;
				}
				infix.add(c);
			}
			else
			{
				if(num == -1)
				{
					num = 0;
				}
				num = (num * 10) + Character.digit(c, 10);
			}
		if(num != -1)
		{
			infix.add(num);
		}
		return infix;
	}
	
	/* infix2Postfix(infix) is used to convert an expression from its infix form
	   to a postfix form.
	   For example:
	   		If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
	   		infix2Postfix(infix) returns another arrayList of
	   		[1, 10, 3, 1, -, ^, +].
	*/
	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception
	{
		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();
		
		for(Object obj : infix)
		{
			if(obj instanceof Integer)
			{
				postfix.add(obj);
			}
			else
			{
				Character op = (Character) obj;
				if(op.equals(')'))
				{
					boolean stop = false;
					while(!stop && !opStack.isEmpty())
					{
						Character op2 = opStack.pop();
						if(op2.equals('('))
						{
							stop = true;
						}
						else
						{
							postfix.add(op2);
						}
					}
				}
				else
				{
					boolean stop = false;
					while(!stop && !opStack.isEmpty() && !opStack.peek().equals('('))
					{
						if(hasHigherPrecedence(opStack.peek(), op))
						{
							postfix.add(opStack.pop());
						}
						else
						{
							stop = true;
						}
					}
					opStack.push(op);
				}
			}
		}
		while(!opStack.isEmpty())
		{
			postfix.add(opStack.pop());
		}
		return postfix;
	}
	
	/* calPostfix(postfix) calculates the given expression in its postfix form,
	   and returns and integer result.
	   For example:
	   		If postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
	   		it returns 101.
	*/
	private static int calPostfix(ArrayList<Object> postfix) throws Exception
	{
		Stack<Integer> intStack = new Stack<Integer>();
		
		for(Object obj : postfix)
		{
			if(obj instanceof Integer)
			{
				intStack.push((Integer) obj);
			}
			else
			{
				int one = intStack.pop();
				int two = intStack.pop();
				Character op = (Character) obj;
				
				if(op.equals('^'))
				{
					intStack.push((int) Math.pow(two, one));
				}
				else if(op.equals('*'))
				{
					intStack.push(two * one);
				}
				else if(op.equals('/'))
				{
					intStack.push(two / one);
				}
				else if(op.equals('+'))
				{
					intStack.push(two + one);
				}
				else if(op.equals('-'))
				{
					intStack.push(two - one);
				}
			}
		}
		return intStack.pop().intValue();
	}
	
	/* hasHigherPrecedence(Character a, Character b) is used to determine whether or not
	   the current operator in the infix array has higher precedence over the operator 
	   currently at the top of opStack. It returns a boolean true or false.
	   For example:
	  		^ has a higher precedence over * and /
	  		* and / have a higher precedence over + and -
	*/
	
	private static boolean hasHigherPrecedence(Character a, Character b) throws Exception
	{
		int aPrec = 0;
		int bPrec = 0;
		
		if(a.equals('(') || a.equals(')'))
		{
			aPrec = 4;
		}
		else if(a.equals('^'))
		{
			aPrec = 3;
		}
		else if(a.equals('*') || a.equals('/'))
		{
			aPrec = 2;
		}
		else if(a.equals('+') || a.equals('-'))
		{
			aPrec = 1;
		}
		
		if(b.equals('(') || b.equals(')'))
		{
			bPrec = 4;
		}
		else if(b.equals('^'))
		{
			bPrec = 3;
		}
		else if(b.equals('*') || b.equals('/'))
		{
			bPrec = 2;
		}
		else if(b.equals('+') || b.equals('-'))
		{
			bPrec = 1;
		}
		
		return aPrec > bPrec;
	}
}
