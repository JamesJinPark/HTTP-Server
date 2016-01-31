package edu.upenn.cis455.webserver;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
		
	public static void main(String[] args) throws Exception{
		if(args.length != 2){
			System.out.println("JamesJinPark\nSeas Login Name: jamespj");
		}
		else{
			
			Map<HttpRequest, HttpRequestHandler> myRoutes = new HashMap<HttpRequest, HttpRequestHandler>();			
			HttpRequestHandler handler = new HttpRequestHandler();
			myRoutes.put(new HttpRequest("GET", "/shutdown"), handler); //logic for shutting down server
			myRoutes.put(new HttpRequest("GET", "/"), handler); //logic for serving root path request
			myRoutes.put(new HttpRequest("GET", "/control"), handler);//logic for bringing up control panel
			String rootDirectory = args[1] + "/index.html"; //what to do when user uses "/"?
			
			//Start server
			HttpServer server = new HttpServer();
			Integer portNum = Integer.valueOf(args[0]); //port number
			server.runServer(portNum, rootDirectory, myRoutes);
		}
	}
	
	public void runServer(Integer portNum, String path, Map<HttpRequest, HttpRequestHandler> myRoutes){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(portNum);
			
			while(true){
				try{
					Socket socket = serverSocket.accept();
					InputStreamReader reader = new InputStreamReader(socket.getInputStream());
					BufferedReader in = new BufferedReader(reader);
					
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					
					HttpRequest request = new HttpRequestParser(in).createHttpRequest();
					HttpResponse response = new HttpResponse(out);
					HttpRequestHandler handler = myRoutes.get(request);
					if(handler != null){
						handler.handle(request, response);
					}else{
						response.writer.println("404 Page Not Found for Request:\n\n \""+request+"" + "\n" + 
								request.getMethod() + "\n"+request.getPath() + myRoutes.entrySet().iterator().next().getKey().getPath());
					}
					out.flush();
					socket.close();
				}catch(IOException e){
					System.out.println(e);
				}
			}
			
		}catch(Exception e){
			System.out.println("Error! Could not connect to socket: " + e);
			e.printStackTrace();
			System.exit(1); //server shuts down due to error
		}finally{
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}