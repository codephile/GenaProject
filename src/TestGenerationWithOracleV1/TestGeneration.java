package TestGenerationWithOracleV1;

import java.util.Formatter;
//import java.util.Scanner;
import java.io.*;
import InterlacedOracle.*;

public class TestGeneration {
	
	public static void main( String[] args ) {
		
		Formatter output, outputExpr;
		// Added by UNO
		Formatter outputTC, outputEC, outputFC, outputEVCF;
		InfixArithFaultDetector socketSUT = new InfixArithFaultDetector();
		
		
		SymbolicGrammar g = new SymbolicGrammar();
		//Submission sub = new Submisison();
		
//		g.createGrammarFromFile("fdTestAddCFGwithSem.txt");
		g.createGrammarFromFile("testSEM.txt");
		System.out.println("Grammar Map:");
		g.printGrammarMap();
		System.out.println("End of Grammar Map");
		
//		output = t.setOutputFile("test1WithSem.txt");
		
		//g.reqProcess("Factor#0+Term#1+ ");
		try {
			outputExpr = new Formatter("testcases.txt");
			for(int testSUTNum = 0; testSUTNum < 15; testSUTNum++)
			{
				CoverageTree t = new CoverageTree(g);
				t.newCoverageTree();
				
				output = t.setOutputFile("casesWithSem_SUT-"+testSUTNum+".txt");
				
				// Added by UNO
				//outputTC = t.setTraversalChainFile("traversalChainperTestCase_SUT-"+testSUTNum+".txt");
				//outputEC = t.setExpressionChainFile("ExpressionChainperTestCase_SUT-"+testSUTNum+".txt");
				//outputFC = t.setFeatureChainFile("FeatureChainperTestCase_SUT-"+testSUTNum+".txt");
				//outputEVCF = t.setEvaluateChainFile("EvaluateChainperTestCase_SUT-"+testSUTNum+".txt");				
				
				for (int i = 0; i < 100; i++) 
				{
					output.format("%d: ", i);
					
					// Added by UNO
					//System.out.println("Before Traversal Chain:");
					//t.resetTraversalChain();
					//System.out.println("Before Reset Expr Chain:");
					//t.resetExpressionChain();
					//System.out.println("Before Reset Feature Chain:");
					//t.resetFeatureChain();
					//System.out.println("Before Reset Evaluate Chain:");
					//t.resetEvaluateChain();
					//System.out.println("Before Reset Parse Chain:");
					//t.resetParseTree();
					//System.out.println("Before New Test CAse Chain:");
					String test = t.newTestCase(i, testSUTNum);
					if (test == null) break;
					
					// Added by UNO
					//System.out.println("Before get all features Chain:");
					/*String reqString = t.getAllFeatures();
					String resultString = "Expected Result: "+t.evaluate();
					String logFileName = "TestCaseNo"+i+"Result.txt";
					socketSUT.feedSUTSingleTestCase(reqString, test, resultString, logFileName, i);*/
					
					output.format("\n");
					
					//t.printParseTree();
					output.format("[[ %s ]]\n", test);
					output.format("Expected Result: %s\n\n", t.evaluate());
					
					// Added by UNO
					//t.dumpTraversalChain(i);
					//t.dumpExpressionChain(i);
					//t.dumpEvaluateChain(i);
					//System.out.println("Before print parse tree Chain:");
					//t.printParseTree();
					
					outputExpr.format("%s\n", test);
				}
				t.closeOutputFile();
				
				// Added by UNO
				//t.closeTraversalChainFile();
				//t.closeExpressionChainFile();
				//t.closeFeatureChainFile();
				//t.closeEvaluateChainFile();
				
				System.out.println("\nSUT "+testSUTNum+" process completed.");
			}
			outputExpr.close();
		}
		catch ( FileNotFoundException e1 ) {
			System.err.println( "Exception: File cannot be created!");
			System.exit(1);
		}
		
/*		Scanner input; 
		
		try {
			input = new Scanner( new File( "TR_1000ExpWithReq.txt" ) );
			output = new Formatter( "TR_1000ExpWithReqPlus.txt" );

			int j = 1;
			while (input.hasNextLine()) {
				String rLine = input.nextLine();
//				String pLine = input.nextLine();
				String eLine = input.nextLine();
				input.nextLine();
				
				rLine = rLine.replaceFirst("\\d+:", "").trim();
//				pLine = pLine.replaceFirst("\\[", "").replaceFirst("\\]","").trim();
//				eLine = eLine.replaceFirst("\\[\\[", "").replaceFirst("\\]\\]","").trim();
				
				rLine = g.reqProcess(rLine);
				
				output.format("%d: %s\n", j, rLine);
				output.format("%s\n\n", eLine);
				
				j++;
			}

			input.close();
			output.close();
			
		}
		catch ( Exception e1 ) {
			System.err.printf( "Error Opening file: %s.\n", e1 );
			System.exit(1);
		}
	*/	
		
//		t.exportCoverageTree();
//		t.exportCoverageTreeWithProbability();
	}
	
	
	
	public static void gena( String arg, int size ) {
		
		Formatter output, outputExpr;
		
		SymbolicGrammar g = new SymbolicGrammar();
		
		g.createGrammarFromFile(arg);
//		g.createGrammarFromFile("fdTestAddCFGwithSem.txt");
//		g.createGrammarFromFile("fdTestAddCFG.txt");
//		g.createGrammarFromFile("testSEM1.txt");
//		g.createGrammarFromFile("testWebSemGrammar.txt");
		g.printGrammarMap();
		
		CoverageTree t = new CoverageTree(g);
		
		t.newCoverageTree();
//		output = t.setOutputFile("test1WithSem.txt");
		output = t.setOutputFile("fdTestAddWithSem.txt");
		
		try {
			outputExpr = new Formatter("fdTestAddcases.txt");
		
			for (int i = 0; i < size; i++) {
				output.format("%d: ", i);
				String test = t.newTestCase(i, i);
				if (test == null) break;
				
				output.format("\n");
				
//				t.printParseTree();
				output.format("[[ %s ]]\n", test);
				output.format("Expected Result: %s\n\n", t.evaluate());
				
				outputExpr.format("%s\n", test);
			}
			t.closeOutputFile();
			outputExpr.close();
		}
		catch ( FileNotFoundException e1 ) {
			System.err.println( "Exception: File cannot be created!");
			System.exit(1);
		}
		
/*		Scanner input; 
		
		try {
			input = new Scanner( new File( "TR_1000ExpWithReq.txt" ) );
			output = new Formatter( "TR_1000ExpWithReqPlus.txt" );

			int j = 1;
			while (input.hasNextLine()) {
				String rLine = input.nextLine();
//				String pLine = input.nextLine();
				String eLine = input.nextLine();
				input.nextLine();
				
				rLine = rLine.replaceFirst("\\d+:", "").trim();
//				pLine = pLine.replaceFirst("\\[", "").replaceFirst("\\]","").trim();
//				eLine = eLine.replaceFirst("\\[\\[", "").replaceFirst("\\]\\]","").trim();
				
				rLine = g.reqProcess(rLine);
				
				output.format("%d: %s\n", j, rLine);
				output.format("%s\n\n", eLine);
				
				j++;
			}

			input.close();
			output.close();
			
		}
		catch ( Exception e1 ) {
			System.err.printf( "Error Opening file: %s.\n", e1 );
			System.exit(1);
		}
	*/	
		
//		t.exportCoverageTree();
//		t.exportCoverageTreeWithProbability();
	}

}
