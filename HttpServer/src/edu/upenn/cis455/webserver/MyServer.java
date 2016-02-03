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
		
		//Retrive HTML pages 
		myRoutes.put(Route.of("GET", "html"), new HttpRequestHandler(){
			@Override
 			public void handle(HttpRequest request, HttpResponse response){
				Path page = rootDir.resolve("." + request.path.toString());
				try{
					byte[] bytes = Files.readAllBytes(page);
					response.setBody(new String(bytes));
					response.headers.put("Content-Type", "text/html");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});

		//Return directory pages 
		myRoutes.put(Route.of("GET", "directory"), new HttpRequestHandler(){
			@Override
			public void handle(HttpRequest request, HttpResponse response){
				File file = new File("." + request.path);
				String temp = "<h1>" + file.getAbsolutePath() + "</h1>";
				for (File folder : file.listFiles()){
					String fileName = folder.toString().substring(2);
					temp += "<a href=/" + fileName +">"+ fileName + "</a>" + "<br>";
				}
				response.setBody(temp);
				response.headers.put("Content-Type", "text/html");				
			}
		});
		
		//Retrive images
		myRoutes.put(Route.of("GET", "image"), new HttpRequestHandler(){
			@Override
 			public void handle(HttpRequest request, HttpResponse response){
				String imageFormat = request.path.substring(request.path.length() - 3);
				System.out.println(imageFormat);
				
				Path page = rootDir.resolve("." + request.path.toString());
				try{
					byte[] bytes = Files.readAllBytes(page);
					response.setBytes(bytes);
					response.headers.put("Content-Type", "image/" + imageFormat);
					System.out.println("I finished imagehandler");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});

				
		//Retrieve page for / directory
		myRoutes.put(Route.of("GET", "/"), new HttpRequestHandler(){
			@Override
			public void handle(HttpRequest request, HttpResponse response){
				File file = new File(".");
				String temp = "<h1>" + file.getAbsolutePath() + "</h1>";
				for (File folder : file.listFiles()){
					String fileName = folder.toString().substring(2);
					temp += "<a href=/" + fileName +">"+ fileName + "</a>" + "<br>";
				}
				response.setBody(temp);
				response.headers.put("Content-Type", "text/html");				
			}
		});
				
		//Special URL shutdown
		myRoutes.put(Route.of("GET", "/shutdown"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response){
				String msg = ("Shutting down server.");
				System.out.println(msg);
				response.setBody(msg);			
				ShutdownHook.shutdown();
			}
		});

		HttpServer httpServer = new HttpServer(port, rootDir, myRoutes);
		httpServer.runServer();
	}
}