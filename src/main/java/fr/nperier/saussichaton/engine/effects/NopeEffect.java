package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.Map;
import java.util.Optional;

/**
 * Effect played on top of another staged effect, that can prevent this effect from happening.
 */
@CardEffectRegistry.RegisterEffect("nope")
public class NopeEffect extends CardEffect {

    private final CardEffect canceled;
    private final boolean isYup;

    public NopeEffect(final Player currentPlayer, final CardEffect pendingCardEffect) {
        super(currentPlayer);
        this.canceled = pendingCardEffect;
        this.isYup = canceled instanceof NopeEffect;
    }

    /**
     * When playing this effect, the nope cards become yup card.
     */
    @Override
    public String getName(final Map<Card, Integer> cards) {
        if(this.isYup) {
            return this.constructName(cards, c -> {
                final String repr = c.toString();
                return "Nope".equals(repr) ? "Yup" : repr;
            });
        }
        return super.getName(cards);
    }

    @Override
    public Optional<GameState> execute() {
        return canceled.cancel();
    }

    /**
     * If this effect is canceled, actually execute the canceled effect.
     */
    @Override
    public Optional<GameState> cancel() {
        return canceled.execute();
    }

}
