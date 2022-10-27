package fr.nperier.saussichaton.utils.io;

/**
 * Error related to object serialisation, typically used to encapsulate another error.
 */
public class SerialiseException extends RuntimeException {

    public SerialiseException(final String message, final Throwable t) {
        super(message, t);
    }

}
