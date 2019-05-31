package il.co.ilrd.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject<T> {
    List<CallBack<T>> callback = new ArrayList<>();

    public void unregister(CallBack<T> observer) {
    	observer.subject = null;

    	callback.remove(observer);
    }

    public void register(CallBack<T> observer) {
    	callback.add(observer);
        observer.subject = this;
    }

    public void notifyAllObservers(T t) {
        for (CallBack<T> c : callback) {
            c.update(t);
        }
    }

    public void stop() {
    	for(CallBack<T> c : callback) {
    		c.notifyDeath();
    	}
    	
    	callback.clear();
    }
}