package edu.upenn.cis455.webserver;

public class Worker extends Thread{
	
	private BlockingQueue<Runnable> queue = null;
	private boolean kill = false;
	
	public Worker(BlockingQueue<Runnable> queue){
		this.queue = queue;
	}
	
	public void run(){
		while(!kill){//while threads are not told to stop
			try{
				Runnable runnable = (Runnable) queue.dequeue();
				runnable.run();
			}catch(Exception e){
				System.err.println("Thread could not run task:" + e);
			}
		}
	}
	
	public synchronized void kill(){
		this.kill = true;
		this.interrupt(); 
	}
	
	public synchronized boolean getStatus(){
		return this.kill;
	}
}
