package InterlacedOracle;


public class TestCase {

	private String testCase;
//	private String parseTree;
	private String[] requirement;
	private String answer;

	public TestCase(String tc, String[] r) {
		setTestCase(tc);
//		setParseTree(pt);
		setRequirement(r);
	}

	public TestCase(String tc, String[] r, String a) {
		setTestCase(tc);
//		setParseTree(pt);
		setRequirement(r);
		setAnswer(a);
	}

/*	public TestCase(String tc, String pt, String[] r) {
		setTestCase(tc);
		setParseTree(pt);
		setRequirement(r);
	}

	public TestCase(String tc, String pt, String[] r, String a) {
		setTestCase(tc);
		setParseTree(pt);
		setRequirement(r);
		setAnswer(a);
	}
*/
	private void setTestCase(String tc) {
		testCase = tc;
	}
	
	public String getTestCase() {
		return testCase;
	}

/*	private void setParseTree(String pt) {
		parseTree = pt;
	}
	
	public String getParseTree() {
		return parseTree;
	}
*/
	private void setRequirement(String[] r) {
		requirement = r;
	}
	
	public String[] getRequirement() {
		return requirement;
	}

	public void setAnswer(String a) {
		answer = a;
	}
	
	public String getAnswer() {
		return answer;
	}

	@Override
	public String toString() {
		
		String reqPrint = "TestRequirement:"; 
				
		for (int i = 0; i < getRequirement().length; i++) {
			reqPrint = String.format("%s %s", reqPrint, getRequirement()[i]);
		}
				
		return String.format("TestCase: %s\n Correct Answer: %s\n%s\n",
				this.getTestCase(), this.getAnswer(), reqPrint);
	}
}
