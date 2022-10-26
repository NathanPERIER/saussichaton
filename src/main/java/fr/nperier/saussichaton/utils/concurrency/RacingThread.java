package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RacingThread<T> extends Thread {

    private static final Logger logger = LogManager.getLogger(RacingThread.class);

    private final RacingRunnable<T> racer;

    public RacingThread(final RacingRunnable<T> racer) {
        super(racer);
        this.racer = racer;
    }

    public void interruptRacer() {
        if(!this.isInterrupted() && !racer.hasFinished()) {
            logger.trace("Interrupting racer");
            racer.interrupt();
        }
    }

}
