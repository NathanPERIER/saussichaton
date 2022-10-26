package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RacingRunnable<T> implements Runnable {

    private static final Logger logger = LogManager.getLogger(RacingRunnable.class);

    protected ThreadLock<T> lock;

    public void giveLock(final ThreadLock<T> lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            this.race();
        } catch(StopRacing e) {
            logger.debug("Running thread received a StopRacing");
        }
    }

    public abstract void race();

    public abstract void interrupt();

}