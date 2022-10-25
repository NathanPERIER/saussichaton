package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RacingThread<T> extends Thread {

    private static final Logger logger = LogManager.getLogger(RacingThread.class);

    private final RacingRunnable<T> runner;

    public RacingThread(final RacingRunnable<T> runner) {
        super(runner);
        this.runner = runner;
    }

    public void interruptRacer() {
        if(!this.isInterrupted()) {
            logger.trace("Racer interrupted");
            runner.interrupt();
        }
    }

    @Override
    public void interrupt() {
        this.interruptRacer();
        super.interrupt();
    }
}
