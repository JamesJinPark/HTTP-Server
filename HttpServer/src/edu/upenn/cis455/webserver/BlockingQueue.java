package edu.upenn.cis455.webserver;

import java.util.*;

public class BlockingQueue<E>{
	
	private LinkedList<E> queue = new LinkedList<E>();
	private int maxLength;
	
	public BlockingQueue(int maxLength){
		this.maxLength = maxLength; //size of thread pool should be around 100
	}
	
	public synchronized void enqueue(E object){
		while(this.isFull()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(this.isEmpty()){  //hmmm should ask around about what this is actually doing and whether this is in the right place
			notifyAll();
		}
		this.queue.add(object);
	}
	
	public synchronized E dequeue(){		
		while(this.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(this.isFull()){ //hmmm should ask around about what this is actually doing and whether this is in the right place
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