package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.List;
import java.util.Optional;

/**
 * Generic effect that allows a player to see a certain number of cards on top of the pile.
 */
public class PeekEffect extends CardEffect {

    private final CommChannel channel;
    private final DrawPile drawPile;
    private final int nCards;

    public PeekEffect(final Player currentPlayer, final CommChannel channel, final DrawPile drawPile,
                      final int nCards) {
        super(currentPlayer);
        this.channel = channel;
        this.drawPile = drawPile;
        this.nCards = nCards;
    }

    @Override
    public Optional<GameState> execute() {
        List<Card> top = drawPile.peek(Math.min(nCards, drawPile.size()));
        StringBuilder builder = new StringBuilder();
        builder.append("The cards on top of the pile are :");
        for(Card c : top) {
            builder.append("\n - ").append(c);
        }
        player.getCommunicator().sendMessage(builder.toString());
        channel.broadcastOthers(player + " has seen the top " + nCards + " cards of the pile", player.getName());
        return Optional.empty();
    }

}
