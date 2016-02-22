package test.edu.upenn.cis455.hw1;


import java.util.Enumeration;

import junit.framework.TestCase;

import edu.upenn.cis455.webserver.MyServletConfig;
import edu.upenn.cis455.webserver.MyServletContext;

public class MyServletConfigTest extends TestCase{
	String name = "testConfig";
	MyServletContext context = new MyServletContext();
	MyServletConfig config = new MyServletConfig(name, context);

	public void testGetServletContext() {
		assertTrue(config.getServletContext().equals(context));
	}

	public void testGetServletConfigName() {
		assertTrue(config.getServletName().equals(name));
	}
	@SuppressWarnings("rawtypes")
	public void testGetConfigInitParams() {
		config.setInitParam("TEST", "1");
		assertTrue(config.getInitParameter("TEST").equals("1"));
		Enumeration keys = config.getInitParameterNames();
		assertTrue(keys.hasMoreElements());//initial parameter keys are not empty
		assertTrue(keys.nextElement().equals("TEST"));//initial parameter key is "TEST" 		
	}

}
