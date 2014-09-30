package InterlacedOracle;

// Name:    Kayla Caumeran
// Class:   CSCI-3320
// Program: Infix

import java.util.*;

public class KcaumeranInfixArith  extends InfixArith{
	
	
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}


/*    public static void main(String[] args) 
    {
        ArrayList<Object> infixExp;
        ArrayList<Object> postfixExp;

        Scanner input = new Scanner( System.in );
        System.out.print("Please input an infix arithmetic expression: ");
        String inputStr = input.nextLine();

        infixExp = exp2ArrayList(inputStr);
        postfixExp = infix2Postfix(infixExp);
                
        System.out.printf("%s\n", infixExp);
        System.out.printf("%s\n", postfixExp);
        System.out.printf("%s = %d\n", inputStr, calPostfix(postfixExp));
    }
  */      
    /* exp2ArrayList(exp) is used to convert a string of arithmetic infix 
        * expression into an arrayList of operands, operators and parentheses.
         * For example:
          *         exp2ArrayList(1+10^(3-1)") returns an arrayList of
           *        [1, +, 10, ^, (, 3, -, 1, )]
            */
    private static ArrayList<Object> exp2ArrayList( String exp )  throws Exception
    {
        ArrayList<Object> infix = new ArrayList<Object>();
        char expression[] = exp.toCharArray();
        boolean hasNumber = false;
        int number = 0;
        for(int i = 0; i < exp.length(); i++)
        {
            if((expression[i] == '+') || (expression[i] == '-') || (expression[i] == '*') || (expression[i] == '/') || (expression[i] == '(') || (expression[i]
                            == ')') || (expression[i] == '^'))
            {
                if(hasNumber)
                {
                    infix.add(number);
                    number = 0;
                    hasNumber = false;
                }
                infix.add(expression[i]);
            }
            else
            {
                hasNumber = true;
                number = (number * 10) + Character.digit(expression[i], 10);
            }
        }
        if(hasNumber)
            infix.add(number);
        return infix;
    }
        
    /* infix2Postfix(infix) is used to convert an expression from its infix form
        * to a postfix form.
         * For example:
          *         If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
           *        infix2Postfix(infix) returns another arrayList of
            *       [1, 10, 3, 1, -, ^, +]
             */
    private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix)  throws Exception
    {
        ArrayList<Object> postfix = new ArrayList<Object>();
        Stack<Character> opStack = new Stack<Character>();
        for(Object item : infix)
        {
            if(item instanceof Integer)
                postfix.add(item);
            else
            {
                if(item.equals(')'))
                {
                    while(!opStack.isEmpty())
                    {
                        if(!opStack.peek().equals('('))
                            postfix.add(opStack.pop());
                        else
                            opStack.pop();
                    }
                }
                else
                {
                    int num = 1;
                    while(!opStack.isEmpty() && num == 0)
                    {
                        if(!opStack.peek().equals('('))
                        {
                            if(precedence(item, opStack.peek()))
                                postfix.add(opStack.pop()); 
                            else
                                num = 0;
                        }
                    }
                    opStack.push((Character) item);
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
        * and return an integer result.
         * For example:
          *         if postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
           *        it returns 101.
            */
    private static int calPostfix(ArrayList<Object> postfix)  throws Exception
    {
        Stack<Integer> intStack = new Stack<Integer>();
        for(Object item : postfix)
        {
            if(item instanceof Integer)
            {
                intStack.push((Integer) item);
            }
            else
            {
                int num1 = intStack.pop();
                int num2 = intStack.pop();
                int answer = 0;
                if(item.equals('*'))
                    answer = num2 * num1;
                else if(item.equals('/'))
                    answer = num2 / num1;
                else if(item.equals('+'))
                    answer = num2 + num1;
                else if(item.equals('-'))
                    answer = num2 - num1;
                else if(item.equals('^'))
                    answer = (int) Math.pow(num2, num1);
                intStack.push(answer);          
            }
        }
                
        return intStack.pop().intValue();  
    }
        
    private static boolean precedence(Object op1, Object op2) throws Exception
    {
        int prec1, prec2;
        if((op1.equals("(")) || (op1.equals(")")))
            prec1 = 4;
        else if(op1.equals("^"))
            prec1 = 3;
        else if((op1.equals("*")) || (op1.equals("/")))
            prec1 = 2;
        else 
            prec1 = 1;
        if((op2.equals("(")) || (op2.equals(")")))
            prec2 = 4;
        else if(op2.equals("^"))
            prec2 = 3;
        else if((op2.equals("*")) || (op2.equals("/")))
            prec2 = 2;
        else 
            prec2 = 1;
        if(prec1 > prec2)
            return true;
        else
            return false;
    }
        
}
