package edu.upenn.cis455.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author James Park
 * @class cis455/555
 * An implementation of the HttpServletRequest interface
 *
 */
public class MyHttpServletRequest implements HttpServletRequest {
	
	private String version; 	//HTTP version 
	private String m_body;
	private Map<String, String> m_headers = new HashMap<String, String>();;
	private Properties m_params = new Properties();
	private Properties m_props = new Properties();
	private MyHttpSession m_session;
	private String m_method;
	public int port;
	private String queryString;
	private StringBuffer requestURL;
	private String requestURI;
	public MyServletContext context;
	
	public MyHttpServletRequest() { }
	
	/**
	 * @param MyHttpSession session
	 * @param in port
	 * @param MyServletContext context
	 * Constructor for MyHttpServletRequest
	 */
	public MyHttpServletRequest(MyHttpSession session, int port, MyServletContext context) {
		this.m_session = session;
		this.port = port; 
		this.context = context;
	}
	
	@Override
	public Object getAttribute(String arg0) {
		return m_props.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		return m_props.keys();
	}

	@Override
	public String getCharacterEncoding() {
		return "ISO-8859-1";
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getContentLength()
	 * returns content lenght from headers.  If no header, returns -1.
	 */
	@Override
	public int getContentLength() {
		if(m_headers.get("Content-Length") != null){
			return Integer.valueOf(m_headers.get("Content-Length"));
		}
		return -1;
	}

	@Override
	public String getContentType() {
		return m_headers.get("Content-Type");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return "127.0.0.1";
	}

	@Override
	public String getLocalName() {
		return "localhost";
	}

	@Override
	public int getLocalPort() {
		return port;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getLocales() {
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		return m_params.getProperty(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap() {
		return m_params;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getParameterNames() {
		return m_params.keys();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 * returns a String array of the values in parameters
	 */
	@Override
	public String[] getParameterValues(String arg0) {
		Object[] objects = m_params.values().toArray();
		String[] strings = new String[objects.length];
		int i = 0;
		for (Object object: objects){
			strings[i] = object.toString();
			i++;
		}
		return strings;
	}

	@Override
	public String getProtocol() {
		return this.version;
	}

	public void setProtocol(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequest#getReader()
	 * returns a reader to this request body
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		BufferedReader reader = new BufferedReader(new StringReader(this.m_body));
		return reader;
	}

	@Override
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getScheme() {
		return "http";
	}

	@Override
	public String getServerName() {
		return "James's Servlet Container";
	}

	@Override
	public int getServerPort() {
		return port;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		m_props.remove(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		m_props.put(arg0, arg1);		
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {		
	}

	@Override
	public String getAuthType() {
		return "BASIC_AUTH('BASIC')";
	}

	@Override
	public String getContextPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 * returns an array of Cookies.  parses strings in the headers to form key/value pairs for cookies
	 */
	@Override
	public Cookie[] getCookies() {
		String cookiesString = m_headers.get("Cookie");
		if(cookiesString == null){
			return null;
		}
		//parse cookies
		//e.g.: NAME1=OPAQUE_STRING1; NAME2=OPAQUE_STRING2
		String[] cookiesStrings = cookiesString.split("\\;"); 
		Cookie[] cookies = new Cookie[cookiesStrings.length];
		for (int i = 0 ; i< cookiesStrings.length; i++){
			String temp[] = cookiesStrings[i].trim().split("\\?|&|="); 
			cookies[i] = new Cookie(temp[0], temp[1]);
		}
		return cookies;
	}

	@Override
	public long getDateHeader(String arg0) {
		return (long) Integer.valueOf(m_headers.get(arg0));
	}

	@Override
	public String getHeader(String arg0) {
		return m_headers.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaderNames() {
		return (Enumeration) m_headers.keySet();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getHeaders(String arg0) {
		return (Enumeration)m_headers.values();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 * gets header that holds a string that must be converted to an int
	 */
	@Override
	public int getIntHeader(String arg0) {
		if(this.m_headers.get(arg0) == null){
			return -1;
		} else{
			return Integer.valueOf(this.m_headers.get(arg0));
		}
	}

	@Override
	public String getMethod() {
		return m_method;
	}

	@Override
	public String getPathInfo() {
		return '/' + this.requestURI;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return this.queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return this.requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		return this.requestURL;
	}

	public void setRequestURL(StringBuffer requestURL) {
		this.requestURL = requestURL;
	}

	@Override
	public String getRequestedSessionId() {
		Cookie[] cookies = getCookies();
		if(cookies != null){
			for (Cookie cookie: cookies){
				if (cookie.getName().equals("JSESSIONID")){
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public String getServletPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 * returns a session if client sent a JSESSIONID cookie.  else returns a new session if set to true.
	 * returns null if set to false unless session already exists.
	 */
	@Override
	public HttpSession getSession(boolean bool) {
		
		//find session
		Cookie[] cookies = getCookies();
		if(cookies != null){
			for(int i = 0; i < cookies.length; i++){
				if(cookies[i].getName().equals("JSESSIONID")){
					String id = cookies[i].getValue();
					MyHttpSession previous_session = context.sessions.get(id);
					if (previous_session != null){
						this.m_session = previous_session;
					}
				}
			}
		}		
		
		if (bool) { 
			if (!hasSession()) {
				this.m_session = new MyHttpSession(this.context);
			} 
		} else {
			if (!hasSession()) {
				this.m_session = null;
			}
		}
		return this.m_session;
	}
	
	public HttpSession getSession() {
		return getSession(true); 
	}
	
	boolean hasSession() {
		if(m_session != null){
		}
		return ((m_session != null) && m_session.isValid());
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		Cookie[] cookies = getCookies();
		if(cookies != null){
			for (Cookie cookie: cookies){
				if (cookie.getName().equals("JSESSIONID")){
					MyHttpSession session = context.sessions.get(cookie.getValue());
					if(session != null){
						return session.isValid();
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}
	
	public void setMethod(String method) {
		m_method = method;
	}
	
	public void setParameter(String key, String value) {
		m_params.setProperty(key, value);
	}
	
	public void clearParameters() {
		m_params.clear();
	}
	
	public void setHeaders(String key, String value) {
		m_headers.put(key, value);
	}
	
	public void setBody(String body) {
		m_body = body;
	}
	
	public String getBody(){
		return m_body;
	}

}