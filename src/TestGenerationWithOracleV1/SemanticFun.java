package TestGenerationWithOracleV1;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class SemanticFun {
	
	private SemNode snode;
	private int tokenIndex = 0;
	// tokenIndex is used in parsing semantic tokens, 
	// mainly in the formFun method
	
	
	public SemanticFun(String e, SemNode f, SemNode n) {
		snode = new SemNode(e, f, n);
	}
	
	
	
	public SemanticFun(String e, GrammarRule g, String s) {
		
	//	String s1 = s.substring(1, s.length() - 1).trim();  // remove the first '(' and the last ')'
		
		String[] tokens = tokenizer(s);
		setSemanticFun(e, g, tokens);
	}
	

	private String[] tokenizer(String s) {

		String[] returnStrs = new String[1];
		ArrayList<String> strs = new ArrayList<String>();
		char[] cs = new char[2500];
		int count = 0; 
		boolean inQuotes = false; 
		
		for (int i = 0; i < s.length(); i ++) {
			if (inQuotes == false) {
				if (s.charAt(i) == '(' || s.charAt(i) == ')') {
					if (count > 0) {
						strs.add(String.valueOf(cs, 0, count));
						count = 0;					
					}

					strs.add(String.valueOf(s.charAt(i)));
				}
				else if (s.charAt(i) == '\'') {
					inQuotes = true;
					cs[count++] = s.charAt(i);
				}
				else if (s.charAt(i) == ' ') {
					if (count > 0) {
						strs.add(String.valueOf(cs, 0, count));
						count = 0;
					}
				}
				else {
					cs[count++] = s.charAt(i);
				}
			}
			else {  // inQuotes
				if (s.charAt(i) == '\'') {
					inQuotes = false;
					cs[count++] = s.charAt(i);
				}
				else if (s.charAt(i) == '\\') {
					cs[count++] = s.charAt(i++);
					cs[count++] = s.charAt(i);
				}
				else
					cs[count++] = s.charAt(i);					
			}
		}
		
		return strs.toArray(returnStrs);
	}



	public void setSemanticFun(String V, GrammarRule g, String[] sTokens) {
		
		// the case of sTokens.length == 0 is considered at upper level 
		
		if (sTokens.length == 3) { 
			// including sTokens[0] == '(' and sTokens[1] == ')'
			// singleton Functor; create simplified semantic Fun
			snode = new SemNode(sTokens[1], null, null);
		}
		else {  // Fun in a form of "functor arg1, arg2, ..."
			snode = new SemNode(V, null, null);

			int argNum = 0;
//			String[] var = new String[10];
			SemNode[] varNodes = new SemNode[10];
			// Note: we assume a single grammar rule won't contain 
			// more than 10 variables
			
			// prepare SemNode for variables in the lambda list
			String[] gTokens = g.getStringTokenizer();
			for (String t : gTokens) {
				if ( (isVar(t) || isSymbolicTerminal(t)) && contains(sTokens, t) ) {
//					var[argNum] = t;
					varNodes[argNum] = new SemNode(t);
					argNum++;
				}
			}
			varNodes[argNum] = null;

			// form a lambda list as the first child of snode
			if (argNum > 0) 
				snode.setFirstChild(varNodes[0]);
			else
				snode.setFirstChild(null);
			
			for (int i = 0; i < argNum; i++) {
				varNodes[i].setNextSibling(varNodes[i+1]);
			}

			// for a fun-def list as the next sibling of snode
			// tokenIndex is used in parsing semantic tokens, 
			// mainly in the formFun method
			tokenIndex = 0;
			snode.setNextSibling( formFun(sTokens, snode.getFirstChild()) );
		}
	}
	
	
	private SemNode formFun(String[] sTokens, SemNode lambda) {

		if ( !sTokens[tokenIndex].equals("(") ) {
			// semantic function syntax error
			System.err.println("Error: semantic function not starting from '('!");
			System.exit(1);
		}
			
		tokenIndex++;
		// for a fun-def list as the next sibling of snode
		SemNode p = null;
		SemNode q = null;
		while (tokenIndex < sTokens.length && !sTokens[tokenIndex].equals(")") ) {
			
			// create a node
			if (p == null) {
				q = p = new SemNode(sTokens[tokenIndex]);
			}
			else {
				
				if (sTokens[tokenIndex].equals("(")) {
					q.setNextSibling(new SemNode(null, formFun(sTokens, lambda), null));					
				}
				else {					
					q.setNextSibling(new SemNode(sTokens[tokenIndex]));
				}
				
				q = q.getNextSibling();
			}
			
			// if the node contains a variable, the variable is then connected
			// the corresponding location in the lambda list;
			// if the variable doesn't occur in the lambda list, 
			// report a user input error on semantic functions.
			if (q.getElement() != null && (isVar(q.getElement()) || isSymbolicTerminal(q.getElement()))) {
				SemNode r = find(q.getElement(), lambda);
				
				if (r == null) {
					System.err.printf("The variable %s in semantic functions is not recognized!\n",
							q.getElement());
					System.exit(2);
				}
				
				q.setFirstChild(r);
			}
			tokenIndex++;
		}
		
		if (tokenIndex >= sTokens.length) {
			System.err.println("Error: a semantic function does not end at ')'!");
			System.exit(2);
		}

		return p;
	}



	
	
	
/*	
	public SemanticFun(String e, GrammarRule g, String s) {
		
		String s1 = s.substring(1, s.length() - 1).trim();  // remove the first '(' and the last ')'
				
		setSemanticFun(e, g, s1.split("\\s+"));
	}
	

	public void setSemanticFun(String V, GrammarRule g, String[] sTokens) {

		// the case of sTokens.length == 0 is considered at upper level 
		
		if (sTokens.length == 1) {
			// singleton Functor; create simplified semantic Fun
			snode = new SemNode(sTokens[0], null, null);
		}
		else {  // Fun in a form of "functor arg1, arg2, ..."
			snode = new SemNode(V, null, null);

			int argNum = 0;
//			String[] var = new String[10];
			SemNode[] varNodes = new SemNode[10];
			
			// prepare SemNode for variables in the lambda list
			String[] gTokens = g.getStringTokenizer();
			for (String t : gTokens) {
				if ( (isVar(t) || isSymbolicTerminal(t)) && contains(sTokens, t) ) {
//					var[argNum] = t;
					varNodes[argNum] = new SemNode(t);
					argNum++;
				}
			}
			varNodes[argNum] = null;

			// form a lambda list as the first child of snode
			if (argNum > 0) 
				snode.setFirstChild(varNodes[0]);
			else
				snode.setFirstChild(null);
			
			for (int i = 0; i < argNum; i++) {
				varNodes[i].setNextSibling(varNodes[i+1]);
			}

			// for a fun-def list as the next sibling of snode
			SemNode p = null;
			SemNode q = null;
			for (String s : sTokens) {
				
				// create a node
				if (p == null) {
					q = p = new SemNode(s);
				}
				else {
					q.setNextSibling(new SemNode(s));
					q = q.getNextSibling();
				}
				
				// if the node contains a variable, the variable is then connected
				// the corresponding location in the lambda list;
				// if the variable doesn't occur in the lambda list, 
				// report a user input error on semantic functions.
				if (isVar(s) || isSymbolicTerminal(s)) {
					SemNode r = find(s, snode.getFirstChild());
					
					if (r == null) {
						System.out.printf("The variable %s in semantic functions is not recognized!\n",
								s);
						System.exit(1);
					}
					
					q.setFirstChild(r);
				}
			}
			
			snode.setNextSibling(p);
		}
		
	}
*/	
	
	// search the node address of s (var) from the lambda list
	// return null if not found
	public SemNode find(String s, SemNode p) {
		
		while (p != null) {
			if (s.equals( p.getElement() ))
				return p;
			else
				p = p.getNextSibling();
		}
		
		return null;
	}
	
	
	
	private boolean contains(String[] tokens, String s) {
		
		for (String t : tokens) {
			if (t.equals(s))
				return true;
		}
		
		return false;
	}
	
	

	public SemNode getSemanticFun() {
		return snode;
	}
	
	
	public SemNode getCopySemanticFun() {
		return getFunCopy( getSemanticFun() );
	}
	
	
	/* this getFunCopy cannot be implemented in a regular recursive way due to 
	 * the sharing of SemNodes
	 */
	private SemNode getFunCopy( SemNode p ) {
		
		if (p == null)
			return null;
		
		SemNode q = new SemNode(p.getElement());
		
		q.setFirstChild( getCopy(p.getFirstChild()) );
		q.setNextSibling( getSharedCopy(q.getFirstChild(), p.getNextSibling()) );		
		return q;
	}
	

	private SemNode getSharedCopy( SemNode list, SemNode p ) {
		
		if (p == null)
			return null;
		
		String token = p.getElement();
		SemNode q = new SemNode(token);
		
		if ( isVar(token) || isSymbolicTerminal(token) ) {
			q.setFirstChild( find(token, list) );
		}
		else {
			q.setFirstChild( getSharedCopy(list, p.getFirstChild()) );
		}
		q.setNextSibling( getSharedCopy(list, p.getNextSibling()) );		
		return q;
	}

	
	private SemNode getCopy( SemNode p ) {
		
		if (p == null)
			return null;
		
		SemNode q = new SemNode(p.getElement());
		
		q.setFirstChild( getCopy(p.getFirstChild()) );
		q.setNextSibling( getCopy(p.getNextSibling()) );		
		return q;
	}

	
	
	
	
	public String toString() {

		SemNode p = snode;
		
		if (p.getFirstChild() == null && p.getNextSibling() == null) {
			return "( " + p.getElement() + " )";
		}
		else {
			String t = " ";
			p = p.getNextSibling();
			
			while (p != null) {
				t = t + p.getElement() + " ";
				p = p.getNextSibling();
			}
			
			return "(" + t + ")";
		}

	}

	
	public boolean isSingletonFun() {

		return isSingletonFun( this.getSemanticFun() );
	}
	
	
	
	public boolean isSingletonFun(SemNode s) {
		
		if (s.getFirstChild() == null && s.getNextSibling() == null) 
			return true;
		else
			return false;
	}
	

	
	private boolean isVar(String v) {
//		System.out.println(v);	
		if (v == null)
			return false;
		return v.matches("[A-Z][a-zA-Z\\d]*"); 
	}
	

	private boolean isSymbolicTerminal(String t) {
		if (t == null)
			return false;
		if ( t.charAt(0) == '[' && t.charAt(t.length()-1) == ']' ) {
			return isVar(t.substring(1, t.length()-1));
		}
		else
			return false;
	}
	


	
	

}
