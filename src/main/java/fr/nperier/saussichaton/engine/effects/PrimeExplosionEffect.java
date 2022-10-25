package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.rules.CardEffectRegistry;

@CardEffectRegistry.RegisterEffect("prime_explosion")
public class PrimeExplosionEffect extends CardEffect {

    public PrimeExplosionEffect(final Player currentPlayer) {
        super(currentPlayer);
    }

    @Override
    public GameState execute() {
        return GameState.PRIME_EXPLOSION;
    }

}
