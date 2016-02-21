package edu.upenn.cis455.webserver;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.servlet.*;

/**
 * @author James Park
 * @class cis455/555
 * An implementation of the ServletConfig interface
 * Contains a reference to the ServletContext that exists in the web application
 */
public class MyServletConfig implements ServletConfig{
	private String name;
	private MyServletContext context;
	private HashMap<String,String> initParams;
	
	/**
	 * @param String name
	 * @param MyServletContext context
	 */
	public MyServletConfig(String name, MyServletContext context) {
		this.name = name;
		this.context = context;
		initParams = new HashMap<String,String>(); //grabs initial params from web.xml document
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
	 * returns initial parameters taken from web.xml document
	 */
	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getInitParameterNames()
	 * returns a collection of initial parameter keys 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletConfig#getServletContext()
	 * returns the Servlet Context of the current web application
	 */
	@Override
	public ServletContext getServletContext() {
		return context;
	}

	@Override
	public String getServletName() {
		return name;
	}
	
	void setInitParam(String name, String value) {
		initParams.put(name, value);
	}
}
