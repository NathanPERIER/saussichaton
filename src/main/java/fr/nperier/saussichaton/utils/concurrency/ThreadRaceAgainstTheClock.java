package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Special thread race during which the threads compete against a timer that will automatically
 * set the value after a given amount of time.
 * If the other threads want to have a chance to win the race, they must provide an answer within the delay of the timer.
 * Note : A potential improvement would be to make the timer return immediately if all the other threads have returned.
 * This would imply modifying the thread running the timer, as well as the control thread.
 * @see ThreadRace
 */
public class ThreadRaceAgainstTheClock<T> extends ThreadRace<T> {

    private static final Logger logger = LogManager.getLogger(ThreadRaceAgainstTheClock.class);

    public ThreadRaceAgainstTheClock(final long delay, final T clockValue) {
        super();
        final ClockRacingRunnable<T> clock = new ClockRacingRunnable<>(delay, clockValue);
        this.addRacer(clock);
        clock.setThread(this.threads.get(0));
    }


    /**
     * Special racer that yields a giver value after a set time.
     */
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
