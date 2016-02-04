package edu.upenn.cis455.webserver;

import java.util.*;
import java.io.*;

public class DefaultRequestParser implements HttpRequestParser{

	@Override
	public HttpRequest parse(BufferedReader in){
		try{
			String line = in.readLine();			
			String[] parts = line.split("\\s+");
			String method = parts[0];
			String path = parts[1];
			String version = parts[2];
			Map<String, String> headers = new HashMap<>();
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
}
