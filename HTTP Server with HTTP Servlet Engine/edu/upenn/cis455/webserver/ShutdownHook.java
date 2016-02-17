package edu.upenn.cis455.webserver;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author James Park
 * @class cis455/555
 * 
 * AtomicBoolean that is accessible to all classes.  Acts like a switch to exit all threads 
 * and processes and shutdown server.
 *
 */
public class ShutdownHook {
	public static final AtomicBoolean isShutdown = new AtomicBoolean(false);
	public static void shutdown(){
		isShutdown.set(true);
	}
}
