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
		
		//ExtraServlet0		
//		response.addHeader("H1", "value1");
//		PrintWriter p = response.getWriter();
//		response.resetBuffer();
//		p.println("hey");
//		boolean flag = false;
//		try{
//			response.reset();
//		}
//		catch(IllegalStateException e){
//			p.println("PASSED: Buffer test");
//			flag = true;
//		}
//	
//		if(flag == false){
//			p.println("FAILED: Buffer test - can't reset buffer after you write in body");
//		}
//		p.flush();
//		p.close();
	}
}