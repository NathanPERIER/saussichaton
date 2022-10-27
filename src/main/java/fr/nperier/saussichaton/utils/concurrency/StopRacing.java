package fr.nperier.saussichaton.utils.concurrency;

/**
 * Exception thrown to indicate that the race is over and the other threads should stop.
 * @see ThreadLock#setValue
 */
public class StopRacing extends RuntimeException {
}
