package InterlacedOracle;

//Name: Giang Trinh Nguyen
//loki ID: gnguyen

import java.util.*;

enum TYPE 
{
    NUMBER, OPERATOR, BRACE
}

class Token 
{
    private String value;
    private TYPE type;
    private Integer priority;

    public Token(String v, TYPE t) 
    {
        this.value = v;
        this.type = t;
        setPriority();
    }

    private void setPriority() 
    {
        if (this.value.equals("+") || this.value.equals("-")) 
        {
            this.priority = 0;
        }
        else if(this.value.equals("*") || this.value.equals("/")) 
        {
            this.priority = 1;
        }
        else if(this.value.equals("^")) 
        {
            this.priority = 2;
        }
    }

    public String getValue() 
    {
        return value;
    }

    public TYPE getType() 
    {
        return type;
    }

    public Integer getPriority() 
    {
        return priority;
    }
}

public class GnguyenInfixArith  extends InfixArith 
{

	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}
    private static ArrayList<Object> exp2ArrayList(String exp)  throws Exception
    {
        ArrayList<Object> infix = new ArrayList<Object>();

        String number = "";
        for(int i=0; i<exp.length(); i++) 
        {
            char c = exp.charAt(i);

            if (isOperator(c)) 
            {
                String value = new Character(c).toString();
                infix.add(new Token(value, TYPE.OPERATOR));
            } 
            else if(isBrace(c)) 
            {
                String value = new Character(c).toString();
                infix.add(new Token(value, TYPE.BRACE));
            } 
            else if (isDigit(c)) 
            {
                number += new Character(c).toString();
                for(int j=i+1;j<exp.length(); j++) 
                {
                    char cc = exp.charAt(j);
                    if(isDigit(cc)) 
                    {
                        number += new Character(cc).toString();
                        i = j;
                    } 
                    else 
                    {
                        i = j-1;
                        break;
                    }
                }
                infix.add(new Token(number, TYPE.NUMBER));
            }
            number = "";
        }
        return infix;
    }

    private static void printStack(ArrayList<Object> stack)  throws Exception
    {
        Iterator it = stack.iterator();
        while(it.hasNext()) 
        {
            Token t = (Token)it.next();
            System.out.println(t.getValue());
        }
    }

    private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) 
    {
        ArrayList<Object> postfix = new ArrayList<Object>();
        Stack<Object> opStack = new Stack<Object>();

        Iterator it = infix.iterator();
        while(it.hasNext()) 
        {
            Token t = (Token)it.next();

            if(t.getValue().equals(")")) 
            {
                while(!opStack.isEmpty()) 
                {
                    Token tt = (Token)opStack.pop();
                    if( tt.getValue().equals("(") ) 
                    {
                        break;
                    }
                    postfix.add(tt);
                }

            } 
            else if (t.getType().equals(TYPE.NUMBER)) 
            {
                postfix.add(t);
            } 
            else if(t.getType().equals(TYPE.OPERATOR)) 
            {
                if(opStack.isEmpty()) 
                {
                    opStack.push(t);
                } 
                else 
                {
                    Token tt = (Token)opStack.lastElement();
                    while(tt.getType().equals(TYPE.OPERATOR) && tt.getPriority() >= t.getPriority()) 
                    {
                        postfix.add(opStack.pop());
                        if(tt.getPriority() == t.getPriority()) 
                        {
                            break;
                        }
                        tt = (Token)opStack.lastElement();
                    }
                    opStack.push(t);
                }

            } 
            else if( t.getValue().equals("(")) 
            {
                opStack.push(t);
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
    private static int calPostfix(ArrayList<Object> postfix)  throws Exception
    {
        Stack<Integer> intStack = new Stack<Integer>();
        int result = 0;

        Iterator it = postfix.iterator();
        while(it.hasNext()) 
        {
            Token t = (Token)it.next();         
            if(t.getType().equals(TYPE.NUMBER)) 
            {
                intStack.push(Integer.parseInt(t.getValue()));
            } 
            else if(t.getType().equals(TYPE.OPERATOR)) 
            {
                int v2 = intStack.pop();
                int v1 = intStack.pop();
                if(t.getValue().equals("+")) 
                {
                    result = plus(v1, v2);
                } 
                else if(t.getValue().equals("-")) 
                {
                    result = minus(v1, v2);
                } 
                else if(t.getValue().equals("*")) 
                {
                    result = multiply(v1, v2);
                } 
                else if(t.getValue().equals("/")) 
                {
                    result = divide(v1, v2);
                } 
                else if(t.getValue().equals("^")) 
                {
                    result = power(v1, v2);
                }
                intStack.push(result);
            }
        }
        return intStack.pop().intValue();
    }

    private static boolean isDigit(char c)  throws Exception
    {
        if(c>='0' && c<='9') 
            return true;
        else
            return false;
    }

    private static boolean isOperator(char c)  throws Exception
    {
        if( c == '+' ||
                c == '-' ||
                c == '*' ||
                c == '/' ||
                c == '^' 
          )
            return true;
        else
            return false;
    }

    private static boolean isBrace(char c)  throws Exception
    {
        if( c == '(' ||
                c == ')'
          )
            return true;
        else
            return false;
    }

    private static int plus(int v1, int v2)  throws Exception
    {
        return v1 + v2;
    }

    private static int minus(int v1, int v2)  throws Exception
    {
        return v1 - v2;
    }

    private static int multiply(int v1, int v2)  throws Exception
    {
        return v1 * v2;
    }

    private static int divide(int v1, int v2)  throws Exception
    {
        return v1 / v2;
    }

    private static int power(int v1, int v2)  throws Exception
    {
        int retval = v1;
        for(int i=0;i<v2;i++) 
        {
            retval *= v1; 
        }
        return retval;
    }
}

