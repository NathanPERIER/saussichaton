package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Special runnable to be executed as part of a thread race.
 * @see ThreadRace
 */
public abstract class RacingRunnable<T> implements Runnable {

    private static final Logger logger = LogManager.getLogger(RacingRunnable.class);

    /**Object that allows the runnable to return the value*/
    protected ThreadLock<T> lock;
    protected boolean finished;

    public RacingRunnable() {
        this.finished = false;
    }

    /**
     * Method used to set the object allowing to return the value.
     */
    public void giveLock(final ThreadLock<T> lock) {
        this.lock = lock;
    }

    public boolean hasFinished() {
        return finished;
    }

    @Override
    public void run() {
        try {
            this.race();
        } catch(StopRacing e) {
            logger.debug("Running thread received a StopRacing");
        }
    }

    /**
     * Method that will be executed during the race.
     */
    public abstract void race();

    /**
     * Method used to perform a soft interrupt, allowing the racer to finish gracefully.
     */
    public abstract void interrupt();

}
