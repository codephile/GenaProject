package InterlacedOracle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class AllTestCases {

	private final static int Maximum = 10001;
	private int size;
	private TestCase[] testCases;
	// Added by UNO
	private TestCase singleTestCase;
	
	public AllTestCases() {
		testCases = new TestCase[Maximum];
		size = 0;
	}
	
	private void incSize() {
		size++;
	}
	
	
	public int getSize() {
		return size;
	}
	
	
	private void setNextTestCase(TestCase tc) {
		testCases[size] = tc;
		incSize();
	}
	
	// Added by UNO
	private void setSingleTestCase(TestCase tc) {
		singleTestCase = tc;
	}
	
	// Added by UNO
	public TestCase getSingleTestCase() {
		return singleTestCase;
	}
	
	public TestCase getTestCaseAt(int i) {
		return testCases[i];
	}
	
	
	public void loadTestCasesFromFile(String fileName) {
		Scanner input = openFile(fileName);
		loadData(input);
		closeFile(input);
	}
	
	// Added by UNO
	public void assignSingleTestCase(String reqLine, String testCase, String testExpectedResult)
	{
		String rLine = reqLine.trim();
		String eLine = testCase;
		String answerString = testExpectedResult.substring(8).trim();
		
		String[] reqArray = reducedReq(rLine);
		
		TestCase tc = new TestCase(eLine, reqArray, answerString);
		setSingleTestCase(tc);	
	}
	
	
	
	
	private Scanner openFile(String FN) {
		
		Scanner input = null; 
		
		try {
			input = new Scanner( new File( FN ) );
		}
		catch ( FileNotFoundException e1 ) {
			System.err.printf( "Error Opening file: %s.\n", e1 );
			System.exit(1);
		}
		
		return input;
	}
	
	
	private void loadData(Scanner input) {
		
		int count = 0;
		
		while (input.hasNextLine()) {
			input.nextLine();   // skip a line
			String rLine = input.nextLine();
			System.out.println("rLine: "+rLine);
//			String pLine = input.nextLine();
			String eLine = input.nextLine();
			System.out.println("eLine: "+eLine);
			//  remove 8 chars "result: "
			String answerString = input.nextLine().substring(8).trim();  
			System.out.println("answerString: "+answerString);
			input.nextLine();
			
			rLine = rLine.trim();
//			pLine = pLine.replaceFirst("\\[", "").replaceFirst("\\]","").trim();
			eLine = eLine.replaceFirst("\\[\\[", "").replaceFirst("\\]\\]","").trim();
			System.out.println("eLine Replaced: "+eLine);
			
//			if (eLine.equals("194/(921+763-(358)+66+88*517)*312*603"))
//				System.out.printf("index = %d\n", size);
			

			/*
			InfixArith correct = new CorrectInfixArith();
			int answer; 
			String answerString;
			
			try {
				answer = correct.main(eLine);
				answerString = String.valueOf(answer);
			}
			catch (Exception e) {
				answerString = "exception";
			}

			*/
			
//			TestCase tc = new TestCase(eLine, pLine, reducedReq(rLine), answerString);
			String[] reqArray = reducedReq(rLine);
			System.out.println("reqArray: "+reqArray+" Length: "+reqArray.length);
			count += reqArray.length;
			
			TestCase tc = new TestCase(eLine, reqArray, answerString);
			setNextTestCase(tc);
		}
		
		System.out.printf("the total number of test requirements is %d.\n", count);
	}
	
	
	private void closeFile(Scanner input) {
		if (input != null) {
			input.close();
		}
	}
	
	
	public String[] reducedReq(String rs) {
		
		String[] ra = rs.split(" ");
		ArrayList<String> reqs = new ArrayList<String>();
		
		for (int i = 0; i < ra.length; i++) {
			
			int j;
			for (j = 0; j < reqs.size(); j++ ) {
				if ( reqs.get(j).endsWith( ra[i] ) ) {
					break;
				}
				else if ( ra[i].endsWith(reqs.get(j)) ) {
					reqs.set(j, ra[i]);
					break;
				}
			}
			
			if ( j == reqs.size() ) {
				reqs.add(ra[i]);
			}
		}
		
		String[] temp = {""};
		return reqs.toArray(temp);
	}
	
	
	public void print() {
		
		for (int i = 0; i < getSize(); i++) {
			System.out.printf("[%d]: %s\n", this.getTestCaseAt(i));
		}		
	}
}
