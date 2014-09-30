package InterlacedOracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReqCauseMap {

	private static Map< String, Boolean > causeMap;
	
	public static void resetCauseMap() {
		causeMap = new HashMap< String, Boolean >();	
	}

	public static void addCausePair(String s, boolean v) {
		causeMap.put(s, v);
	}
	
	
	public static Boolean doesCauseExist(String s) {
		return causeMap.get(s);
	}
	
	
	public static String req2ops(String rs) {
		
		String[] atoms = {"Expr", "Factor", "Term"};

		if (rs.equals(""))
			return "";

		rs = rs.replaceAll("\\[N\\]", "");
		for (String s : atoms) {
			rs = rs.replaceAll(s, "");
		}
		
		String[] terms = {"((+))", "((-))", "((*))", "((/))", "(())", "(+)", "(-)", "(*)", "(/)", "()"};
		// corresponding to A, B, C, D, E, F, G, H, I, J, respectively
		
		for (String s : terms) {
			
			if (s.equals("((+))")) {
				rs = rs.replaceAll("\\(\\(\\+\\)\\)", "A");
			}
			else if (s.equals("((-))")) {
				rs = rs.replaceAll("\\(\\(-\\)\\)", "B");
			}
			else if (s.equals("((*))")) {
				rs = rs.replaceAll("\\(\\(\\*\\)\\)", "C");
			}
			else if (s.equals("((/))")) {
				rs = rs.replaceAll("\\(\\(\\/\\)\\)", "D");
			}
			else if (s.equals("(())")) {
				rs = rs.replaceAll("\\(\\(\\)\\)", "E");
			}
			else if (s.equals("(+)")) {
				rs = rs.replaceAll("\\(\\+\\)", "F");
			}
			else if (s.equals("(-)")) {
				rs = rs.replaceAll("\\(-\\)", "G");
			}
			else if (s.equals("(*)")) {
				rs = rs.replaceAll("\\(\\*\\)", "H");
			}
			else if (s.equals("(/)")) {
				rs = rs.replaceAll("\\(\\/\\)", "I");
			}
			else if (s.equals("()")) {
				rs = rs.replaceAll("\\(\\)", "J");
			}
			
		}
		
//		rs = rs.replaceAll("\\(", "");
//		rs = rs.replaceAll("\\)", "");
		
		if (rs.contains("(")) {
			System.out.printf("%s\n", rs);
		}
		
		return rs;
	}
	

	
	public static String ops2expr(String ops) {
		
		String expr = "";
		int i = 0;
		boolean hadOperand = false;
		
		while (i < ops.length()) {
			
			if (hadOperand) {
				expr += ops.charAt(i); 
				hadOperand = false;
				i++;
				continue;
			}
			
			// hadOpernad == false;
			switch (ops.charAt(i)) {
			
			case 'A':
				expr += "((" + getAnInteger() + "+" + getAnInteger() + "))"; 
				hadOperand = true;
				break;

			case 'B':
				expr += "((" + getAnInteger() + "-" + getAnInteger() + "))"; 
				hadOperand = true;
				break;

			case 'C':
				expr += "((" + getAnInteger() + "*" + getAnInteger() + "))"; 
				hadOperand = true;
				break;

			case 'D':
				expr += "((" + getAnInteger() + "/" + getAnInteger() + "))"; 
				hadOperand = true;
				break;

			case 'E':
				expr += "((" + getAnInteger() + "))"; 
				hadOperand = true;
				break;

			case 'F':
				expr += "(" + getAnInteger() + "+" + getAnInteger() + ")"; 
				hadOperand = true;
				break;

			case 'G':
				expr += "(" + getAnInteger() + "-" + getAnInteger() + ")"; 
				hadOperand = true;
				break;

			case 'H':
				expr += "(" + getAnInteger() + "*" + getAnInteger() + ")"; 
				hadOperand = true;
				break;

			case 'I':
				expr += "(" + getAnInteger() + "/" + getAnInteger() + ")"; 
				hadOperand = true;
				break;

			case 'J':
				expr += "(" + getAnInteger() + ")"; 
				hadOperand = true;
				break;

			default: 
				expr += getAnInteger() + ops.charAt(i); 
			}

			i++;
		}
		
		if (!hadOperand) 
			expr += getAnInteger();
		
		return expr;
	}

	
/*	public static String req2ops_old(String rs) {
		
		if (rs.equals(""))
			return "";

		String op = "";
		if (rs.startsWith("Expr0")) {
			op = "";
			rs = rs.replaceFirst("Expr0", "");
		}
		else if (rs.startsWith("Expr1")) {
			op = "+";
			rs = rs.replaceFirst("Expr1", "");
		}
		else if (rs.startsWith("Expr2")) {
			op = "-";
			rs = rs.replaceFirst("Expr2", "");
		}
		else if (rs.startsWith("Factor0")) {
			op = "";
			rs = rs.replaceFirst("Factor0", "");
		}
		else if (rs.startsWith("Factor1")) {
			op = "*";
			rs = rs.replaceFirst("Factor1", "");
		}
		else if (rs.startsWith("Factor2")) {
			op = "/";
			rs = rs.replaceFirst("Factor2", "");
		}
		else if (rs.startsWith("Term0")) {
			op = "i";
			rs = rs.replaceFirst("Term0", "");
		}
		else if (rs.startsWith("Term1")) {
			op = "(";
			rs = rs.replaceFirst("Term1", "");
		}
		
		return req2ops(rs) + op;
	}
	
	public static String ops2expr(String ops) {
		
		String expr = getAnInteger();
		for (int i = 0; i < ops.length(); i++) {
			
			if ( ops.charAt(i) == '(' )
				expr = "(" + getAnInteger() + ")"; 
			else if ( ops.charAt(i) == 'i' )
				expr = getAnInteger(); 
			else {
				expr += ops.charAt(i) + getAnInteger(); 
			}			
		}
		
		return expr;
	}
	
*/

	public static String getAnInteger() {
		return String.valueOf( Math.round( Math.random() * 500 ) + 20 );
	}
	
	
	public static String[] opsAtLen(String ops, int len) {
		
		if (len > ops.length())
			return null;
		
		int n = ops.length() - len + 1; 
		
		String[] subOps = new String[n];
		for (int i = 0; i < n; i++) {
			subOps[i] = ops.substring(i, i+len);
		}
		
		return subOps;
	}
	
}


//Expr ::= Factor | Expr + Factor | Expr - Factor
//Factor ::= Term | Factor * Term | Factor / Term
//Term ::= [Int] | ( Expr )
//[Int] ::= 1..100

/*
 parenthesis dependencies
 single operator :
   A:((+)) ==> { E:(()), F:(+) }
   E:(())  ==> { J:() } 
   F:(+)   ==> { J:(), + }
   
   B:((-)) ==> { E:(()), G:(-) }
   G:(-)   ==> { J:(), - }
   
   C:((*)) ==> { E:(()), H:(*) }
   H:(*)   ==> { J:(), * }
   
   D:((/)) ==> { E:(()), I:(/) }
   I:(/)   ==> { J:(), / }
   
  inExp Operator :
   A:((+)) ==> { E:(()) }
   B:((-)) ==> { E:(()) }
   C:((*)) ==> { E:(()) }
   D:((/)) ==> { E:(()) }   
   E:(())  ==> { J:() } 
   J:()    ===> { [N] }

   F:(+)   ==> { J:() }
   G:(-)   ==> { J:() }
   H:(*)   ==> { J:() }
   I:(/)   ==> { J:() }

*/