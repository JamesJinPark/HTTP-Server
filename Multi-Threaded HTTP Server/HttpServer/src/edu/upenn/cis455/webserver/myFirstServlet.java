package edu.upenn.cis455.webserver;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.ws.Response;

public class myFirstServlet extends HttpServlet{
	
	public void init() throws ServletException{
		//this is called once when the servlet is loaded into the 
		//servlet engine before the servlet is asked to process its 
		//first request
	}
	
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException{
		//called to process a request.  It can be called zero, one or many 
		//times until the servlet is unladed.  multiple threads (one/request)
		//can execute this method in parallel so it must be threadsafe
	}
	
	public void destroy(){
		//called once just before the servelt is unladed and taken out of service
		
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		resp.setContentType("type/html");
		PrintWriter out = resp.getWriter();
		
		out.println("<HTML>");
		out.println("<h1>Hello World!</h1>");
		out.println("getCharacterEncoding: " + req.getCharacterEncoding());
		out.println("getContentLength: " + req.getContentLength());
		out.println("getContentType: " + req.getContentType());
		out.println("getProtocol: " + req.getProtocol());
		out.println("getRemoteAddr: " + req.getRemoteAddr());
		out.println("getRemoteHost: " + req.getRemoteHost());
		out.println("getScheme: " + req.getScheme());
		out.println("getServerName: " + req.getServerName());
		out.println("getServerPort: " + req.getServerPort());
		out.println("getAuthType: " + req.getAuthType());
		out.println("getMethod: " + req.getMethod());
		out.println("getPathInfo: " + req.getPathInfo());
		out.println("getPathTranslated: " + req.getPathTranslated());
		out.println("getQueryString: " + req.getQueryString());
		out.println("getRemoteUser: " + req.getRemoteUser());
		out.println("getRequestURI: " + req.getRequestURI());
		out.println("getServletPath: " + req.getServletPath());
		  out.println();
		  out.println("Parameters:");
		 
		  Enumeration paramNames = req.getParameterNames();
		  while (paramNames.hasMoreElements()) {
		    String name = (String) paramNames.nextElement();
		    String[] values = req.getParameterValues(name);
		    out.println("    " + name + ":");
		    for (int i = 0; i < values.length; i++) {
		      out.println("      " + values[i]);
		    }
		  }

		  out.println();
		  out.println("Request headers:");
		  Enumeration headerNames = req.getHeaderNames();
		  while (headerNames.hasMoreElements()) {
		    String name = (String) headerNames.nextElement();
		    String value = req.getHeader(name);
		    out.println("  " + name + " : " + value);
		  }

		  out.println();
		  out.println("Cookies:");
		  Cookie[] cookies = req.getCookies();
		  for (int i = 0; i < cookies.length; i++) {
		    String name = cookies[i].getName();
		    String value = cookies[i].getValue();
		    out.println("  " + name + " : " + value);
		  }

		  // Print the HTML footer
		  out.println("</PRE></BODY></HTML>");
		  out.close();
		}
		
		
	}

