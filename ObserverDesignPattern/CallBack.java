package il.co.ilrd.observer;
import java.util.function.Consumer;

public class CallBack<T> {
    Subject<T> subject;
    private final Consumer<T> cons;
    private final Runnable notifyDeath;

    public CallBack(Consumer<T> cons, Runnable notifyDeath) {
        this.cons = cons;
        this.notifyDeath = notifyDeath;
    }

    public CallBack(Consumer<T> cons) {
        this.cons = cons;
        this.notifyDeath = ()->{};
    }

    void update(T msg) {
        cons.accept(msg);
    }

    void notifyDeath() {
        notifyDeath.run();
    }
    
    public void unregister() {
    	subject.unregister(this);
    }
}