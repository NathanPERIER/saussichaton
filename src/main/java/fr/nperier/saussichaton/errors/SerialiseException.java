package fr.nperier.saussichaton.errors;

public class SerialiseException extends RuntimeException {

    public SerialiseException(final String message, final Throwable t) {
        super(message, t);
    }

}
