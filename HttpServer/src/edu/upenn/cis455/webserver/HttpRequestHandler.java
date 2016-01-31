package edu.upenn.cis455.webserver;

import java.io.*;

public interface HttpRequestHandler{ 
	//this is interface because HttpRequestHandler only describes behavior and does not hold state.
	//we don't want to hold onto state because then it's more difficult to reason about code.
	//no assumptions are required for interfaces that you're using.	
	void handle(HttpRequest request, HttpResponse response);
}