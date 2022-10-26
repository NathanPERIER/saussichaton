package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadRaceAgainstTheClock<T> extends ThreadRace<T> {

    private static final Logger logger = LogManager.getLogger(ThreadRaceAgainstTheClock.class);

    public ThreadRaceAgainstTheClock(final long delay, final T clockValue) {
        super();
        final ClockRacingRunnable<T> clock = new ClockRacingRunnable<>(delay, clockValue);
        this.addRacer(clock);
        clock.setThread(this.threads.get(0));
    }


    private static class ClockRacingRunnable<R> extends RacingRunnable<R> {

        private final long delay;
        private final R value;
        private Thread thread;

        public ClockRacingRunnable(final long delay, final R value) {
            this.delay = delay;
            this.value = value;
        }

        public void setThread(final Thread thread) {
            this.thread = thread;
        }

        @Override
        public void race() {
            try {
                Thread.sleep(delay);
                this.lock.setValue(value);
                logger.trace("Clock thread won");
            } catch(InterruptedException e) {
                logger.debug("Clock running thread interrupted");
            }
        }

        @Override
        public void interrupt() {
            thread.interrupt();
        }
    }

}
