package edu.upenn.cis455.webserver;

import java.util.*;

public class ThreadPool {
	private BlockingQueue<Runnable> queue = null;
	private ArrayList<Worker> threads = new ArrayList<Worker>();
	
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
		if(ShutdownHook.isShutdown.get())
			System.out.println("Should not be here: " + ShutdownHook.isShutdown.get());
			try {
				throw new Exception("Threads in process of shutting down.");
			} catch (Exception e) {
				// Allow threads to exit safely
			}
		this.queue.enqueue(task);
	}
	
	public void killAllThreads(){
		System.out.println("In the killAllThreads method");
		synchronized(threads){
			for(Thread thread : threads){
				System.out.println("Killing thread:" + thread.getId());
				thread.interrupt(); //kills thread by interrupting thread
			}
		}
		for(Thread thread : threads){
			System.out.println(thread.getId() + " status: "  + thread.isAlive()); //kills thread by interrupting thread
		}
	}
}
