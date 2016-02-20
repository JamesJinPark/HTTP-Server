package edu.upenn.cis455.webserver;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

public class MyHttpSession implements HttpSession{

	private boolean m_valid = true;
	private Properties m_properties = new Properties();
	private int max_inactive_interval = 0;
	private String id;
	public boolean attributesExist;

	MyHttpSession() {
		Random rand = new Random();
		this.id = String.valueOf(rand.nextInt(1000000 - 1));
		this.attributesExist = false;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		m_valid = false;		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
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
		this.attributesExist = true;
		m_properties.put(arg0, arg1);		
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		this.max_inactive_interval = arg0;
	}

	boolean isValid() {
		return m_valid;
	}

}
