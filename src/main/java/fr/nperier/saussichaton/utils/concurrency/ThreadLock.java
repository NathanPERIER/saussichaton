package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Object shared between racers of a thread race, allowing them to synchronise with the main thread
 * and to set the resulting value.
 * @see ThreadRace
 * @see RacingThread
 */
public class ThreadLock<T> {

    private static final Logger logger = LogManager.getLogger(ThreadLock.class);

    private T value;
    private boolean isSet;

    public ThreadLock() {
        this.value = null;
        this.isSet = false;
    }

    public synchronized boolean hasValue() {
        return this.isSet;
    }

    /**
     * Method to set the value resulting of the race.
     * Can only be called once.
     * @throws StopRacing if a thread attempts to set the value for the second time.
     */
    public synchronized void setValue(final T value) throws StopRacing {
        if(isSet) {
            throw new StopRacing();
        }
        this.value = value;
        this.isSet = true;
        this.notifyAll();
    }

    /**
     * Method allowing to get the resulting value.
     * As long as the value has not been set, the threads that attempt to read it will be blocked.
     */
    public synchronized T getValue() {
        if(!this.isSet) {
            try {
                this.wait();
            } catch(InterruptedException e) {
                logger.error("Interrupted while waiting for the value of a thread lock", e);
            }
            this.notify();
        }
        return value;
    }

}
