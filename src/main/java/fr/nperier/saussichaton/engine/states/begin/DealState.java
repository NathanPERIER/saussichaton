package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.rules.CardRegistry;

public class DealState extends StateAction {

    private final Player firstPlayer;
    private final DrawPile drawPile;
    private final CardRegistry cards;

    public DealState(final Player currentPlayer, final DrawPile drawPile, final CardRegistry cards) {
        this.firstPlayer = currentPlayer;
        this.drawPile = drawPile;
        this.cards = cards;
    }

    @Override
    public GameState execute() {
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
        return GameState.POST_DEAL;
    }
}
