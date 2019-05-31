package il.co.ilrd.threadpool;

import java.util.Objects;
import java.util.concurrent.*;

public class GetThreadPool{
    private volatile boolean isPaused = false;
    private volatile boolean isShutDown = false;
    private Semaphore semPause = new Semaphore(0);
    private volatile int numOfThread;
    private final WaitablePQ<Task<?>> workQ;
    private enum Status {
		NEW, RUN, DONE, CANCEL;
	}
    
    public enum Priority {
        MIN(), NORMAL(), MAX(), HIGHEST();
    }
    /**************************************CTOR*******************************************/

    public GetThreadPool(int numOfThread) {
        this.numOfThread = numOfThread;
        workQ = new WaitablePQ<>(((task, t1) -> task.priority - (t1.priority)));
    }
    /************************************SUBMIT*****************************************/

	public <T> Future<T> submit(Runnable runnable, Priority priority, T tValue) throws RejectedExecutionException {
		Objects.requireNonNull(runnable);

		return submit(Executors.callable(runnable, tValue), priority.ordinal());
	}
	
	public <T> Future<T> submit(Runnable runnable, Priority priority) throws RejectedExecutionException {
		Objects.requireNonNull(runnable);

		return submit(Executors.callable(runnable, null), priority.ordinal());
	}
	
	public <T> Future<T> submit(Callable<T> callable) throws RejectedExecutionException {
		Objects.requireNonNull(callable);

		return submit(callable, Priority.NORMAL.ordinal());
	}
	
	public <T> Future<T> submit(Callable<T> callable, Priority priority) throws RejectedExecutionException {
		Objects.requireNonNull(callable);

		return submit(callable, priority.ordinal());
	}

	private <T> Future<T> submit(Callable<T> callable, int priority) throws RejectedExecutionException {
		Objects.requireNonNull(callable);
		if (isShutDown) {
			throw new RejectedExecutionException();
		}
		
		Task<T> task = new Task<T>(callable, priority);
		workQ.enqueue(task);			
		
		return task.getFuture();
	}
	/*************************************API**********************************************/

	public void run() {
		setThreads(numOfThread);
	}
	
    public void resume() throws InterruptedException {
    	if (isPaused) {
    		semPause.release(numOfThread);
    		isPaused = false;
    	}
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return new CountDownLatch(numOfThread).await(timeout, unit);
    }

    public void shutdown() {
    	isShutDown = true;
    	killThreads(Priority.MIN, numOfThread);
    }

    public void pause() throws InterruptedException {	
    	isPaused = true;
		for (int i = 0; i < numOfThread; ++i) {
			submit((Callable<Object>)()-> {semPause.acquire(numOfThread);
												return null; }, Priority.MAX.ordinal());
		}
    }
    
    public void setNumOfThreads(int numOfThreads) {
		Objects.requireNonNull(numOfThreads);
		
    	int finalNumThread = numOfThreads - this.numOfThread;
    	
    	if (finalNumThread > 0) {
    		setThreads(finalNumThread);
    	}
    	else {
    		killThreads(Priority.MAX, -finalNumThread);
    	}
    	
		this.numOfThread = numOfThreads;
    }
    /*************************************utilities**********************************************/

    private void killThreads(Priority priority, int numOfThreads) {
    	for(int i = 0; i < numOfThreads; ++i) {
			submit(new badApple() , priority);
		}
    }
    
    private void setThreads(int numThreads) {
    	for (int i = 0; i < numThreads; ++i) {
    		WorkerThread th = (WorkerThread) new WorkerThread();
    		th.start();
    	}
    }
    /*******************************CLASS TASK****************************************************/

    private static class Task<T>{
        private Callable<T> callable;
        private int priority;
        private MyFuture future;
		private final Semaphore sem;
		
    	public Task(Callable<T> callable, int priority) {
			this.callable = callable;
			this.priority = priority;
			sem =  new Semaphore(0);
			future = new MyFuture();
		}

		public void run() {

			future.status = Status.RUN;
	
				try {
					future.result = (callable.call());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				future.status = Status.DONE;
				sem.release();
		}
		
		private MyFuture getFuture() {
			return this.future;
		}
		/******************My future*********************/

        private class MyFuture implements Future<T> {
    		public volatile Status status = Status.NEW;
			private T result;
			private Exception exception;
			
            @Override
            public boolean cancel(boolean b) {

				if (status == Status.NEW) {
					status = Status.CANCEL;
		    		callable = null;
				
					return true;
				}
				return false;
			}
            @Override
            public boolean isCancelled() {
                return status == Status.CANCEL;
            }

            @Override
            public boolean isDone() {
                return status == Status.DONE;
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
            	if (exception != null) { throw new ExecutionException(exception); }
            		
            	
            	if (status == Status.CANCEL) { throw new CancellationException(); }
				
        		sem.acquire();
        		sem.release();
        	 	
				return result;
			}
            
            @Override
            public T get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            	if (exception != null) { throw new ExecutionException(exception);}
            	if (isCancelled()) { throw new CancellationException(); }
            	
        		if (!sem.tryAcquire(l, timeUnit)) { throw new TimeoutException(); }
        		sem.release();
       		
				return result;           
			}
        }
    }
/********************************WorkerThread Class*********************************************/
    public class WorkerThread extends Thread {
        private volatile boolean continueRunning = true;
		private Task<?> task;

    	@Override
    	public void run() {
    		while (continueRunning) {
				task = null;
					try {
						task = workQ.dequeue();
					} catch (InterruptedException e) {
						task.future.exception = e;
					}
				
				if (task.getFuture().status != Status.CANCEL)
					task.run();
    		}
    	}
    }
    /*****************************************BadApple*********************************************/
    
    private class badApple implements Runnable {
    	public void run() {
    		WorkerThread currentThread =  (WorkerThread) Thread.currentThread();
    		currentThread.continueRunning = false;
    	}
    }
}
