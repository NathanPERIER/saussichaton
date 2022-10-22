package fr.nperier.saussichaton.errors;

/**
 *  Error for when programmer is stoopid
 */
public class GameLogicException extends RuntimeException {

    public GameLogicException(final String message) {
        super(message);
    }

}
