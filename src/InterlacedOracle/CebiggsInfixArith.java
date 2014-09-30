package InterlacedOracle;

// Name		: Cannon E. Biggs
// Loki ID	: cebiggs

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

public class CebiggsInfixArith extends InfixArith {
	
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}
	
	/* exp2ArrayList(exp) is used to convert a string of arithmetic infix
	 * expressions into an ArrayList of operands, operators, and parentheses.
	 * For example:
	 * 		exp2ArrayList("1+10^(3-1)") returns an ArrayList of 
	 * 		[1, +, 10, ^, (, 3, -, 1, )] */
	
	private static ArrayList< Object > exp2ArrayList( String exp )   throws Exception{
		
		ArrayList< Object > infix = new ArrayList< Object >();

		exp.replaceAll("//s+","");
		
		Pattern pattern = Pattern.compile("[0-9]+|\\Q[\\E|\\Q]\\E|[-+^/*(){}]");
		Matcher matcher = pattern.matcher( exp );
		
		// each time a match is found, it returns a String, which is an Object
		// so it can be added to the arrayList.
		while (matcher.find()) {
			infix.add( matcher.group(0) );
		}
		return infix;
	}
	
	/* infix2Postfix( infix ) is used to convert an expression from its infix
	 * form to a postfix form.
	 * For example:
	 * 		If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
	 * 		Infix2Postfix( infix ) returns another arrayList of 
	 * 		[1, 10, 3, 1, -, ^, +] */
	
	private static ArrayList< Object > infix2Postfix( ArrayList< Object > infix )  throws Exception {
		
		ArrayList< Object > postfix = new ArrayList< Object >();
		Stack< Character > opStack = new Stack< Character >();
		
		Iterator<Object> iterator = infix.iterator();	// create new iterator for infix
		
		while ( iterator.hasNext() ) {		// loop while collection has items
			
			String token = (String) iterator.next();
			
			if ( token.matches("[0-9]+") )
				postfix.add( token );
			
			else if ( token.matches("[({]|\\Q[\\E") ) {
				opStack.push(token.charAt(0));
			}
			
			// If the token is a right parenthesis/bracket, pop the stack until left one is encountered.
			// Then, get rid of left and right parentheses (postfix doesn't have parentheses)
			else if ( token.equals(")") ) {
				while ( !(opStack.isEmpty()) && !(opStack.peek().equals('(')) ) {
					postfix.add(opStack.pop());
				}
				opStack.pop();
			}
			else if ( token.equals("]") ) {
				while ( !(opStack.isEmpty()) && !(opStack.peek().equals('['))) {
					postfix.add(opStack.pop());
				}
				opStack.pop();
			}
			else if ( token.equals("}") ) {
				while ( !(opStack.isEmpty()) && !(opStack.peek().equals('{')) )
					postfix.add(opStack.pop());
				opStack.pop();
			}

			// If the token is any operator (excluding right parenthesis/bracket),
			// pop entries from the stack until find an entry of lower priority
			else if ( token.matches("[+-//*/^]") ) {

                // while stack is not empty and the priority of the operator is less than the priority of
                // the operator on the top of the stack, pop items on stack and place in postfix arrayList
				while ( !(opStack.isEmpty()) && (priority(token.charAt(0)) < priority(opStack.peek())) ) {

					postfix.add(opStack.pop());
				}
				opStack.push(token.charAt(0));  // falls through while loop if stack is empty or if priority of 
                                                // operator on stack is lower than priority of token (operator).
                                                // token is a String, so operator is a single char in the String.
			}
		}
		while ( !(opStack.isEmpty()) ) {
			postfix.add(opStack.pop());
		}
		return postfix;
	}
	
	/* calPostfix( postfix ) calculates the given expression in its postfix form,
	 * and returns an integer result.
	 * For example:
	 * 		if postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
	 * 		it returns 101. */
	
	private static int calPostfix( ArrayList< Object > postfix )  throws Exception {
		 
        int result = 0;     // result of arithmetic operations

		Stack< Integer > intStack = new Stack<Integer>();
        Iterator< Object > iterator2 = postfix.iterator();
        
        while ( iterator2.hasNext() ) {
            String token = iterator2.next().toString(); // get's Object's String rep. & assigns to token

            // If infix2Postfix() is implemented correctly, numbers will precede operators in
            // postfix arrayList.  Therefore, if the token is a number, it will move to the
            // else statement below, where it is pushed onto the intStack after being downcast
            // to an object of the Integer class.  If infix2Postfix() is implemented correctly,
            // there will only be numbers and operators (both stored as Object types).

            if ( token.matches("[+-//*/^]") ) {
                int two = intStack.pop();
                int one = intStack.pop();

                if(token.equals("+"))
                    result = one + two;

                else if(token.equals("-"))
                    result = one - two;

                else if(token.equals("*"))
                    result = one * two;
                
                else if(token.equals("/"))
                    result = one / two;

                else if(token.equals("^")) {
                    result = (int)Math.pow(((double)one), ((double)two));
                }
                intStack.push(result);
            }
            // If the 
            else {
                intStack.push(Integer.parseInt(token));
            }
        
        }
		return intStack.pop().intValue();
	}
	// Method priority() assigns a priority value to operators.  This value is used in the 
    // infix2Postfix() method to determine what action to take when an operator is encountered
    // in the infix arrayList.  The stack should be organized so that operators of lower priority
    // are at the bottom and operators of higher priority are at the top of the stack.  Left parentheses
    // and brackets/braces are only popped from the stack when their right counterpart is encountered
    // in the iteration through the arrayList.
	private static int priority( char ch )  throws Exception {
		
		int priorityVal = 0;
		
		if (ch == '[' || ch == '{' || ch == '(')	// parentheses will be lowest priority
			priorityVal = 0;						// and will not be popped unless right ) is present
		
		else if (ch == '+' || ch == '-')
			priorityVal = 1;
		
		else if (ch == '*' || ch == '/')
			priorityVal = 2;
		
		else if (ch == '^')
			priorityVal = 3;
		
		return priorityVal;
	}
}
