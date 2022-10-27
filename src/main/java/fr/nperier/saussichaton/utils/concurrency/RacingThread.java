package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A thread that accepts a runnable for the race. It has a special method to interrupt the racer softly.
 * @see RacingRunnable
 * @see ThreadRace
 */
public class RacingThread<T> extends Thread {

    private static final Logger logger = LogManager.getLogger(RacingThread.class);

    private final RacingRunnable<T> racer;

    public RacingThread(final RacingRunnable<T> racer) {
        super(racer);
        this.racer = racer;
    }

    /**
     * Interrupts the racer softly, if not already interrupted.
     */
    public void interruptRacer() {
        if(!this.isInterrupted() && !racer.hasFinished()) {
            logger.trace("Interrupting racer");
            racer.interrupt();
        }
    }

}
