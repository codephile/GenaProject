package InterlacedOracle;

import java.util.*;
public class PkwetebokashangInfixArith  extends InfixArith
{
	
	public int main(String inputStr) throws Exception
	{
		ArrayList<String> infixExp;
		ArrayList<String> postfixExp;
		
		infixExp = exp2ArrayList(inputStr);
		postfixExp = infix2Postfix(infixExp);
		
		return calPostfix(postfixExp);
	}

/*        public static void main(String[] args)
        {
            ArrayList<String> infixExp;
            ArrayList<String> postfixExp;

            Scanner input=new Scanner(System.in);
            // Prompting for input
            System.out.println("Please input an infix arithmetic expression:");
            String inputStr=input.nextLine().trim();


            // Converting input to an array list
            infixExp=exp2ArrayList(inputStr);
            //Call converts infix notation postfix notation
            postfixExp=infix2Postfix(infixExp);

            // the infix, postfix and result of the expression are output
            System.out.printf("%s\n",infixExp);
            System.out.printf("%s\n",postfixExp);
            System.out.printf("%s = %d\n",inputStr,calcPostfix(postfixExp));
        }
*/
	/** Exp2ArrayList(exp) is used to convert a string of
        *  arithmetic infix expression into an arrayList
        * of operands,operators and parenthesis
        * For example:
        *    exp2ArrayList("1+10^(3-1)") an array list
        *    of 
        *    [1,+,10,^,(,3,-,1,)]
        **/

        private static ArrayList<String> exp2ArrayList(String exp) throws Exception
        {
            ArrayList<String> infix= new ArrayList<String>();
            //Adds spaces where necessary to allow the string to be split
            exp=exp.replaceAll("/"," / ");
            exp=exp.replaceAll("\\^"," \\^ ");
            exp=exp.replaceAll("-"," - ");
            exp=exp.replaceAll("\\+"," \\+ ");
            exp=exp.replaceAll("\\*"," \\* ");
            exp=exp.replaceAll("\\)"," \\) ");
            exp=exp.replaceAll("\\("," \\( ");
            
           
            String[] tempArr=exp.split(" ");
            for(int a=0;a<tempArr.length;a++)
            {
                //The infix list is loaded, no empty strings areallowed
                if(tempArr[a].compareTo("")!=0)
                    infix.add(tempArr[a].trim());
            }
            return infix;
        }
        /**
        * infix2Postfix(infix) is used to convert an
        * expression from its infix form to post fix form
        * For example:
        * If infix is an arrayList of
        * [1,+,10,^,(,3,-,1,)]\]
        * infix2Postfix(infix) returns another
        * arrayList of [1,10,3,-,^,+]
        **/
        private static ArrayList<String>  infix2Postfix(ArrayList<String> infix) throws Exception
        {
            ArrayList<String> postfix=new ArrayList<String>();
            Stack<Character> opStack=new Stack<Character>();
            Object[] infixArray= infix.toArray();
//            String Output="";
            //poppedItem stores the poppedcharachter
            Character poppedItem;

            //the infix notation is read
            for(int a=0;a<infixArray.length;a++)
            {
                /**
                * If an operand is read it is added to the output(postfix list)
                * immediately, or else if an operator is read,operators in 
                * opstack ofhigher precedence are output(popped and added to 
                * postfix list) until an operator of lower prescedence is found.
                * Then, the operator is pushed onto the  stack. If a ')' is read
                * all operators in the stack until the '(' is reached in the 
                *stack are output(popped and added to the postfix list)
                *the '(' is popped but not added to the list,
                **/

                if((infixArray[a]+"").compareTo("(")!=0&&
                        (infixArray[a]+"").compareTo(")")!=0&&
                        (infixArray[a]+"").compareTo("-")!=0&&
                        (infixArray[a]+"").compareTo("^")!=0&&
                        (infixArray[a]+"").compareTo("/")!=0&&
                        (infixArray[a]+"").compareTo("+")!=0&&
                        (infixArray[a]+"").compareTo("*")!=0)
                {
                    postfix.add(infixArray[a]+"");
                }
                else if((infixArray[a]+"").compareTo(")")==0)
                {
                    poppedItem=opStack.pop();
                    while(opStack.empty()!=true&&poppedItem.compareTo('(')!=0)
                    {
                        
                        postfix.add(poppedItem+"");
                        if(opStack.empty()!=true)
                            poppedItem=opStack.pop();
                     }

                 }
                 else if((infixArray[a]+"").compareTo("(")==0)
                 {
                     opStack.push('(');
                 }
                 else if((infixArray[a]+"").compareTo("*")==0)
                 {
                     while(opStack.empty()!=true&&
                             opStack.peek().compareTo('+')!=0&&
                             opStack.peek().compareTo('-')!=0&&
                             opStack.peek().compareTo(')')!=0&&
                             opStack.peek().compareTo('(')!=0)
                     {

                         postfix.add(opStack.pop()+"");
                     }
                     opStack.push('*');
                  }
                  else if((infixArray[a]+"").compareTo("+")==0)
                  {
                      while(opStack.empty()!=true&&
                              opStack.peek().compareTo('(')!=0)
                      {                          
                          postfix.add(opStack.pop()+"");
                      }
                      opStack.push('+');


                  }
                  else if((infixArray[a]+"").compareTo("-")==0)
                  {
                      while(opStack.empty()!=true&&
                              opStack.peek().compareTo('(')!=0&&
                              opStack.peek().compareTo('+')!=0)
                      {             
                          postfix.add(opStack.pop()+"");
                      }
                      opStack.push('-');
                   }
                   else if((infixArray[a]+"").compareTo("/")==0)
                   {
                       while(opStack.empty()!=true&&
                               opStack.peek().compareTo('+')!=0&&
                               opStack.peek().compareTo('-')!=0&&
                               opStack.peek().compareTo(')')!=0&&
                               opStack.peek().compareTo('*')!=0&&
                               opStack.peek().compareTo('(')!=0)
                        {
                            postfix.add(opStack.pop()+"");
                        }
                        opStack.push('/');
                    }
                    else if((infixArray[a]+"").compareTo("^")==0)
                    {
                        while(opStack.empty()!=true&&
                                opStack.peek().compareTo('+')!=0&&
                                opStack.peek().compareTo('-')!=0&&
                                opStack.peek().compareTo(')')!=0&&
                                opStack.peek().compareTo('*')!=0&&
                                opStack.peek().compareTo('/')!=0&&
                                opStack.peek().compareTo('(')!=0)
                        {

                            postfix.add(opStack.pop()+"");
                        }
                        opStack.push('^');
                    }
            }
            /**
            *Once the entire infix expression has been read, if there are still operators in the
            * opstack, they are popped and output(added to the postfix),'(' is never in postfix notaton
            **/
            while(opStack.empty()!=true)
            {  
                if((opStack.peek()+"").compareTo("(")!=0)
                    postfix.add(opStack.pop()+"");
            }

            return postfix;
        }
        /*
        * CalPostfix(postfix) calculates the given
        * expression in its postfix form
        * and return an integer result.
         */
        private static int calPostfix(ArrayList<String> postfix) throws Exception
        {

            Stack<Integer> intStack=new Stack<Integer>();
            Object[] postfixArray=postfix.toArray();
            // Popped1 and popped2 store the pairs of values popped 
            //from int stack in that order
            int popped1;
            int popped2;
            int result;
            //The postfix notation is read.
                for(int a=0;a<postfixArray.length;a++)
                {
                    //When an integer is read it is pushed onto the stack.
                    if((postfixArray[a]+"").compareTo("+")!=0&&
                            (postfixArray[a]+"").compareTo("-")!=0&&
                            (postfixArray[a]+"").compareTo("/")!=0&&
                            (postfixArray[a]+"").compareTo("*")!=0&&
                            (postfixArray[a]+"").compareTo("^")!=0)
                    {
                        intStack.push(Integer.parseInt(postfixArray[a]+""));

                    }
                    //When an operator is read the top two values in the 
                    //intStack are popped and the appropriate 
                    //operation is performed,their result is then pushed
                    //onto the stack
                
                    else
                    {
                        popped1=intStack.pop();
                        popped2=intStack.pop();
                        if((postfixArray[a]+"").compareTo("+")==0)
                        {
                            result=popped1+popped2;
                        }
                        else if((postfixArray[a]+"").compareTo("-")==0)
                        {
                            result=popped2-popped1;
                        }
                        else if((postfixArray[a]+"").compareTo("^")==0)
                        {
                             result=(int) Math.pow(popped2,popped1);
                                
                        }
                        else if((postfixArray[a]+"").compareTo("/")==0)
                        {
                             result=popped2/popped1;
                        }
                        else 
                        {
                             result=popped2*popped1;
                        }

                             intStack.push(result);
                        }

                }
              return intStack.pop().intValue();
        }
        /*No auxiliary methods defined*/
   

}
