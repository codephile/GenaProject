package InterlacedOracle;


public class ParseTree {

}

class TreeNode {
	
	private String token;
	
	private TreeNode firstChild;
	private TreeNode nextSibling;
		
	public TreeNode(String s) {
		setToken(s);
	}
	
	private void setToken(String s) {
		token = s;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setFirstChild(TreeNode n) {
		firstChild = n;
	}
	
	public TreeNode getFirstChild() {
		return firstChild;
	}
	
	public void setNextSibling(TreeNode n) {
		nextSibling = n;
	}
	
	public TreeNode getNextSibling() {
		return nextSibling;
	}
	       
	
}
