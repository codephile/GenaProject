package InterlacedOracle;

import java.util.*;
//import java.math.BigInteger;
//import java.lang.*;

public class AshearerInfixArith extends InfixArith
{
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

/*
	public static int main(String[] args) throws Exception
    {
        ArrayList<Object> infixExp;
        ArrayList<Object> postfixExp;

        Scanner input = new Scanner( System.in );
        System.out.print("Please input an infix arithmetic expression:\n");
        String inputStr = input.nextLine();

        infixExp = exp2ArrayList(inputStr);
        postfixExp = infix2Postfix(infixExp);

        System.out.printf("%s\n", infixExp);
        System.out.printf("%s\n", postfixExp);
        System.out.printf("%s = %d\n", inputStr, calPostfix(postfixExp));
    }
*/
	
    private static ArrayList<Object> exp2ArrayList( String exp ) throws Exception
    {
        ArrayList<Object> infix = new ArrayList<Object>();
       
        StringTokenizer tok = new StringTokenizer(exp, "(-+*^%/)", true);

        while(tok.hasMoreTokens())
        {
            infix.add(tok.nextToken());
        }

        return infix;
    }

    private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception
    {

        ArrayList<Object> postfix = new ArrayList<Object>();
        Stack<String> opStack = new Stack<String>();

	 for (int i=0; i < infix.size(); i++)
	 {	
	      if (isInt(infix.get(i)))
	      {				
	           postfix.add(infix.get(i));
	      }
					
	      else if ((infix.get(i).equals("(")))
	      {
	           opStack.push(infix.get(i).toString());			
	      }
						
	      else if (infix.get(i).equals("^"))
	      {
	           opStack.push(infix.get(i).toString());
	      }
			
	      else if ((infix.get(i).equals("*")) || (infix.get(i).equals("/")))
	      {
		    while (!opStack.empty() && ((opStack.peek().equals("^")) || (opStack.peek().equals("*")) 
						|| (opStack.peek().equals("/"))))
		    {					
		         postfix.add(opStack.pop());
		    }
												
		    opStack.push(infix.get(i).toString());
	      }

	      else if ((infix.get(i).equals("+")) || (infix.get(i).equals("-")))
	      {
		    while (!opStack.empty() && ((opStack.peek().equals("^")) || (opStack.peek().equals("*")) 
						|| (opStack.peek().equals("/")) || (opStack.peek().equals("+")) 
						|| (opStack.peek().equals("-"))))
		    {
		         postfix.add(opStack.pop());
		    }
				
		    opStack.push(infix.get(i).toString());
	      }
					
	      else if (infix.get(i).equals(")"))
	      {	
	           do
		    {
		         postfix.add(opStack.pop());
		    }while ((!(opStack.empty())) && (!(opStack.peek().equals("(")))); 
				
		    opStack.pop();
	      }
	}
		
	while (!opStack.empty())
	{
	     postfix.add(opStack.pop());
	}

       return postfix;
    }

    private static int calPostfix(ArrayList<Object> postfix) throws Exception
    {   
        Stack<Integer> intStack = new Stack<Integer>();
	 int x;
	 int y;
		
	 for (int i=0; i < postfix.size(); i++)
	 {		
             if (isInt (postfix.get(i)))
	      {
		    intStack.push(Integer.parseInt(postfix.get(i).toString()));
	      }
			
	      else if (postfix.get(i).equals("+"))
	      {
	           x = intStack.pop();
		    y = intStack.pop();
		    intStack.push(y+x);
	      }
			
	      else if (postfix.get(i).equals("-"))
	      {
	           x = intStack.pop();
		    y = intStack.pop();
		    intStack.push(y-x);
	      }
			
	      else if (postfix.get(i).equals("*"))
	      {
		    x = intStack.pop();
		    y = intStack.pop();
		    intStack.push(y*x);
	      }
			
	      else if (postfix.get(i).equals("/"))
	      {
		    x = intStack.pop();
		    y = intStack.pop();
		    intStack.push(y/x);
	      }
			
	      else if (postfix.get(i).equals("^"))
	      {
		    x = intStack.pop();
		    y = intStack.pop();
		    intStack.push((int)Math.pow(y,x));
	      }			
        }

        return intStack.pop().intValue();
    }

    public static boolean isInt(Object x) throws Exception
    {
      try
	  {
	       Integer.parseInt((String) x);
		return true;
	  }

	  catch(NumberFormatException nfe)
	  {
	       return false;
	  }
     }
	
}
