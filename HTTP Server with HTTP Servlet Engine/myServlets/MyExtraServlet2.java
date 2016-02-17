package myServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyExtraServlet2 extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		//ExtraServlet2		
//		PrintWriter p = response.getWriter();
//		p.println("hey");
//		response.addHeader("H1", "value1");		
//		p.flush();
//		p.close();
		
		//ExtraServlet1
		response.sendError(404);
	}

}
