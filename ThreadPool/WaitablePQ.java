package il.co.ilrd.threadpool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class WaitablePQ<E> {
	private final PriorityQueue<E> pq;
	private final ReentrantLock lockForPQ = new ReentrantLock();
	private Semaphore sem = new Semaphore(0);

	public WaitablePQ() {
		pq = new PriorityQueue<>();
	}
	
	public WaitablePQ(Comparator<? super E> comparator) {
		pq = new PriorityQueue<>(comparator);
		
	}
	
	public boolean enqueue(E arg) {	
		boolean isAdded = false;
	
		synchronized(lockForPQ) {
			isAdded =  pq.add(arg);

			sem.release();
		}
		
		return isAdded;
	}
	
	public E dequeue() throws InterruptedException{
		E element = null;
		
		sem.acquire();

		synchronized(lockForPQ) {
			element = pq.poll();
		}
		
		return element;
	}
	
	public E dequeueWithTO(long time, TimeUnit unit) throws TimeoutException,InterruptedException{
		E element = null;
		
		if (sem.tryAcquire(time, unit)) {
			synchronized(this) {
				element = pq.poll();
			}
		}
		
		return element;
	}
	

	public boolean remove(E object) {
		
	if (sem.tryAcquire()) {
		synchronized (lockForPQ) {
			if (pq.remove(object)) {
				return true;
			}
			else {
				sem.release();
			}
		}				
	}
	return false;
}
}