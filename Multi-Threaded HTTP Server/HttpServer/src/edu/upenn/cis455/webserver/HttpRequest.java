package edu.upenn.cis455.webserver;

import java.util.Map;

/**
 * @author James Park
 * @class cis455/555
 * 
 * Holds the HTTP request and organizes the information into different categories
 */
public class HttpRequest {
	public Map<String, String> headers;
	public String version; //HTTP version 
	public String method; //request method
	public String path; //requested path (e.g.: shutdown)
	public boolean isHead = false;
	public boolean isError = false;
	public boolean isModified = true;
	public boolean isUnmodified = true;
	
	//constructor
	public HttpRequest(Map<String, String> headers, String method, String path, String version){
		this.headers = headers;
		this.method = method.toUpperCase();
		this.path = path;
		this.version = version;
	}

	//constructor without headers
	public HttpRequest(String method, String path, String version){
		this.method = method.toUpperCase();
		this.path = path;
		this.version = version;
	}

	@Override
	public String toString() {
		String s = "Method: " + this.method;
		s += "\nPath: " + this.path;		
		for(Map.Entry<String, String> entry: this.headers.entrySet()){
			s += "\n" + entry.getKey() + ": " + entry.getValue();
		}
		return s;
	}	
}
