package edu.upenn.cis455.webserver;

import java.io.*;

public class HttpResponse {
	public PrintWriter writer;
	public PrintStream streamer;
	public HttpResponse(PrintWriter writer){
		this.writer = writer;
	}
}
