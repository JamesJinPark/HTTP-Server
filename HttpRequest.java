package edu.upenn.cis455.webserver;

import java.util.Map;

public class HttpRequest {
	public Map<String, String> headers;
	private String method; //request method
	private String path; //requested path (e.g.: shutdown)
	
	//constructor
	public HttpRequest(Map<String, String> headers, String method, String path){
		
	}
	//temporary constructor until headers are implemented
	public HttpRequest(String method, String path){
		this.method = method;
		this.path = path;
	}
	
	public String getMethod(){
		return this.method;
	}

	public String getPath(){
		return this.path;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		final HttpRequest other = (HttpRequest) obj;
		if((this.getMethod() == null) || (this.getPath()== null) ||(other.getMethod() == null || (other.getPath() == null))){
			return false;
		}
		if(!this.getMethod().equals(other.getMethod())){
			return false;
			
		}
		if(!this.getPath().equals(other.getPath())){
			return false;
		}
		return true;
	}

	@Override
	public int hashCode(){
		int hash = 4;
		hash = 53 * 3 + (this.getMethod() != null ? this.getMethod().hashCode() : 0);
		hash = 53 * hash + this.getPath().length();
		return hash;
	}
}

