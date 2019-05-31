package il.co.ilrd.threadpool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class WaitablePQCV<E> {
	private final PriorityQueue<E> pq;
	private final Lock lockForPQ = null;
	private final Condition cv =  lockForPQ.newCondition();

	public WaitablePQCV() {
		pq = new PriorityQueue<>();
	}
	
	public WaitablePQCV(Comparator<? super E> comparator) {
		pq = new PriorityQueue<>(comparator);
		
	}
	
	public boolean enqueue(E arg) {	
		
		lockForPQ.lock();
		pq.add(arg);
		cv.signal();
		lockForPQ.unlock();
		
		return true;
	}
	
	public E dequeue() throws InterruptedException{
		try {
			lockForPQ.lock();
			while(pq.isEmpty()) {
				cv.await();					
			}

	        return pq.poll();
			
		}finally{
			lockForPQ.unlock();
		}
	}
	
	public E dequeueWithTO(long time, TimeUnit unit) throws TimeoutException,InterruptedException{
	
			lockForPQ.lock();
			while(pq.isEmpty()) {
				if (!cv.await(time, unit)) {
					return null;
				}
			
				lockForPQ.unlock();

			return pq.poll();
	
}
			return null;
}
}