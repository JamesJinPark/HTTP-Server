package edu.upenn.cis455.webserver;

import java.io.*;

public class HttpRequestParser {
	
	String[] parsedRequest;

	public HttpRequestParser(BufferedReader in){		
		//parsedRequest[0] is type of request (e.g.: GET) 
		//parsedRequest[1] is requested path
		//parsedRequest[2] is HTTP version number
		try {
			parsedRequest = in.readLine().split("\\s+");
		} catch (IOException e) {
			System.out.println("Error!  Could not parse request: " + e);
		}
	}
	
	public HttpRequest createHttpRequest(){
		HttpRequest request = new HttpRequest(parsedRequest[0], parsedRequest[1]);
		return request;
	}
}
