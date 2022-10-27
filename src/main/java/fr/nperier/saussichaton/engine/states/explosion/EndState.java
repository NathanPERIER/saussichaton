package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;

/**
 * Last state of the game that announces the winner.
 */
public class EndState extends StateAction {

    private final Player currentPlayer;
    private final CommChannel channel;

    public EndState(final Player currentPlayer, final CommChannel channel) {
        this.currentPlayer = currentPlayer;
        this.channel = channel;
    }

    @Override
    public GameState execute() {
        currentPlayer.getCommunicator().sendMessage("You won the game \uD83C\uDF89"); // And you just lost :p
        channel.broadcastOthers(currentPlayer + " wins \uD83C\uDF89", currentPlayer.getName());
        return null;
    }
}
