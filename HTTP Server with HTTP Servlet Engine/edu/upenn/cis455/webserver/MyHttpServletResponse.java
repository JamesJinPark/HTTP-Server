package edu.upenn.cis455.webserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Date;

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
	private MyWriter writer;
	private Cookie cookie;
	private String statusMsg;
	private MyHttpServletRequest request;

	public boolean flushed = false;
	public String version; 

	class MyWriter extends PrintWriter{
		boolean isCommitted = false;
		public StringBuffer buffer = new StringBuffer();
		OutputStream outputStream;
		MyHttpServletResponse response;
		
		public MyWriter(OutputStream outputStream, MyHttpServletResponse response) {
			super(outputStream);
			this.outputStream = outputStream;
			this.response = response;
		}
		public MyWriter(BufferedOutputStream output){
			super(output);
		}

		public int length() {
			return buffer.length();
		}		

		@Override
		public void write(String string){
			buffer.append(string);
			this.isCommitted = true;
		}
		
		@Override
		public void write(char[] cbuf, int off, int len){
			int i;
			for (i = off ; i< len; i++){
				buffer.append(cbuf[i]);				
			}
			this.isCommitted = true;
		}

		@Override
		public void println(String string){
			buffer.append(string);
			buffer.append('\n');
			this.isCommitted = true;
		}

		@Override
		public void flush(){
			response.setStatusMsg();
			String httpResponse = response.version + " " + response.status + " " + response.statusMsg;
			byte[] bytes = buffer.toString().getBytes();
			response.setHeader("Content-Length", String.valueOf(buffer.length()));
			if(response.request.getSession().getAttributeNames() != null){
				@SuppressWarnings("rawtypes")
				Enumeration temp = response.request.getSession().getAttributeNames();
				while(temp.hasMoreElements()){
					String param = (String)temp.nextElement();
					String value = (String)response.request.getSession().getAttribute(param);
					Cookie sessionCookie = new Cookie(param, value);
					response.addCookie(sessionCookie);
				}
				Cookie jsession = new Cookie("JSESSIONID", response.request.getSession().getId());
				response.addCookie(jsession);
			}
			try {
				outputStream.write(httpResponse.getBytes()); // write status code
				for(Map.Entry<String, Object> m_header : response.m_headers.entrySet()){ //write headers
					outputStream.write("\r\n".getBytes());
					outputStream.write((m_header.getKey() + ": " + m_header.getValue()).getBytes());
					System.out.println((m_header.getKey() + ": " + m_header.getValue()).getBytes());
				}
				outputStream.write("\r\n".getBytes());
				outputStream.write("\r\n".getBytes());
				outputStream.write(bytes); //buffer
				outputStream.flush();
				flushed = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}	

	MyHttpServletResponse (Socket socket, MyHttpServletRequest request){
		this.request = request;
		this.socket = socket;
		try {
			//this.out = new PrintWriter(socket.getOutputStream());
			//this.output = new BufferedOutputStream(socket.getOutputStream());  
			this.writer = new MyWriter(socket.getOutputStream(), this); 
			this.body = null;
			m_headers.put("Content-Type", "text/plain");
			m_headers.put("Content-Length", 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void flushBuffer() throws IOException {
		System.out.println("Flush Buffer");
		if(writer.isCommitted == false){
			writer.isCommitted = false;
			this.flushed = true;
			writer.flush();
		} else {
			
		}
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return writer.buffer.length();
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
		return this.writer;
	}

	@Override
	public boolean isCommitted() {
		return this.isCommitted();
	}

	@Override
	public void reset() {
		if(this.writer.isCommitted){
			throw new IllegalStateException();
		} else{
			this.writer.buffer = new StringBuffer();
			this.status = 200;
			this.m_headers = new HashMap<String, Object>();;
			this.body = null;
			m_headers.put("Content-Type", "text/plain");
			m_headers.put("Content-Length", 0);	
		}
	}

	@Override
	public void resetBuffer() {
		if(this.writer.isCommitted){
			throw new IllegalStateException();
		} else{
			this.writer.buffer = new StringBuffer();
			this.body = null;
		}
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
		this.cookie = arg0;
		if(cookie.getMaxAge() != -1){
			Calendar currentTime = Calendar.getInstance();
			currentTime.add(Calendar.SECOND, cookie.getMaxAge());
			DateFormat dateTemplate = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z");
			dateTemplate.setTimeZone(TimeZone.getTimeZone("GMT"));
			this.m_headers.put("Set-Cookie", cookie.getName() + '=' +
					cookie.getValue() + "; " + "Expires=" + dateTemplate.format(currentTime.getTime()));
		}else {
			this.m_headers.put("Set-Cookie", cookie.getName() + '=' +
				cookie.getValue());
		}
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		this.m_headers.put(arg0, arg1);
		
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
		if(this.m_headers.isEmpty())
			return false;
		return true;
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
		this.status = arg0;
		this.writer.write(status);
		this.flushed = true;
		this.flushBuffer();
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		if(this.isCommitted()){
			throw new IllegalStateException();
		}
		this.status = arg0;
		this.setHeader("Content-Type", "text/html");
		this.writer.write(status);
		this.writer.println(" " + arg1);
		this.flushed = true;
		this.flushBuffer();
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		this.m_headers.put(arg0, arg1);		
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

	public String getBody(String body){
		return this.body;
	}

	public void setBytes(byte[] bytes){
		m_headers.put("Content-Length", bytes.length);
		this.bytes = bytes;
	}
	
	private void setStatusMsg(){
		switch(this.status){
		case 100: 	this.statusMsg = "Continue";
					break;
		case 200: 	this.statusMsg = "OK";
					break;
		case 404: 	this.statusMsg = "Not Found";
					break;
		case 500: 	this.statusMsg = "Internal Service Error";
					break;
		}
			
	}

}