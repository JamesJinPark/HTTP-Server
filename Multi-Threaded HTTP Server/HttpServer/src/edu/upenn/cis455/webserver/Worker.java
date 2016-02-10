package edu.upenn.cis455.webserver;

/**
 * @author James Park
 * @class cis455/555
 *
 */
public class Worker extends Thread{
	
	private BlockingQueue<Runnable> queue = null;
	public String path = null;
	
	public Worker(BlockingQueue<Runnable> queue){
		this.queue = queue;
	}
	
	public void run(){
		while(!ShutdownHook.isShutdown.get() && !Thread.interrupted()){//while threads are not told to stop
			try{
				Runnable runnable = queue.dequeue();
				runnable.run();
			}catch(Exception e){
			}
		}
		System.out.println("Exiting thread: " + this.getId());
	}
}
