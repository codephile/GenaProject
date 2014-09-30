package InterlacedOracle;


// using leftmost derivation

import java.io.FileNotFoundException;
import java.util.*;

public class InfixArithFaultDetector {

	private final static int size = 15;
	private static AllTestCases atc;
	private static Submission[] programs;
	
	// Added by UNO
	private static String[] testName = {"AgillespieInfixArith", "AshearerInfixArith", "BrothInfixArith",
			"CfrankInfixArith", "KcaumeranInfixArith", "KfloresInfixArith", "PkwetebokashangInfixArith",
			"AhazratiInfixArith", "AhclarkeInfixArith", "BalbuloushiInfixArith", "CebiggsInfixArith",
			"GnguyenInfixArith", "MeisnerInfixArith", "RhuInfixArith", "WfloresInfixArith"
	};
	 
	
	public InfixArithFaultDetector() {

	}
	
	public static void main(String[] args) {
		
		
		atc = new AllTestCases();
		
		atc.loadTestCasesFromFile("casesWithSem.txt");
		
		programs = new Submission[size];
		initialize( programs );
		//programs[0].findCausesPerRequirement("[N]*((Expr+Factor))/[N]*[N]", 0);
		
		for (int i = 0; i < size; i++) {
			System.out.printf("testing submision %d ....\n", i);
			programs[i].setRunningResult(atc);
			
			programs[i].setCauseReqSet(atc);
			
		}

		printResults();
		printResults("FaultResult_1000Exp_ASE2013.txt");
	}
	
	// Added by UNO
	public void feedSUTSingleTestCase(String GenaReqLine, String GenaTestCase, String GenaTestExpectedResult, String logFileName, int testCaseNum) {
		
		/*atc = new AllTestCases();
		
		atc.assignSingleTestCase(GenaReqLine, GenaTestCase, GenaTestExpectedResult);*/
		
		programs = new Submission[size];
		initialize( programs );
		//programs[0].findCausesPerRequirement("[N]*((Expr+Factor))/[N]*[N]", 0);
		
		/*for (int i = 0; i < size; i++) {
			//System.out.printf("testing submision %d ....\n", i);
			programs[i].setRunningResultSingleCase(atc);
			programs[i].setCauseReqSetSingleTestCase(atc, testName[i], testCaseNum);
		}

		printResultsSingleCase();
		printResultsSingleCase(logFileName);*/
	}	
	
	public ArrayList<String> detectErroringPatterns(String reqString, int SUTNumber, int rqNum) {
		
		/*atc = new AllTestCases();
		
		atc.assignSingleTestCase(GenaReqLine, GenaTestCase, GenaTestExpectedResult);*/
		ArrayList<String> errorCausesPatterns = new ArrayList<String>();
		
		programs = new Submission[size];
		initialize( programs );
		errorCausesPatterns = programs[0].findCausesPerRequirement(reqString, SUTNumber, rqNum);
		
		/*for (int i = 0; i < size; i++) {
			//System.out.printf("testing submision %d ....\n", i);
			programs[i].setRunningResultSingleCase(atc);
			programs[i].setCauseReqSetSingleTestCase(atc, testName[i], testCaseNum);
		}

		printResultsSingleCase();
		printResultsSingleCase(logFileName);*/
		return errorCausesPatterns;
	}	

	public int detectIsErroringPatterns(String reqString, int SUTNumber, int rqNum, int SUTActual) {
		
		/*atc = new AllTestCases();
		
		atc.assignSingleTestCase(GenaReqLine, GenaTestCase, GenaTestExpectedResult);*/
		
		
		programs = new Submission[size];
		initialize( programs );
		int returnVal = programs[SUTActual].findIfErrorRequirement(reqString, SUTNumber, rqNum);
		
		/*for (int i = 0; i < size; i++) {
			//System.out.printf("testing submision %d ....\n", i);
			programs[i].setRunningResultSingleCase(atc);
			programs[i].setCauseReqSetSingleTestCase(atc, testName[i], testCaseNum);
		}

		printResultsSingleCase();
		printResultsSingleCase(logFileName);*/
		return returnVal;
	}	

	// Added by UNO
	private static void printResultsSingleCase(String FN) {

		try {
			Formatter output = new Formatter(FN);
			
			for (int i = 0; i < size; i++) {
				String successStatus;
				if(programs[i].isSuccess())
					successStatus = "Yes";
				else
					successStatus = "No";
				output.format("\n---------Begin Test Case Analysis---------\n");
				output.format("%s\n", atc.getSingleTestCase());
				output.format("Test Number: %d Test Name: %s Success: %s\n", i, testName[i], successStatus);
				output.format("%s\n\n", programs[i].getCauseReqSet());
				output.format("---------End Test Case Analysis---------\n");
			}
			output.format("\n");
			/*for (int j = 0; j < atc.getSize(); j++) {
				output.format("[%d] %s\n", j, atc.getTestCaseAt(j));
			}*/
			output.close();
		}
		catch ( FileNotFoundException e1 ) {
			System.err.printf( "Exception: File %s does not exits.\n", FN );
		}
	}

	// Added by UNO
	private static void printResultsSingleCase() {
		
		for (int i = 0; i < size; i++) {
			String successStatus;
			if(programs[i].isSuccess())
				successStatus = "Yes";
			else
				successStatus = "No";
			System.out.printf("Test Case: %s\n", atc.getSingleTestCase());
			System.out.printf("Test Number: %d Test Name: %s Success: %s\n", i, testName[i], successStatus);
			System.out.printf("%s\n\n", programs[i].getCauseReqSet());
			
		}

/*		System.out.println();
		for (int j = 0; j < atc.getSize(); j++) {
			System.out.printf("[%d] %s\n", j, atc.getTestCaseAt(j));
		}
*/		
	}	
	
	private static void printResults(String FN) {

		try {
			Formatter output = new Formatter(FN);
			
			for (int i = 0; i < size; i++) {
				output.format("[%d]: %5.2f%%\n", i, programs[i].getRatio() * 100);
				output.format("%s\n\n", programs[i].getCauseReqSet());
			}

			output.format("\n");
			for (int j = 0; j < atc.getSize(); j++) {
				output.format("[%d] %s\n", j, atc.getTestCaseAt(j));
			}
			output.close();
		}
		catch ( FileNotFoundException e1 ) {
			System.err.printf( "Exception: File %s does not exits.\n", FN );
		}
	}

	
	
	private static void printResults() {
		
		for (int i = 0; i < size; i++) {
			System.out.printf("[%d]: %5.2f%%\n", i, programs[i].getRatio() * 100);
			System.out.printf("%s\n\n", programs[i].getCauseReqSet());
			
		}

/*		System.out.println();
		for (int j = 0; j < atc.getSize(); j++) {
			System.out.printf("[%d] %s\n", j, atc.getTestCaseAt(j));
		}
*/		
	}
	

	
	
	private static void initialize( Submission[] program ) {
		
		program[0] = new Submission( new AgillespieInfixArith() );
		program[1] = new Submission( new AshearerInfixArith() );
		program[2] = new Submission( new BrothInfixArith() );
		program[3] = new Submission( new CfrankInfixArith() );
		program[4] = new Submission( new KcaumeranInfixArith() );
		program[5] = new Submission( new KfloresInfixArith() );
		program[6] = new Submission( new PkwetebokashangInfixArith() );

		program[7] = new Submission( new AhazratiInfixArith() );
		program[8] = new Submission( new AhclarkeInfixArith() );
		program[9] = new Submission( new BalbuloushiInfixArith() );
		program[10] = new Submission( new CebiggsInfixArith() );
		program[11] = new Submission( new GnguyenInfixArith() );
		program[12] = new Submission( new MeisnerInfixArith() );
		program[13] = new Submission( new RhuInfixArith() );
		program[14] = new Submission( new WfloresInfixArith() );
	}
	
}
