package edu.upenn.cis455.webserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class ShutdownHook {
	public static final AtomicBoolean isShutdown = new AtomicBoolean(false);
	public static void shutdown(){
		isShutdown.set(true);
	}
}
