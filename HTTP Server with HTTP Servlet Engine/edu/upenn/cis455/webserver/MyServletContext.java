package edu.upenn.cis455.webserver;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

/**
 * @author James Park
 * @class cis455/555
 * An implementation of the ServletContext interface
 * Contains HashMaps for all servlets described in web.xml and for all sessions created 
 */
public class MyServletContext implements ServletContext{
	public HashMap<String,HttpServlet> servlets;
	public HashMap<String,MyHttpSession> sessions; //key is session Id

	private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	public String contextName;

	/**
	 * Initializes hashmaps for servlets, http sessions, initial parameters, and attributes
	 */
	public MyServletContext(){
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
		servlets = new HashMap<String,HttpServlet>();
		sessions = new HashMap<String, MyHttpSession>();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 * returns attribute using name as key
	 */
	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 * returns a collection of attribute keys
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 * Returns this instance of ServletContext 
	 */
	@Override
	public ServletContext getContext(String arg0) {
		return this;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 * returns an initial parameter written in the web.xml document based on the name
	 */
	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	/**
	 * @param name
	 * @param value
	 * sets initial parameters using a name as the key
	 */
	public void setInitParam(String name, String value) {
		initParams.put(name, value);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 * retuns a collection of initial parameter keys or names
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	@Override
	public int getMajorVersion() {
		return 2;
	}

	@Override
	public String getMimeType(String arg0) {
		return null;
	}

	@Override
	public int getMinorVersion() {
		return 4;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		return null; //I'm still not clear what this does...
	}

	@Override
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Set getResourcePaths(String arg0) {
		return null;
	}

	@Override
	public String getServerInfo() {
		return "James's Servlet Container";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 * returns servlet using servlet name as key
	 */
	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		return servlets.get(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletContextName()
	 * returns the name of this context
	 */
	@Override
	public String getServletContextName() {
		return this.contextName;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServletNames()
	 * returns a collection of all servlet names
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getServletNames() {
		return (Enumeration) servlets.keySet();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContext#getServlets()
	 * returns a collection of servlets
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getServlets() {
		return (Enumeration) servlets;
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
