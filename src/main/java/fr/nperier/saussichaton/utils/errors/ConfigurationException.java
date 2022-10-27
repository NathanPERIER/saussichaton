package fr.nperier.saussichaton.utils.errors;

/**
 * Error related to the configuration of the game.
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable t) {
        super(message, t);
    }

}
