package myServlets;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class CalculatorServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
//		if(request.getParameterMap().size() == 0){
			BufferedReader bf = new BufferedReader(new FileReader(new File("index.html")));
			int noOfByte = 0;
			char[] buf = new char[1024];
			while((noOfByte = bf.read(buf)) != -1){
				out.write(buf, 0, noOfByte);
			}
			out.flush();
			bf.close();
	//	}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int v1 = Integer.parseInt(request.getParameter("num1"));
		int v2 = Integer.parseInt(request.getParameter("num2"));
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<html><head><title>Foo</title></head>");
		out.println("<body>" + v1 + "+" + v2 + "=" + (v1 + v2) + "</body></html>");
		response.flushBuffer();
//		out.flush();
	}
}
