package edu.upenn.cis455.webserver;

import java.util.*;

public class ThreadPool {
	private BlockingQueue<Runnable> queue = null;
	private List<Worker> threads = new ArrayList<Worker>();
	
	//Constructor
	public ThreadPool(int numThreads){
		queue = new BlockingQueue<>(numThreads);
		for(int i = 0; i < numThreads; i++){
			threads.add(new Worker(queue));
		}
		for(Worker thread : threads){
			thread.start();
		}
	}
	
	public synchronized void run(Runnable task){
		if(ShutdownHook.isShutdown.get())
			try {
				throw new Exception("Threads in process of shutting down.");
			} catch (Exception e) {
				// Allow threads to exit safely
			}
		this.queue.enqueue(task);
	}
	
	public int howManyThreadsAlive(){
		int count = 0;
		for(Thread thread : threads){
			if (thread.isAlive()){
				count += 1;
				thread.interrupt();
			}
		}
		return count;
	}

	@SuppressWarnings("deprecation")
	public void emergencyShutdown(){
		for(Thread thread : threads){
			if (thread.isAlive()){
				thread.stop();
			}
		}
	}

	
	public String getThreadsStatus(String threadPath){
		String temp = "";
		for(Thread thread : threads){
			if (thread.isAlive()){
				temp += "Thread #" + thread.getId() + " status: "  + ((thread.getState() == Thread.State.WAITING) ? "waiting" : "Handing request to " + threadPath) + "<br>"; //kills thread by interrupting thread
			}
		}
		return temp;
	}
	
	public void killAllThreads() throws InterruptedException{
		synchronized(threads){
			for(Thread thread : threads){
				thread.interrupt(); //kills thread by interrupting thread
			}
		}
		Thread.sleep(3000);
		for(Thread thread : threads){
			System.out.println(thread.getId() + " status: "  + (thread.isAlive() ? "alive" : "dead")); //kills thread by interrupting thread
		}
	}
}
