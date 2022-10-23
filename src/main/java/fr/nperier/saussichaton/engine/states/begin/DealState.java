package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.helpers.ChannelMessageOverlay;
import fr.nperier.saussichaton.rules.CardRegistry;

public class DealState extends StateAction {

    private final Player firstPlayer;
    private final DrawPile drawPile;
    private final CardRegistry cards;
    private final ChannelMessageOverlay messages;

    public DealState(final Player currentPlayer, final DrawPile drawPile, final CardRegistry cards,
                     ChannelMessageOverlay messages) {
        this.firstPlayer = currentPlayer;
        this.drawPile = drawPile;
        this.cards = cards;
        this.messages = messages;
    }

    @Override
    public GameState execute() {
        messages.dealingCards();
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
            ChannelMessageOverlay.initialHand(player);
        }
        return GameState.POST_DEAL;
    }
}
