package InterlacedOracle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;

public class Submission {
	
	private InfixArith program;
	private String[] runningResult;
	private boolean[] isCorrect;
	private ArrayList<String> causeReqSet;
	private double ratio;
	private Formatter outputCause, outputFailedFeature, outputIndividualReqPattern;
	private String outputCauseFile, outputFailedFeatureFile, outputIndividualReqPatternFile;
	
	public Submission(InfixArith p) {
		setProgram(p);
	}
	
	private void setProgram(InfixArith p) {
		program = p;
	}
	
	public Submission(){
		
	}
	
	public InfixArith getProgram() {
		return program;
	}
	
	// Added by UNO
	public String getCauseFile() {
		return outputCauseFile;
	}
	
	// Added by UNO
	public Formatter setOutputCauseFile(String s) {
		outputCauseFile = s;
		try {
			outputCause = new Formatter(getCauseFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getCauseFile() + "\n" + e);
			System.exit(0);
		}	
		return outputCause;	
	}
	
	// Added by UNO
	public void closeOutputCauseFile() {
		if (outputCause != null)
			outputCause.close();
	}	
	
	// Added by UNO
	public String getOutputIndividualReqPatternFile() {
		return outputIndividualReqPatternFile;
	}
	
	// Added by UNO
	public Formatter setOutputIndividualReqPatternFile(String s) {
		outputIndividualReqPatternFile = s;
		try {
			outputIndividualReqPattern = new Formatter(getOutputIndividualReqPatternFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getOutputIndividualReqPatternFile() + "\n" + e);
			System.exit(0);
		}	
		return outputIndividualReqPattern;	
	}
	
	// Added by UNO
	public void closeOutputIndividualReqPatternFile() {
		if (outputIndividualReqPattern != null)
			outputIndividualReqPattern.close();
	}	

	// Added by UNO
	public String getOutputFailedFeatureFile() {
		return outputFailedFeatureFile;
	}
	
	// Added by UNO
	public Formatter setOutputFailedFeatureFile(String s) {
		outputFailedFeatureFile = s;
		try {
			outputFailedFeature = new Formatter(getOutputFailedFeatureFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getOutputFailedFeatureFile() + "\n" + e);
			System.exit(0);
		}	
		return outputFailedFeature;	
	}
	
	// Added by UNO
	public void closeOutputFailedFeatureFile() {
		if (outputFailedFeature != null)
			outputFailedFeature.close();
	}		
	
	
	//Added by UNO
	public void setRunningResultSingleCase(AllTestCases allTestCases) {

		runningResult = new String[1];
		isCorrect = new boolean[1];
		
		int answer;
		String answerString;
		String expr = allTestCases.getSingleTestCase().getTestCase();
		
		String correctAnswer = allTestCases.getSingleTestCase().getAnswer();
		//System.out.println("setRunningResult->correctAnswer: "+correctAnswer);
		if (correctAnswer.startsWith("Result:")) {
			correctAnswer = correctAnswer.substring("Result: ".length());
		}
		
		try {
			answer = getProgram().main( expr );
			//System.out.println("setRunningResult->answer: "+answer);
			answerString = String.valueOf(answer);
		}
		catch (Exception e) {
			answerString = "exception";
		}
		
		runningResult[0] = answerString;
		if ( runningResult[0].equals( correctAnswer ) ) {
			isCorrect[0] = true;
			//System.out.println("setRunningResult->correctCount: "+correctCount);
		}
		else {
			isCorrect[0] = false;
		}
	}

	//Added by UNO
	public void setCauseReqSetSingleTestCase(AllTestCases allTestCases, String testName, int testCaseNum) {
		
		ArrayList<String> reqs = new ArrayList<String>();

		//int size = allTestCases.getSize();
		ReqCauseMap.resetCauseMap();

		if (isCorrect[0] == false)
		{
			String[] rs = allTestCases.getSingleTestCase().getRequirement();
			//System.out.println("For Test case index: "+i);
			setOutputCauseFile("OutputCauseFileforTestCase"+testName+"-"+testCaseNum+".txt");
			setOutputFailedFeatureFile("OutputCauseFeatureforTestCase"+testName+"-"+testCaseNum+".txt");
			outputCause.format("\n Requirement String:");
			for (String r : rs) {
			outputCause.format(" "+r);
			}
			outputCause.format("\n");
			rs = findCauses(rs, testCaseNum);
		
			if (rs == null) 
			{
				System.out.printf("warning: unable to find any faults for %s\n", allTestCases.getSingleTestCase().getTestCase());
				System.out.println("test requirements:");
				String[] rs1 = allTestCases.getSingleTestCase().getRequirement();
				for (String t : rs1) {
					System.out.printf("%s\n", t);
				}
				
			}
			else 
			{
				for (int j = 0; j < rs.length; j++) {
				
					int k;
					for (k = 0; k < reqs.size(); k++ ) {
						if ( reqs.get(k).endsWith( rs[j] ) ) {
							break;
						}
						else if ( rs[j].endsWith(reqs.get(k)) ) {
							reqs.set(k, rs[j]);
						}
					}
					
					if ( k == reqs.size() ) {
						reqs.add(rs[j]);
					}
				}
			}
		}
				
		causeReqSet = postProcessing(reqs);
		
		//outputFailedFeature.format("\n Converted Expressions: ");
		Iterator<String> i = causeReqSet.iterator();
		while(i.hasNext())
		{
			String expTemp = i.next();
			outputCause.format("\n r: %s",expTemp);
			outputFailedFeature.format("\n r: %s", expTemp);
		}
		closeOutputCauseFile();
		closeOutputFailedFeatureFile();
	}	
	
	public void setRunningResult(AllTestCases allTestCases) {
		
		int size = allTestCases.getSize();
		//System.out.println("setRunningResult->Size: "+size);
		int correctCount = 0;
		
		runningResult = new String[size];
		isCorrect = new boolean[size];
		
		for (int i = 0; i < size; i++) {

			int answer;
			String answerString;
			String expr = allTestCases.getTestCaseAt(i).getTestCase();
			//System.out.println("setRunningResult->expr: "+expr);
			
/*			if (expr.equals("194/(921+763-(358)+66+88*517)*312*603")) {
				System.out.printf("yes, %d\n", i);
			}
*/			
			String correctAnswer = allTestCases.getTestCaseAt(i).getAnswer();
			//System.out.println("setRunningResult->correctAnswer: "+correctAnswer);
			if (correctAnswer.startsWith("Result:")) {
				correctAnswer = correctAnswer.substring("Result: ".length());
			}
			
			try {
				answer = getProgram().main( expr );
				//System.out.println("setRunningResult->answer: "+answer);
				answerString = String.valueOf(answer);
			}
			catch (Exception e) {
				answerString = "exception";
			}
			
			runningResult[i] = answerString;
			if ( runningResult[i].equals( correctAnswer ) ) {
				isCorrect[i] = true;
				correctCount++;
				//System.out.println("setRunningResult->correctCount: "+correctCount);
			}
			else {
				isCorrect[i] = false;
			}
		}	
		setRatio(correctCount * 1.0 / size);
	}
	
	
	private void setRatio(double r) {
		ratio = r;
	}
	
	public double getRatio() {
		return ratio;
	}
	
	// Added by UNO
	public boolean isSuccess()
	{
		return isCorrect[0];
	}

	public ArrayList<String> getCauseReqSet() {
		return causeReqSet;
	}
	
	
	public void setCauseReqSet(AllTestCases allTestCases) {
		
		ArrayList<String> reqs = new ArrayList<String>();
		
		
		int size = allTestCases.getSize();
		ReqCauseMap.resetCauseMap();
		for (int i = 0; i < size; i++ ) {
			
			if (i == 858) {
				System.out.println();
			}

			if (isCorrect[i] == true) 
				continue;
			
			
			String[] rs = allTestCases.getTestCaseAt(i).getRequirement();
			//System.out.println("For Test case index: "+i);
			//for (String t1 : rs) {
				//System.out.printf("findCause->%s\n", t1);
			//}
			rs = findCauses(rs, i);
			
		
			if (rs == null) {
				System.out.printf("warning: unable to find any faults for %s\n", allTestCases.getTestCaseAt(i).getTestCase());
				System.out.println("test requirements:");
				String[] rs1 = allTestCases.getTestCaseAt(i).getRequirement();
				for (String t : rs1) {
					System.out.printf("%s\n", t);
				}
				continue;
			}
			

			
			for (int j = 0; j < rs.length; j++) {
			
				int k;
				for (k = 0; k < reqs.size(); k++ ) {
					if ( reqs.get(k).endsWith( rs[j] ) ) {
						break;
					}
					else if ( rs[j].endsWith(reqs.get(k)) ) {
						reqs.set(k, rs[j]);
					}
				}
				
				if ( k == reqs.size() ) {
					reqs.add(rs[j]);
				}
			}	
		}
		
				
		causeReqSet = postProcessing(reqs);
	}	
	
	
	
	private ArrayList<String> postProcessing(ArrayList<String> InputReqs) {

		ArrayList<String> outputReqs = new ArrayList<String>();
		
		while (!InputReqs.isEmpty()) {
			
			Iterator<String> i = InputReqs.iterator();

			String s = i.next();
			i.remove();
			
			boolean discard = false;
			
			for (String t : outputReqs) {
				if (pOrder(t, s)) { // partial order
					discard = true;
					break;
				}
			}
		
			if (!discard) {
				while (i.hasNext()) {
					String s1 = i.next();
					if (pOrder(s1, s)) {
						discard = true;
						break;
					}
				}
				
				if (!discard) {
					outputReqs.add(s);
				}
			}
		}
		
		return outputReqs;
	}
	
	
	
	// return null if no faults is able to be found
	private String[] findCauses(String[] reqs, int testCaseNum) {
		
		
		ArrayList<String> causes = new ArrayList<String>();
		InfixArith correct = new CorrectInfixArith();
		boolean foundAnyFaults = false;
		String[] temp = new String[0];
		
		if (reqs.length == 1 && reqs[0].equals("[N]")) {
			causes.add("");
			return causes.toArray(temp);
		}
		
		for (String r : reqs) {
			outputCause.format("\n String r: %s",r);
			boolean foundLowLevelFaults = false;
		    String ops = ReqCauseMap.req2ops(r);
		    outputCause.format("\n String r ops: %s",ops);
			for (int len = 1; len <= ops.length(); len++) {
				String subReq[] = ReqCauseMap.opsAtLen(ops, len);
				
				for (String sr : subReq) {
					outputCause.format("\n String r ops substring sr: %s",sr);					
					Boolean B = ReqCauseMap.doesCauseExist(sr);
					String e;
					int answer, correctAnswer;
					String answerString, correctAnswerString;
					
					if (B == null) { // not exist or no fault found yet
						e = ReqCauseMap.ops2expr(sr);
						outputCause.format("\n String r ops expression e: %s",e);	

						try {
							answer = getProgram().main( e );
							answerString = String.valueOf(answer);
							outputCause.format("\n String r ops answer: %s",answerString);	
						}
						catch (Exception ex) {
							answerString = "exception";
						}

						
						try {
							correctAnswer = correct.main( e );
							correctAnswerString = String.valueOf(correctAnswer);
							outputCause.format("\n String r ops correct answer: %s",correctAnswerString);	
						}
						catch (Exception ex) {
							correctAnswerString = "exception";
						}
						
						if (answerString.equals(correctAnswerString)) 
						{
							outputCause.format("\n Answers Match");
							continue;
//							ReqCauseMap.addCausePair(sr, true);
						}
						else {
							outputCause.format("\n Answers Do Not Match");
							outputFailedFeature.format("\nReduced Requirement in error: %s", r);
							outputFailedFeature.format("\nPattern in error: %s", sr);
							ReqCauseMap.addCausePair(sr, false);
							foundAnyFaults = true;
							foundLowLevelFaults = true;
							causes.add(sr);
						}
					}
					else if (B.booleanValue() == false) {
						outputCause.format("\n Answers Do Not Match: Outside Loop");
						outputFailedFeature.format("\nReduced Requirement in error: %s", r);
						outputFailedFeature.format("\nPattern in error outside loop: %s", sr);						
						foundAnyFaults = true;
						foundLowLevelFaults = true;
					}
				}
				
				if (foundLowLevelFaults) 
					break;
			}
		}
		
		
		
		if (!foundAnyFaults) 
			return null;
		else 	
			return causes.toArray(temp);	
	}
	
	// Added by UNO
	public ArrayList<String> findCausesPerRequirement(String reqs, int testCaseNum, int reqNum) {
		
		setOutputIndividualReqPatternFile("OutputIndividualReqPatternFile"+testCaseNum+"_"+reqNum+".txt");
		ReqCauseMap.resetCauseMap();
		ArrayList<String> causes = new ArrayList<String>();
		ArrayList<String> finalCauses = new ArrayList<String>();
		InfixArith correct = new CorrectInfixArith();
		boolean foundAnyFaults = false;
		String[] temp = new String[0];
		
		if(reqs == null)
		{
			return null;
		}
		
		if (reqs.equals("[N]")) {
			return null;
		}
		
//		for (String r : reqs) 
//		{
		outputIndividualReqPattern.format("\n String r: %s",reqs);
			boolean foundLowLevelFaults = false;
		    String ops = ReqCauseMap.req2ops(reqs);
		    outputIndividualReqPattern.format("\n String r ops: %s",ops);
			for (int len = 1; len <= ops.length(); len++) {
				String subReq[] = ReqCauseMap.opsAtLen(ops, len);
				
				for (String sr : subReq) {
					outputIndividualReqPattern.format("\n String r ops substring sr: %s",sr);					
					Boolean B = ReqCauseMap.doesCauseExist(sr);
					String e;
					int answer, correctAnswer;
					String answerString, correctAnswerString;
					
					if (B == null) { // not exist or no fault found yet
						e = ReqCauseMap.ops2expr(sr);
						outputIndividualReqPattern.format("\n String r ops expression e: %s",e);	

						try {
							answer = getProgram().main( e );
							answerString = String.valueOf(answer);
							outputIndividualReqPattern.format("\n String r ops answer: %s",answerString);	
						}
						catch (Exception ex) {
							answerString = "exception";
						}

						
						try {
							correctAnswer = correct.main( e );
							correctAnswerString = String.valueOf(correctAnswer);
							outputIndividualReqPattern.format("\n String r ops correct answer: %s",correctAnswerString);	
						}
						catch (Exception ex) {
							correctAnswerString = "exception";
						}
						
						if (answerString.equals(correctAnswerString)) 
						{
							outputIndividualReqPattern.format("\n Answers Match");
							continue;
//							ReqCauseMap.addCausePair(sr, true);
						}
						else {
							outputIndividualReqPattern.format("\n Answers Do Not Match");
							outputIndividualReqPattern.format("\nReduced Requirement in error: %s", reqs);
							outputIndividualReqPattern.format("\nPattern in error: %s", sr);
							ReqCauseMap.addCausePair(sr, false);
							foundAnyFaults = true;
							foundLowLevelFaults = true;
							causes.add(sr);
						}
					}
					else if (B.booleanValue() == false) {
						outputIndividualReqPattern.format("\n Answers Do Not Match: Outside Loop");
						outputIndividualReqPattern.format("\nReduced Requirement in error: %s", reqs);
						outputIndividualReqPattern.format("\nPattern in error outside loop: %s", sr);						
						foundAnyFaults = true;
						foundLowLevelFaults = true;
					}
				}
				
				if (foundLowLevelFaults) 
					break;
			}
		
//		}
		
		if (!foundAnyFaults) 
		{
			outputIndividualReqPattern.format("\n Causes to Array: No Causes Found");
			closeOutputIndividualReqPatternFile();
			return null;
		}
			
		else
		{
			ArrayList<String> reqsarray = new ArrayList<String>();
			String rs[] = causes.toArray(temp);
			for (int j = 0; j < rs.length; j++) 
			{
			
				int k;
				for (k = 0; k < reqsarray.size(); k++ ) {
					if ( reqsarray.get(k).endsWith( rs[j] ) ) {
						break;
					}
					else if ( rs[j].endsWith(reqsarray.get(k)) ) {
						reqsarray.set(k, rs[j]);
					}
				}
				
				if ( k == reqsarray.size() ) {
					reqsarray.add(rs[j]);
				}
			}	

		
				
			finalCauses = postProcessing(reqsarray);			
			outputIndividualReqPattern.format("\n Causes to Array: %s", finalCauses.toString());
			closeOutputIndividualReqPatternFile();
			return finalCauses;	
		}
		
	}	
	
	public int findIfErrorRequirement(String reqs, int testCaseNum, int reqNum) {
		
		//setOutputIndividualReqPatternFile("OutputIndividualReqPatternFile"+testCaseNum+"_"+reqNum+".txt");
		ReqCauseMap.resetCauseMap();
		ArrayList<String> causes = new ArrayList<String>();
		ArrayList<String> finalCauses = new ArrayList<String>();
		InfixArith correct = new CorrectInfixArith();
		boolean foundAnyFaults = false;
		String[] temp = new String[0];
		
		if(reqs == null)
		{
			return 0;
		}
		
		if (reqs.equals("[N]")) {
			return 0;
		}
		
//		for (String r : reqs) 
//		{
		//outputIndividualReqPattern.format("\n String r: %s",reqs);
			boolean foundLowLevelFaults = false;
		    String ops = ReqCauseMap.req2ops(reqs);
		    //outputIndividualReqPattern.format("\n String r ops: %s",ops);
			for (int len = 1; len <= ops.length(); len++) {
				String subReq[] = ReqCauseMap.opsAtLen(ops, len);
				
				for (String sr : subReq) {
					//outputIndividualReqPattern.format("\n String r ops substring sr: %s",sr);					
					Boolean B = ReqCauseMap.doesCauseExist(sr);
					String e;
					int answer, correctAnswer;
					String answerString, correctAnswerString;
					
					if (B == null) { // not exist or no fault found yet
						e = ReqCauseMap.ops2expr(sr);
						//outputIndividualReqPattern.format("\n String r ops expression e: %s",e);	

						try {
							answer = getProgram().main( e );
							answerString = String.valueOf(answer);
							//outputIndividualReqPattern.format("\n String r ops answer: %s",answerString);	
						}
						catch (Exception ex) {
							answerString = "exception";
						}

						
						try {
							correctAnswer = correct.main( e );
							correctAnswerString = String.valueOf(correctAnswer);
							//outputIndividualReqPattern.format("\n String r ops correct answer: %s",correctAnswerString);	
						}
						catch (Exception ex) {
							correctAnswerString = "exception";
						}
						
						if (answerString.equals(correctAnswerString)) 
						{
							//outputIndividualReqPattern.format("\n Answers Match");
							continue;
//							ReqCauseMap.addCausePair(sr, true);
						}
						else {
							//outputIndividualReqPattern.format("\n Answers Do Not Match");
							//outputIndividualReqPattern.format("\nReduced Requirement in error: %s", reqs);
							//outputIndividualReqPattern.format("\nPattern in error: %s", sr);
							ReqCauseMap.addCausePair(sr, false);
							foundAnyFaults = true;
							foundLowLevelFaults = true;
							causes.add(sr);
							return 1;
						}
					}
					else if (B.booleanValue() == false) {
						//outputIndividualReqPattern.format("\n Answers Do Not Match: Outside Loop");
						//outputIndividualReqPattern.format("\nReduced Requirement in error: %s", reqs);
						//outputIndividualReqPattern.format("\nPattern in error outside loop: %s", sr);						
						foundAnyFaults = true;
						foundLowLevelFaults = true;
						return 1;
					}
				}
				
				if (foundLowLevelFaults) 
					break;
			}
			return 0;
//		}
	}		
	
	private boolean pOrder(String s1, String s2) {
		
		// partial order on parenthesis terms only
		String[] partialOrder = {"*H", "+F", "-G", "/I", "FA", "GB", "HC", "ID"};
		String s = s1+s2;
		if ( Arrays.binarySearch(partialOrder, s) >= 0 ) 
			return true;

		// partial order on expressions which may contain parenthesis
		int i = 0;
		int j = 0;
		boolean isPOrder = true;
		String[] partialOrderExp = {"EA", "EB", "EC", "ED", "JA", "JB", "JC", "JD", "JE", "JF", "JG", "JH", "JI"};
		
		while (i < s1.length() && j < s2.length() && isPOrder) {
			
			String c1 = String.valueOf( s1.charAt(i) );
			String c2 = String.valueOf( s2.charAt(j) );
			
			if (c1.equals(c2)) {
				i++;
				j++;
			} 
			else if ( isOperator(c1) && isOperator(c2) ) {
				return false;
			}
			else if ( isOperator(c1) ) {
				j++;
			}
			else if ( isOperator(c2) ) {
				return false;
			}
			else {
				String t = c1 + c2;

				if ( Arrays.binarySearch(partialOrderExp, t) >= 0 ) { 
					i++;
					j++;
				}
				else {
					return false; 
				}
			}
		}
		
		if (i < s1.length())
			return false;
		else
			return true;
	}
	
	
	private boolean isOperator(String c) {
		return c.matches("[+-/*]");
	}
	
	
}


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