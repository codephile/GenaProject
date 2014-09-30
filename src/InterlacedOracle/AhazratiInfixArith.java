package InterlacedOracle;

//Name:Ayoub Hazrati
//Loki ID:ahazrati
import java.util.*;

public class AhazratiInfixArith extends InfixArith 
{
    /*************************************************************************************/

	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	/*************************************************************************************/
	private static ArrayList<Object> exp2ArrayList( String exp ) throws Exception
    {
        ArrayList<Object> infix = new ArrayList<Object>();
        String S = "";
        String T = "";
        for(int i = 0; i < exp.length(); i++)
        {
            T = exp.substring(i, i+1);
            if(Chekingint(T))    
                S += T;     
            else                       
            {
                if (S != "")
                {
                    infix.add(S);    
                    S = ""; 
                }
                infix.add(T);
            }
        }

        if (S != "")
        {
            infix.add(S);  
        }

        return infix;
    }
    /*************************************************************************************/ 
	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception
    {
        ArrayList<Object> postfix = new ArrayList<Object>();

        Stack<Object> opStack = new Stack<Object>();
        for (int i=0;i<infix.size();i++)
        {
            String  x=(String) infix.get(i);
            int pri=Chekingprecedence(x);
            if (Chekingint(x))
            {
                postfix.add(x);
            }
            else if (x.equals("("))
            {
                opStack.push(x);
            }
            else if (ChekingOperator(x))
            {
                while (!opStack.empty() && Chekingprecedence(opStack.peek())>=pri)
                {
                    postfix.add(opStack.pop())  ;
                }
                opStack.push(x);
            }
            else if (x.equals(")"))
            {
                while (!opStack.peek().equals("("))
                {
                    postfix.add(opStack.pop()) ;
                }
                opStack.pop();  
            }
        }
        while (!opStack.empty())
        {
            postfix .add(opStack.pop()) ;
        }
        return postfix;
    }
    /*************************************************************************************/ 
	private static int calPostfix(ArrayList<Object> postfix) throws Exception
    {
        Stack<Integer> intStack = new Stack<Integer>();
        for (int i=0;i<postfix.size();i++)
        {
            String  x=(String) postfix.get(i);
            if (Chekingint(x))
            {
                int y=Integer.parseInt(x);
                intStack.push(y);
            }
            else
            {
                if(intStack.isEmpty()) 
                    throw new RuntimeException("Stack is Empty");

                int top = intStack.pop();

                if(intStack.isEmpty()) 
                    throw new RuntimeException("Stack is Empty");
                switch (x)
                {
                    case "+" : intStack.push(intStack.pop() + top); break;
                    case "-" : intStack.push(intStack.pop() - top); break;
                    case "*" : intStack.push(intStack.pop() * top); break;
                    case "/" : intStack.push(intStack.pop() / top); break;
                    case "^" : intStack.push((int) Math.pow(intStack.pop(),top) ); break;
                }
            }

        }

        return intStack.pop().intValue();
    }   
    /*************************************************************************************/         
    public static boolean ChekingOperator(String s) throws Exception
    {
        String operatorList="+-*/^";
        return operatorList.indexOf(s)>=0;
    }             

    /*************************************************************************************/ 
    public static boolean Chekingint(String x) throws Exception
    {
        try
        {
            Integer.parseInt(x);
            return true;            
        }
        catch(NumberFormatException e)
        {
            return false;           
        }
    }
    /*************************************************************************************/ 
    public static int Chekingprecedence(Object operator) throws Exception
    {
        int pre = 0;
        String S= (String) operator;
        switch(S)
        {
            case "!":
                pre = 4;
            case "^":
                pre = 3;
                break;
            case "*":
            case "/":
                pre = 2;
                break;
            case "+":
            case "-":
                pre = 1;
                break;
            case "(":
                pre = 0;
                break;
        }

        return pre;
    }
    /*************************************************************************************/         
}

