package edu.upenn.cis455.webserver;

import java.io.*;
import java.text.ParseException;

/**
 * @author James Park
 * @class cis455/555
 * 
 * Interface for a HTTP request parser
 *
 */
public interface HttpRequestParser {
	
	public HttpRequest parse(String line, BufferedReader in);
	
	public boolean checkDate(String date, File file) throws ParseException;	
	
}
