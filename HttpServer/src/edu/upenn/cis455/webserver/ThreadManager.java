package edu.upenn.cis455.webserver;

import java.io.IOException;
import java.net.ServerSocket;

public class ThreadManager extends Thread{
	public boolean managerAlive = true;
	ThreadPool threads = null;
	ServerSocket socket = null;
	
	public ThreadManager(ThreadPool threads, ServerSocket socket){
		this.threads = threads;
		this.socket = socket;
	}

	public void run(){
		System.out.println("Thread manager started!");
		while(!ShutdownHook.isShutdown.get()){//while all threads are alive
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		threads.killAllThreads();
		System.out.println("Exiting Thread Manager.");
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
