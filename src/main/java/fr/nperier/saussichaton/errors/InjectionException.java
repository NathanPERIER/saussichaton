package fr.nperier.saussichaton.errors;

public class InjectionException extends RuntimeException {

    public InjectionException(String message) {
        super(message);
    }

    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
