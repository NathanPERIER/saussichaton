package fr.nperier.saussichaton.utils.errors;

/**
 *  Error related to the logic of the unwinding of the game
 */
public class GameLogicException extends RuntimeException {

    public GameLogicException(final String message) {
        super(message);
    }

}
