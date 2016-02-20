package myServlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class CookieServlet2 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(1);
		Cookie[] cookies = request.getCookies();
		System.out.println(2);

		Cookie c = null;
		System.out.println(3);
		for (int i = 0; i < cookies.length; ++i) {
			System.out.println(3.1);

			if (cookies[i].getName().equals("TestCookie")) {
				System.out.println(3.2);
				c = cookies[i];
			}
			System.out.println(3.3);
		}
		
		System.out.println(4);

		if (c != null) {
			c.setMaxAge(0);
			response.addCookie(c);
		}
		System.out.println(5);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Cookie Servlet 2</TITLE></HEAD><BODY>");
		if (c == null) {
			out.println("<P>Couldn't retreive value for cookie with name 'TestCookie'.</P>");
		} else {
			out.println("<P>Retrieved value '" + c.getValue() + "' from cookie with name 'TestCookie'.</P>");
			out.println("<P>Deleted cookie (TestCookie,54321) in response.</P>");
		}
		out.println("<P>Continue to <A HREF=\"cookie3\">Cookie Servlet 3</A>.</P>");
		out.println("</BODY></HTML>");
	}
}

