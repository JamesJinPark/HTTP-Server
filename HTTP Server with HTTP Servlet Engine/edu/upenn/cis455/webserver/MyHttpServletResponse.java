package edu.upenn.cis455.webserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MyHttpServletResponse implements HttpServletResponse{
	public Map<String, Object> m_headers = new HashMap<String, Object>();;
	private String body; //response body
	private byte[] bytes; //response bytes
	private int status = 200;
	private Socket socket; 
	private PrintWriter out;
	private BufferedOutputStream output; 
	private MyWriter buffer;

	class MyWriter extends PrintWriter{
		public StringBuffer buffer = new StringBuffer();
		
		public MyWriter(PrintWriter out) {
			super(out);
		}
		public MyWriter(BufferedOutputStream output){
			super(output);
		}
		
		@Override
		public void write(String string){
			buffer.append(string);
		}
		@Override
		public void println(String string){
			buffer.append(string);
			//set body here?
			//set isCommitted to true here?
		}

		@Override
		public void flush(){
			try {
				((PrintWriter) out).println(String.valueOf(buffer));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}	

	MyHttpServletResponse (Socket socket){
		this.socket = socket;
		try {
			//this.out = new PrintWriter(socket.getOutputStream());
			//this.output = new BufferedOutputStream(socket.getOutputStream()); //can I do this? 
			this.buffer = new MyWriter(new PrintWriter(socket.getOutputStream())); //can I do this? 
			this.body = null;
			m_headers.put("Content-Type", "text/plain");
			m_headers.put("Content-Length", 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void flushBuffer() throws IOException {
//		out.flush();//does this work?
//		output.flush();
		buffer.flush();
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		return (String) this.m_headers.get("Content-Type");
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
//		return this.out; //this uses PrintWrtier
		return this.buffer;
	}

	@Override
	public boolean isCommitted() {
		if(this.body == null){
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLength(int arg0) {
		this.m_headers.put("Content-Length", arg0);		
	}

	@Override
	public void setContentType(String arg0) {
		this.m_headers.put("Content-Type", arg0);
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCookie(Cookie arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addHeader(String arg0, String arg1) {
		this.m_headers.put(arg0, arg1);
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsHeader(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		buffer.println(arg0);		
		buffer.flush();
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub	
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeader(String arg0, String arg1) {
		this.m_headers.put(arg0, arg1);
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		this.m_headers.put(arg0, arg1);		
	}

	@Override
	public void setStatus(int arg0) {
		this.status = arg0;
	}

	@Override
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub
		//	Deprecated
	}

//	MyWriter println() sets body instead
//	public void setBody(String body){ 
//		m_headers.put("Content-Length", body.length());
//		this.body = body;
//	}
	
	public String getBody(String body){
		return this.body;
	}

	public void setBytes(byte[] bytes){
		m_headers.put("Content-Length", bytes.length);
		this.bytes = bytes;
	}

}