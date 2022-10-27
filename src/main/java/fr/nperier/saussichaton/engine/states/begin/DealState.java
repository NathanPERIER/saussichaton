package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;

/**
 * State during which we deal the cards to the players.
 */
public class DealState extends StateAction {

    private final Player firstPlayer;
    private final DrawPile drawPile;
    private final CardRegistry cards;
    private final CommChannel channel;

    public DealState(final Player currentPlayer, final DrawPile drawPile, final CardRegistry cards,
                     final CommChannel channel) {
        this.firstPlayer = currentPlayer;
        this.drawPile = drawPile;
        this.cards = cards;
        this.channel = channel;
    }

    @Override
    public GameState execute() {
        channel.broadcast("Dealing cards...");
        cards.stream()
                .forEach(c -> {
                    if(c.getGivenAtStart() > 0) {
                        for(Player player : firstPlayer.getAllPlayers()) {
                            player.giveCard(c, c.getGivenAtStart());
                        }
                    }
                });
        for(int i = 0; i < GlobalConstants.BEGIN_CARD_COUNT; i++) {
            for(Player player : firstPlayer.getAllPlayers()) {
                player.giveCard(drawPile.draw());
            }
        }
        for(Player player : firstPlayer.getAllPlayers()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Your current hand is :");
            for(Card c : player.getHand()) {
                builder.append("\n - ").append(c);
            }
            player.getCommunicator().sendMessage(builder.toString());
        }
        return GameState.POST_DEAL;
    }
}
