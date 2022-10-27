package fr.nperier.saussichaton.injection;

/**
 * Error related to dependency injection
 */
public class InjectionException extends RuntimeException {

    public InjectionException(String message) {
        super(message);
    }

    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
