package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;

/**
 * Action executed during a state of the game.
 * Is typically instantiated by the engine via dependency injection.
 */
public abstract class StateAction implements Resolvable {

    /**
     * Method executed during a game state.
     * @return the next state of the game
     */
    public abstract GameState execute();

}
