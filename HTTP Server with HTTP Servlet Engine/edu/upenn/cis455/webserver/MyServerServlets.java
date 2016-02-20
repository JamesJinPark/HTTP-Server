package edu.upenn.cis455.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author James Park
 * @class cis455/555
 * Application specific logic for Servlet Engine (Servlet Container)
 */
public class MyServerServlets {	
	static class Handler extends DefaultHandler {
	
		private int m_state = 0;
		private String m_servletName;
		private String m_paramName;
		HashMap<String,String> m_servlets = new HashMap<String,String>();
		HashMap<String,String> m_contextParams = new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> m_servletParams = new HashMap<String,HashMap<String,String>>();
		public String displayName;
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.compareTo("servlet-name") == 0) {
				m_state = 1;
			} else if (qName.compareTo("servlet-class") == 0) {
				m_state = 2;
			} else if (qName.compareTo("context-param") == 0) {
				m_state = 3;
			} else if (qName.compareTo("init-param") == 0) {
				m_state = 4;				
			} else if (qName.compareTo("display-name") == 0) {
				m_state = 5;
			} else if (qName.compareTo("param-name") == 0) {
				m_state = (m_state == 3) ? 10 : 20;
			} else if (qName.compareTo("param-value") == 0) {
				m_state = (m_state == 10) ? 11 : 21;
			}
		}
		
		public void characters(char[] ch, int start, int length) {
			String value = new String(ch, start, length);
			if (m_state == 1) {
				m_servletName = value;
				m_state = 0;
			} else if (m_state == 2) {
				m_servlets.put(m_servletName, value);
				m_state = 0;
			} else if (m_state == 5) {
				this.displayName = value;
				m_state = 0;
			} else if (m_state == 10 || m_state == 20) {
				m_paramName = value;
			} else if (m_state == 11) {
				if (m_paramName == null) {
					System.err.println("Context parameter value '" + value + "' without name");
					System.exit(-1);
				}
				m_contextParams.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			} else if (m_state == 21) {
				if (m_paramName == null) {
					System.err.println("Servlet parameter value '" + value + "' without name");
					System.exit(-1);
				}
				HashMap<String,String> p = m_servletParams.get(m_servletName);
				if (p == null) {
					p = new HashMap<String,String>();
					m_servletParams.put(m_servletName, p);
				}
				p.put(m_paramName, value);
				m_paramName = null;
				m_state = 0;
			}
		}
	}
		
	private static Handler parseWebdotxml(String webdotxml) throws Exception {
		Handler h = new Handler();
		File file = new File(webdotxml);
		if (file.exists() == false) {
			System.err.println("error: cannot find " + file.getPath());
			System.exit(-1);
		}
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(file, h);
		return h;
	}
	
	private static MyServletContext createContext(Handler h) {
		MyServletContext servletContext = new MyServletContext();
		for (String param : h.m_contextParams.keySet()) {
			servletContext.setInitParam(param, h.m_contextParams.get(param));
		}
		servletContext.contextName = h.displayName;
		return servletContext;
	}
	
	private static HashMap<String,HttpServlet> createServlets(Handler h, MyServletContext fc) throws Exception {
		HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
		for (String servletName : h.m_servlets.keySet()) {
			MyServletConfig config = new MyServletConfig(servletName, fc);
			String className = h.m_servlets.get(servletName);
			Class servletClass = Class.forName(className);
			HttpServlet servlet = (HttpServlet) servletClass.newInstance();
			HashMap<String,String> servletParams = h.m_servletParams.get(servletName);
			if (servletParams != null) {
				for (String param : servletParams.keySet()) {
					config.setInitParam(param, servletParams.get(param));
				}
			}
			servlet.init(config);
			servlets.put('/' + servletName, servlet);
			servlets.put(servletName, servlet);
			servlets.put(servletName + '/', servlet);
			servlets.put('/' + servletName + '/', servlet);
		}
		
		return servlets;
	}
	
	public void run(final String[] args) throws Exception{
		if(args.length != 3){ //check number of args
			System.out.println("JamesJinPark\nSeas Login Name: jamespj");
			System.exit(1);			
		} 
		final int port = Integer.valueOf(args[0]); //port number
		final Path rootDir = Paths.get(args[1]); //get root directory
		String xmlPath = rootDir + args[2];
		
		//check whether web.xml file exists and can be accessed
		Handler h = parseWebdotxml(xmlPath);
		
		//initialize context and config
		MyServletContext context = createContext(h);
		
		final HashMap<String,HttpServlet> servlets = createServlets(h, context);
			
		final ThreadPool threads = new ThreadPool(10);//creates threadpool of 100 threads and BlockingQueue of size 100;
		
		//connect to port 
		try (ServerSocket serverSocket = new ServerSocket(port, 400)) {
			serverSocket.setSoTimeout(200); // timeout so we can periodically check flag
			System.out.println("Server started and listening for connections.");

			while (!ShutdownHook.isShutdown.get()) {
				try {
					final Socket socket = serverSocket.accept();
	    		   
					Runnable runnable = new Runnable() {
               	
						@Override
						public void run() {
							
							MyHttpSession httpSession = null;
		                	   
				            //do something with connection
				            try ( //all these are closeable by putting them in as arguments for try()
				                    InputStreamReader reader = new InputStreamReader(socket.getInputStream());
				                    BufferedReader in = new BufferedReader(reader);
				              ) { //this is tryInside
				
					    		//initialize request and response
					    		MyHttpServletRequest request = new MyHttpServletRequest(httpSession, port);
					    		MyHttpServletResponse response = new MyHttpServletResponse(socket, request);
				
					    		//parse request
								String line = in.readLine();			
								String[] parts = line.split("\\s+");
								String method = parts[0]; // e.g.: GET
								String path = parts[1]; // e.g.: demo
								String httpVersion = parts[2]; //e.g.: HTTP/1.1								

								
								request.setProtocol(httpVersion);
								
								response.version = httpVersion;								

								String[] strings = path.split("\\?|&|="); //splits path
					    		
					    		//capture query string
				    			Pattern queryPattern = Pattern.compile("(/?)(.+?)(\\?)(.*)");
				    			Matcher q = queryPattern.matcher(path);
				    			if(q.matches()){
				    				request.setQueryString(q.group(4));
									request.setRequestURL(new 
											StringBuffer(httpVersion).append(request.getServerName()).
											append(port).append(strings[0]));
									request.setRequestURI(strings[0]);									
				    			}
				    			
				    			
				    			//capture servlet if request URL has multiple '/'
				    			Pattern pattern = Pattern.compile("(/?)(.+?)(/)(.*)");
				    			Matcher m = pattern.matcher(strings[0]);

				    			if(m.matches()){
					    			strings[0] = m.group(2);
					    		} 

					    		HttpServlet servlet = servlets.get(strings[0]); //tries to find servlet
					    							    		
					    		//check whether servlet is requested or servlet exists in web.xml	
					    		if (servlet == null) {
					    			//if servlet is not requested or servlet does not exist, go to MS1 HTTPserver
					    			System.out.println("Entering Milestone1");
					    			MyServer milestone1 = new MyServer();
					    			milestone1.run(args, socket, in, line, threads);
					    		} else{
					    			//saves parameters
						    		for (int j = 1; j < strings.length - 1; j += 2) {
						    			request.setParameter(strings[j], strings[j+1]);
						    		}
						    		if (method.compareTo("GET") == 0 || method.compareTo("POST") == 0 || 
						    				method.compareTo("HEAD") == 0) { // add HEAD?
						    			request.setMethod(method);
						    			
							    		//saves all headers
						    			System.out.println("THIS IS REQUEST");
					    				while(true) {
					    					line = in.readLine();
											if (line == null || line.isEmpty()) break;
											//e.g: Host: localhost:90
											String key = null;
											String value = null;
											try{
												int separator = line.indexOf(':');
												key = line.substring(0, separator).trim(); //Host
												value = line.substring(separator + 1).trim(); //localhost:90
												System.out.println(line);
											}catch(Exception e){
												System.err.println("Could not parse header: " + line + " " + e);
											}	
											request.setHeaders(key, value);
										}

						    			
						    			if(method.compareTo("POST") == 0){				
						    				if(request.getContentType().equals("application/x-www-form-urlencoded")){
							    				//saves body
												//e.g: name1=value1&name2=value2
							    				int length = request.getContentLength();
								    			if(length > 0 ){
								    				char[] buf = new char[1024];	
								    				int count = in.read(buf, 0, length);
								    				request.setBody(String.valueOf(buf));
								    			}
											} else{
												//something else
											}
							    			if(request.getBody() != null){
							    				String[] messageParts = request.getBody().split("\\?|&|="); 
									    		for (int j = 0; j < messageParts.length - 1; j += 2) {
									    			request.setParameter(messageParts[j].trim(), messageParts[j+1].trim());
									    		}
							    			}
						    			}
						    			servlet.service(request, response);
						    			
							    		if(response.flushed == false){//if the servlet has not flushed the buffer
						    				response.getWriter().flush();						    				
						    			}
						    		} else {
						    			System.err.println("error: expecting 'GET' or 'POST', not '" + method + "'");
						    			System.exit(-1);
						    		}
						    			
						    		httpSession = (MyHttpSession) request.getSession(false);
					    		}
							
					            } catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ServletException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	   }
		                   
						}; //ends runnable
			       	threads.run(runnable);//adds this instance of runnable (request) to the queue of the thread pool
					} catch(Exception e){	
					}
			}//end of while loop
				threads.killAllThreads();
				serverSocket.close();
			} catch (Exception e) {
	            System.err.println("Error! Could not connect to socket:");
	            e.printStackTrace();
	            System.exit(-1); //server shuts down due to error
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
	       System.out.println("Successfully shut down server.");
           System.exit(0); //server shuts down correctly
	}
}
