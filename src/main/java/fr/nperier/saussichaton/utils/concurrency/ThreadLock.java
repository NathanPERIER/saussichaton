package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadLock<T> {

    private static final Logger logger = LogManager.getLogger(ThreadLock.class);

    private T value;
    private boolean isSet;

    public ThreadLock() {
        this.value = null;
        this.isSet = false;
    }

    public synchronized void setValue(final T value) throws StopRacing {
        if(isSet) {
            throw new StopRacing();
        }
        this.value = value;
        this.isSet = true;
        this.notifyAll();
    }

    public synchronized T getValue() {
        try {
            this.wait();
        } catch(InterruptedException e) {
            logger.error("Interrupted while waiting for the value of a thread lock", e);
        }
        this.notify();
        return value;
    }

}
