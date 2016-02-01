package edu.upenn.cis455.webserver;

import java.util.*;

public class ThreadPool {
	private BlockingQueue<Runnable> queue = null;
	private ArrayList<Worker> threads = new ArrayList<Worker>();
	private boolean killThreads = false; //boolean to kill all threads
	
	//Constructor
	public ThreadPool(int numThreads){
		queue = new BlockingQueue<Runnable>(numThreads);
		for(int i = 0; i < numThreads; i++){
			threads.add(new Worker(queue));
		}
		for(int j = 0; j< threads.size(); j++){
			threads.get(j).start();
		}
	}
	
	public synchronized void run(Runnable task){
		if(this.killThreads)
			try {
				throw new Exception("Threads in process of shutting down.");
			} catch (Exception e) {
				System.out.println(e); // Allow threads to exit safely
			}
		this.queue.enqueue(task);
	}
	
	public synchronized void killAllThreads(){
		this.killThreads = true;
		for(int i = 0; i < this.threads.size(); i++){
			threads.get(i).kill(); //kills thread by setting the kill boolean to true and interrupting thread
		}
	}
}
