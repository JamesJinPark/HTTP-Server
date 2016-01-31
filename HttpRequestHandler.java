package edu.upenn.cis455.webserver;

import java.io.*;

public class HttpRequestHandler{
	
	void handle(HttpRequest request, HttpResponse response){
		
		//shutdown
		if(request.getMethod().equalsIgnoreCase("get") && request.getPath().equals("/shutdown")){
			System.out.println("Shutting down server.");
			//make sure threads exit
			System.exit(0);
		}
		
		//control panel
		else if(request.getMethod().equalsIgnoreCase("get") && request.getPath().equals("/controlPanel")){
			System.out.println("Displaying Control Panel.");
		}
			
		//
		else if(request.getMethod().equalsIgnoreCase("get")){
			
			response.writer.println("200 OK\n\n");
			
			String requestedPath = request.getPath() + "/home/cis455/workspace/HW1/index.html";//figure out how not to hardcode!

			File path = new File(requestedPath);//requested path
			try{
				byte[] buffer = new byte[2000];
				
				FileInputStream inputStream = new FileInputStream(path);
				int totalBytes = 0;
				int tempBytes = 0;
				while((tempBytes = inputStream.read(buffer)) != -1){
					response.writer.println(new String(buffer));					
					totalBytes += tempBytes;
				}
				System.out.println("Delivered requested content (total bytes sent: " + totalBytes + ")");
				inputStream.close();
			}
			catch(Exception e){
				System.out.println("Could not read file: " + e);
			}
		}else{
			response.writer.println("500 Error\n\nInternal Server Error:\n\n \""+request+"");
		}
	}
}