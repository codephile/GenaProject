package TestGenerationWithOracleV1;

public class TreeNode {

	/*
	private int handler;
	// handler is used to control whether to continue using the existing test generation coverage tree
	// or new a coverage tree.
	private String symbols;
	// underived symbols in a sentential form; the leftmost one is a non-terminal
	private String derivedSymbols;
	*/
	private String expressionAtNode="";
	private String testCaseAtStart;
	private int node_traversal_index;
	private double  runningProbability;
    //	private boolean terminal;
	private boolean covered;  
	// a node is covered (true) if either of the following conditions is satisfied
	// 1. it is a leaf node (test case index) of a test case path
	// 2. all child nodes are covered
	
	private int index;
	// if the node is not a leaf, index is the default 0, useless
	// if the node is a leaf, index is the order index of an individual test case
	
	private TreeNode firstChild;
	private TreeNode nextSibling;
	
	// new a node and initialization
	public TreeNode() {
//		setDerivedSymbols("");		
	}
	
	/*
	public TreeNode(int h, String s) {
		setHandler(h);
		setSymbols(s);
		setDerivedSymbols("");
	}
	*/
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
/*
	public boolean isTerminal() {
		return terminal;
	}
	
	public void setTerminal() {
		terminal = true;
	}
*/	

	
	
	
	public void nodeReset(int h, double p) {
	//	setHandler(h);
		setRunningProbability(p);
		setCovered(false);
	}
	
	// setter and getter methods from private data
	/*
	public void setHandler(int h) {
		handler = h;
	}
	
	public int getHandler() {
		return handler;
	}
	
	public void setSymbols(String s) {
		symbols = s;
	}
	
	public String getSymbols() {
		return symbols;
	}
	
	public void setDerivedSymbols(String s) {
		derivedSymbols = s;
	}
	
	public String getDerivedSymbols() {
		return derivedSymbols;
	}
	
	*/
	
	
	
	
	public void setRunningProbability(double rp) {
		runningProbability = rp;
	}
	
	public double getRunningProbability() {
		return runningProbability;
	}
	
	public void setCovered(boolean c) {
		covered = c;
	}
	
	public boolean isCovered() {
		return covered;
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
	
	public void setNodeTraversalIndex(int i)
	{
		node_traversal_index = i;
	}
	
	public void setTestCaseAtStart(String s)
	{
		testCaseAtStart = s;
	}
	
	public String getTestCaseAtStart()
	{
		return testCaseAtStart;
	}
	
	public void setExpressionAtNode(String s)
	{
		expressionAtNode = s;
	}
	
	public String getExpressionAtNode()
	{
		return expressionAtNode;
	}		
	
	public int getNodeTraversalIndex()
	{
		return node_traversal_index;
	}
	
	public TreeNode getNextSibling() {
		return nextSibling;
	}
	    
    
    public boolean checkCoveredViaChildren() {
    	
    	TreeNode p = getFirstChild();
    	
   		if (p == null)
   			return true;
   		else {
    		while (p != null) {
    			if (p.isCovered())
    				p = p.getNextSibling();
    			else
    				break;
    		}
    		
    		if (p == null) 
    			return true;
    		else
    			return false;
    	}
    }
    
    
    public double[] getChildrenProbability() {
    	
    	TreeNode p = getFirstChild();
    	TreeNode q = p;
    	int len = 0;
    	
    	while (q != null) {
    		len++;
    		q = q.getNextSibling();
    	}
    	
    	double[] a = new double[len];
    	int i = 0;
    	
    	while (p != null) {
    		a[i] = p.getRunningProbability();
    		i++;
    		p = p.getNextSibling();
    	}
    	
    	return a;
    }
	
}
