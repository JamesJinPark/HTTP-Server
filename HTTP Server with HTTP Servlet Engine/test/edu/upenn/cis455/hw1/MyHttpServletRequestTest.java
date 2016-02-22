package test.edu.upenn.cis455.hw1;

import javax.servlet.http.Cookie;

import edu.upenn.cis455.webserver.MyHttpServletRequest;
import edu.upenn.cis455.webserver.MyHttpSession;
import edu.upenn.cis455.webserver.MyServletContext;
import junit.framework.TestCase;

public class MyHttpServletRequestTest extends TestCase{
	MyServletContext context = new MyServletContext();
	MyHttpSession session = new MyHttpSession(context);
	int port = 8080;
	
	MyHttpServletRequest request = new MyHttpServletRequest();

	public void testHeaders() {
		request.setHeaders("Content-Type", "text/plain");
		assertTrue(request.getHeader("Content-Type").equals("text/plain"));
	}

	
	public void testMethods() {
		request.setMethod("GET");
		assertTrue(request.getMethod().equals("GET"));
	}

	public void testCookies() {
		request.setHeaders("Cookie", "test=1");
		Cookie[] cookies = request.getCookies();
		assertTrue(cookies[0].getName().equals("test"));
		assertTrue(cookies[0].getValue().equals("1"));
	}

}
