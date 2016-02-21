package test.edu.upenn.cis455.hw1;

import edu.upenn.cis455.webserver.MyServerServlets;
import junit.framework.TestCase;

public class MyServerServletsTest extends TestCase {
	MyServerServlets server = new MyServerServlets();
	String[] args = {"8080", ".", "/conf/web.xml"};
	
	public void testStartAndShutdownServer() {
		try {
			server.run(args);
		} catch (Exception e) {
			e.printStackTrace();
			fail(); 
		}
		
		assertTrue(true);
  }
}
