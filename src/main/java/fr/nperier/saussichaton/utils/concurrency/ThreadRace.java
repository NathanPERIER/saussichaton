package fr.nperier.saussichaton.utils.concurrency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ThreadRace<T> {

    private static final Logger logger = LogManager.getLogger(ThreadRace.class);

    protected final List<RacingThread<T>> threads;
    protected final ThreadLock<T> lock;
    protected final ControlRunnable<T> control;

    public ThreadRace() {
        threads = new ArrayList<>();
        lock = new ThreadLock<>();
        control = new ControlRunnable<>(lock, threads);
    }

    public void addRacer(final RacingRunnable<T> racer) {
        racer.giveLock(lock);
        threads.add(new RacingThread<>(racer));
    }

    public T go() {
        logger.trace("Starting thread race");
        Thread controlThread = new Thread(control);
        logger.trace("Starting control thread");
        controlThread.start();
        logger.trace("Main thread starts waiting for the result");
        T res = lock.getValue();
        logger.trace("Main thread obtained the result, starts interrupting racers");
        for(RacingThread<T> t : threads) {
            // We interrupt the racer, but we let the thread exit properly
            t.interruptRacer();
        }
        try {
            controlThread.join();
        } catch(InterruptedException e) {
            logger.warn("Interrupted while waiting for control thread", e);
        }
        return res;
    }


    private static class ControlRunnable<R> implements Runnable {

        private final ThreadLock<R> lock;
        private final List<RacingThread<R>> threads;

        public ControlRunnable(final ThreadLock<R> lock, final List<RacingThread<R>> threads) {
            this.lock = lock;
            this.threads = threads;
        }

        @Override
        public void run() {
            logger.trace("Control thread starting racing threads");
            for(Thread t : threads) {
                t.start();
            }
            for(int i = 0; i < threads.size(); i++) {
                try {
                    threads.get(i).join();
                } catch(InterruptedException e) {
                    logger.warn("Control thread interrupted while joining thread " + i);
                }
            }
            logger.trace("All threads have been joined, calling notify on the lock");
            synchronized(lock) {
                lock.notifyAll();
            }
            logger.trace("Control thread exiting");
        }
    }

}
