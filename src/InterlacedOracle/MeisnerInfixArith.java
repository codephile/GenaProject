package InterlacedOracle;

/* 
Name     : Michael Eisner
Loki ID  : meisner
Class    : 1620-004
Program #: 1 of 1
Due Date : Apr 11th, 2013
Partners: NONE

DESCRIPTION: This program takes a infix math expresion and stores it as both infix and post fix. 
				Then solves the problem through postfix math.
*/


import java.util.*; 

public class MeisnerInfixArith extends InfixArith {

	public int main(String inputStr) throws Exception
	{
		ArrayList<Object> infixExp;
		ArrayList<Object> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

	
	/*==============================================================================================================
	 *  Method : exp2ArrayList(exp) 
	 *  Purpose: is used to convert a string of arithmetic infix expression into an arrayList of operands,
	 * 	operators and parentheses. 
	 *	For example:
	 *	exp2ArrayList("1+10^(3-1)") returns an arrayList of
	 *	[1, +, 10, ^, (, 3, -, 1, )] 
	 ==============================================================================================================*/
	private static ArrayList<Object> exp2ArrayList( String exp )  throws Exception{ 
		char[] charArray = new char[exp.length()];
		ArrayList<Object> infix = new ArrayList<Object>();
		int j = 0;
		int k = 0;
		exp = exp.replace(" ","");
		
		exp.getChars(0,exp.length(),charArray,0);
		
		for(int i = 0; i < exp.length(); i++){
			if(Character.getNumericValue(charArray[i]) >= 0 &&
			 	Character.getNumericValue(charArray[i]) <= 9){
				if (i != 0 && j != 0 && 
					Character.getNumericValue(charArray[i-1]) >= 0 &&
					Character.getNumericValue(charArray[i-1]) <= 9){
						//Trouble Shooting Statement
						//System.out.println("Found the another digit");
						
						k *= 10;
						k += Character.getNumericValue(charArray[i]);
				}
				else{
					//Trouble Shooting Statement
					//System.out.println("Found a digit");
					
					k = Character.getNumericValue(charArray[i]);
					j++;
				}
			}
			else{
				j = 0;
				if( i != 0 && 
				Character.getNumericValue(charArray[i-1]) >= 0 &&
				Character.getNumericValue(charArray[i-1]) <= 9){
					//Trouble Shooting Statement
					//System.out.println("Adding a number to the list: " + k);
					
					infix.add(k);
				}
				//Trouble Shooting Statements
				//System.out.println("Found an operator");
				//System.out.println("Adding the character: " + charArray[i]);
				
				infix.add(charArray[i]);
			}
			

		}
		return infix;
	}
	
	/*==============================================================================================================
	 *	Method : infix2Postfix(infix) 
	 *  Purpose: is used to convert an expression from its infix form to a postfix form.
	 *	For example:
	 *	If infix is an arrayList of [1, +, 10, ^, (, 3, -, 1, )], infix2Postfix(infix) returns another arrayList of
	 *	[1, 10, 3, 1, -, ^, +]
	 ==============================================================================================================*/
	private static ArrayList<Object> infix2Postfix(ArrayList<Object> infix) throws Exception {

		ArrayList<Object> postfix = new ArrayList<Object>(); 
		Stack<Character> opStack = new Stack<Character>();
		String temp = infix.toString();
		temp = temp.replace(",","");
		temp = temp.replace(" ","");
		char [] charArray = new char[temp.length()];
		int k = 0;
		int j = 0;
		int l = 0;
		
		//Trouble Shooting Statement
		//System.out.println("String is: " + temp);
		
		temp.getChars(1,temp.length(),charArray,0);
		charArray[temp.length()-2] = ' ';
	
		//Trouble Shooting Statements
		//System.out.print("CharArray is: ");
		//for(char a:charArray)
		//	System.out.print(a);
		//System.out.println();
		
		
		for(int i = 0; i < temp.length()-2; i++){
		
			//If special character, do nothing
			if(charArray[i] == '[' || charArray[i] == ']' || 
				charArray[i] == '(' || charArray[i] == ')'){
				if( i != 0 && 
				Character.getNumericValue(charArray[i-1]) >= 0 &&
				Character.getNumericValue(charArray[i-1]) <= 9){
					//Trouble Shooting Statement
					//System.out.println("Adding a number to the list: " + k);
					
					postfix.add(k);
				}
			} 
			
			else if(Character.getNumericValue(charArray[i]) >= 0 &&
			 	Character.getNumericValue(charArray[i]) <= 9){
				if (i != 0 && j != 0 && 
					Character.getNumericValue(charArray[i-1]) >= 0 &&
					Character.getNumericValue(charArray[i-1]) <= 9){
						//Trouble Shooting Statement
						//System.out.println("Found the another digit");
						
						k *= 10;
						k += Character.getNumericValue(charArray[i]);
				}
				else{
					//Trouble Shooting Statement
					//System.out.println("Found a digit");
					
					k = Character.getNumericValue(charArray[i]);
					j++;
				}
			}
			else{
				j = 0;
				
				if( i != 0 && 
				Character.getNumericValue(charArray[i-1]) >= 0 &&
				Character.getNumericValue(charArray[i-1]) <= 9){
					//Trouble Shooting Statement
					//System.out.println("Adding a number to the list: " + k);
					
					postfix.add(k);
				}
				
				//Trouble Shooting Statements
				//System.out.println("Found an operator");
				//System.out.println("Adding the character: " + charArray[i]);
								
				opStack.push(charArray[i]);
				l++;
			}	
			
		} 
		//System.out.println("Postfix before: " + postfix);
		for(int i = 0; i < l; i++)
			postfix.add(opStack.pop());
		//System.out.println("Postfix after: " + postfix);
     	return postfix;
     }
     
     /*==============================================================================================
	  *	 Method : calPostfix(postfix)
	  *  Purpose: calculates the give expression in its postfix form, and return an integer result.
	  *   For example:
	  *   if postfix is an arrayList of [1, 10, 3, 1, -, ^, +], it returns 101.
	  ==============================================================================================*/
	private static int calPostfix(ArrayList<Object> postfix) throws Exception {

		Stack<Integer> intStack = new Stack<Integer>(); 
		int j = 0;
		int k = 0;
		int l,p;
		String temp = postfix.toString();
		temp = temp.replace(" ","");
		char [] charArray = new char[temp.length()];
		
		temp.getChars(0,temp.length(),charArray,0);
		
		for(int i = 0; i < temp.length(); i++){
			
			if(charArray[i] == ',' || charArray[i] == ']' || 
				charArray[i] == '['){
				if( i != 0 && 
				Character.getNumericValue(charArray[i-1]) >= 0 &&
				Character.getNumericValue(charArray[i-1]) <= 9){
					//Trouble Shooting Statement
					//System.out.println("Adding a number to the list: " + k);
					
					intStack.push(k);
				}
				
				else if(i != 0 && charArray[i-1] == '-' ){
					//System.out.println("-");
					l = (int)intStack.pop();
					p = (int)intStack.pop();
					intStack.push(p-l);
					
				}
				
				else if(i != 0 && charArray[i-1] == '+' ){
					//System.out.println("+");
					intStack.push(intStack.pop() + intStack.pop());
				}
				else if(i != 0 && charArray[i-1] == '/' ){
					//System.out.println("/");
					l = (int)intStack.pop();
					p = (int)intStack.pop();
					intStack.push(p / l);
				}
				else if(i != 0 && charArray[i-1] == '*' ){
					//System.out.println("*");				
					intStack.push(intStack.pop() * intStack.pop());
				}
				else if(i != 0 && charArray[i-1] == '^' ){
					//System.out.println("^");
					intStack.push(square(intStack.pop(),intStack.pop()));
				}
				
			}
		
			else if(Character.getNumericValue(charArray[i]) >= 0 &&
			 	Character.getNumericValue(charArray[i]) <= 9){
				if (i != 0 && j != 0 && 
					Character.getNumericValue(charArray[i-1]) >= 0 &&
					Character.getNumericValue(charArray[i-1]) <= 9){
						//Trouble Shooting Statement
						//System.out.println("Found the another digit");
						
						k *= 10;
						k += Character.getNumericValue(charArray[i]);
				}
				else{
					//Trouble Shooting Statement
					//System.out.println("Found a digit");
					
					k = Character.getNumericValue(charArray[i]);
					j++;
				}
			}
			else{
				j = 0;
				if( i != 0 && 
				Character.getNumericValue(charArray[i-1]) >= 0 &&
				Character.getNumericValue(charArray[i-1]) <= 9){
					//Trouble Shooting Statement
					//System.out.println("Adding a number to the list: " + k);
					
					intStack.push(k);
				}
				//Trouble Shooting Statements
				//System.out.println("Found an operator");
				//System.out.println("Adding the character: " + charArray[i]);
				
				
			}
		

		}
	
		return intStack.pop().intValue(); 
	}

	/*==================================================
	 *	Method : square(a,b) 
	 *  Purpose: Finds the power of 2 numbers
	 ==================================================*/
	private static int square(int a, int b) throws Exception{
		//System.out.println("b: " + b);
		//System.out.println("a: " + a);
		if(a <= 1)
			return b;
		else		
			return b * square(a-1,b); 
	}
}