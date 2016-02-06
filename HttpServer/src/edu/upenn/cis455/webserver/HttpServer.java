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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.*;

public class HttpServer {

    final private int port;

    final private Path rootDir; 

	final ThreadPool threads = new ThreadPool(20);//creates threadpool of 100 threads and BlockingQueue of size 100;

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
    
    
    
	//Special URL shutdown
    final HttpRequestHandler shutdownHandler = new HttpRequestHandler() {
	    public void handle(HttpRequest request, HttpResponse response){
			String msg = ("Shutting down server.");
			System.out.println(msg);
			response.setBody(msg);			
			ShutdownHook.shutdown();
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

        try (ServerSocket serverSocket = new ServerSocket(this.port, 100)) {
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
                                String absoluteSpecialPath = null;
                                if(request.headers.get("Host") != null){
                                	absoluteSpecialPath = ("http://" + request.headers.get("Host"));
                                } 
                            	System.out.println("Here is the request: \n" + request + "\n\nEnd of Transmission\n");
                            	                            	
                                Route requestedRoute = null;
                                HttpRequestHandler handler = null;

                            	if(request.method.toUpperCase().equals("HEAD") && !request.path.equals("/shutdown") && 
                            			!request.path.equals(absoluteSpecialPath + "/shutdown") && 
                            			!request.path.equals(absoluteSpecialPath + "/control") &&
                            			!request.path.equals("/control") ){
                            		request.isHead = true;
                            		request.method = "GET";
                            	} 
                            	
                            	if (request.method.toUpperCase().equals("HEAD") && request.path.equals("/shutdown") ||
                            			request.path.equals(absoluteSpecialPath + "/shutdown") || 
                            			request.path.equals(absoluteSpecialPath + "/control") ||
                            			request.path.equals("/control")){
                			        requestedRoute = Route.of(request.method, "405Error");
                			        handler = routes.get(requestedRoute);   
                            	} 
                            	
                                //complicated path
                                String HTMLregex =  "([^\\s]+(\\.(?i)(html))$)";
                                String ImageRegex = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|))$)";
                                String pdfRegex =  "([^\\s]+(\\.(?i)(pdf))$)";
                                String relativePathRegex = "([^\\s]+(\\.(?i)(.))$)";
                                Matcher htmlFinder = Pattern.compile(HTMLregex).matcher(request.path);
                                Matcher imageFinder = Pattern.compile(ImageRegex).matcher(request.path);
                                Matcher pdfFinder = Pattern.compile(pdfRegex).matcher(request.path);
                                Matcher relativePathFinder = Pattern.compile(relativePathRegex).matcher(request.path);
                                
                                if(relativePathFinder.find()){
                                	request.path = request.path.substring(0, request.path.lastIndexOf('/'));
                                }
                                File f = new File(rootDir + "/" + request.path);
                                String absolutePath = null;
                                if(request.headers.get("Host") != null){
                                	request.path.replace("http://", "").replace(request.headers.get("Host"), "");
                                }
                                File absoluteFile = new File(rootDir + absolutePath);
                                
                                if(htmlFinder.find() && f.isFile()){
                                	System.out.println("Successfully found html");
                    			    requestedRoute = Route.of(request.method, "html");
		                            handler = routes.get(requestedRoute);   
                                }else if (htmlFinder.find() &&  absoluteFile.isFile()){
                                	System.out.println("Successfully found html");
                                	request.path = absolutePath;
                    			    requestedRoute = Route.of(request.method, "html");
		                            handler = routes.get(requestedRoute);   
                                }else if (imageFinder.find() && f.isFile()){
                                	System.out.println("Successfully found image");
                                	requestedRoute = Route.of(request.method, "image");
	                                handler = routes.get(requestedRoute);   
                                }else if (imageFinder.find() && absoluteFile.isFile()){
                                	System.out.println("Successfully found image");
                                	request.path = absolutePath;
                                	requestedRoute = Route.of(request.method, "image");
	                                handler = routes.get(requestedRoute);   
                                }else if (f.isDirectory()){
                                	System.out.println("Successfully found directory");
                                	requestedRoute = Route.of(request.method, "directory");
	                                handler = routes.get(requestedRoute);   
                                }else if (absoluteFile.isDirectory()){
                                	System.out.println("Successfully found directory");
                                	request.path = absolutePath;
                                	requestedRoute = Route.of(request.method, "directory");
	                                handler = routes.get(requestedRoute);   
                                }else if (pdfFinder.find() && f.isFile()){
                                	System.out.println("Successfully found pdf file.");
                                	requestedRoute = Route.of(request.method, "pdf");
	                                handler = routes.get(requestedRoute);   
                                }else if (pdfFinder.find() && absoluteFile.isFile()){
                                	System.out.println("Successfully found pdf file.");
                                	request.path = absolutePath;
                                	requestedRoute = Route.of(request.method, "pdf");
	                                handler = routes.get(requestedRoute);   

                                }else if (f.isFile()){
                                	System.out.println("Treat like text file.");
                                	requestedRoute = Route.of(request.method, "plainText");
	                                handler = routes.get(requestedRoute);   
                                }else if (absoluteFile.isFile()){
                                	System.out.println("Treat like text file.");
                                	request.path = absolutePath;
                                	requestedRoute = Route.of(request.method, "plainText");
	                                handler = routes.get(requestedRoute);   
                                }else{ //root, special URLs
                                	requestedRoute = Route.of(request.method, request.path);
	                                handler = routes.get(requestedRoute);                                	
                                }
                            	if ((requestedRoute.method.equals(Route.of("GET", "/control").method)&& 
                            			requestedRoute.path.equals(Route.of("GET", "/control").path)) || 
                            			((requestedRoute.method.equals(Route.of("GET", absolutePath + "/control").method)&& 
                                    			requestedRoute.path.equals(Route.of("GET", absolutePath + "/control").path)))
                            		){
                                	handler = controlHandler;
                                }
                            	
                            	//shutdown handler
                            	if ((requestedRoute.method.equals(Route.of("GET", "/shutdown").method)&& 
                            			requestedRoute.path.equals(Route.of("GET", "/shutdown").path)) || 
                            			((requestedRoute.method.equals(Route.of("GET", absoluteSpecialPath + "/shutdown").method)&& 
                                    			requestedRoute.path.equals(Route.of("GET", absoluteSpecialPath + "/shutdown").path)))
                            		){
                                	handler = shutdownHandler;
                                }                            	

                            	if(request.headers.get("Host") == null && request.version.equals("HTTP/1.1")){
                                	requestedRoute = Route.of("GET", "400Error");
	                                handler = routes.get(requestedRoute);
                            	}
                            	if(!request.method.equals("GET") || request.method.equals("HEAD")){
                                	requestedRoute = Route.of("GET", "501Error");
                                	handler = routes.get(requestedRoute);                            		
                            	}

                            	if (handler == null) {
                                	requestedRoute = Route.of("GET", "404Error");
                                	request.isError = true;
                                	handler = routes.get(requestedRoute);                                	
                                } else {//handler is not null but we need to check if-modified header	
	                            	if(request.headers.get("If-Modified-Since") != null && 
	                            			request.version.equals("HTTP/1.1")){
	                            		if (request.method.equals("GET")){
	                            			File file = new File("." + request.path);
	                            			boolean tempIsModified = false;
											try {
												tempIsModified = httpRequestParser.checkDate(request.headers.get("If-Modified-Since"), file);
											} catch (ParseException e) {
												e.printStackTrace();
											}
	                            			if(!tempIsModified){
	                                        	requestedRoute = Route.of("GET", "304Error"); //Not modified error. sets isModified to false
	    	                                	handler = routes.get(requestedRoute);                            			
	                            			}
	                            		} else {//If-Modified-Since does not work methods other than "GET"
	                                    	requestedRoute = Route.of("GET", "501Error"); 
		                                	handler = routes.get(requestedRoute);                            			
	                            		}
	                            	}
	
	                            	if(request.headers.get("If-Unmodified-Since") != null && 
	                            			request.version.equals("HTTP/1.1")){
	                            		File file = new File("." + request.path);
                            			boolean tempIsModified = false;
										try {
											tempIsModified = httpRequestParser.checkDate(request.headers.get("If-Unmodified-Since"), file);
										} catch (ParseException e) {
											e.printStackTrace();
										}
										System.out.println("Testing!" + tempIsModified);
										if(tempIsModified){
                                        	requestedRoute = Route.of("GET", "412Error"); //Not modified error. sets isModified to false
    	                                	handler = routes.get(requestedRoute);                            														
										}
	                                }
                                }
                            	
                                handler.handle(request, response);
                                if(request.version.equals("HTTP/1.1")){
                                	out.println("HTTP/1.1 100 Continue");
                                	out.println("");
                                }
                                out.println("HTTP/1.1 " + response.getStatus()); //Response includes HTTP version
                                final Date currentTime = new Date();
                                final SimpleDateFormat dateTemplate = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss");
                                dateTemplate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                
                                if(!request.isError){ //if the response is not error response
	                                out.println("Date: " + dateTemplate.format(currentTime) + " GMT");//Response includes date
	                                out.println("Server: James's HTTP Server"); //Response includes host
	                                System.out.println("HTTP/1.1 " + response.getStatus());//DELETE
                                }
                                if(!request.isModified){//should only get in here if the document IS NOT MODIFIED or FALSE
	                                out.println("Date: "+dateTemplate.format(currentTime) + " GMT");//Response includes date                                	
	                                out.println("");
                                }
                                if(!request.isModified){//should only get in here if the document IS NOT MODIFIED or FALSE
	                                out.println("");
                                }

                                for (Map.Entry<String, Object> header : response.headers.entrySet()) {
                                    out.println(header.getKey() + ": " + header.getValue());
                                    System.out.println("test " + header.getKey() + ": " + header.getValue());//DELETE
                                }
                                if(!request.isHead || request.isModified){//if the request was not a HEAD method and is modified
	                                if (response.getBody() != null && response.getBody().length() > 0) {
	                                	System.out.println("Sending strings");
	                                	out.println("");
	                                    out.println(response.getBody());
	                                    out.flush();
	                                }
	
	                                if (response.getMyBytes() != null){
	                                	System.out.println("Sending bytes");
	                                	BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());

	                                	output.write(("HTTP/1.1 " + response.getStatus()).getBytes()); //Response includes HTTP version
	                                	output.write("\r\n".getBytes());
	                                	output.write(("Date: "+dateTemplate.format(currentTime) + " GMT").getBytes());//Response includes date
	                                	output.write("\r\n".getBytes());
	                                	output.write("Server: James's HTTP Server".getBytes()); //Response includes host
		                                for (Map.Entry<String, Object> header : response.headers.entrySet()) {
		                                	output.write("\r\n".getBytes());
		                                    output.write((header.getKey() + ": " + header.getValue()).getBytes());
		                                }
	                                	output.write("\r\n".getBytes());
	                                	output.write("\r\n".getBytes());
	                                	output.write(response.getMyBytes(), 0, response.getMyBytes().length);
	                                	output.flush();
	                                	output.close();
	                                }
                            	} else{
                                    out.flush();                            		
                            	}
                                out.println("Connection: close"); //Connection automatically closes. No persistent connection.
                                if(request.headers.get("Connection").equals("close")){
                                	//currently do nothing.  
                                }

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
            threads.killAllThreads();
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Error! Could not connect to socket:");
            e.printStackTrace();
            System.exit(1); //server shuts down due to error
        }
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
    	int count = 1; 
        while(threads.howManyThreadsAlive() > 0 ){
        	if (count < 6){
        		System.out.println("Waiting on " + threads.howManyThreadsAlive() + " thread(s) to exit. " + "Loop #" + count);
        	} else {
        		System.out.println("Manually shutting down thread(s).");
        		threads.emergencyShutdown();
        		break;
        	}
			try {
				count += 1;
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}        	
        }
        System.out.println("Server shut down.");
    }
}