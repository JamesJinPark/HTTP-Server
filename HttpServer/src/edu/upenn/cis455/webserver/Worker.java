package edu.upenn.cis455.webserver;

public class Worker extends Thread{
	
	private BlockingQueue<Runnable> queue = null;
	
	public Worker(BlockingQueue<Runnable> queue){
		this.queue = queue;
	}
	
	public void run(){
		while(!ShutdownHook.isShutdown.get()){//while threads are not told to stop
			try{
				Runnable runnable = (Runnable) queue.dequeue();
				runnable.run();
				break;
			}catch(Exception e){
//				System.err.println("Thread could not run task:" + e);
				break;
			}
		}
		System.out.println("Exiting thread: " + this.getId());
	}
}
