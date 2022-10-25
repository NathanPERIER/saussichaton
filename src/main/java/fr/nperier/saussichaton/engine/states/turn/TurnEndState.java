package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;

public class TurnEndState extends StateAction {

    private final Player currentPlayer;

    public TurnEndState(final Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public GameState execute() {
        currentPlayer.endTurn();
        if(currentPlayer.getRemainingTurns() > 0) {
            return GameState.TURN_BEGIN;
        }
        return GameState.PLAYER_SWITCH;
    }
}
