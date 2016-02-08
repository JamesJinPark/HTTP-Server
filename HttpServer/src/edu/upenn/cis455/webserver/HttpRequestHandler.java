package edu.upenn.cis455.webserver;

/**
 * @author James Park
 * @class cis455/555
 * 
 * Interface for handling HTTP requests
 *
 */
public interface HttpRequestHandler{ 
	//this is interface because HttpRequestHandler only describes behavior and does not hold state.
	//we don't want to hold onto state because then it's more difficult to reason about code.
	//no assumptions are required for interfaces that you're using.	
	void handle(HttpRequest request, HttpResponse response);
}