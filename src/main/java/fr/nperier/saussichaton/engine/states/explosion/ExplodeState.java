package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;

/**
 * State during which a player explodes.
 */
public class ExplodeState extends StateAction {

    private final Player currentPlayer;
    private final CommChannel channel;

    public ExplodeState(final Player currentPlayer, final CommChannel channel) {
        this.currentPlayer = currentPlayer;
        this.channel = channel;
    }

    @Override
    public GameState execute() {
        final int nTurn = currentPlayer.getTotalTurnsPlayed() + 1;
        currentPlayer.explode();
        currentPlayer.getCommunicator().sendMessage("You exploded \uD83D\uDCA5" );
        channel.broadcastOthers(currentPlayer + " has exploded after " + nTurn + " turns \uD83D\uDCA5", currentPlayer.getName());
        return GameState.PLAYER_SWITCH;
    }
}
