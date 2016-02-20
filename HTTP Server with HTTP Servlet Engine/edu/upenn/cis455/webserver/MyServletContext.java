package edu.upenn.cis455.webserver;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.*;

public class MyServletContext implements ServletContext{
	private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	public String contextName;

	public MyServletContext(){
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	@Override
	public ServletContext getContext(String arg0) {
		return this;
	}

	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	public void setInitParam(String name, String value) {
		initParams.put(name, value);
	}

	@Override
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMimeType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getResourcePaths(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerInfo() {
		return "James's Servlet Container";
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletContextName() {
		return this.contextName;
	}

	@Override
	public Enumeration getServletNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getServlets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void log(String message) {
		System.err.println(message);		
	}

	@Override
	public void log(Exception e, String msg) {
		log(msg, (Throwable) e);
	}

	@Override
	public void log(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace(System.err);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

}
