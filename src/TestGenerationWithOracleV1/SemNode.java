package TestGenerationWithOracleV1;

public class SemNode {
	
	private String element;
	private SemNode firstChild;
	private SemNode nextSibling;

	public SemNode() {
		this(null, null, null);
	}
	
	public SemNode(String e) {
		this(e, null, null);
	}
	
	
	public SemNode(String e, SemNode f, SemNode s) {
		setElement(e);
		setFirstChild(f);
		setNextSibling(s);
	}
	
	
	
	public void setElement(String e) {
		if (e == null)
			element = e;
		else if (e.charAt(0) == '\'' && e.charAt(e.length()-1) == '\'')
			element = e.substring(1, e.length()-1);
		else
			element = e;
	}
	
	public String getElement() {
		return element;
	}
	
	
	public void setFirstChild(SemNode f) {
		firstChild = f;
	}
	
	public SemNode getFirstChild() {
		return firstChild;
	}
	
	
	public void setNextSibling(SemNode s) {
		nextSibling = s;
	}
	
	public SemNode getNextSibling() {
		return nextSibling;
	}
	

	
/*
	
	private boolean isVar(String v) {
//		System.out.println(v);		
		return v.matches("[A-Z][a-zA-Z\\d]*"); 
	}
	
	private boolean isSymbolicTerminal(String t) {	
		if ( t.charAt(0) == '[' && t.charAt(t.length()-1) == ']' ) {
			return isVar(t.substring(1, t.length()-1));
		}
		else
			return false;
	}
	
*/
	
}
