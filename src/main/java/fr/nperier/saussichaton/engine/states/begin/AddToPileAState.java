package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.rules.CardRegistry;

public abstract class AddToPileAState extends StateAction {

    protected final DrawPile drawPile;
    protected final CardRegistry cards;
    protected final GameState currentState;
    protected final GameState nextState;

    public AddToPileAState(final DrawPile drawPile, final CardRegistry cards, final GameState currentState,
                           final GameState nextState) {
        this.drawPile = drawPile;
        this.cards = cards;
        this.currentState = currentState;
        this.nextState = nextState;
    }

    @Override
    public GameState execute() {
        cards.stream()
                .filter(c -> c.getInsertState().equals(currentState))
                .forEach(c -> drawPile.push(c, c.getInitialNumber()));
        drawPile.shuffle();
        return nextState;
    }

}
