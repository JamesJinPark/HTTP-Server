package edu.upenn.cis455.webserver;

import java.io.BufferedReader;
import java.io.IOException;
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

public class MyHttpServletRequest implements HttpServletRequest {
	
	private String version; 	//HTTP version 
	private String m_body;
	private Map<String, String> m_headers = new HashMap<String, String>();;
	private Properties m_params = new Properties();
	private Properties m_props = new Properties();
	private MyHttpSession m_session = null;
	private String m_method;
	private int port;
	private String queryString;
	private StringBuffer requestURL;
	private String requestURI;
	
	MyHttpServletRequest() { }
	
	MyHttpServletRequest(MyHttpSession session, int port) { 
		m_session = session; 
		this.port = port; 
	}
	
	@Override
	public Object getAttribute(String arg0) {
		return m_props.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		return m_props.keys();
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		return m_params.getProperty(arg0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getParameterNames() {
		return m_params.keys();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocol() {
		return this.version;
	}

	public void setProtocol(String version) {
		this.version = version;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

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
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean bool) {
		if (bool) { //what is bool here? 
			if (!hasSession()) {
				this.m_session = new MyHttpSession();
			} else {
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
		return ((m_session != null) && m_session.isValid());
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
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