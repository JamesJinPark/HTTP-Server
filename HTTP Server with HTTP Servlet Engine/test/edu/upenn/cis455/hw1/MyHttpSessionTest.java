package test.edu.upenn.cis455.hw1;

import junit.framework.TestCase;

import org.junit.Test;

import edu.upenn.cis455.webserver.MyHttpSession;
import edu.upenn.cis455.webserver.MyServletContext;

public class MyHttpSessionTest extends TestCase{
	MyServletContext context = new MyServletContext();
	MyHttpSession session = new MyHttpSession(context);
	
	public void testisValidandInvalidate() {
		assertTrue(session.isValid());
		session.invalidate();
		assertFalse(session.isValid());
	}

	public void testGetId() {
		String temp = session.getId();
		assertTrue(temp != null);
		int num = Integer.valueOf(temp); //no error thrown is sign that successfully got int from string
	}
	
	public void testSetMaxInactiveInterval() {
		session.setMaxInactiveInterval(1000);
		int num = this.session.max_inactive_interval; 
		Integer temp = new Integer(num);
		assertTrue(temp.equals(1000));		
	}

	
	public void testAttributes() {
		session.putValue("TEST", 1000);
		assertTrue(session.getAttribute("TEST").equals(1000));
		assertTrue(session.getValue("TEST").equals(1000));
		session.removeValue("TEST");
		assertFalse(session.getAttributeNames().hasMoreElements());//attributes are empty
		session.putValue("TEST", 1000);
		session.removeValue("TEST");
		assertTrue(session.getValueNames().length == 0);//values are empty
	}

}
