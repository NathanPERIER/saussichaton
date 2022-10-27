package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.data.Card;

/**
 * Helper to provide easier interactions with a communication channel.
 */
public class ChannelMessageOverlay {

    private final CommChannel channel;

    public ChannelMessageOverlay(final CommChannel channel) {
        this.channel = channel;
    }

    /**
     * Sends a message to all the players who were previously here to indicate that a new player has joined the game.
     * Also sends a message to the new player with a list of the other players.
     *
     * Supposes that the new player has already been added to the ring and to the communication channel.
     */
    public void playerJoin(final Player player, final int nPlayers, final int maxPlayers) {
        channel.broadcastOthers(player + " joined the game (" + nPlayers + "/" + maxPlayers + ")", player.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("You joined the game (").append(nPlayers).append("/").append(maxPlayers).append(")");
        if(!player.isAlone()) {
            builder.append("\nThe other players are :");
            for(Player p : player.getOtherPlayers()) {
                builder.append("\n - ").append(p);
            }
        }
        player.getCommunicator().sendMessage(builder.toString());
    }

}
