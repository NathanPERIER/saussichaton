package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.rules.CardRegistry;

/**
 * State that adds cards to the draw pile after dealing cards.
 */
public class PostDealState extends AddToPileAState {

    public PostDealState(final DrawPile drawPile, final CardRegistry cards, final GameState currentState) {
        super(drawPile, cards, currentState, GameState.PLAYER_SWITCH);
    }

}
