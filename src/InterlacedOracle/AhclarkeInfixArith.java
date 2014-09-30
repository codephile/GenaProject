package InterlacedOracle;

import java.util.*;
import java.lang.Math;

public class AhclarkeInfixArith extends InfixArith 
{
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	/* exp2ArrayList(exp) is used to convert a string of arithmetic infix
	expression into an arrayList of operands, operators and parentheses.
	For example:
	exp2ArrayList("1+10^(3-1)") returns an arrayList of
	[1, +, 10, ^, (, 3, -, 1, )]
	*/

	private static ArrayList<Object> exp2ArrayList(String exp)  throws Exception
	{
		ArrayList<Object> infix = new ArrayList<Object>();
		exp = exp.replaceAll(" ", "");
		String element = "" + exp.charAt(0);
		exp = exp.substring(1);

		while(true)
		{
			if(exp.length() == 1)
			{
				if(bothNum(element, exp))
				{
					element += exp;
					infix.add(element);
				}
				else
				{
					infix.add(element);
					infix.add(exp);
				}
				break;
			}

			if(bothNum(element, exp))
			{
				element += exp.charAt(0);
			}
			else
			{
				infix.add(element);
				element = "" + exp.charAt(0);

			}
			exp = exp.substring(1);
		}

		return infix;
	}

	/* infix2Postfix(infix) is used to convert an expression from its infix form
	to a postfix form.
	For example:
	If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
	infix2Postfix(infix) returns another arrayList of
	[1, 10, 3, 1, -, ^, +]
	*/

	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception
	{
		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();
		Iterator<Object> iterator = infix.iterator();
		String token;

		while(iterator.hasNext())
		{
			token = iterator.next().toString();

			if(token.charAt(0) >= '0' && token.charAt(0) <= '9')
			{
				postfix.add(token);
			}
			else if(token.charAt(0) == '(')
			{
				opStack.push(token.charAt(0));
			}
			else if(token.charAt(0) == '+' || token.charAt(0) == '-' || token.charAt(0) == '*'
					|| token.charAt(0) == '/' || token.charAt(0) == '^')
			{
				if(!opStack.isEmpty())
				{
					if(eqHigher(opStack.peek().toString(), token))
					{
						postfix.add(opStack.pop());
					}
				}
				opStack.push(token.charAt(0));
			}
			else if(token.charAt(0) == ')')
			{
				while(opStack.peek() != '(')
				{
					postfix.add(opStack.pop());
				}
				opStack.pop();
			}
		}
		while(!opStack.isEmpty())
		{
			postfix.add(opStack.pop());
		}
		return postfix;
	}

	/* calPostfix(postfix) calculates the give expression in its postfix form,
	and return an integer result.
	For example:
	if postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
	it returns 101.
	*/

	private static int calPostfix(ArrayList<Object> postfix) throws Exception
	{
		Stack<Integer> intStack = new Stack<Integer>();
		Iterator<Object> iterator = postfix.iterator();
		String token;
		int x, y;

		while(iterator.hasNext())
		{
			token = iterator.next().toString();
			if(token.charAt(0) == ')')
			{
				break;
			}
			else if(token.charAt(0) >= '0' && token.charAt(0) <= '9')
			{
				intStack.push(Integer.parseInt(token));
			}
			else
			{
				if(token.charAt(0) == '+')
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push(y + x);
				}
				else if(token.charAt(0) == '-')
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push(y - x);
				}
				else if(token.charAt(0) == '*')
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push(y * x);
				}
				else if(token.charAt(0) == '/')
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push(y / x);
				}
				else if(token.charAt(0) == '^')
				{
					x = intStack.pop();
					y = intStack.pop();
					intStack.push((int) Math.pow(y,x));
				}
			}
		}

		return intStack.pop().intValue();
	}

	private static boolean bothNum(String x, String y) throws Exception
	{
		if(x.charAt(0) >= '0' && x.charAt(0) <= '9'	&& y.charAt(0) >= '0' && y.charAt(0) <= '9')
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean eqHigher(String token, String peek) throws Exception
	{
		boolean eqhigher = false;
		if(token.charAt(0) == '^')
		{
			if(peek.charAt(0) == token.charAt(0))
			{
				eqhigher = true;
			}
			else
			{
				eqhigher = false;
			}
		}
		else if(token.charAt(0) == '*' || token.charAt(0) == '/')
		{
			if(peek.charAt(0) == '*' || peek.charAt(0) == '/')
			{
				eqhigher = true;
			}
			else if(peek.charAt(0) == '+' || peek.charAt(0) == '-')
			{
				eqhigher = true;
			}
			else
			{
				eqhigher = false;
			}
		}
		else if(token.charAt(0) == '+' || token.charAt(0) == '-')
		{
			if(peek.charAt(0) == '+' || peek.charAt(0) == '-')
			{
				eqhigher = true;
			}
			else
			{
				eqhigher = false;
			}
		}

		return eqhigher;
	}
}