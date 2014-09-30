package TestGenerationWithOracleV1;

// import java.lang.StringBuffer;
import java.io.File;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
//import java.util.Vector;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Set;
//import java.util.Scanner;
import java.util.Random;


public class SymbolicGrammar {
	
	private String mainVar;
	private Map< String, ArrayList<GrammarRule> > grammarMap;
	private Map< String, ArrayList<SemanticFun> > semanticsMap;
	private Map< String, ArrayList<Double> > probMap;
	private Map< String, ArrayList<Double> > hitsMap;
	private int ii = 0;
	private int testCaseNumber=100;
	private Formatter symGrammar;
	private String symGrammarFile;
	private int recursionNumber = 0;
	
	public SymbolicGrammar() {
		grammarMap = new HashMap< String, ArrayList<GrammarRule> >();
		semanticsMap = new HashMap< String, ArrayList<SemanticFun> >();
		probMap = new HashMap< String, ArrayList<Double> >();
		hitsMap = new HashMap< String, ArrayList<Double> >();	
	}
	
	
	public ArrayList<SemanticFun> getSemantics(String key) {
		return semanticsMap.get(key);
	}
	
	public ArrayList<GrammarRule> getGrammar(String key) {
		return grammarMap.get(key);
	}
	
	public ArrayList<Double> getProbs(String key) {
		return probMap.get(key);
	}

	public ArrayList<Double> getHits(String key) {
		return hitsMap.get(key);
	}
	
	public String getSymGrammarFile() {
		return symGrammarFile;
	}	
	
	private void setSymGrammarFile(String s) {
		symGrammarFile = s;
		try {
			symGrammar = new Formatter(getSymGrammarFile());
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + getSymGrammarFile() + "\n" + e);
			System.exit(0);
		}	
		
	}	
	
	public void closeSymGrammarFile() {
		if (symGrammar != null)
			symGrammar.close();
	}
	
	public void resetHitsMap() {
		
		Set<String> vars = getHitsMap().keySet();
		
		for (String key : vars) {
			ArrayList<Double> a = getHits(key);
			int s = a.size() - 1;
			while (s >= 0) {
				a.set(s, 1.0);
				s--;
			}
		}

	}
	
	
	public String getMainVar() {
		return mainVar;
	}
	
	public void setTestCaseNumber(int testCaseNum)
	{
		testCaseNumber = testCaseNum;
	}
	
	public void setMainVar(String v) {
		if ( isVar(v) )
			mainVar = v;
		else {
			System.err.println( v + " is not a variable!");
			System.exit(0);		
		}
	}

	public Map< String, ArrayList<GrammarRule> > getGrammarMap() {
		return grammarMap;
	}
	

	public Map< String, ArrayList<SemanticFun> > getSemanticsMap() {
		return semanticsMap;
	}
			
	
	public Map< String, ArrayList<Double> > getProbMap() {
		return probMap;
	}

	public Map< String, ArrayList<Double> > getHitsMap() {
		return hitsMap;
	}
	
	
	public void setHitsMap(String key, ArrayList<Double> array) {
		getHitsMap().put(key, array);
	}


	public boolean isVar(String v) {
//		System.out.println(v);		
		return v.matches("[A-Z][a-zA-Z\\d]*"); 
	}
	
	public boolean isSymbolicTerminal(String t) {	
		if ( t.charAt(0) == '[' && t.charAt(t.length()-1) == ']' ) {
			return isVar(t.substring(1, t.length()-1));
		}
		else
			return false;
	}
	
	public boolean isTerminal(String t) {
		if (isVar(t) || isSymbolicTerminal(t))
			return false;
		else
			return true;
	}
	
	
	public String anInstanceFromSymbolic(String token) {
		// token is a symbolic terminal
	
		String[] bounds = getGrammar(token).get(0).getStringTokenizer();

		int lower = Integer.valueOf(bounds[0].trim());
		int upper = Integer.valueOf(bounds[1].trim());
		
		Random r = new Random();
		
		int i = r.nextInt(upper - lower + 1) + lower;

		return String.valueOf(i);
	}

	
	public void createGrammarFromFile(String filename) {
		
		Scanner file;
		try {
			
			file = new Scanner(new File(filename));

			ArrayList<GrammarRule> grammar;
			ArrayList<SemanticFun> semFun;
			ArrayList<Double> prob;
			ArrayList<Double> hits;
		
			while(file.hasNext())  
			{	// if file is not empty yet
				String line=file.nextLine().trim();
			
				if (line.length() == 0 || line.charAt(0) == '%')
					continue;
				
				grammar = new ArrayList<GrammarRule>();
				semFun = new ArrayList<SemanticFun>();
				prob = new ArrayList<Double>();
				hits = new ArrayList<Double>();
			
				String defSign;
				// a valid grammar rule uses either "::=" or "->"
				if (line.contains("::=")) 
					defSign = "::=";
				else 
					defSign = "->";
			
				String[] headBody = line.split(defSign);
//				StringTokenizer headBody = new StringTokenizer(line, defSign); 
			
				if (headBody.length > 1) {
					String head = headBody[0].trim(); // get the variable name
					if (getMainVar() == null)  // the variable in the first line is the main variable 
						setMainVar(head);
				
					String tail = headBody[1];
					
					if (isSymbolicTerminal(head)) {
						// if the head is a symbolic Terminal
						// a symbolic terminal is in a form of [Vstr]
						// where Vstr is a valid variable string
						// the semantic fun for a symbolic terminal is null.
						GrammarRule r = new GrammarRule();
						SemanticFun s = null;
						r.newGrammarRule( tail.trim().split("\\.\\.") );
						grammar.add(r);
						semFun.add(s);
						updateGrammarMap(head, grammar, semFun);
					}
					else {
						// at previous version, we assume '|' is not part of a terminal
						// it has been extended at this point
						if ( isAnyVLterminal(tail) ) {
							tail = substituteVL(tail);
						}
						
						StringTokenizer ruleBody = new StringTokenizer(tail,"|");

						int numberOfRules = ruleBody.countTokens();
				
						for (int i = 0; i < numberOfRules; i++) {
							GrammarRule r = new GrammarRule();
							SemanticFun s;
							Double p;
							Double h;
						
							String rule = ruleBody.nextToken();
							rule = restoreVL(rule);
							
							if (rule.contains("%%")) {
								StringTokenizer temp = new StringTokenizer(rule,"%%");
								StringTokenizer completeRule = new StringTokenizer(temp.nextToken(), "@@");
								String t = completeRule.nextToken().trim();
								r.newGrammarRule(t);
								
								// if the rule contains a semantic part
								if (completeRule.countTokens() > 0) {
									t = completeRule.nextToken().trim();
									s = new SemanticFun(head, r, t);
								}
								else 
									s = null;

								p = Double.valueOf( temp.nextToken().trim() ).doubleValue();
								h = 1.0;
							}
							else {		
								StringTokenizer completeRule = new StringTokenizer(rule.trim(), "@@");
								String t = completeRule.nextToken().trim();
								r.newGrammarRule(t);
								
								// if the rule contains a semantic part
								if (completeRule.countTokens() > 0) {
									t = completeRule.nextToken().trim();
									s = new SemanticFun(head, r, t);
								}
								else 
									s = null;
								
								p = 0.0;
								h = 1.0;
							}
					
							grammar.add(r);
							semFun.add(s);
							prob.add(p);
							hits.add(h);
						}
						updateGrammarMap(head, grammar, semFun, prob, hits);
					}
				}
				else {
					// skip the line
					// invalid syntax
				}
			}
			file.close();
		}
		catch ( Exception e) {
			System.err.println( "Unable to open file: " + filename + "\n" + e);
			System.exit(0);
		}	
	}

	
	private String restoreVL(String s) {
		return s.replaceAll("\\\\VL", "\\|");
	}


	private String substituteVL(String s) {
		return s.replaceAll("\\|", "\\\\VL");
	}


	private boolean isAnyVLterminal(String s) {

		boolean inQuotes = false;
		
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\'')
				inQuotes = (inQuotes == false) ? true : false;
			
			if (s.charAt(i) == '|' && inQuotes == true)
				return true;
		}
		
		return false;
	}


	// adding grammar rules into GrammarMap
	private void updateGrammarMap(String var,  ArrayList<GrammarRule> ag,  ArrayList<SemanticFun> as) {
		
		ArrayList<GrammarRule> r = getGrammarMap().get(var);
		ArrayList<SemanticFun> s;
		
		
		if (r != null) {
			r.addAll(ag);
			s = getSemanticsMap().get(var);
			s.addAll(as);
		}
		else {
			r = ag;
			s = as;
		}
		
		getGrammarMap().put(var, r);
		getSemanticsMap().put(var, s);
	}
	
	
	private void updateGrammarMap(String var,  ArrayList<GrammarRule> ag,  ArrayList<SemanticFun> as, 
			ArrayList<Double> ap, ArrayList<Double> ah) {
		
		ArrayList<GrammarRule> r = getGrammarMap().get(var);
		ArrayList<SemanticFun> s;
		ArrayList<Double> p;
		ArrayList<Double> h;
		
		if (r != null) {
			r.addAll(ag);
			p = getProbMap().get(var);
			p.addAll(ap);
			h = getHitsMap().get(var);
			h.addAll(ah);
			s = getSemanticsMap().get(var);
			s.addAll(as);
		}
		else {
			r = ag;
			p = ap;
			h = ah;
			s = as;
		}
		
		getGrammarMap().put(var, r);
		getProbMap().put(var, p);
		getHitsMap().put(var, h);
		getSemanticsMap().put(var, s);
	}
	
	
	
	
	public void printGrammarMap() {
		
		Set<String> vars = getGrammarMap().keySet();
		
		for (String key : vars) {
			ArrayList<GrammarRule> rules = getGrammarMap().get(key);
			ArrayList<SemanticFun> funs = getSemanticsMap().get(key);
			
			for (int i = 0; i < rules.size(); i++) {
				GrammarRule r = rules.get(i);
				SemanticFun f = funs.get(i);
				System.out.printf("%s ::= ", key);
				if (isSymbolicTerminal(key)) 
					System.out.printf("%s ", r.stString());
				else
					System.out.printf("%s @@ %s", r, f);

				System.out.println();
			}
			
		}
		
	}
	
	// Added by UNO
	public ArrayList<String> reqProcessIndividual(String reqs) {
		try
		{
		String prevReq = null;
		String[] ra = reqs.split(" ");
		
		ArrayList<String> reqsPlus = new ArrayList<String>();
		ArrayList<String> returnRequirements = new ArrayList<String>();
		
		HashMap<String, String> reqMap = new HashMap<String, String>();
		
		setSymGrammarFile("SymbolicGrammarParseTestCaseIndividual"+testCaseNumber+".txt");
		
		returnRequirements.clear();
		
		int iii = 0;
		for(iii = ra.length - 1; iii >= 0; iii--)
		{
			ii = iii;
			reqsPlus.clear();
			if(iii < ra.length - 1)
			{
				if(reqMap.get(ra[iii]) == null)
				{
					while (ii < ra.length) 
					{
						recursionNumber = 0;
						reqs2Plus(ra, reqsPlus);
						ii++; 
					}
					String outputReq = arrayList2str(reqsPlus, " ");

						String[] prevReqParts = prevReq.split(" ");
						String[] currReqParts = outputReq.split(" ");

						int currReqCount = 0, expFound = 0, newReq = 0;
						while(currReqCount < currReqParts.length)
						{
							expFound = 0;
							for(String temp: prevReqParts)
							{
								if(temp.equals(currReqParts[currReqCount]))
								{
									expFound = 1;
									break;
								}
							}
							if(expFound == 0)
							{
								newReq = 1;
								reqMap.put(ra[iii], currReqParts[currReqCount]);
							}
							currReqCount++;
						}
						if(newReq == 0)
						{
							String[] uniqueCurrItems = null, uniquePrevItems = null;
							int uniqueCurrCount = 0, uniquePrevCount = 0;
							
							HashMap<String, Integer> currItemCount = new HashMap<String, Integer>();
							HashMap<String, Integer> prevItemCount = new HashMap<String, Integer>();
							
							uniqueCurrItems = new HashSet<String>(Arrays.asList(currReqParts)).toArray(new String[currReqParts.length]);
							uniquePrevItems = new HashSet<String>(Arrays.asList(prevReqParts)).toArray(new String[prevReqParts.length]);
							
							for(String tempUnique: uniqueCurrItems)
							{
								for(String tempOrig: currReqParts)
								{
									if(tempOrig.equals(tempUnique))
										uniqueCurrCount++;
								}
								currItemCount.put(tempUnique, uniqueCurrCount);
							}
							
							for(String tempUnique: uniquePrevItems)
							{
								for(String tempOrig: prevReqParts)
								{
									if(tempOrig.equals(tempUnique))
										uniquePrevCount++;
								}
								prevItemCount.put(tempUnique, uniquePrevCount);
							}							
							
							for(String tempUnique: uniquePrevItems)
							{
								if(currItemCount.get(tempUnique) > prevItemCount.get(tempUnique))
								{
									if(tempUnique == null)
										tempUnique = "[N]";
									reqMap.put(ra[iii], tempUnique);
								}
							}							
						}
						prevReq = outputReq;
					}
			}
			else
			{
				while (ii < ra.length) 
				{
					recursionNumber = 0;
					reqs2Plus(ra, reqsPlus);
					ii++; 
				}
					String outputReq = arrayList2str(reqsPlus, " ");
					if(outputReq == null)
						outputReq = "[N]";
					reqMap.put(ra[iii], outputReq);
					prevReq = outputReq;				
			}
		}
		for(String r: ra)
		{
			symGrammar.format("\nSub-Feature: %s \nCorresponding Requirement String: %s", r, reqMap.get(r));
			returnRequirements.add(reqMap.get(r));
		}
		closeSymGrammarFile();
		//return arrayList2str(reqsPlus, " ");
		return returnRequirements;
		}
		catch(Exception e)
		{
			return null;
		}
	}	

	// Added by UNO
	public HashMap<String, String> reqProcessIndividualHashMap(String reqs) {
		try
		{
		String prevReq = null;
		String[] ra = reqs.split(" ");
		
		ArrayList<String> reqsPlus = new ArrayList<String>();
		ArrayList<String> returnRequirements = new ArrayList<String>();
		
		HashMap<String, String> reqMap = new HashMap<String, String>();
		
		//setSymGrammarFile("SymbolicGrammarParseTestCaseIndividual"+testCaseNumber+".txt");
		
		returnRequirements.clear();
		
		int iii = 0;
		for(iii = ra.length - 1; iii >= 0; iii--)
		{
			ii = iii;
			reqsPlus.clear();
			if(iii < ra.length - 1)
			{
				if(reqMap.get(ra[iii]) == null)
				{
					while (ii < ra.length) 
					{
						recursionNumber = 0;
						reqs2Plus(ra, reqsPlus);
						ii++; 
					}
					String outputReq = arrayList2str(reqsPlus, " ");

						String[] prevReqParts = prevReq.split(" ");
						String[] currReqParts = outputReq.split(" ");

						int currReqCount = 0, expFound = 0, newReq = 0;
						while(currReqCount < currReqParts.length)
						{
							expFound = 0;
							for(String temp: prevReqParts)
							{
								if(temp.equals(currReqParts[currReqCount]))
								{
									expFound = 1;
									break;
								}
							}
							if(expFound == 0)
							{
								newReq = 1;
								reqMap.put(ra[iii], currReqParts[currReqCount]);
							}
							currReqCount++;
						}
						if(newReq == 0)
						{
							String[] uniqueCurrItems = null, uniquePrevItems = null;
							int uniqueCurrCount = 0, uniquePrevCount = 0;
							
							HashMap<String, Integer> currItemCount = new HashMap<String, Integer>();
							HashMap<String, Integer> prevItemCount = new HashMap<String, Integer>();
							
							uniqueCurrItems = new HashSet<String>(Arrays.asList(currReqParts)).toArray(new String[currReqParts.length]);
							uniquePrevItems = new HashSet<String>(Arrays.asList(prevReqParts)).toArray(new String[prevReqParts.length]);
							
							for(String tempUnique: uniqueCurrItems)
							{
								for(String tempOrig: currReqParts)
								{
									if(tempOrig.equals(tempUnique))
										uniqueCurrCount++;
								}
								currItemCount.put(tempUnique, uniqueCurrCount);
							}
							
							for(String tempUnique: uniquePrevItems)
							{
								for(String tempOrig: prevReqParts)
								{
									if(tempOrig.equals(tempUnique))
										uniquePrevCount++;
								}
								prevItemCount.put(tempUnique, uniquePrevCount);
							}							
							
							for(String tempUnique: uniquePrevItems)
							{
								if(currItemCount.get(tempUnique) > prevItemCount.get(tempUnique))
									if(tempUnique == null)
										tempUnique = "[N]";		
									reqMap.put(ra[iii], tempUnique);
							}							
						}
						prevReq = outputReq;
					}
			}
			else
			{
				while (ii < ra.length) 
				{
					recursionNumber = 0;
					reqs2Plus(ra, reqsPlus);
					ii++; 
				}
					String outputReq = arrayList2str(reqsPlus, " ");
					if(outputReq == null)
						outputReq = "[N]";
					reqMap.put(ra[iii], outputReq);

					prevReq = outputReq;				
			}
		}
		for(String r: ra)
		{
			//symGrammar.format("\nSub-Feature: %s \nCorresponding Requirement String: %s", r, reqMap.get(r));
			returnRequirements.add(reqMap.get(r));
		}
		closeSymGrammarFile();
		//return arrayList2str(reqsPlus, " ");
		return reqMap;
		}
		catch(Exception e)
		{
			return null;
		}
	}	
		
	
	
	public String reqProcess(String reqs) {
		try
		{
		String[] ra = reqs.split(" ");
		ArrayList<String> reqsPlus = new ArrayList<String>();
		
		setSymGrammarFile("SymbolicGrammarParseTestCase"+testCaseNumber+".txt");
		
			ii = 0;
			while (ii < ra.length) 
			{
				symGrammar.format("\nStarting off ....");
				recursionNumber = 0;
				symGrammar.format("\n[%d] ii = %d: thisR before = %s", recursionNumber, ii, "R");

				reqs2Plus(ra, reqsPlus);

				ii++; 
			}
		closeSymGrammarFile();
		return arrayList2str(reqsPlus, " ");
		}
		catch(Exception e)
		{
			return "Exception";
		}
	}
	
	
	
	private String reqs2Plus(String[] reqs, ArrayList<String> reqsPlus  ) {
		try{
		//symGrammar.format("\n[%d][%d] Recursion number: %d", ii, recursionNumber, recursionNumber);
		//.format("\n[%d][%d]Current sub expression: %s at ii = %d", ii, recursionNumber, reqs[ii], ii);
		String[] r = reqs[ii].split("\\+");
		String thisR = "";
		String principalReq = "";    // used to represent the principle operator of this req. 

		String var = getVar( r[r.length - 1] );
		int index = getIndex( r[r.length - 1] );
		//symGrammar.format("\n[%d][%d]Value of r.Length: %d", ii, recursionNumber, r.length);
		//symGrammar.format("\n[%d][%d]Var: %s at index = %d", ii, recursionNumber, var, index);
		// get the leftmost token of thisR
		String[] token = getGrammar(var).get(index).getStringTokenizer();
		//symGrammar.format("\n[%d][%d]Token Length: %d", ii, recursionNumber, token.length);
		for (int k = 0; k < token.length; k++ ) {
			//symGrammar.format("\n[%d][%d]Loop k: %d, token[k]: %s", ii, recursionNumber, k, token[k]);
			if (isVar(token[k])) {
				//symGrammar.format("\n[%d][%d]isVar is true", ii, recursionNumber);
				recursionNumber++;
				ii++;
				//symGrammar.format("\n[%d][%d] ii = %d: thisR before = %s", ii, recursionNumber, ii, thisR);
				thisR += reqs2Plus(reqs, reqsPlus);
				//symGrammar.format("\n[%d][%d] ii = %d: thisR after = %s", ii, recursionNumber, ii, thisR);
			}
			else {
				//symGrammar.format("\n[%d][%d]isVar is false", ii, recursionNumber);
				thisR += token[k];
				//symGrammar.format("\n[%d][%d]Value of thisR: %s", ii, recursionNumber, thisR);
			}		
		}
		principalReq = thisR;
		//symGrammar.format("\n[%d][%d]Value of principalReq: %s", ii, recursionNumber, principalReq);

		// from now on, for each applied grammar rule (e.g., E ::= E - F)
		// only "- F" will be append into thisR
		for (int j = r.length - 2; j >= 0; j--) {
		
			var = getVar( r[j] );
			index = getIndex( r[j] );
			//symGrammar.format("\n[%d][%d]Value of j: %d Value of r[j]: %s, var: %s index: %d", ii, recursionNumber, j, r[j], var, index);
			token = getGrammar(var).get(index).getStringTokenizer();
			//symGrammar.format("\n[%d][%d]Value of token length: %s", ii, recursionNumber, token.length);
			if (token.length > 1) {  // to find the principle non-unit production
				principalReq = arrayList2str(token);
				//symGrammar.format("\n[%d][%d]Token greater than 1 -> Value of principalReq: %s", ii, recursionNumber, principalReq);
			}
			for (int k = 1; k < token.length; k++ ) {	
				//symGrammar.format("\n[%d][%d]Later Loop k: %d, token[k]: %s", ii, recursionNumber, k, token[k]);
				if (isVar(token[k])) {
					//symGrammar.format("\n[%d][%d]isVar is true", ii, recursionNumber);
					recursionNumber++;
					ii++;
					//symGrammar.format("\n[%d][%d] ii = %d: thisR before = %s", ii, recursionNumber, ii, thisR);
					thisR += reqs2Plus(reqs, reqsPlus);
					//symGrammar.format("\n[%d][%d] ii = %d: thisR after = %s", ii, recursionNumber, ii, thisR);
				}
				else {
					//symGrammar.format("\n[%d][%d]isVar is false", ii, recursionNumber);
					thisR += token[k];
					//symGrammar.format("\n[%d][%d]Value of thisR: %s", ii, recursionNumber, thisR);
				}	
			}			
		}
		
		//symGrammar.format("\n[%d][%d]Added to reqsPlus: %s", ii, recursionNumber, thisR);
		reqsPlus.add(thisR);
		recursionNumber--;
		return principalReq;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	
	
	private String getVar( String r ) {
		
		String[] st = r.split("#");
		return st[0];
	}
	
	
	private int getIndex( String r ) {
		
		String[] st = r.split("#");
		return Integer.valueOf( st[1] );
	}

	

	private String arrayList2str( String[] rs) {
		
		String s = "";
		
		for (String a : rs) {
			s += "" + a;
		}
		
		return s;
	}

	
	private String arrayList2str( ArrayList<String> rs, String delimiter ) {
		
		String s = "";
		
		for (String a : rs) {
			s += delimiter + a;
		}
		
		return s;
	}
	
	
	
	private String[] reducedReq(String rs) {
		
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
				}
			}
			
			if ( j == reqs.size() ) {
				reqs.add(ra[i]);
			}
		}
		
		String[] temp = {""};
		return reqs.toArray(temp);
	}

	
	
}




