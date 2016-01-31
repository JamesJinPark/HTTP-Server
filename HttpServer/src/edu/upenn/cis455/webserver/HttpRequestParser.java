package edu.upenn.cis455.webserver;

import java.io.*;

public interface HttpRequestParser {
	
	public HttpRequest parse(BufferedReader in);
}
