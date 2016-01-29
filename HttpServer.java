package edu.upenn.cis455.webserver;

import java.io.*;
import java.net.*;

class HttpServer {
  	
  public static void main(String[] args) throws Exception {
	  HttpServer server = new HttpServer();
	  if (args.length != 2){
		  System.out.println("James Jin Park\nSEAS login name: jamespj");
	  }
	  else {
		  server.run_server(args);
	  }
  }
  
  public void run_server(String[] args){
	  ServerSocket serverSocket = null;
	  try{
		  serverSocket = new ServerSocket(Integer.valueOf(args[0])); //args[0] contains port number //do I need to close this?
	  }catch(Exception e){
		  System.out.println("Error! Could not connect to socket: " + e);
		  System.exit(1); //server shuts down due to error		  
	  }
	  while(true){
		  try{
			  Socket socket = serverSocket.accept(); 
			  InputStreamReader reader = new InputStreamReader(socket.getInputStream());
			  BufferedReader in = new BufferedReader(reader);
			  PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	
			  String request = in.readLine(); //Request line
	
			  if(request == null){
				  out.println("HTTP/1.1 400 Error\n \n Bad Request: Missing Request Line");
				  break;
			  }		
			  String[] request_components = parser(request); //splits the Request line into their components
			  if(!request_components[0].equals("GET")){
				  out.println(request_components[2] + " 400 Error\n \n Bad Request: \""+request+"");				  
			  }
			  else if(request.equals("GET / HTTP/1.1")){
				  out.println(request_components[2] + " 200 OK\n\n<html><body>Welcome to James' Server!</body></html>\n");
				  out.println(request_components[0]);
				  out.println(request_components[1]);
				  out.println(request_components[2]);
			  }
			  else if(request.equals("GET / shutdown")){
				  System.exit(0); //server properly shuts down
			  }
			  else if(request.equals("GET / control")){
				  //must return control panel
			  }
			  else{
				  out.println(request_components[2] + " 500 Error\n \n Internal Server Error: \""+request+"");
			  }
			  out.flush();
			  socket.close();
		  }
		  catch(IOException e){
			  System.out.println("Error:" + e);
		  }
	  }
	  
  }

  //client request parser  
  public String[] parser(String request){
	  String[] request_components = request.split("\\s+");
	  //request_components[0] is type of request 
	  //request_components[1] is path
	  //request_components[2] is HTTP version number
	  return request_components;
  }
    
  //authorization checker - access monitor
  
  //logger
  
  //URL controller
    
  //response constructor
  
  //transmission to client
  
}
