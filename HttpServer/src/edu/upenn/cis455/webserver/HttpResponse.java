package edu.upenn.cis455.webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James Park
 * @class cis455/555
 * 
 * Builds the server response to the HTTP request
 *
 */
public class HttpResponse {
	public final Map<String, Object> headers = new HashMap<>();
	private String body;//response body
	private byte[] bytes;//response bytes
	private String status = "200 OK";
	
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

	public byte[] getMyBytes(){
		return this.bytes;
	}

	public void setStatus(String status){
		this.status = status;
	}
	
	public String getStatus(){
		return this.status;
	}
}
