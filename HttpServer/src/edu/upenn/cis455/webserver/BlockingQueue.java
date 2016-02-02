package edu.upenn.cis455.webserver;

import java.util.*;

public class BlockingQueue<E>{
	
	private LinkedList<E> queue = new LinkedList<E>();
	private int maxLength;
	
	public BlockingQueue(int maxLength){
		this.maxLength = maxLength; //size of thread pool should be around 100
	}
	
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
	
	public synchronized E dequeue(){		
		while(this.isEmpty() && !ShutdownHook.isShutdown.get()){
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//allow threads to die
				break;
			}
		}
		if(!this.isEmpty()){ 
			notifyAll();
		}
		return this.queue.poll();
	}
	
	private boolean isEmpty(){
		return this.queue.size() == 0;
	}

	private boolean isFull(){
		return this.queue.size() == this.maxLength;
	}

}