package edu.upenn.cis455.webserver;

import java.util.Map;

public class HttpRequest {
	public Map<String, String> headers;
	final public String method; //request method
	final public String path; //requested path (e.g.: shutdown)
	
	//constructor
	public HttpRequest(Map<String, String> headers, String method, String path){
		this.headers = headers;
		this.method = method.toUpperCase();
		this.path = path;
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

