package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.rules.CardRegistry;

public class PreDealState extends AddToPileAState {

    public PreDealState(final DrawPile drawPile, final CardRegistry cards, final GameState currentState) {
        super(drawPile, cards, currentState, GameState.DEAL);
    }

}
