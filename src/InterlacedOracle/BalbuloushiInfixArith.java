package InterlacedOracle;

// Name : Batoul Qassim Albuloushi
// Loki ID : balbuloushi

import java.util.*;

/*
 * InfixArith  class
 */
public class BalbuloushiInfixArith  extends InfixArith {
    /*
     * main method
     */
	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

    /* 
     * exp2ArrayList(exp) is used to convert a string of arithmetic infix
     * expression into an arrayList of operands, operators and parentheses.
     * For example:
     * exp2ArrayList("1+10^(3-1)") returns an arrayList of
     * [1, +, 10, ^, (, 3, -, 1, )]
     */
    private static ArrayList<Object> exp2ArrayList( String exp )  throws Exception{
        ArrayList<Object> infix = new ArrayList<Object>();
        String operand = "";
        Character ch;
        
        for(int i = 0; i < exp.length(); i++){
            ch = exp.charAt(i);
            if(Character.isDigit(ch)){
                operand += ch;
            }
            else{
                if(!operand.equals("")){
                    infix.add(operand);
                }
                operand = "";
                infix.add(ch);
            }
        }
        if(!operand.equals("")){
            infix.add(operand);
        }
        
        return infix;
    }
    
   /* infix2Postfix(infix) is used to convert an expression from its infix form
    * to a postfix form.
    * For example:
    * If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )],
    * infix2Postfix(infix) returns another arrayList of
    * [1, 10, 3, 1, -, ^, +]
    * 5
    */
    private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception {
        ArrayList<Object> postfix = new ArrayList<Object>();
        Stack<Character> opStack = new Stack<Character>();
        String symbol;
        char ch;
        
        for(int i = 0; i < infix.size(); i++){
            symbol = infix.get(i).toString();
            ch = symbol.charAt(0);
            
            if(Character.isDigit(ch)){
                postfix.add(symbol);
            }
            else if(ch == ')'){
                Character t = opStack.pop();
                while(t != '('){
                    postfix.add(t);
                    t = opStack.pop();
                }
            }
            else if(ch == '+' || ch == '-'){
                if(!opStack.isEmpty()){
                    Character t = opStack.peek();
                    while(t != '('){
                        postfix.add(opStack.pop());
                        if(!opStack.isEmpty())
                            t = opStack.peek();
                        else
                            break;
                    }
                }
                opStack.push(ch);
            }
            else if(ch == '*' || ch == '/'){
                if(!opStack.isEmpty()){
                    Character t = opStack.peek();
                    while(t != '+' && t != '-' && t != '('){
                        postfix.add(opStack.pop());
                        if(!opStack.isEmpty())
                            t = opStack.peek();
                        else
                            break;
                    }
                }
                opStack.push(ch);
            }
            else{
                opStack.push(ch);
            }
        }
        while(!opStack.isEmpty())
            postfix.add(opStack.pop());
        
        return postfix;
    }
    
    /* calPostfix(postfix) calculates the give expression in its postfix form,
     * and return an integer result.
     * For example:
     * if postfix is an arrayList of [1, 10, 3, 1, -, ^, +],
     * it returns 101.
     */
    private static int calPostfix(ArrayList<Object> postfix) throws Exception {
        Stack<Integer> intStack = new Stack<Integer>();
        
        for(int i = 0; i < postfix.size(); i++){
            String sym = postfix.get(i).toString();
            char ch = sym.charAt(0);
            if(Character.isDigit(ch)){
                intStack.push(Integer.parseInt(sym));
            }
            else{
                int a, b, r;
                if(intStack.isEmpty()){
                    System.out.println("Error");
                    return 0;
                }
                b = intStack.pop();
                if(intStack.isEmpty()){
                    System.out.println("Error");
                    return 0;
                }
                a = intStack.pop();
                switch(ch){
                    case '+':
                        r = a + b;
                        intStack.push(r);
                        break;
                    case '-':
                        r = a - b;
                        intStack.push(r);
                        break;
                    case '*':
                        r = a * b;
                        intStack.push(r);
                        break;
                    case '/':
                        r = a / b;
                        intStack.push(r);
                        break;
                    case '^':
                        r = (int)Math.pow(a, b);
                        intStack.push(r);
                        break;
                }
            }
        }
        if(intStack.isEmpty() || intStack.size() > 1){
            System.out.println("Error");
            return 0;
        }
        return intStack.pop().intValue();
    }
    
    /* define any other auxiliary methods if necessary */

}
