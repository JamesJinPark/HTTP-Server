package edu.upenn.cis455.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.*;

public class HttpServer {

    final private int port;

    final private Path rootDir; 
    
    final HttpRequestHandler notFoundHandler = new HttpRequestHandler() {
        public void handle(HttpRequest request, HttpResponse response) {
            String body = "404 Page Not Found for Request:\n\n" + request;
            response.setBody(body);
            response.setStatus(404);
        }
    };
    
	final ThreadPool threads = new ThreadPool(100);//creates threadpool of 100 threads and BlockingQueue of size 100;

	/*	Special URL control
		Returns page with 
	    	a: student information
			b: list of all threads in the thread pool 
			c: status of each thread (waiting or URL it is currently handling)
			d: a button that shuts down the server 
	 */
    final HttpRequestHandler controlHandler = new HttpRequestHandler() {
    	public void handle(HttpRequest request, HttpResponse response) {
			String threadsStatus = threads.getThreadsStatus(request.path);

			String msg = ("<!DOCTYPE html><html><body>" +
					"<h1>Student Information</h1><p>Name:  James Jin Park</p>" +
					"<p>SEAS login:  jamespj</p>" +
					"<h1>Server Information</h1>" +
					"<p>" + threadsStatus + "</p>" +					
					"<button onclick=\"location.href='/shutdown'\">Shutdown Server</button>" +					
					"</body></html>" );
			response.headers.put("Content-Type", "text/html");
			response.setBody(msg);			            
        }
    };    
    
    final private Map<Route, HttpRequestHandler> routes;

    final private HttpRequestParser httpRequestParser = new DefaultRequestParser();

    public HttpServer(int port, Path rootDir, Map<Route, HttpRequestHandler> routes) {
        this.port = port;
        this.rootDir = rootDir;
        this.routes = routes;
    }

    public void runServer() {

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            serverSocket.setSoTimeout(200); // timeout so we can periodically check flag

            while (!ShutdownHook.isShutdown.get()) {
                try {
                    final Socket socket = serverSocket.accept();

                    Runnable runnable = new Runnable() {
                    	
                        @Override
                        public void run() {
                            try ( //all these are closeable by putting them in as arguments for try()

                                  InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                                  BufferedReader in = new BufferedReader(reader);

                                  PrintWriter out = new PrintWriter(socket.getOutputStream());
                            ) {

                                HttpRequest request = httpRequestParser.parse(in);
                                HttpResponse response = new HttpResponse();

                            	System.out.println("Here is the request: \n" + request + "\n\nEnd of Transmission\n");
                                
                                Route requestedRoute = null;
                                HttpRequestHandler handler = null;
                                //complicated path
                                String HTMLregex =  "([^\\s]+(\\.(?i)(html))$)";
                                String ImageRegex = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|))$)";
                                Matcher htmlFinder = Pattern.compile(HTMLregex).matcher(request.path);
                                Matcher imageFinder = Pattern.compile(ImageRegex).matcher(request.path);
                                
                                File f = new File(rootDir + "/" + request.path);
                				
                                if(htmlFinder.find() && f.isFile()){
                                	System.out.println("Successfully found html");
                    			        requestedRoute = Route.of(request.method, "html");
		                                handler = routes.get(requestedRoute);   
                                }else if (imageFinder.find() && f.isFile()){
                                	System.out.println("Successfully found image");
                                	requestedRoute = Route.of(request.method, "image");
	                                handler = routes.get(requestedRoute);   
                                }else if (f.isDirectory()){
                                	System.out.println("Successfully found directory");
                                	requestedRoute = Route.of(request.method, "directory");
	                                handler = routes.get(requestedRoute);   
                                }else{ //root, special URLs
                                	requestedRoute = Route.of(request.method, request.path);
	                                handler = routes.get(requestedRoute);                                	
                                }
                            	
                            	if (requestedRoute.method.equals(Route.of("GET", "/control").method)&& 
                            			requestedRoute.path.equals(Route.of("GET", "/control").path)
                            		){
                                	handler = controlHandler;
                                }

                                if (handler == null) {
                                    handler = notFoundHandler;
                                }

                                handler.handle(request, response);
                                out.println("HTTP/1.1 " + response.getStatus());
                                System.out.println("HTTP/1.1 " + response.getStatus());//DELETE
                                for (Map.Entry<String, Object> header : response.headers.entrySet()) {
                                    out.println(header.getKey() + ": " + header.getValue());
                                    System.out.println(header.getKey() + ": " + header.getValue());//DELETE
                                }

                                if (response.getBody() != null && response.getBody().length() > 0) {
                                	System.out.println("Sending strings");
                                	out.println("");
                                    out.println(response.getBody());
                                }

                                if (response.getBytes() != null){
                                	System.out.println("Sending bytes");
                                	BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
                                	output.write('\n');
                                	output.write(response.getBytes());
                                	output.flush();
                                	output.close();
                                }
                                out.flush();

                                System.out.println("Here is the response: \n" + response.getBytes() + "\n\nEnd of Transmission\n");

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    threads.run(runnable);//adds this instance of runnable (request) to the queue of the thread pool
                } catch (SocketTimeoutException ste) {
                    // fine poll flag; should be timing out 
                }
            }
            serverSocket.close();
            threads.killAllThreads();

        } catch (Exception e) {
            System.err.println("Error! Could not connect to socket:");
            e.printStackTrace();
            System.exit(1); //server shuts down due to error
        }
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
        System.out.println("Server safely shut down.");
    }
}