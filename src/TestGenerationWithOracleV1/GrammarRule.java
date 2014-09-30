package TestGenerationWithOracleV1;

public class GrammarRule {

//		double probability;
		String[] tokens;
		
	/*	public void setProbability(double p) {
			probability = p;
		}
		
		public double getProbability() {
			return probability;
		}
	*/	
		public void setStringTokenizer(String[] t) {
			tokens = t;
		}
		
		public String[] getStringTokenizer() {
			return tokens;
		}
		
//		public void newGrammarRule(double p, StringTokenizer t) {
	  	public void newGrammarRule(String[] t) {
	 
//			setProbability(p);
			setStringTokenizer(t);
		}

		
	  	public void newGrammarRule(String body) {

	  		char[] temp = new char[256];
	  		int j = 0;
	  		
	  		// use ~~~ as a delimiter to separate all atoms in the rule body
	  		if (body.contains("..")) {
	  			body = body.replaceFirst("\\.\\.", "~~~");
	  		}
	  		else {
	  			int i = 0; 
	  			boolean begin = true;
	  			while (i < body.length()) {
	  				while (i < body.length() && body.charAt(i) == ' ') {
	  					i++;
	  				}
	  					
	  				if (i ==  body.length()) break;
	  				
	  				if (body.charAt(i) == '\'') {
	  					// in a pair of quotes (special terminal string)
	  					if (!begin) {
	  						temp[j++] = '~';
	  						temp[j++] = '~';
	  						temp[j++] = '~';
	  					}
	  					else
	  						begin = false;
	  					
						temp[j++] = body.charAt(i++);
	  					
	  					while (i < body.length() && body.charAt(i) != '\'') {
	  						if (body.charAt(i) == '\\') {
	  							temp[j++] = body.charAt(i++);
	  							if (body.charAt(i) == '\'')
	  								j--;
	  						}
	  						temp[j++] = body.charAt(i++);
	  					}
	  					
	  					if (i == body.length()) {
	  						System.out.println("Grammar syntax error: missing \'!");
	  						System.exit(0);
	  					}
	  					else {
	  						temp[j++] = body.charAt(i++);
	  					}
	  				}
	  				else {  // not a terminal in a pair of quotes
	  					
	  					if (begin == false) {
	  						// not beginning of string, should have a delimiter
	  						temp[j++] = '~';
	  						temp[j++] = '~';
	  						temp[j++] = '~';
	  					}
	  					else 
	  						begin = false;
	  					
	  					while (i < body.length() && body.charAt(i) != ' ') {
	  						temp[j++] = body.charAt(i++);
	  					}
	  				}
	  				
	  			}	
	  		} 
	  		
	  		String t = String.valueOf(temp, 0, j);

	  		setStringTokenizer(t.split("~~~"));

	  	}

		
		public String toString() {

			String f = "";
			int n = tokens.length;
			for (int i = 0; i < n; i++) {
				f = f + tokens[i] + " ";
			}
			return f;
	/*
			if (probability > 0.0)
				return String.format("%s%%%% %f ", f, probability);
			else
				return f;
	*/
		}
		
		public String stString() {
			return String.format("%s .. %s", tokens[0].trim(), tokens[1].trim());
		}
}
		
