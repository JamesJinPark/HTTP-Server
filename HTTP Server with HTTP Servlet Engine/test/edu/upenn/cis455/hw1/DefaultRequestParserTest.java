package test.edu.upenn.cis455.hw1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.text.ParseException;

import org.junit.Test;

import edu.upenn.cis455.webserver.DefaultRequestParser;
import junit.framework.TestCase;

public class DefaultRequestParserTest extends TestCase {
	String line = null;
	DefaultRequestParser parser = new DefaultRequestParser();
	File file;
	
	public void createFile() throws IOException{
		this.file = new File("Test File for DefaultRequestParserTest.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);
	}
	
	public void deleteFile(){
		this.file.delete();
	}
	
	/**
	 * tests month conversion method in DefaultRequestParser
	 */
	public void testMonthConverter(){
		assertTrue(parser.monthConverter("Jan").equals("01/"));
		assertTrue(parser.monthConverter("Dec").equals("12/"));
		assertTrue(parser.monthConverter("Januaray").equals("Error! monthConverter failed!"));
		assertFalse(parser.monthConverter("Jan").equals("02/"));
	}
	
	public void testCheckDate() throws ParseException, IOException{
		createFile();
		assertTrue(parser.checkDate("Friday, 31-Dec-99 23:59:59 GMT", this.file)); //asks if modified after Dec. 1999
		assertFalse(parser.checkDate("Fri Dec 31 23:59:59 2017", this.file)); // asks if modified after Dec. 2017
		deleteFile();
	}
	
	public void testHttpParse() throws IOException{
		createFile();
		Reader reader = new FileReader(this.file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		try{
			parser.parse(line, bufferedReader); 
		}catch(NullPointerException e){
			assertTrue(true);
		}
		deleteFile();		
	}

}
