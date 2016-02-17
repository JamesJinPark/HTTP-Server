package myServlets;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.upenn.cis455.webserver.MyHttpServletRequest;
import edu.upenn.cis455.webserver.MyHttpServletResponse;

import java.io.*;

public class DemoServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Simple Servlet</TITLE></HEAD><BODY>");
		out.println("<p>Hello James!</p>");
		out.println("</BODY></HTML>");
//		out.flush();
//		out.close();
	}
}