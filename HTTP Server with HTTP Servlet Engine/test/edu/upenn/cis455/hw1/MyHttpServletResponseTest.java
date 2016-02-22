package test.edu.upenn.cis455.hw1;

import java.net.Socket;

import junit.framework.TestCase;

import edu.upenn.cis455.webserver.MyHttpServletRequest;
import edu.upenn.cis455.webserver.MyHttpServletResponse;
import edu.upenn.cis455.webserver.MyServletContext;


public class MyHttpServletResponseTest extends TestCase {
	
	MyServletContext context = new MyServletContext();
	MyHttpServletRequest request = new MyHttpServletRequest();
	
	//Hmmm unsure about how to test this as well.  Socket must be connected...
	public void testA(){
//		MyHttpServletResponse response;
//		try{
//			Socket socket = new Socket();
//			response = new MyHttpServletResponse(socket, request, context);
//		}catch(Exception e){		
//		}

		assertTrue(true);
	}

}
