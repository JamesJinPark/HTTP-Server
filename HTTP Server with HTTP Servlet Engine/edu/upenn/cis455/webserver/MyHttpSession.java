package edu.upenn.cis455.webserver;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

/**
 * @author James Park
 * @class cis455/555
 * An implementation of the HttpSession interface
 *
 */
public class MyHttpSession implements HttpSession{

	private boolean m_valid = true;
	private Properties m_properties = new Properties();
	public int max_inactive_interval = 0;
	private String id;
	private MyServletContext context;

	/**
	 * Constructor for MyHttpSession
	 * generates a random number for the session id
	 */
	public MyHttpSession(MyServletContext context) {
		this.context = context;
		Random rand = new Random();
		this.id = String.valueOf(rand.nextInt(1000000 - 1));
	}
	
	@Override
	public Object getAttribute(String arg0) {
		return m_properties.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		return m_properties.keys();
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.max_inactive_interval;
	}

	@Override
	public ServletContext getServletContext() {
		return this.context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		return m_properties.get(arg0);
	}

	@Override
	public String[] getValueNames() {
		Object[] objects = m_properties.values().toArray();
		String[] strings = new String[objects.length];
		int i = 0;
		for (Object object: objects){
			strings[i] = object.toString();
			i++;
		}
		return strings;		
	}

	@Override
	public void invalidate() {
		m_valid = false;		
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		m_properties.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(String arg0) {
		m_properties.remove(arg0);		
	}

	@Override
	public void removeValue(String arg0) {
		m_properties.remove(arg0);
	}
	
	@Override
	public void setAttribute(String arg0, Object arg1) {
		m_properties.put(arg0, arg1);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.max_inactive_interval = arg0;
	}

	public boolean isValid() {
		return m_valid;
	}

}
