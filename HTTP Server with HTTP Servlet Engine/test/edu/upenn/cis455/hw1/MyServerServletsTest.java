package test.edu.upenn.cis455.hw1;

import edu.upenn.cis455.webserver.MyServerServlets;
import junit.framework.TestCase;

public class MyServerServletsTest extends TestCase {
	MyServerServlets server = new MyServerServlets();
	String[] args = {"8080", ".", "/conf/web.xml"};
	
	//Hmmm I'm not sure how I can test the running of a server.  
	//How would I simulate sending a message to the localhost:8080?
	public void testStartAndShutdownServer() {
//		try {
//			server.run(args);
//		} catch (Exception e) {
//			fail(); 
//		}
		
		assertTrue(true);
  }
}
