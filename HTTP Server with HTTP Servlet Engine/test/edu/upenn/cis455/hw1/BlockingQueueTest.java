package test.edu.upenn.cis455.hw1;

import edu.upenn.cis455.webserver.BlockingQueue;
import junit.framework.TestCase;

public class BlockingQueueTest extends TestCase {
	
	@SuppressWarnings("rawtypes")
	BlockingQueue queue = new BlockingQueue(2);//creates queue of size 2 .
	String test1 = "test 1";
	String test2 = "test 2";
	
	
	@SuppressWarnings("unchecked")
	public void testEnque(){
		assertTrue(queue.isEmpty()); 	//queue is empty
		queue.enqueue(test1);			//add one string object
		assertTrue(!queue.isFull()); 	//queue is not full
		queue.enqueue(test2);			//add one string object
		assertTrue(queue.isFull()); 	//queue is not full
	}

	@SuppressWarnings("unchecked")
	public void testDeque() throws InterruptedException{
		assertTrue(queue.isEmpty()); 	//queue is empty
		queue.enqueue(test1);			//add one string object
		queue.enqueue(test2);			//add one string object
		assertTrue(queue.isFull()); 	//queue is not full
		
		Object newString1 = queue.dequeue(); //dequeues "test 1"
		Object newString2 = queue.dequeue(); //dequeues "test 2"
		assertTrue(queue.isEmpty()); 	//queue is empty
		
		assertTrue(newString1.equals(test1));
		assertTrue(newString2.equals(test2));

	}
	
}
