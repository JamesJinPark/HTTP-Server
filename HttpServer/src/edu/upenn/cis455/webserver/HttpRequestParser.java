package edu.upenn.cis455.webserver;

import java.io.*;
import java.text.ParseException;

public interface HttpRequestParser {
	
	public HttpRequest parse(BufferedReader in);
	
	public boolean checkDate(String date, File file) throws ParseException;	
	
}
