package edu.upenn.cis455.webserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MyServer {
	
	//application specific logic for the HTTP server
	
	public static void main(String[] args){
		
		if(args.length != 2){ //must have two arguments
			System.out.println("JamesJinPark\nSeas Login Name: jamespj");
			System.exit(1);
		}
		
		//Start server
		int port = Integer.valueOf(args[0]).intValue(); //port number
		final Path rootDir = Paths.get(args[1]); //get root directory
		
		Map<Route, HttpRequestHandler> myRoutes = new HashMap<Route, HttpRequestHandler>();			
		myRoutes.put(Route.of("GET", "/"), new HttpRequestHandler(){
			@Override
			public void handle(HttpRequest request, HttpResponse response){
				Path indexPage = rootDir.resolve("Index.html");				
				try{
					byte[] bytes = Files.readAllBytes(indexPage);
					response.setBody(new String(bytes));
					response.headers.put("Content-Type", "text/html");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
				
		myRoutes.put(Route.of("GET", "/shutdown"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response){
				String msg = ("Shutting down server.");
				System.out.println(msg);
					
				response.setBody(msg);
				
				//make sure threads exit				
				ShutdownHook.shutdown();
			}
		});

		HttpServer httpServer = new HttpServer(port, rootDir, myRoutes);
		httpServer.runServer();
	}
}
