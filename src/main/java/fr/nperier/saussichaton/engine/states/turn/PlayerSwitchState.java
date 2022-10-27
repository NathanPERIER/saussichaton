package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;

/**
 * State during which the next player becomes the active player (after the current player finished all their turns).
 * This state is also responsible for checking if there is only one player remaining, in which case they won the game.
 */
public class PlayerSwitchState extends StateAction {

    private final Player currentPlayer;
    private final GameEngine engine;

    public PlayerSwitchState(final Player currentPlayer, final GameEngine engine) {
        this.currentPlayer = currentPlayer;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        currentPlayer.clearTurns();
        final Player newPlayer = currentPlayer.nextNeighbour();
        engine.setCurrentPlayer(newPlayer);
        if(newPlayer.isAlone()) {
            return GameState.END;
        }
        if(newPlayer.getTurnsToPlay() == 0) {
            newPlayer.addTurns(1);
        }
        return GameState.TURN_BEGIN;
    }
}
