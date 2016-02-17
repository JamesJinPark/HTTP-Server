package edu.upenn.cis455.webserver;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

public class MyHttpSession implements HttpSession{

	private boolean m_valid = true;
	private Properties m_properties = new Properties();

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_properties.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_properties.keys();
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return m_properties.get(arg0);
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		m_valid = false;		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		m_properties.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		m_properties.remove(arg0);		
	}

	@Override
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		m_properties.remove(arg0);
	}
	
	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		m_properties.put(arg0, arg1);		
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}

	boolean isValid() {
		return m_valid;
	}

}
