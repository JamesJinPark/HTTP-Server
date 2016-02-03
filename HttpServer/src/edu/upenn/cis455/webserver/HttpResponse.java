package edu.upenn.cis455.webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	public final Map<String, Object> headers = new HashMap<>();
	private String body;//response body
	private byte[] bytes;//response bytes
	private int status = 200;
	
	public HttpResponse(){
		headers.put("Content-Type", "text/plain");
		headers.put("Content-Length", "0");
	}
	
	public void setBody(String body){ 
		headers.put("Content-Length", body.length());
		this.body = body;
	}

	public void setBytes(byte[] bytes){ 
		headers.put("Content-Length", bytes.length);
		this.bytes = bytes;
	}

	public String getBody(){
		return this.body;
	}

	public byte[] getBytes(){
		return this.bytes;
	}

	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
}
