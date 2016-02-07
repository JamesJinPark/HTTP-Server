package edu.upenn.cis455.webserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MyServer {
	
	//application specific logic for the HTTP server
	
	public static void main(String[] args){
	
		if(args.length > 3 || args.length< 2){ //must have two arguments
			System.out.println("JamesJinPark\nSeas Login Name: jamespj");
			System.exit(1);
		} else if (args.length == 3){ //temporary place holder
			System.out.println("JamesJinPark\nSeas Login Name: jamespj");
			System.exit(1);			
		} else {
		
		//Start server
		int port = Integer.valueOf(args[0]).intValue(); //port number
		final Path rootDir = Paths.get(args[1]); //get root directory
		
		Map<Route, HttpRequestHandler> myRoutes = new HashMap<Route, HttpRequestHandler>();			
		
		//Retrieve HTML pages 
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

		//Retrieve plain text pages
		myRoutes.put(Route.of("GET", "plainText"), new HttpRequestHandler(){
			@Override
 			public void handle(HttpRequest request, HttpResponse response){
				Path page = rootDir.resolve("." + request.path.toString());
				try{
					byte[] bytes = Files.readAllBytes(page);
					response.setBody(new String(bytes));
					response.headers.put("Content-Type", "text/plain");
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
		
		//Retrieve images
		myRoutes.put(Route.of("GET", "image"), new HttpRequestHandler(){
			@Override
 			public void handle(HttpRequest request, HttpResponse response){
				String imageFormat = request.path.substring(request.path.length() - 3);				
				Path page = rootDir.resolve("." + request.path.toString());
				File file = new File(page.toString());
				try{
					BufferedInputStream input = new BufferedInputStream(new FileInputStream(page.toString()));
					byte[] bytes = new byte[(int)file.length()];
					input.read(bytes, 0 , bytes.length);
					input.close();
					//byte[] bytes = Files.readAllBytes(page);
					response.setBytes(bytes);
					response.headers.put("Content-Type", "image/" + ((imageFormat.equals("jpg")) ? "jpeg" : imageFormat));
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});

		//Retrieve pdf
		myRoutes.put(Route.of("GET", "pdf"), new HttpRequestHandler(){
			@Override
 			public void handle(HttpRequest request, HttpResponse response){
				Path page = rootDir.resolve("." + request.path.toString());
				try{
					byte[] bytes = Files.readAllBytes(page);
					response.setBytes(bytes);
					response.headers.put("Content-Type", "application/pdf");
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

		//304 status code handler
		myRoutes.put(Route.of("GET", "304Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
            	request.isError = true;
            	request.isModified = false;
	            response.setStatus("304 Not Modified");
	        }
		});

		//400 status code handler
		myRoutes.put(Route.of("GET", "400Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
	            String body = "400 Bad Request\n No header received: \n\n" + request;
            	request.isError = true;
	            response.setBody(body);
	            response.setStatus("400 Bad Request");
	        }
		});
		
		//404 status code handler
		myRoutes.put(Route.of("GET", "404Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
	            String body = "404 Page Not Found for Request:\n\n" + request;
	            request.isError = true;
	            response.setBody(body);
	            response.setStatus("404 Not Found");
	        }
	    });
	    

		//405 status code handler
		myRoutes.put(Route.of("GET", "405Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
	            String body = "405 Method Used Incorrectly for Request:\n\n" + request;
            	request.isError = true;
	            response.setBody(body);
	            response.setStatus("405 Method Not Allowed");
	        }
		});

		
		//412 status code handler
		myRoutes.put(Route.of("GET", "412Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
            	request.isError = true;
	            response.setStatus("412 Precondition Failed");
            	request.isUnmodified = false;
			}
		});

		//501 status code handler
		myRoutes.put(Route.of("GET", "501Error"), new HttpRequestHandler(){
			public void handle(HttpRequest request, HttpResponse response) {
	            String body = "501 Method Not Recognized for Request:\n\n" + request;
            	request.isError = true;
	            response.setBody(body);
	            response.setStatus("501 Not Implemented");
	        }
		});

		HttpServer httpServer = new HttpServer(port, rootDir, myRoutes);
		httpServer.runServer();
		}
	}
}
