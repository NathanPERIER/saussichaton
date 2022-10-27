package fr.nperier.saussichaton.networking.exceptions;

/**
 * General exception used for all communications.
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException(final String message) {
        super(message);
    }

    public CommunicationException(final String message, final Throwable t) {
        super(message, t);
    }

}
