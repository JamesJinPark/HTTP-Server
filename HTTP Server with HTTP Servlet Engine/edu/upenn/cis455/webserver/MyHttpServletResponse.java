package edu.upenn.cis455.webserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Park
 * @class cis455/555
 * An implementation of the HttpServletResponse interface
 */
public class MyHttpServletResponse implements HttpServletResponse{
	public Map<String, Object> m_headers = new HashMap<String, Object>();;
	private String body; //response body
	private String status = "200";
	private MyWriter writer;
	private Cookie cookie;
	private String statusMsg;
	private MyHttpServletRequest request;
	public MyServletContext context;
	public Socket socket;
	private int bufferSize = 1024;
	private Locale locale;
	public boolean flushed = false;
	public String version; 

	/**
	 * @author James Park
	 * @class cis455/555
	 * A custom class that inherits from PrintWriter.  Written to implement buffering features.
 	 */
	class MyWriter extends PrintWriter{
		boolean isCommitted = false;
		public StringBuffer buffer;
		OutputStream outputStream;
		MyHttpServletResponse response;
		
		public MyWriter(OutputStream outputStream, MyHttpServletResponse response) {
			super(outputStream);
			this.buffer = new StringBuffer(response.bufferSize);
			this.outputStream = outputStream;
			this.response = response;
		}
		public MyWriter(BufferedOutputStream output){
			super(output);
		}

		public int length() {
			return buffer.length();
		}		

		/* (non-Javadoc)
		 * @see java.io.PrintWriter#write(java.lang.String)
		 * sets the isCommitted flag true
		 */
		@Override
		public void write(String string){
			buffer.append(string);
			this.isCommitted = true;
		}
		
		/* (non-Javadoc)
		 * @see java.io.PrintWriter#write(char[], int, int)
		 * sets the isCommitted flag true
		 */
		@Override
		public void write(char[] cbuf, int off, int len){
			int i;
			for (i = off ; i< len; i++){
				buffer.append(cbuf[i]);				
			}
			this.isCommitted = true;
		}

		/* (non-Javadoc)
		 * @see java.io.PrintWriter#println(java.lang.String)
		 * sets the isCommitted flag true
		 */
		@Override
		public void println(String string){
			buffer.append(string);
			buffer.append("\n");
			this.isCommitted = true;
		}
		
		/* (non-Javadoc)
		 * @see java.io.PrintWriter#flush()
		 * Actually constructs the Http Response, complete with status code, http version, headers, and body
		 */
		@Override
		public void flush(){
			response.setStatusMsg();
			m_headers.put("Host", request.getHeader("Host"));
			String httpResponse = response.version + " " + response.status + " " + response.statusMsg;
			byte[] bytes = buffer.toString().getBytes();
			response.setHeader("Content-Length", String.valueOf(buffer.length()));
			MyHttpSession session = (MyHttpSession) response.request.getSession();
			if(session.getAttributeNames().hasMoreElements() && session.isValid()){
				context.sessions.put(session.getId(), session);	
				
				Cookie jsession = new Cookie("JSESSIONID", session.getId());
				response.addCookie(jsession);
			}
			try {
				outputStream.write(httpResponse.getBytes()); // write status code
				outputStream.write("\r\n".getBytes());
				for(Map.Entry<String, Object> m_header : response.m_headers.entrySet()){ //write headers
					outputStream.write((m_header.getKey() + ": " + m_header.getValue()).getBytes());
					outputStream.write("\r\n".getBytes());
				}
				outputStream.write("Connection: close".getBytes());
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

	/**
	 * @param Socket socket
	 * @param MyHttpServletRequest request
	 * @param MyServletContext context
	 * Constructor for HttpServletResponse
	 */
	public MyHttpServletResponse (Socket socket, MyHttpServletRequest request, MyServletContext context){
		this.request = request;
		this.context = context;
		this.socket = socket;
		try {
			this.writer = new MyWriter(socket.getOutputStream(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		this.body = null;
		m_headers.put("Content-Type", "text/html");
		m_headers.put("Content-Length", 0);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#flushBuffer()
	 * Sets flag for isCommitted and calls MyWriter.flush()
	 */
	@Override
	public void flushBuffer() throws IOException {
		if(writer.isCommitted == false){
			writer.isCommitted = false;
			this.flushed = true;
			writer.flush();
		} else {
			//does nothing.  This is to account for servlets sometimes calling flush and sometimes not
		}
	}

	@Override
	public int getBufferSize() {
		return writer.buffer.length();
	}

	@Override
	public String getCharacterEncoding() {
		return "ISO-8859-1";
	}

	@Override
	public String getContentType() {
		return (String) this.m_headers.get("Content-Type");
	}

	@Override
	public Locale getLocale() {
		if (this.locale == null){
			return null;			
		} else{
			return this.locale;
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return this.writer;
	}

	@Override
	public boolean isCommitted() {
		return this.isCommitted();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#reset()
	 * resets headers, body, and status code
	 */
	@Override
	public void reset() {
		if(this.writer.isCommitted){
			throw new IllegalStateException();
		} else{
			this.writer.buffer = new StringBuffer();
			this.status = "200";
			this.m_headers = new HashMap<String, Object>();;
			this.body = null;
			m_headers.put("Content-Type", "text/plain");
			m_headers.put("Content-Length", 0);	
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 * only resets the body.  Keeps the header and status code.
	 */
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
		this.bufferSize = arg0;
	}

	@Override
	public void setCharacterEncoding(String arg0) {
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
		this.locale = arg0;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 * adds cookie to headers.  adds expiration time if max age has been set.
	 */
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
		this.m_headers.put(arg0, arg1);		
	}

	@Override
	public boolean containsHeader(String arg0) {
		if(this.m_headers.isEmpty())
			return false;
		return true;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 * Sends error with html body with status and error message
	 */
	@Override
	public void sendError(int arg0) throws IOException {
		this.status = String.valueOf(arg0);
		this.setStatusMsg();
		this.setHeader("Content-Type", "text/html");
		this.writer.println("<html>");
		this.writer.write(status);
		this.writer.println(" " + this.statusMsg + "</html>");
		this.flushBuffer();
	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		if(this.isCommitted()){
			throw new IllegalStateException();
		}
		this.status = String.valueOf(arg0);
		this.setHeader("Content-Type", "text/html");
		this.writer.write(status);
		this.writer.println(" " + arg1);
		this.flushBuffer();
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		sendError(302);
		m_headers.put("Location", arg0);
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
		this.status = String.valueOf(arg0);
	}

	@Override
	public void setStatus(int arg0, String arg1) {
		//	Deprecated
	}

	public String getBody(String body){
		return this.body;
	}
	
	private void setStatusMsg(){
		switch(this.status){
		case "100": 	this.statusMsg = "Continue";
						break;
		case "200": 	this.statusMsg = "OK";
						break;
		case "300":		this.statusMsg = "Multiple Choices";
						break;
		case "302": 	this.statusMsg = "Redirect";
						break;	
		case "304":		this.statusMsg = "Not Modified";
						break;
		case "305":		this.statusMsg = "Use Proxy";
						break;
		case "400":		this.statusMsg = "Bad Request";
						break;
		case "401":		this.statusMsg = "Unauthorized";
						break;
		case "403": 	this.statusMsg = "Forbidden";
						break;
		case "404": 	this.statusMsg = "Not Found";
						break;						
		case "405":		this.statusMsg = "Method Not Allowed";
						break;	
		case "411":		this.statusMsg = "Length Required";
						break;
		case "412":		this.statusMsg = "Precondition Failed";
						break;
		case "415":		this.statusMsg = "Unsupported Media Type";
						break;
		case "500":		this.statusMsg = "Internal Server Error";
						break;
		case "501":		this.statusMsg = "Not Implemented";
						break;
		case "502":		this.statusMsg = "Bad Gateway";
						break;
		case "503":		this.statusMsg = "Service Unavailable";
						break;
		case "505":		this.statusMsg = "HTTP Version Not Supported";	
						break;
		}
			
	}

}
