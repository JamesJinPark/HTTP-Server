package edu.upenn.cis455.webserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

/**
 * @author James Park
 * @class cis455/555
 *
 */
public class DefaultRequestParser implements HttpRequestParser{

	@Override
	public HttpRequest parse(String line, BufferedReader in){
		try{
			Map<String, String> headers = new HashMap<>();
//			String line = in.readLine();			
			String[] parts = line.split("\\s+");
			if(parts.length != 3){
				return new HttpRequest(headers, "Missing", "Missing", "Missing");				
			}
			String method = parts[0];
			String path = parts[1];
			String version = parts[2];
			while((line = in.readLine()) != null && !line.isEmpty()){
				//e.g: Host: localhost:90
				try{
					int separator = line.indexOf(":");
					String key = line.substring(0, separator).trim(); //Host
					String value = line.substring(separator + 1).trim(); //localhost:90
					headers.put(key, value);
				}catch(Exception e){
					System.err.println("Could not parse header: " + line);
				}	
			}
			return new HttpRequest(headers, method, path, version);
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param month
	 * @return String month or error
	 * Used for converting the date from HTTP request into a usable format
	 */
	public String monthConverter(String month){
		switch(month){
		case "Jan":		return "01/";
		case "Feb":		return "02/";
		case "Mar":		return "03/";
		case "Apr":		return "04/";
		case "May":		return "05/";
		case "Jun":		return "06/";
		case "Jul":		return "07/";
		case "Aug":		return "08/";
		case "Sep":		return "09/";
		case "Oct":		return "10/";
		case "Nov":		return "11/";
		case "Dec":		return "12/";		
		}
		return "Error! monthConverter failed!";
	}
	
	/* (non-Javadoc)
	 * @see edu.upenn.cis455.webserver.HttpRequestParser#checkDate(java.lang.String, java.io.File)
	 * Returns boolean whether the date from HTTP request is before the date on the file requested
	 */
	@Override
	public boolean checkDate(String date, File file) throws ParseException {
		//must parse date to three formats
		//numbers correspond to order in which described in HTTP made really easy

		//		If-Modified-Since:  Fri, 31 Dec 1999 23:59:59 GMT 
		//		If-Modified-Since:  Friday, 31-Dec-99 23:59:59 GMT
		//		If-Modified-Since:  Fri Dec 31 23:59:59 1999
	
		String parts[] = date.split("\\s*(,|\\s)\\s*");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");		                            				
		Date dateFile = sdf.parse(sdf.format(file.lastModified()));
		if(parts[0].contains("day")){ //Friday, 31-Dec-99 23:59:59 GMT
			String temp = "";
			//31-Dec-99
			String tempParts[] = parts[1].split("-");
			if(tempParts.length != 3){
				System.out.println("Wrong format of time!");
				System.out.println(tempParts.length);
				System.out.println(tempParts);
				return false;
			}
			temp += monthConverter(tempParts[1]); // add month
			temp += tempParts[0] + "/"; // add date
			int currentYear = 16;
			if (Integer.valueOf(tempParts[2]) > currentYear){
				temp +=  "19" + tempParts[2]; // add year
			}else{
				temp +=  "20" + tempParts[2]; // add year				
			}
			
			temp += " " + parts[2]; //time in the format of 23:59:59
			Date dateHeader = sdf.parse(temp);
			return dateFile.after(dateHeader);
		}else if(isInteger(parts[1])){ //Fri, 31 Dec 1999 23:59:59 GMT
			String temp = "";
			//"MM/dd/yyyy HH:mm:ss"
			temp += monthConverter(parts[2]); //Dec
			temp += parts[1] + "/"; //31 
			temp += parts[3]; //1999
			temp += " " + parts[4]; //23:59:59
			Date dateHeader = sdf.parse(temp);
			return dateFile.after(dateHeader);
		}else if(isInteger(parts[2])){//Fri Dec 31 23:59:59 1999
			String temp = "";
			temp += monthConverter(parts[1]); //Dec 
			temp += parts[2] + "/"; //31
			temp += parts[4]; //1999
			temp += " " + parts[3]; //23:59:59
			Date dateHeader = sdf.parse(temp);
			return dateFile.after(dateHeader);
		}else{//not valid date
			return false;
		}			
	}

	public static boolean isInteger( String input ){
	   try {
	      Integer.parseInt( input );
	      return true;
	   } catch(Exception e) {
	      return false;
	   }
	}

}