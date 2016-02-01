package edu.upenn.cis455.webserver;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.Map;

public class HttpServer {
	
	final private int port;
	
	@SuppressWarnings("unused")
	final private Path rootDir; //not unused but Eclipse is being stupid so added SuppressWarnings.  Used in MyServer.
	
	final HttpRequestHandler notFoundHandler = new HttpRequestHandler(){
		public void handle(HttpRequest request, HttpResponse response){
			String body = "404 Page Not Found for Request:\n\n" + request;
			response.setBody(body);
			response.setStatus(404);
		}
	};
	final private Map<Route, HttpRequestHandler> routes;
	
	final private HttpRequestParser httpRequestParser = new DefaultRequestParser();
			
	public HttpServer(int port, Path rootDir, Map<Route, HttpRequestHandler> routes){
		this.port = port;
		this.rootDir = rootDir;
		this.routes = routes;
	}
	
	public void runServer(){		
		
		ThreadPool threads = new ThreadPool(100);//creates threadpool of 100 threads and BlockingQueue of size 100;

		try(ServerSocket serverSocket = new ServerSocket(this.port)){
			while(!ShutdownHook.isShutdown.get()){
				try( //all these are closeable by putting them in as arguments for try()
					Socket socket = serverSocket.accept();
					InputStreamReader reader = new InputStreamReader(socket.getInputStream());
					BufferedReader in = new BufferedReader(reader);
						
					PrintWriter out = new PrintWriter(socket.getOutputStream());
				) {
					
					Runnable runnable = new Runnable(){
						@Override
						public void run(){
						
							HttpRequest request = httpRequestParser.parse(in);
							HttpResponse response = new HttpResponse();
		
							Route requestedRoute = Route.of(request.method, request.path);
							HttpRequestHandler handler = routes.get(requestedRoute);
		
							if(handler == null){
								handler = notFoundHandler;
							}
												
							handler.handle(request, response);
							out.println("HTTP/1.1 " + response.getStatus());
							for(Map.Entry<String, Object> header: response.headers.entrySet()){
								out.println(header.getKey() + ": " + header.getValue());
							}
							
							if(response.getBody().length() > 0){
								out.println("");
								out.println(response.getBody());
							}
							out.flush();
						}
					};
					threads.run(runnable);//adds this instance of runnable (request) to the queue of the thread pool
					
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
		}catch(Exception e){
			System.err.println("Error! Could not connect to socket:");
			e.printStackTrace();
			System.exit(1); //server shuts down due to error
		}
		threads.killAllThreads(); 
		System.exit(0);
	}
}
