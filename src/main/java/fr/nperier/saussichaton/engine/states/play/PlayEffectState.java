package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.StateAction;

/**
 * State that applies the effect of a card effect, after potentially one or many nope(s).
 */
public class PlayEffectState extends StateAction {

    private final CardEffect pendingCardEffect;
    private final GameEngine engine;

    public PlayEffectState(final CardEffect pendingCardEffect, final GameEngine engine) {
        this.pendingCardEffect = pendingCardEffect;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        engine.setPendingCardEffect(null);
        engine.setCardPlayer(null);
        return pendingCardEffect.execute().orElse(GameState.PLAY_CHOICE);
    }

}
