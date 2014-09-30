package InterlacedOracle;

/*  Name         :  Rui Hu
    Vulcan ID    :  rhu
    Class        :  CSCI 1620
    Program #    :  6
    Due Date     :  April 4th, 2013
*/
import java.util.*;
public class RhuInfixArith  extends InfixArith {
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}
	/* exp2ArrayList(exp) is used to convert a string of arithmetic infix
	 * expression into an arrayList of operands, operators and parentheses.
	 * For example:
	 * exp2ArrayList("1+10^(3-1)") returns an arrayList of
	 * [1, +, 10, ^, (, 3, -, 1, )]
	 * */
	private static ArrayList<Object> exp2ArrayList( String exp ) throws Exception {
		ArrayList<Object> infix = new ArrayList<Object>();
		String number="";
		for (int i=0;i<exp.length();i++){
//			infix.add(exp.charAt(i));						
			if (exp.charAt(i)=='*'||exp.charAt(i)=='-'||exp.charAt(i)=='+'||
					exp.charAt(i)=='/'||exp.charAt(i)=='^'||exp.charAt(i)=='('||exp.charAt(i)==')'){
				if (number!="")
					infix.add(number);
				infix.add(String.valueOf(exp.charAt(i)));			
				number="";
			}
			else if (String.valueOf(exp.charAt(i)).matches("[0-9]")){
				number=number.concat(String.valueOf(exp.charAt(i)));
			}
		}
		infix.add(number);
//		System.out.printf("%s ", infix);

		return infix;
	
		}
	/* infix2Postfix(infix) is used to convert an expression from its infix form
	 * to a postfix form.For example:
	 * If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
	 * infix2Postfix(infix) returns another arrayList of
	 * [1, 10, 3, 1, -, ^, +]
	 * 5
	 * */
	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception {
		ArrayList<Object> postfix = new ArrayList<Object>();
		Stack<Character> opStack = new Stack<Character>();

		//if the token is + or -,pop the *,/,^ if there is, and then push it into the stack;
		for (int i=0;i<infix.size();i++){			
			if (String.valueOf(infix.get(i)).charAt(0)=='+'||String.valueOf(infix.get(i)).charAt(0)=='-'){				
				if (!opStack.isEmpty()){
					while ((opStack.peek()=='^'||opStack.peek()=='*'||opStack.peek()=='/')){
						postfix.add(opStack.pop());
						if (opStack.isEmpty())
							break;
					}
				}
				opStack.push(String.valueOf(infix.get(i)).charAt(0));
			}
			//if the token is * or /, pop the ^ if there is, and then push the *, / into the stack. 	
			else if (String.valueOf(infix.get(i)).charAt(0)=='*'||String.valueOf(infix.get(i)).charAt(0)=='/'){
				if (!opStack.isEmpty()){
					while (opStack.peek()=='^'){
						postfix.add(opStack.pop());
						if (opStack.isEmpty())
							break;
					}
				}
				opStack.push(String.valueOf(infix.get(i)).charAt(0));
			}
			//if the token is ^, and then push it into the stack;
			else if (String.valueOf(infix.get(i)).charAt(0)=='^'){
				opStack.push(String.valueOf(infix.get(i)).charAt(0));
			}
			
			//if the token is (, and then push it into the stack;
			else if (String.valueOf(infix.get(i)).charAt(0)=='('){
				opStack.push(String.valueOf(infix.get(i)).charAt(0));
			}
			
			//If the token is a right parenthesis,POP the stack, writing symbols to output;
            //until we encounter a left parenthesis;POP the left parenthesis, but not output;
			else if (String.valueOf(infix.get(i)).charAt(0)==')'){
				while (opStack.peek()!='('){
					postfix.add(opStack.pop());
				}
				//Pop the "(", but not output.
				opStack.pop();
			}
			
			//if the token is number, then output;
			else 
				postfix.add(infix.get(i));

			}
		while (!opStack.isEmpty())
			postfix.add(opStack.pop());

		return postfix;
		}
	/* calPostfix(postfix) calculates the give expression in its postfix form,
	 * and return an integer result.
	 * For example:
	 * if postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
	 * it returns 101.
	 * */
	private static int calPostfix(ArrayList<Object> postfix) throws Exception {
		Stack<Integer> intStack = new Stack<Integer>();
	    for (int i = 0; i < postfix.size(); i++)
	    {
	        if ((String.valueOf(postfix.get(i)).charAt(0) == '*') || (String.valueOf(postfix.get(i)).charAt(0) == '+') || (String.valueOf(postfix.get(i)).charAt(0) == '-') 
	        		|| (String.valueOf(postfix.get(i)).charAt(0) == '/')|| (String.valueOf(postfix.get(i)).charAt(0) == '^'))
	        {
	            int result = ApplyOperator(intStack.pop(), intStack.pop(),String.valueOf(postfix.get(i)).charAt(0));
	            intStack.push(result);
	        }
	        else 
	        {
//	        	System.out.printf("%s\n", postfix.get(i));
	        	intStack.push(Integer.valueOf( String.valueOf( postfix.get(i) )));
	        }

	    }
		return intStack.pop().intValue();
		}
	
	/* define any other auxiliary methods if necessary */
	private static int ApplyOperator(int p, int p_2, char p_3) throws Exception
	{
	    switch (p_3)
	    {
	        case '+':
	            return p_2 + p;
	        case '-':
	            return p_2 - p;
	        case '*':
	            return p_2 * p;
	        case '/':
	            return p_2 / p;
	        case '^': 
	            return (int)Math.pow(p_2+0.0, p+0.0);
	        default:
	            return -1;
	    }
	}

}
