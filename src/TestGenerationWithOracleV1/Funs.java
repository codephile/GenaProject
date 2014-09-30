package TestGenerationWithOracleV1;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Funs {

	public static String apply(String functor, String[] args) {
		
		String result = null;
		
		// for integer arithmetic application
		if (functor.equals("intAdd")) {
			result = intAdd(args[0], args[1]);
		}
		else if (functor.equals("intSub")) {
			result = intSub(args[0], args[1]);
		}
		else if (functor.equals("intMul")) {
			result = intMul(args[0], args[1]);
		}
		else if (functor.equals("intDiv")) {
			result = intDiv(args[0], args[1]);
		}
		// for the parking lot web application
/*		else if (functor.equals("strcat")) {
			result = strcat(args[0], args[1]);
		}
*/
		else if (functor.equals("strcat")) {
			result = strcat(args[0], args[1]);
		}
		else if (functor.equals("dateStd")) {
			result = dateStd(args[0], args[1], args[2]);
		}
		else if (functor.equals("timeStd")) {
			result = timeStd(args[0], args[1]);
		}
		else if (functor.equals("time24Std")) {
			result = time24Std(args[0], args[1]);
		}
		else if (functor.equals("dtimeStd")) {
			result = dtimeStd(args[0], args[1]);
		}
		else if (functor.equals("timeSub")) {
			result = timeSub(args[0], args[1]);
		}
		else if (functor.equals("price")) {
			result = price(args[0], args[1]);
		}
		// for the first Data application on collections
		else if (functor.equals("collectionSize")) {
			result = collectionSize(args[0]);
		}
		else if (functor.equals("addDescs")) {
			result = addDescs(args[0], args[1]);
		}
		else if (functor.equals("add1Desc")) {
			result = add1Desc(args[0], args[1]);
		}
		else if (functor.equals("descWithNewDate")) {
			result = descWithNewDate(args[0], args[1]);
		}
		else if (functor.equals("setFSNDate")) {
			result = setFSNDate(args[0]);
		}
		else if (functor.equals("newDate")) {
			result = newDate(args[0], args[1], args[2]);
		}
		else if (functor.equals("removeIndexes")) {
			result = removeIndexes(args[0], args[1]);
		}
		else if (functor.equals("deleteDescs")) {
			result = deleteDescs(args[0], args[1]);
		}
		
	
		return result;
	}
	
	

	private static String deleteDescs(String indexList, String collections) {

		if (indexList.equals("[]"))
			return collections;
		
		String i = getFirst(indexList);
		collections = removeByIndex(collections, i);
		
		return deleteDescs(getRest(indexList), collections);
	}



	private static String removeByIndex(String collections, String i) {

		int index = Integer.valueOf(i);
		
		if (index <= 0)
			return collections;
		
		int from = 1;
		for (int k = 1; k <= index; k++) {
			
			int pos = collections.indexOf('[', from);
			if (pos > 0)
				from = pos + 1;
			else // index i out of bound
				return collections;
		}
		
		return collections.substring(0, from-1) + 
				collections.substring( collections.indexOf(']', from) + 1);
	}



	private static String getFirst(String indexList) {
		
		StringTokenizer tokens = new StringTokenizer(indexList, ",");
		
		if (tokens.countTokens() > 1)
			return tokens.nextToken().substring(1);
		else
			return indexList.substring(1, indexList.length() - 1);
	}


	private static String getRest(String indexList) {
		
		StringTokenizer tokens = new StringTokenizer(indexList, ",");
		
		if (tokens.countTokens() > 1)
			return "[" + indexList.substring(indexList.indexOf(",")+1);
		else
			return "[]";
	}



	private static String removeIndexes(String i, String indexList) {
		
		if (indexList.equals("[]"))
			return "[" + i + "]";
		else
			return "[" + i + "," + indexList.substring(1);
	}



	private static String collectionSize(String descList) {

		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1){

		       lastIndex = descList.indexOf("[",lastIndex);
		       
		       if( lastIndex != -1){
		             count ++;
		             lastIndex++;
		      }
		}
		
		// count reduced by 1 because of the enclosed [] layer
		return String.valueOf(count-1);
	}






	private static String addDescs(String addDescList, String descList) {

		if (addDescList.equals("[]"))
			return descList;
		
		String firstDesc = addDescList.substring( 
				addDescList.indexOf("[", 1), addDescList.indexOf("]") + 1);
		String restAddDescList = "[" + addDescList.substring( addDescList.indexOf("]") + 1 );
		String newDescList = add1Desc(firstDesc, descList);
		
		return addDescs(restAddDescList, newDescList);
	}




	private static String add1Desc(String desc, String descList) {

		if (descList.indexOf( desc.trim() ) >= 0)
			// a duplicate
			return descList;
		else {
			// not a duplicate, append 'desc' at the end of descList
			return descList.substring(0, descList.lastIndexOf("]")) + desc.trim() + "]";
		}
	}




	private static String descWithNewDate(String desc, String fsnDate) {
		// desc: [<system,1000><prin,1001><type,SystemDescriptorTypeF><reporting,4444><reportingPrin,3333><effectiveDate,04/01/2008>]
		// fsnDate: <effectiveDate,??/??/??>
		
		String[] strs = desc.split("<effectiveDate,");
		int p = strs[1].indexOf(">");
		
		return strs[0] + fsnDate + strs[1].substring(p+1);
	}




	private static String setFSNDate(String date) {
		
		return "<effectiveDate," + date + ">";
	}




	private static String newDate(String month, String day, String year) {

		return month + "/" + day + "/" + year;
	}




	public static String timeSub(String Exit, String Enter) {
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		try {
			Date d1 = format.parse(Enter);
			Date d2 = format.parse(Exit);
			//in milliseconds
			long diff = (d2.getTime() - d1.getTime()) / 60000;
			
			return String.valueOf(diff);
		}
		catch (Exception e) {
			System.out.print("parsing date error\n");
		}

		return null;
		
	}


	public static String dtimeStd(String date, String time) {

		return date + " " + time;
	}


	public static String time24Std(String ap, String time) {
		
		String [] hourToken = time.split(":");
		int hour = Integer.valueOf(hourToken[0]);
			
		hour %= 12;
		
		if ( ap.contains("pm") ) 
			hour += 12;
		
		return String.valueOf(hour) + ":" + hourToken[1] + ":00";
	}


	// timeStd [Hour] [Minute]
	public static String timeStd(String hour, String minute) {

		String h = (hour.length() == 1 ? "0" + hour : hour);
		String m = (minute.length() == 1 ? "0" + minute : minute);
		
		return h + ":" + m + ":00";
	}
	
	// (dateStd [Month] [Day] [Year])
	public static String dateStd(String month, String day, String year) {

		String m = (month.length() == 1 ? "0" + month : month);
		String d = (day.length() == 1 ? "0" + day : day);
		
		return m + "/"+ d + "/" + year;
	}





	public static String strcat(String a, String b) {
		return a + b;
	}


	public static String intAdd(String a, String b) {
		return String.valueOf( Integer.valueOf(a) + Integer.valueOf(b) );
	}

	
	public static String intSub(String a, String b) {
		return String.valueOf( Integer.valueOf(a) - Integer.valueOf(b) );
	}

	
	public static String intMul(String a, String b) {
		return String.valueOf( Integer.valueOf(a) * Integer.valueOf(b) );
	}

	
	public static String intDiv(String a, String b) {
		return String.valueOf( Integer.valueOf(a) / Integer.valueOf(b) );
	}
	
	public static String price (String Lot, String Time)
	{
		if (Integer.valueOf(Time) < 0)
			return "ERROR! YOUR EXIT DATE OR TIME IS BEFORE YOUR ENTRY DATE OR TIME";
		
		int day = Integer.valueOf(Time)/(24*60);
		int hour = (Integer.valueOf(Time)%(24*60)) / 60;
		int minute = (Integer.valueOf(Time)%(24*60)) % 60;
		int result=0;

		System.out.printf("Time diff is: Day %s Hour %s Min %s\n", day, hour, minute);
		
		/*Short-Term (hourly) Parking
		$2.00 first hour; $1.00 each additional 1/2 hour
		$24.00 daily maximum*/
		if (Lot.equals("shortTerm"))
		{
			/*if (Integer.valueOf(Time) < 0)
				return "ERROR! YOUR EXIT DATE OR TIME IS BEFORE YOUR ENTRY DATE OR TIME";*/
			
			
			System.out.printf("\nLot is: %s\n", Lot);
			
			if (hour >= 12)
				result = 24* (day + 1);
			
			else if (day >=1 && minute >= 30)
			{
				result = 24*day + 2*hour + 1;		
			}
			
			else if (day >=1 && minute < 30)
			{
				result = 24*day + 2*hour;		
			}
			
			else if (day < 1 && minute >= 30)
			{
				result = 2*hour + 1;		
			}
			
			else if (day < 1 && minute < 30)
			{
				result = 2*hour;		
			}
			
		}
		
		/*Economy Lot Parking
		$2.00 per hour
		$9.00 daily maximum
		$54.00 per week (7th day free)*/
		else if (Lot.equals("economy"))
		{
			/*if (Integer.valueOf(Time) < 0)
				return "0";*/
			
			System.out.printf("\nLot is: %s\n", Lot);
			
			/*//calculate discount day
			day = day - day/7;
			int hourPrice = 2*hour;
			
			if (hourPrice >= 9)
				result = 9*(day+1);
			else if (hourPrice < 9 && minute > 0)
				result = 9*day + 2*(hour+1);*/
			
			
			//calculate discount day
			day = day - day/7;
			int hourPrice = 2*hour;
			int remainDay = day%7; 
			
			if (remainDay < 6)
			{
				if (hourPrice > 9)
					result = 9*(day+1);
				else
					result = 9*day + 2*hour;
			}
			else if (remainDay == 6)
			{
				result = 9*(day/7+1);
			}		
			
			
		}
		
		/*Long-Term Surface Parking (North Lot)
		$2.00 per hour
		$10.00 daily maximum
		$60.00 per week (7th day free)*/
		else if (Lot.equals("surface"))
		{
			/*if (Integer.valueOf(Time) < 0)
				return "0";*/
			
			System.out.printf("\nLot is: %s\n", Lot);
	
			//calculate discount day
			day = day - day/7;
			int hourPrice = 2*hour;
			int remainDay = day%7; 
			
			if (remainDay < 6)
			{
				if (hourPrice > 10)
					result = 10*(day+1);
				else
					result = 10*day + 2*hour;
			}
			else if (remainDay == 6)
			{
				result = 10*(day/7+1);
			}		
			
		}
		
		/*Long-Term Garage Parking
		$2.00 per hour
		$13.00 daily maximum
		$78.00 per week (7th day free)*/
		else if (Lot.equals("garage"))
		{
			/*if (Integer.valueOf(Time) < 0)
				return "0";*/
			
			System.out.printf("\nLot is: %s\n", Lot);
			
			//calculate discount day
			day = day - day/7;
			int hourPrice = 2*hour;
			int remainDay = day%7; 
			
			if (remainDay < 6)
			{
				if (hourPrice > 13)
					result = 13*(day+1);
				else
					result = 13*day + 2*hour;
			}
			else if (remainDay == 6)
			{
				result = 13*(day/7+1);
			}		
			
		}
		
		/*Valet Parking
		$18 per day
		$12 for five hours or less*/
		
		// note: on the website, the actual calculation is if hour < 6, the price is 12, >= 6 count as a whole day		
		else if (Lot.equals("valet"))
		{
			/*if (Integer.valueOf(Time) < 0)
				return "ERROR! YOUR EXIT DATE OR TIME IS BEFORE YOUR ENTRY DATE OR TIME";*/
			
			System.out.printf("\nLot is: %s\n", Lot);
					
			if (hour < 6)
				result = 18*day + 12;
			else
				result = 18*(day + 1);
			
		}
		return String.valueOf(result);		
	}

}

