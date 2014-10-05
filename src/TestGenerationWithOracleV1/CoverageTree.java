package TestGenerationWithOracleV1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Formatter;
//Added by UNO
import java.util.LinkedList;
import java.util.Iterator;

import InterlacedOracle.AllTestCases;
import InterlacedOracle.InfixArithFaultDetector;
import InterlacedOracle.Submission;
import InterlacedOracle.ReqCauseMap;

public class CoverageTree {

	private TreeNode root;
	private double globalNodeID;
	private SymbolicGrammar sg;
	private Stack<String> stack;
	private Stack<SemNode> semStack;
	private Map< String, ArrayList<Double> > hits;
	private int handler;
	// handler is used to control whether to continue using the existing test generation coverage tree
	// or new a new coverage tree.
	private int testCaseIndex = 0;
	// the index order of a test case, shown in the leaf node of each test case path
	private String outputFileName;
	private Formatter output;
	private String feature;
	private String allFeatures;
	private SemNode sroot;
	// Added by UNO
	private LinkedList<TreeNode> traversalChain = new LinkedList<TreeNode>();
	private LinkedList<String> expressionChain = new LinkedList<String>();
	private LinkedList<String> featureChain = new LinkedList<String>();
	private LinkedList<String> evaluateChain = new LinkedList<String>();
	private String traversalChainFile;
	private String expressionChainFile;
	private String featureChainFile;
	private String evaluateChainFile, FeatureErrorFile, outputTreeFile;
	private Formatter outputTC, outputEC, outputFC, outputEVCF, outputFE, outputTree;
	private int featureCounter=1, node_traversal_count = 0;
	private String parseTree;
	private int testCaseNum;
	private int isStartOfFeature = 1;
	private ArrayList<Double> featureStartNodeIndices = new ArrayList<Double>();
	private ArrayList<String> featureSetAtIndex = new ArrayList<String>();
	private ArrayList<String> errorPatterns = new ArrayList<String>();
	private static String[] SUTName = {"AgillespieInfixArith", "AshearerInfixArith", "BrothInfixArith",
		"CfrankInfixArith", "KcaumeranInfixArith", "KfloresInfixArith", "PkwetebokashangInfixArith",
		"AhazratiInfixArith", "AhclarkeInfixArith", "BalbuloushiInfixArith", "CebiggsInfixArith",
		"GnguyenInfixArith", "MeisnerInfixArith", "RhuInfixArith", "WfloresInfixArith"
};	
	
	public CoverageTree(SymbolicGrammar s) {
		setSymbolicGrammar(s);
		stack = new Stack<String>();
		semStack = new Stack<SemNode>();
		globalNodeID = 0;
	}
	
	public ArrayList<Double> getFeatureStartIndexList()
	{
		return featureStartNodeIndices;
	}
	
	public ArrayList<String> getFeatureSetAtIndexList()
	{
		return featureSetAtIndex;
	}	
	
	private void resetSemTree(String s) {
		sroot = new SemNode(s);
	}
	
	
	private SemNode getSemTreeRoot() {
		return sroot;
	}
	
	
	
	public void resetParseTree() {
		parseTree = "";
	}
	
	private void appendParseTree(String t) {
		if (parseTree.equals(""))
			parseTree = t;
		else if (parseTree.endsWith("(")) 
			parseTree += t;
		else if (parseTree.endsWith(")")) {
			if (t.startsWith(")"))
				parseTree += t;
			else
				parseTree += "," + t;
		}			
	}

	public void printParseTree() {
		output.format("\n Printing Parse Tree\n");
		output.format("[%s] \n", parseTree);
		output.format("\n End of Parse Tree\n");
	}
	
	
	private boolean isEmptyFeature() {
		return (feature.equals(""));
	}
	
	
	private void resetAllFeatures() {
		allFeatures = "";
	}
	
	
	private void resetFeature() {
		feature = "";
	}
	
	private void appendFeature(String f) {
		feature += f;
	}
	
	private void printFeature() {
//		feature = sg.reqProcess(feature);
		//AllTestCases localatc = new AllTestCases();
		allFeatures += feature + " ";
		
		// Added by UNO
		/*featureChain.add(allFeatures);
		String newfeature = feature+" ";
		String newAllfeature = allFeatures;
		
		String reqsingle = sg.reqProcess(newfeature);
		String reqAll = sg.reqProcess(newAllfeature);
		String[] allF = localatc.reducedReq(reqAll.trim());
		String[] thisF = localatc.reducedReq(reqsingle.trim());
		outputFC.format("\nFeature Index: %d Current Feature: %s, Feature Set: %s", featureCounter, feature, allFeatures);
		outputFC.format("\n Current Feature Reduced Requirement: ");
		for(String r : thisF)
		{
			outputFC.format(" %s", r);
		}
		outputFC.format("\n Current Feature Set Reduced Requirement: ");
		for(String r : allF)
		{
			outputFC.format(" %s", r);
		}
		featureCounter++;*/
		
		output.format("%s ", feature);
	}
	
	public void getErroringSubFeatures(int testCaseNum, ArrayList<String> requirementStrings)
	{
		HashMap<String, String> returnHashMap = sg.reqProcessIndividualHashMap(allFeatures);
		HashMap<String, String> patternsInError = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> expressionsList = new HashMap<String, ArrayList<String>>();
		HashMap<String, String> testStringList = new HashMap<String, String>();
		InfixArithFaultDetector evaluatorInstance = new InfixArithFaultDetector();
		ReqCauseMap reqCauseMapInstance = new ReqCauseMap();
		setFeatureErrorFile("FeatureErrorFileperSUTforTestCaseNumber"+testCaseNum+".txt");
		
		/*for(String a: returnHashMap.keySet())
		{
			outputFE.format("\nSub-Feature "+a+": "+returnHashMap.get(a));
		}*/
		outputFE.format("Analysis for Test Case"+testCaseNum+"\n");
		for(int l = 0; l < 15; l++)
		{
			outputFE.format("\nSUT "+SUTName[l]+": \n");
			for(int i = 0; i < requirementStrings.size(); i++)
			{
				ArrayList<String> exprList = new ArrayList<String>();
				String testString = new String();
				int isError = evaluatorInstance.detectIsErroringPatterns(requirementStrings.get(i), testCaseNum, i, l, exprList, testString);
				testStringList.put(requirementStrings.get(i), testString);
				if(isError == 1)
				{
					patternsInError.put(requirementStrings.get(i), "NOK");
					// 
					
					for(String j: returnHashMap.keySet())
					{
						if(returnHashMap.get(j).equals(requirementStrings.get(i)))
						{
							int indexNum = 0; Integer temp = new Integer(0);
							for(String feature: featureSetAtIndex)
							{
								if(feature.equals(j))
								{
									System.out.println("\n ESF = "+j+" start node = "+featureStartNodeIndices.get(indexNum));
									int k = findNodeByID(root, featureStartNodeIndices.get(indexNum), temp);
								}
								indexNum++;
							}
						}
					}
				}
				else
				{
					patternsInError.put(requirementStrings.get(i), "OK");
				}
				
				
				expressionsList.put(requirementStrings.get(i), exprList);

			}
			String statusCode = "NOK";
			int wasAnyError = 0, printIndex = 1;
			for(String k: patternsInError.keySet())
			{
				//if(patternsInError.get(k).equals(statusCode))
				//{
					for(String j: returnHashMap.keySet())
					{
						if(returnHashMap.get(j).equals(k))
						{
							outputFE.format("\n"+(printIndex++)+". SubFeature : "+j);
							outputFE.format("\n   Requirement String: "+k);
							outputFE.format("\n   Test Case Generated:");
							//ArrayList<String> temp = expressionsList.get(returnHashMap.get(j));
							String exprString = reqCauseMapInstance.req2ops(returnHashMap.get(j));
							String temp = reqCauseMapInstance.ops2expr(exprString);
							if(returnHashMap.get(j).trim().equals("[N]"))
							{
								String[] tokens = temp.split(" ");
								temp = tokens[0];
							}
							/*for(int i = 0; i < temp.size(); i++)
							{
								outputFE.format(" "+temp.get(i));
							}*/
							outputFE.format(" "+temp);
							if(patternsInError.get(k).equals(statusCode))
								outputFE.format("\n   Result: Fail");
							else
								outputFE.format("\n   Result: Pass");
							//wasAnyError = 1;
						}
					}
				//}
			}
			/*if(wasAnyError == 0)
			{
				outputFE.format("\n No Sub-Features induce error");
			}*/
			outputFE.format("\n");
			patternsInError.clear();
		}
		closeFeatureErrorFile();
	}
	
	private void printAllFeatures(int testCaseNum) {
		
		output.format("\nBefore: %s", allFeatures);
		sg.setTestCaseNumber(testCaseNum);
		ArrayList<String> requirementStrings = new ArrayList<String>();
		ArrayList<String> tempReturn = new ArrayList<String>();
		
		String[] currReqParts = null;
		String[] uniqueCurrItems = null;
		
		errorPatterns.clear();
		requirementStrings = sg.reqProcessIndividual(allFeatures);
		//HashMap<String, String> returnHashMap = sg.reqProcessIndividualHashMap(allFeatures);
		//HashMap<String, Integer> patternsInError = new HashMap<String, Integer>();
		InfixArithFaultDetector evaluatorInstance = new InfixArithFaultDetector();
		for(int i = 0; i < requirementStrings.size(); i++)
		{
			tempReturn = evaluatorInstance.detectErroringPatterns(requirementStrings.get(i), testCaseNum, i);
			//int isError = evaluatorInstance.detectIsErroringPatterns(requirementStrings.get(i), testCaseNum, i);
			/*if(isError == 1)
			{
				patternsInError.put(requirementStrings.get(i), isError);
			}
			else
			{
				patternsInError.put(requirementStrings.get(i), isError);
			}*/
			if(tempReturn != null)
			{
				for(int j = 0; j < tempReturn.size(); j++)
				{
					if(tempReturn.get(j) != null)
						errorPatterns.add(tempReturn.get(j));
				}
			}
		}
		/*output.format("\nError Inducing Subfeatures: \n");
		for(String k: patternsInError.keySet())
		{
			if(patternsInError.get(k) == 1)
			{
				for(String j: returnHashMap.keySet())
				{
					if(returnHashMap.get(j).equals(k))
					{
						output.format("\nSub-feature: "+j);
					}
				}
			}
		}*/
		int currIndex = 0;
		currReqParts = new String[errorPatterns.size()];
		for(int i = 0; i < errorPatterns.size(); i++)
		{
			if(errorPatterns.get(i) != null && errorPatterns.get(i) != "")
				currReqParts[currIndex++] = errorPatterns.get(i);	
		}
		uniqueCurrItems = new HashSet<String>(Arrays.asList(currReqParts)).toArray(new String[currReqParts.length]);
		output.format("\nError Patterns: \n");
		for(int i = 0; i < errorPatterns.size(); i++)
		{
			if(errorPatterns.get(i) != null)
			output.format("%s, ", errorPatterns.get(i));
		}
		output.format("\nError Patterns Unique: \n");
		for(int i = 0; i < uniqueCurrItems.length; i++)
		{
			if(uniqueCurrItems[i] != null && uniqueCurrItems[i] != "")
			output.format("%s, ", uniqueCurrItems[i]);
		}	
		getErroringSubFeatures(testCaseNum, requirementStrings);
		allFeatures = sg.reqProcess(allFeatures);
		output.format("\nAfter: %s", allFeatures);
	}
	
	//Added by UNO
	public String getAllFeatures()
	{
		return allFeatures;
	}
	
	
	public String getOutputFile() {
		return outputFileName;
	}
	
	public String getFeatureErrorFile() {
		return FeatureErrorFile;
	}	
	
	// Added by UNO
	public String getTraversalChainFile() {
		return traversalChainFile;
	}	
	
	// Added by UNO
	public String getExpressionChainFile() {
		return expressionChainFile;
	}	
	
	// Added by UNO
	public String getFeatureChainFile() {
		return featureChainFile;
	}	
	
	// Added by UNO
	public String getEvaluateChainFile() {
		return evaluateChainFile;
	}	
	
	public Formatter setOutputFile(String s) {
		outputFileName = s;
		try {
			output = new Formatter(getOutputFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getOutputFile() + "\n" + e);
			System.exit(0);
		}	
		return output;
	}
	
	public Formatter setFeatureErrorFile(String s) {
		FeatureErrorFile = s;
		try {
			outputFE = new Formatter(getFeatureErrorFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getFeatureErrorFile() + "\n" + e);
			System.exit(0);
		}	
		return outputFE;
	}	
	
	// Added by UNO
	public Formatter setTraversalChainFile(String s) {
		traversalChainFile = s;
		try {
			outputTC = new Formatter(getTraversalChainFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getTraversalChainFile() + "\n" + e);
			System.exit(0);
		}	
		return outputTC;
	}	

	public Formatter setExpressionChainFile(String s) {
		expressionChainFile = s;
		try {
			outputEC = new Formatter(getExpressionChainFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getExpressionChainFile() + "\n" + e);
			System.exit(0);
		}	
		return outputEC;
	}
	
	public Formatter setFeatureChainFile(String s) {
		featureChainFile = s;
		try {
			outputFC = new Formatter(getFeatureChainFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getFeatureChainFile() + "\n" + e);
			System.exit(0);
		}	
		return outputFC;
	}	
	
	public Formatter setEvaluateChainFile(String s) {
		evaluateChainFile = s;
		try {
			outputEVCF = new Formatter(getEvaluateChainFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getEvaluateChainFile() + "\n" + e);
			System.exit(0);
		}	
		return outputEVCF;
	}
	
	// Added by UNO
	public String getOutputTreeFile() {
		return outputTreeFile;
	}	
	
	public Formatter setOutputTree(String s) {
		outputTreeFile = s;
		try {
			outputTree = new Formatter(getOutputTreeFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getOutputTreeFile() + "\n" + e);
			System.exit(0);
		}	
		return output;
	}	
	
	
	public void closeOutputTreeFile() {
		if (outputTree != null)
			outputTree.close();
	}
	
	public void closeOutputFile() {
		if (output != null)
			output.close();
	}
	
	public void closeFeatureErrorFile() {
		if (outputFE != null)
			outputFE.close();
	}	
	
	// Added by UNO
	public void closeTraversalChainFile() {
		if (outputTC != null)
			outputTC.close();
	}
	
	// Added by UNO
	public void closeExpressionChainFile() {
		if (outputEC != null)
			outputEC.close();
	}	
	
	// Added by UNO
	public void closeFeatureChainFile() {
		if (outputFC != null)
			outputFC.close();
	}
	
	// Added by UNO
	public void closeEvaluateChainFile() {
		if (outputEVCF != null)
			outputEVCF.close();
	}
	
	private int incTestCaseIndex() {
		testCaseIndex++;
		return testCaseIndex;
	}
	
	private void resetTestCaseIndex() {
		testCaseIndex = 0;
	}
	
	
	public void setSymbolicGrammar(SymbolicGrammar s) {
		if (s == null)
			System.err.println("Symbolic Grammar Set Error: no symbolic grammar is given!");
		else
			sg = s;
	}
	
	public SymbolicGrammar getSymbolicGrammar() {
		return sg;
	}
	
	
	private Stack<SemNode> getSemStack() {
		return semStack;
	}
	
/*	private void emptySemStack() {
		getSemStack().removeAllElements();
	}
	
	private boolean isEmptySemStack() {
		return getSemStack().isEmpty();
	}
*/	
	private void pushSemStack(SemNode N) {
		getSemStack().push(N);
	}
	
	private SemNode popSemStack() {
		return getSemStack().pop();
	}	
	
	
	private Stack<String> getStack() {
		return stack;
	}
	
	private void emptyStack() {
		getStack().removeAllElements();
	}
	
	private boolean isEmptyStack() {
//		return getStack().isEmpty();
//		return ( getStack().size() == 2);
		// if all pushed items are with prefix "memo:", then the stack is empty
		int n = getStack().size() - 1;
		boolean emptySoFar = true;
		while (n >= 0 && emptySoFar) {
			emptySoFar = getStack().get(n).startsWith("memo:");
			n--;
		}
		return emptySoFar;
	}
	
	private void pushStack(String s) {
		getStack().push(s);
	}
	
	private String popStack() {
		return getStack().pop();
	}

/*	
	// stack2Str() return an underived string in a sentential form
	private String stack2Str() {
		
		int s = getStack().size();
		String str = "";
		
		for (int i = s-1; i >= 0; i--) {
			if ( !getStack().elementAt(i).startsWith("memo:") )
				str = str + getStack().elementAt(i);
//			str = str + " " + getStack().elementAt(i);
		}
		return str;
	}
*/	
	
/*	private String dumpTerminalPrefixFromStack() {

		int s = getStack().size() - 1;
		String str = "";
		
		while ( s >= 0 ) {
			String token = getStack().elementAt(s);
			if ( token.startsWith("memo:") )
				break;
			if ( getSymbolicGrammar().isVar(token) )
				break;
			popStack();
			str += token;
			s--;
		}		
		return str;
	}
*/	
/*	private String peepStack() {
		return stack.peek();
	}
*/
	
	
	private void newHits() {
//		hits = new HashMap< String, ArrayList<Integer> >();
		sg.resetHitsMap();
		hits = sg.getHitsMap();
//		sg.resetHitsMap();
	}

	public ArrayList<Double> getHits(String key) {		

		return hits.get(key);
		
	}
	


	private double totalHits(ArrayList<Double> hs) {
		
		double t = 0.0;
		double h1 = hs.get(0);
		for (double h : hs) {
			t += h1 / h;
		}
		
		return t;
	}
	
	
	private TreeNode getDerivedNode(TreeNode p, String token) {
		
		TreeNode child = p.getFirstChild();
		ArrayList<Double> tokenHits = sg.getHits(token);
		double total = totalHits(tokenHits);
		int i = 0;
		
		//update the derivation stack
		int previous = stack.lastIndexOf("memo:"+token);
		
		evaluateChain.add("getDerivedNode:DS Previous Index: "+previous+" for token: "+token+" from "+node_traversal_count+" run of testCaseGeneration");
		
		if (previous > 0) {
			// the variable has been met before
			// pi tells which grammar rule was applied when the variable was invoked last time
			int pi = Integer.parseInt( stack.get(previous - 1).substring(5) );
			
			
			// check from stack position: previous + 1  ==> stack.size()
			// to find out the depth of recursion

/*			// linear weight increment
			double w = 1.0;
			// update the hits
			tokenHits.set(pi, w + tokenHits.get(pi));
*/
			
/*			// weight is decreased by the depth of recursion
 * 			double w = 1.0;
 			for (int j = previous + 1; j < stack.size(); j++) {
				if ( stack.get(j).matches("memo:[A-Z].*") )
					w /= 2;
			}
*/			
			// weight increases in an exponential rate
			// update the hits
			tokenHits.set(pi, 2.0 * tokenHits.get(pi));
			
			sg.setHitsMap(token, tokenHits);
			total = totalHits(tokenHits);
		}
		
		if (child != null) { // child nodes already exist
/*			if (child.getHandler() != getHandler()) {			
				// if not current handler, reset nodes
				
				TreeNode q = child;
				i = 0;
				while (q != null) {
					q.nodeReset(getHandler(), (tokenHits.get(0) / tokenHits.get(i)) / total );
					q = q.getNextSibling();
					i++;
				}
			}	
*/
		}
		else { // no children yet

			TreeNode q = null;
			i = 0;
			for (double hit : tokenHits) {
				if (q == null) {
					q = new TreeNode();
					q.setNodeGUID(globalNodeID++);
					q.nodeReset(getHandler(), ( tokenHits.get(0) / hit )  / total );
					// the probability calculation from Hits distribution:
					// probability = (TotalHits - hit) / (TotalHits * (n - 1))
					// where n is the number of grammar rules
					child = q;
					i++;
				}
				else {
					q.setNextSibling(new TreeNode());
					q = q.getNextSibling();
					q.setNodeGUID(globalNodeID++);
					q.nodeReset(getHandler(), ( tokenHits.get(0) / hit )  / total );
					i++;
				}
			}
			
			p.setFirstChild(child);
		}
		
		// select a child node in a random way, based on runtime probability distribution
		double r = Math.random();
		double a = 0.0;

		i = 0;
		do {
			a += child.getRunningProbability();
			if (a < r) {
				child = child.getNextSibling();
				if (child == null)
					break;
				else
					i++;
			}
			else {
				break;
			}
		} while (true);
					
		// the following two pushes are critical 
		// for adjusting the distributed probabilities of
		// recursively defined grammar rules
		
		appendFeature( token+"#" );
		appendFeature( String.valueOf(i) );
		appendFeature( "+" );
		
		String currFeature = token+"#"+String.valueOf(i)+"+";
		evaluateChain.add("getDerivedNode:Insert Exp: "+feature);
		
        // the next line is related to parse tree
				appendParseTree(token+i+"(");
		
		GrammarRule grammar = sg.getGrammar(token).get(i);
		String[] tokens = grammar.getStringTokenizer();

		int j = tokens.length;
/*
		while (j > 0) {
			j--;
			stack.push(tokens[j]);
		}
*/		
//		deriveVarSemStack(token, i);   // old version
		
		if ( !stack.isEmpty() && stack.peek() == "semantics" ) {
			
			evaluateChain.add("getDerivedNode:DS pop: "+stack.peek());
			stack.pop();  // remove the indicator "semantics"
			SemNode sp = semStack.pop();
			evaluateChain.add("getDerivedNode:SS pop: "+sp.getElement());

			stack.push("memo:"+i);
			evaluateChain.add("getDerivedNode:DS push: memo:"+i);
			stack.push("memo:"+token);
			evaluateChain.add("getDerivedNode:DS push: memo:"+token);

			SemanticFun f = getSymbolicGrammar().getSemantics(token).get(i);
			if (f == null) {
				// if the variable (token) has no semantic valuation function defined, 
				// the default one is: (token[0])
				sp.setElement( clearQuotesfromTerminal(tokens[0]) );
				
				while (j > 1) {
					j--;
					stack.push(tokens[j]);
					evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
				}

				if ( isVar(tokens[0]) || isSymbolicTerminal(tokens[0]) ) {
					// push semantic node back to the semantic stack
					stack.push("semantics");
					evaluateChain.add("getDerivedNode:DS push: semantics");
					stack.push(tokens[0]);
					evaluateChain.add("getDerivedNode:DS push: "+tokens[0]);
					semStack.push(sp);
					evaluateChain.add("getDerivedNode:SS push: "+sp.getElement());
					
				}
				else 
				{
					stack.push(tokens[0]);
					evaluateChain.add("getDerivedNode:DS push: "+tokens[0]);
				}
			}
			else if (f.isSingletonFun()) {
				// semantic tree simplification
				String varName = f.getSemanticFun().getElement();
				sp.setElement( clearQuotesfromTerminal(varName) );
				
				if ( isVar(varName) || isSymbolicTerminal(varName) ) {
					// push semantic node back to the semantic stack
					while (j > 0) {
						j--;
						if (tokens[j].equals(varName) ) {
							stack.push("semantics");
							evaluateChain.add("getDerivedNode:DS push: semantics");
							stack.push(tokens[j]);
							evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
							semStack.push(sp);
							evaluateChain.add("getDerivedNode:SS push: "+sp.getElement());
						}
						else {
							stack.push(tokens[j]);
							evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
						}
					}
				}
				else {
					while (j > 0) {
						j--;
						stack.push(tokens[j]);
						evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
					}
				}
			}
			else {
				sp.setFirstChild( f.getCopySemanticFun() );
				
				while (j > 0) {
					j--;
					
					SemNode q = f.find(tokens[j], sp.getFirstChild().getFirstChild());
					if ( q != null ) {
						stack.push("semantics");
						evaluateChain.add("getDerivedNode:DS push: semantics");
						stack.push(tokens[j]);
						evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
						semStack.push(q);
						evaluateChain.add("getDerivedNode:SS push: "+q.getElement());
					}
					else {
						stack.push(tokens[j]);
						evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
					}
				}
				
			}	
		}
		else {
			stack.push("memo:"+i);
			evaluateChain.add("getDerivedNode:DS push: memo:"+i);
			stack.push("memo:"+token);
			evaluateChain.add("getDerivedNode:DS push: memo:"+token);

			while (j > 0) {
				j--;
				stack.push(tokens[j]);
				evaluateChain.add("getDerivedNode:DS push: "+tokens[j]);
			}			
		}
		
		child.setExpressionAtNode(currFeature);
	    if(isStartOfFeature == 1)
	    {
	    	featureStartNodeIndices.add(globalNodeID-1);
	    	isStartOfFeature = 0;
	    }
		return child;				
		
	}
		
	
	
	public String newTestCase(int testCase) {
		
		TreeNode root = getCoverageTree();
		String tc;
		
		if (root.isCovered()) {
			System.out.println("The test coverage tree is completely covered! No more test cases will be generated!");
			return null;
		}
		testCaseNum = testCase;
		newHits();   /* reset the hits tracing for each new test case */
		emptyStack();
		resetFeature();
		resetAllFeatures();
//		resetParseTree();
		resetSemTree(getSymbolicGrammar().getMainVar());
		
		/* "semantics" is an indication to pop an item from the SemStack
		   it's a synchronization between (derivation) stack and SemStack */
		pushStack( "semantics" ); 
		evaluateChain.add("newTestCase:DS push: semantics");
		String thisValue = getSymbolicGrammar().getMainVar();
		pushStack( thisValue );
		evaluateChain.add("newTestCase:DS push: "+thisValue);
		
		pushSemStack( getSemTreeRoot() );
		evaluateChain.add("newTestCase:SS push: "+getSemTreeRoot().getElement());
		
		featureCounter=1;
		outputFC.format("\nFeature Chain for test case "+testCaseNum+"....");
		node_traversal_count = 0;
		//exportCoverageTree(node_traversal_count);
		tc = testCaseGeneration(root);
		/*Integer foundFlag = new Integer(0);
		int m = findNodeByID(root, featureStartNodeIndices.get(1), foundFlag);
		if(foundFlag == 1)
		{
			System.out.println("\nWas Deleted");
		}*/
		//exportCoverageTree(99);
		//exportCoverageTreeWithProbability(100);
		outputFC.format("\n--------End of Feature Chain--------\n");
		
		printAllFeatures(testCaseNum);
		//closeOutputTreeFile();
		return tc;
	}
	
	
	public String evaluate() {
		
		try {
			return evaluate( getSemTreeRoot() );
		}
		catch (Exception e) {
			return "exception";
		}
	}
	
		
	// evaluate the semantic node p, 
	// and write the evaluation result as p.element 
	// and return the evaluation result.
	private String evaluate(SemNode p) {
		try {	
		if (p.getFirstChild() == null) 
			return p.getElement();
		
		p.setElement( evaluateFun(p.getFirstChild()) );
		return p.getElement();
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	
	private String evaluateFun( SemNode p ) {
		try
		{
		SemNode lambda = p.getFirstChild();
		
		// evaluate the lambda list and 
		// write the evaluation results into the list
		while (lambda != null) {
			evaluate(lambda);
			lambda = lambda.getNextSibling();
		}
		
		SemNode q = p.getNextSibling();
		
		return evaluateFunRec(q);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	
		
	private String evaluateFunRec(SemNode q) {
		try
		{
		String functor = q.getElement();
		evaluateChain.add("evaluateFunRec:functor:"+functor);
		String args = null; 
		
		q = q.getNextSibling();
		while (q != null) {
			String str;
			if ( q.getElement() == null ) {
				// a nested semantic fun
				str = evaluateFunRec(q.getFirstChild());
			}
			else if ( isVar(q.getElement()) || isSymbolicTerminal(q.getElement()) ) 
				str = q.getFirstChild().getElement();  // retrieve value from the lambda list
			else
				str = q.getElement();
			
			if (args == null)
			{
				args = str;
				evaluateChain.add("evaluateFunRec: "+args);
			}
			else
			{
				args = args + "#" + str;
				evaluateChain.add("evaluateFunRec: "+args);
			}
			
			q = q.getNextSibling();
		}
		
		return Funs.apply(functor, args.split("[#]"));
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}

	// Added by UNO
	/* This method will print the TreeNode details in the Traversal Chain for a particular
	 * test case. The traversal index simply enumerates the TreeNodes in this chain and has
	 * no relation to the index variable in the TreeNode structure.
	 */
	public void dumpTraversalChain(int i)
	{
		Iterator<TreeNode> itr = traversalChain.iterator();
		int traversalIndex = 1, isCoveredVal = 0, hasChild=0, hasSibling=0;
		outputTC.format("\nTraversal Chain for test case "+i+"....");
		while(itr.hasNext())
		{
			TreeNode temp = itr.next();
			if(temp.isCovered())
				isCoveredVal = 1;
			else
				isCoveredVal = 0;
			if(temp.getFirstChild() != null)
			{
				hasChild = 1;
			}
			else
			{
				hasChild = 0;
			}
			if(temp.getNextSibling() != null)
			{
				hasSibling = 1;
			}
			else
			{
				hasSibling = 0;
			}
			
			outputTC.format("\nTraversal Index: %d Tree Node Information: (%d, %f, %d, %d, %s)", traversalIndex, temp.getNodeTraversalIndex(), temp.getRunningProbability(), temp.getIndex(), isCoveredVal, temp.getExpressionAtNode());
			if(hasChild == 1)
			{
				if(temp.getFirstChild().isCovered())
					isCoveredVal = 1;
				else
					isCoveredVal = 0;
				outputTC.format("  Child Node Information: (%f, %d, %d, %s)", temp.getFirstChild().getRunningProbability(), temp.getFirstChild().getIndex(), isCoveredVal, temp.getFirstChild().getExpressionAtNode());
			}
			if(hasSibling == 1)
			{
				if(temp.getNextSibling().isCovered())
					isCoveredVal = 1;
				else
					isCoveredVal = 0;
				outputTC.format("  Sibling Node Information: (%f, %d, %d, %s)", temp.getNextSibling().getRunningProbability(), temp.getNextSibling().getIndex(), isCoveredVal, temp.getNextSibling().getExpressionAtNode());
			}
            traversalIndex++;
		}
		outputTC.format("\n--------End of Traversal Chain--------\n");
	}
	
	// Added by UNO
	/* Clearing the traversal chain before the next test case */
	public void resetTraversalChain()
	{
		traversalChain.clear();
	}
	
	// Added by UNO
	/* Returns the length of the current traversal chain */
	public void lengthTraversalChain()
	{
		traversalChain.size();
	}
	
	// Added by UNO
	/* This method can be used to pop the nodes in the traversal chain
	 * if it is required by external application code.
	 */
	public void popTraversalChain()
	{
		traversalChain.pop();
	}	
	
	// Added by UNO
	/* This method will print the TreeNode details in the Traversal Chain for a particular
	 * test case. The traversal index simply enumerates the TreeNodes in this chain and has
	 * no relation to the index variable in the TreeNode structure.
	 */
	public void dumpExpressionChain(int i)
	{
		Iterator<String> itr = expressionChain.iterator();
		int expressionIndex = 1;
		outputEC.format("\nExpression Chain for test case "+i+"....");
		while(itr.hasNext())
		{
			outputEC.format("\nExpression Index: %d Expression: %s", expressionIndex, itr.next());
			expressionIndex++;
		}
		outputTC.format("\n--------End of Expression Chain--------\n");
	}
	
	// Added by UNO
	/* Clearing the exoression chain before the next test case */
	public void resetExpressionChain()
	{
		expressionChain.clear();
	}
	
	// Added by UNO
	/* Returns the length of the current expression chain */
	public void lengthExpressionChain()
	{
		expressionChain.size();
	}
	
	// Added by UNO
	/* This method can be used to pop the nodes in the expression chain
	 * if it is required by external application code.
	 */
	public void popExpressionChain()
	{
		expressionChain.pop();
	}	
	

	// Added by UNO
	/* Clearing the feature chain before the next test case */
	public void resetFeatureChain()
	{
		featureChain.clear();
	}
	
	// Added by UNO
	/* Returns the length of the current feature chain */
	public void lengthFeatureChain()
	{
		featureChain.size();
	}
	
	// Added by UNO
	/* This method can be used to pop the nodes in the feature chain
	 * if it is required by external application code.
	 */
	public void popFeatureChain()
	{
		featureChain.pop();
	}	
	
	// Added by UNO
	/* This method will print the TreeNode details in the Traversal Chain for a particular
	 * test case. The traversal index simply enumerates the TreeNodes in this chain and has
	 * no relation to the index variable in the TreeNode structure.
	 */
	public void dumpEvaluateChain(int i)
	{
		Iterator<String> itr = evaluateChain.iterator();
		int evaluateIndex = 1;
		outputEVCF.format("\nEvaluate Chain for test case "+i+"....");
		while(itr.hasNext())
		{
			outputEVCF.format("\nEvaluate Index: %d -> %s", evaluateIndex, itr.next());
			evaluateIndex++;
		}
		outputEVCF.format("\n--------End of Evaluate Chain--------\n");
	}
	
	// Added by UNO
	/* Clearing the traversal chain before the next test case */
	public void resetEvaluateChain()
	{
		evaluateChain.clear();
	}
	
	// Added by UNO
	/* Returns the length of the current traversal chain */
	public void lengthEvaluateChain()
	{
		evaluateChain.size();
	}
	
	// Added by UNO
	/* This method can be used to pop the nodes in the traversal chain
	 * if it is required by external application code.
	 */
	public void popEvaluateChain()
	{
		evaluateChain.pop();
	}		
	

	private String testCaseGeneration(TreeNode p) {
		
		// Added by UNO
		
		node_traversal_count++;
		p.setNodeTraversalIndex(node_traversal_count);
		p.setTestCaseAtStart("");
		p.setExpressionAtNode(feature);
		traversalChain.add(p);
		evaluateChain.add("testCaseGeneration: New Tree Node arrived: "+node_traversal_count+" Feature: "+feature);
		
		
		if ( isEmptyStack() ) 
		{
			// record the index at the leaf node of the path
			p.setIndex( incTestCaseIndex() );
			p.setCovered(true);
		}
		
		// if stack is not empty, test case generation continues ...
		String token = popStack();
		
		if (token.startsWith("memo:")) 
		{
			// the end of derivation for the variable "token"
			evaluateChain.add("testCaseGeneration: In Starting token with memo (token = "+token+")");
			token = token.substring(5);
			popStack();
			// the next line is related to parse tree
						appendParseTree(")");
	/*		
			int previous = stack.lastIndexOf("memo:"+token);

			if (previous > 0) {
				// the variable has been met before
				int pi = Integer.parseInt( stack.get(previous - 1).substring(5) ); 
				// remove the prefix "memo:"
				
//				ArrayList<Double> tokenHits = sg.getHits(token);

//				double w = 1.0;
				for (int j = previous + 1; j < stack.size(); j++) {
					if ( stack.get(j).matches("memo:[A-Z].*") )
						w /= 2;
				}
				
				// update the hits
//				tokenHits.set(pi, tokenHits.get(pi) / 2.0);
//				sg.setHitsMap(token, tokenHits);
			}
*/			node_traversal_count--;
			return testCaseGeneration(p);
		}
		
		String testcase="";
			
//		p.setSymbols(token);
		if ( isVar(token) ) 
		{
			evaluateChain.add("testCaseGeneration: In token is var (token = +"+token+")");
/*			if ( isEmptyFeature() )
				appendFeature(token);
	*/		
			
	// save space to comment the next line
	//		p.setSymbols( token + stack2Str() );
			TreeNode q = getDerivedNode(p, token);
//			q.setDerivedSymbols("");
			testcase = testCaseGeneration(q);
			if (q.isCovered()) 
			{
				// if a child is recently completely covered
				if (p.checkCoveredViaChildren()) 
				{
					// if all children are completely covered
					// in the other words, if p is completely covered
					p.setCovered(true);
					// no need to adjust the children's distributed probabilities
					// since they won't be visited again
				}
				else 
				{
					// q is a new completely covered node
					double d = q.getRunningProbability();
					q.setRunningProbability(0.0);
					adjustChildPropDistribution(p, d);
				}
			}
			
			// Added by UNO
			expressionChain.add("token (= "+token+" ) is var: "+testcase);
			//
			node_traversal_count--;
			return testcase;
		}
		else 
		{ // token is a regular terminal or symbolic terminal
			
	// to save space to comment the next line
	//		p.setDerivedSymbols( p.getDerivedSymbols() + token.replace("'", "") );
			evaluateChain.add("testCaseGeneration: In Var is not token (token = "+token+")");
			if ( !isEmptyFeature() ) 
			{
				evaluateChain.add("testCaseGeneration: Feature print");
				printFeature();
				isStartOfFeature = 1;
				evaluateChain.add("newTestCase:Feature Generated: "+feature);
				featureSetAtIndex.add(feature);
				resetFeature();
			}
			
			String ins;
			if (isSymbolicTerminal(token)) 
			{
				evaluateChain.add("testCaseGeneration: Symbolic Terminal (token = "+token+")");
				ins = getSymbolicGrammar().anInstanceFromSymbolic(token);
				//evaluateChain.add(ins);
				if ( !stack.isEmpty() && stack.peek() == "semantics" ) 
				{
					stack.pop();  // remove the indicator "semantics"
					SemNode sp = semStack.pop();
					sp.setElement(ins);
				}
			}
			else 
			{
				evaluateChain.add("testCaseGeneration: Clear Quotes");
				ins = clearQuotesfromTerminal(token);
//				ins = token.replace("'", "");
			}
			
			
			if (isEmptyStack()) 
			{
				evaluateChain.add("testCaseGeneration: Empty Stack");
//				p.setSymbols(null);
				p.setCovered(true);   // leaf node
				p.setIndex( incTestCaseIndex() );
		
			  // the following lines for parse tree	
				String s = "";
				for (int i = 0; i < getStack().size() / 2; i++) {
					s += ")";
				}
				appendParseTree(s);
				
    			
				// Added by UNO
				//expressionChain.add(ins);
				node_traversal_count--;
				return ins;
			}
			else 
			{
				evaluateChain.add("testCaseGeneration: Not Empty Stack");
/*				TreeNode child = p.getFirstChild();
				
				if (child == null) {
					child = new TreeNode();
					p.setFirstChild(child);
				}
				
				child.nodeReset(getHandler(), 1.0);
*/				
				testcase = ins + testCaseGeneration(p);
				
				// Added by UNO
				expressionChain.add("token (= "+token+" ) is not var stack not empty: " + testcase);
				//evaluateChain.add(evaluate());
				node_traversal_count--;
				return testcase;				
			}
		}
			
	}
	
	
	private String clearQuotesfromTerminal(String s) {
		
		if (s.charAt(0) == '\'' && s.charAt(s.length()-1) == '\'') 
			return s.substring(1, s.length()-1);
		else
			return s;
	}
	
	
	

	public TreeNode newCoverageTree() {
		
		resetTestCaseIndex();
		if (getCoverageTree() != null) {
			incHandler();
//			getCoverageTree().setHandler( getHandler() );
			//System.out.println(getCoverageTree());
			return getCoverageTree();
		}
		else if (getSymbolicGrammar() != null) {
			setHandler(0);
			newARoot();
			
			return getCoverageTree();
		}
		else {
			System.err.println("Coverage Tree Generation Error: no symblic grammar is set!");
			return null;
		}
	}
	
	
	private void newARoot() {
		root = new TreeNode();
		root.setNodeGUID(globalNodeID++);
//		root = new TreeNode(getHandler(), "start");
		// create a dummy "start" node, the root of a new coverage tree.
	}
	
	
	
	private void incHandler() {
		setHandler( getHandler() + 1 );
	}
	
	
	private void setHandler(int h) {
		handler = h;
	}
	
	private int getHandler() {
		return handler;
	}
	
	
	public TreeNode getCoverageTree() {
		//System.out.println(root);
		return root;
	}
	
	private void adjustChildPropDistribution(TreeNode p, double d) {
		
		TreeNode child = p.getFirstChild();
		double newTotal = 1.0 - d;
		
		while (child != null) {
			child.setRunningProbability( child.getRunningProbability() / newTotal );
			child = child.getNextSibling();
		}
	}
	
	
	
/*	public void exportCoverageTree(int levelnum) {
		setOutputTree("coverageTree_"+testCaseNum+"_Level_"+levelnum+".dot");
		
		outputTree.format("digraph G{\n");
		exportCoverageTree(root, 1);
		outputTree.format("}\n");
		
		closeOutputTreeFile();
	}
	

	private int exportCoverageTree(TreeNode p, int i) {
		
		// output the current node
		String symbol = p.getExpressionAtNode();
//		if (symbol != null) {
		if ( !p.isCovered() ) {
			if ( symbol != null )
				outputTree.format("    n%d[label=\"%s\"];\n", i, symbol);
		}
		else if (symbol != null){
			outputTree.format("    n%d[label=\"%s\",style=filled,color=\".7 .3 1.0\"];\n", i, symbol);
		}
		else {
			outputTree.format("    n%d[peripheries=2,label=\"\",width=.1,height=.1,shape=box,style=filled,color=\".7 .3 1.0\"];\n", i);			
		}
//		}
//		else
//			output.format("    n%d[label=\"?\"];\n", i);
			
		int j = i + 1;
		
		TreeNode child = p.getFirstChild();
		while (child != null) {
			if ( child.isCovered() || child.getExpressionAtNode() != null ) {
				outputTree.format("    n%d -> n%d [label=\"%s\"];\n", i, j, child.getExpressionAtNode());
				j = exportCoverageTree(child, j);
			}
			child = child.getNextSibling();
		}
		
		return j;
	}*/

	
	
/*	public void exportCoverageTreeWithProbability(int levelNum) {
		setOutputTree("coverageTree_"+testCaseNum+"_Level_"+levelNum+".dot");
		
		outputTree.format("digraph G{\n");
		outputTree.format("    node [shape = record];");
		exportCoverageTreeWithProbability(root, 1);
		outputTree.format("}\n");
		
		closeOutputTreeFile();
	}
	
	
	private int exportCoverageTreeWithProbability(TreeNode p, int i) {
		
		// output the current node
		String symbol = p.getExpressionAtNode();
		double nodeguid = p.getNodeGUID();
		String s;
//		if (symbol != null) {
		if ( !p.isCovered() ) {
			if ( symbol != null ) {
				double[] a = p.getChildrenProbability();
				if(a.length == 0.0 )
				{
					 s = String.format("<f0> %.2f", 0.0);
				}
				else
				{
					 s = String.format("<f0> %.2f", a[0]);
				}
				int k = 1;
				
				while (k < a.length) {
					 s = String.format("%s |<f%d> %.2f", s, k, a[k]);
					k++;
				}
				
				outputTree.format("    n%d[label=\"{%s | {%s}}\"];\n", i, symbol, s);
			}
		}
		else if (symbol != null){
			outputTree.format("    n%d[label=\"%s %f\",style=filled,color=\".7 .3 1.0\"];\n", i, symbol, nodeguid);
		}
		else {
			outputTree.format("    n%d[peripheries=2,label=\"\",width=.1,height=.1,shape=box,style=filled,color=\".7 .3 1.0\"];\n", i);			
		}
//		}
//		else
//			output.format("    n%d[label=\"?\"];\n", i);
			
		int j = i + 1;
		
		TreeNode child = p.getFirstChild();
		int k = 0;
		
		while (child != null) {
			if ( child.isCovered() || child.getExpressionAtNode() != null ) {
				outputTree.format("    n%d:<f%d> -> n%d [label=\"%s %f\"];\n", i, k, j, child.getExpressionAtNode(), child.getNodeGUID());
				j = exportCoverageTreeWithProbability(child, j);
			}
			child = child.getNextSibling();
			k++;
		}
		
		return j;
	}*/
	
	// Added by UNO
	public int findNodeByID(TreeNode p, double i, Integer wasFound) {
		
		// output the current node
		if(p!=null)
		{
		double nodeguid = p.getNodeGUID();
		
		if(nodeguid == i)
		{
			p.setCovered(true);
			System.out.println("\nNode set as covered");
			wasFound = 1;
			return 0;
		}
		}
			
		
/*		String s;
//		if (symbol != null) {
		if ( !p.isCovered() ) {
			if ( symbol != null ) {
				double[] a = p.getChildrenProbability();
				if(a.length == 0.0 )
				{
					 s = String.format("<f0> %.2f", 0.0);
				}
				else
				{
					 s = String.format("<f0> %.2f", a[0]);
				}
				int k = 1;
				
				while (k < a.length) {
					 s = String.format("%s |<f%d> %.2f", s, k, a[k]);
					k++;
				}
				
				outputTree.format("    n%d[label=\"{%s | {%s}}\"];\n", i, symbol, s);
			}
		}
		else if (symbol != null){
			outputTree.format("    n%d[label=\"%s %d\",style=filled,color=\".7 .3 1.0\"];\n", i, symbol, nodeguid);
		}
		else {
			outputTree.format("    n%d[peripheries=2,label=\"\",width=.1,height=.1,shape=box,style=filled,color=\".7 .3 1.0\"];\n", i);			
		}
//		}
//		else
//			output.format("    n%d[label=\"?\"];\n", i); */
			
		//int j = i + 1;
		if(p!=null)
		{
			TreeNode child = p.getFirstChild();
		//int k = 0;
		
		while (child != null) {
			//if ( child.isCovered() || child.getExpressionAtNode() != null ) {
//				outputTree.format("    n%d:<f%d> -> n%d [label=\"%s %d\"];\n", i, k, j, child.getExpressionAtNode(), child.getNodeGUID());
				int k = findNodeByID(child, i, wasFound);
				if(child != null)
				{
				if(child.getNodeGUID() == i)
				{
					p.setCovered(true);
					System.out.println("\nNode set as covered");
					wasFound = 1;
					return 0;
				}
				}
			//}
			child = child.getNextSibling();
			if(child != null)
			{
			if(child.getNodeGUID() == i)
			{
				p.setCovered(true);
				System.out.println("\nNode set as covered");
				wasFound = 1;
				return 0;
			}	
			}
			//k++;
		}
		}
		
		return 0;
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
