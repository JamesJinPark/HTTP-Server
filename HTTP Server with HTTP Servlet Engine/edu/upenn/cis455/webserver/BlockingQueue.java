package edu.upenn.cis455.webserver;

import java.util.*;

/**
 * @author James Park
 * @class cis455/555
 *
 * @param <E>
 * Blocking queue data structure to hold runnable tasks
 */
public class BlockingQueue<E>{
	
	private LinkedList<E> queue = new LinkedList<E>();
	private int maxLength;
	
	public BlockingQueue(int maxLength){
		this.maxLength = maxLength; //size of thread pool should be around 100
	}
	
	/**
	 * @param object
	 * Adds object to the queue
	 */
	public synchronized void enqueue(E object){
		while(this.isFull() && !ShutdownHook.isShutdown.get()){
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		if(!this.isFull()){  
			notifyAll();
		}
		this.queue.add(object);
	}
	
	/**
	 * @return
	 * @throws InterruptedException
	 * Removes item from the queue
	 */
	public synchronized E dequeue() throws InterruptedException{		
		while(this.isEmpty() && !ShutdownHook.isShutdown.get()){
			wait();
		}
		if(!this.isEmpty()){ 
			notifyAll();
		}
		return this.queue.poll();
	}
	
	public boolean isEmpty(){
		return this.queue.size() == 0;
	}

	public boolean isFull(){
		return this.queue.size() == this.maxLength;
	}

}