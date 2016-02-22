package test.edu.upenn.cis455.hw1;

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;
import edu.upenn.cis455.webserver.MyServletContext;

import org.junit.Test;

public class MyServletContextTest extends TestCase {
	MyServletContext context = new MyServletContext();

	public void testGetContext() {
		assertTrue(context.getContext("TEST").equals(this.context));
	}

	/**
	 * Tests setAttribute, getAttribute, getAttributeNames
	 */
	public void testGetAttributes() {
		context.setAttribute("TEST", 1);
		
		assertTrue(context.getAttribute("TEST").equals(1));
		Enumeration keys = context.getAttributeNames();
		assertTrue(keys.hasMoreElements()); //getAttributes returns a non-empty collection of keys
		assertTrue(keys.nextElement().equals("TEST"));
	}
	
	

}
