package InterlacedOracle;

import java.util.*;
import java.io.*;

public class InfixArithTest {

	private final static int size = 15;
	
	public static void main(String[] args) {

		Scanner input;
		Formatter output;
		String inputFile = "testCasesSemanticResult.txt";
		
		String outputFile = "ExpTestingResultSem.txt";
		//String outputFile = "";
		InfixArith[] assignment = new InfixArith[size];
//		InfixArith correct = new CorrectInfixArith();
		
		int totalTests = 0;
		int[] correctTests = new int[size];
		
		for (int i = 0; i < size; i++) {
			correctTests[i] = 0;
		}
		
		try {
			input = new Scanner( new File( inputFile ) );
			output = new Formatter( outputFile );

			initialize( assignment );

			while ( input.hasNextLine() ) {
				String expr = input.nextLine().trim();
				if ( expr.equals("") )
					continue;
					
				//  remove 8 chars "result: "
				String answerString = input.nextLine().substring(8).trim();  
				
				// skip a blank line
				input.nextLine();
				
				totalTests++;

				/*
				int answer = 0;
				String answerString = null;
				
				try {
					answer = correct.main(expr);
					answerString = String.valueOf(answer);
				}
				catch (Exception e) {
					answerString = "exception";
				}
				*/
				
				
				System.out.printf("%-10s ", answerString);
				output.format("%-10s ", answerString);
				
				for (int i = 0; i < size; i++) {

					int result = 0;
					String resultString = null;
					
					try {
						result = assignment[i].main(expr);
						resultString = String.valueOf(result);
					}
					catch (Exception e) {
						resultString = "exception";
					}
					
					System.out.printf("%-10s ", resultString);
					output.format("%-10s ", resultString);
					
					if (resultString.equals(answerString)) {
						correctTests[i]++;
					}

				}
				System.out.println();
				output.format("\n");
			}
			
			System.out.printf("%10s ", " ");
			output.format("%10s ", " ");
			for (int count : correctTests) {
				System.out.printf("%-10s ", String.format("%6.2f%%", 100.0 * count / totalTests ));
				output.format("%-10s ", String.format("%6.2f%%", 100.0 * count / totalTests ));
			}

			input.close();
			output.close();
			
		}
		catch ( FileNotFoundException e1 ) {
			System.err.printf( "Exception: File %s does not exits.\n", inputFile );
		}
		catch (Exception e ) {
			System.err.println(e);
		}		
	}

	
	
	private static void initialize( InfixArith[] assignment ) {
		
		assignment[0] = new AgillespieInfixArith();
		assignment[1] = new AshearerInfixArith();
		assignment[2] = new BrothInfixArith();
		assignment[3] = new CfrankInfixArith();
		assignment[4] = new KcaumeranInfixArith();
		assignment[5] = new KfloresInfixArith();
		assignment[6] = new PkwetebokashangInfixArith();
		assignment[7] = new AhazratiInfixArith();
		assignment[8] = new AhclarkeInfixArith();
		assignment[9] = new BalbuloushiInfixArith();
		assignment[10] = new CebiggsInfixArith();
		assignment[11] = new GnguyenInfixArith();
		assignment[12] = new MeisnerInfixArith();
		assignment[13] = new RhuInfixArith();
		assignment[14] = new WfloresInfixArith();
		}
	
}
