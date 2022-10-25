package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.Optional;

@CardEffectRegistry.RegisterEffect("prime_explosion")
public class PrimeExplosionEffect extends CardEffect {

    private final CommChannel channel;
    private final Card drawnCard;

    public PrimeExplosionEffect(final Player currentPlayer, final Card drawnCard, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
        this.drawnCard = drawnCard;
    }

    @Override
    public Optional<GameState> execute() {
        StringBuilder builder = new StringBuilder();
        builder.append(player).append(" drew a");
        if("aeiouy".indexOf(Character.toLowerCase(drawnCard.getName().charAt(0))) >= 0) {
            builder.append("n");
        }
        builder.append(" ").append(drawnCard).append(" !");
        channel.broadcastOthers(builder.toString(), player.getName());
        return Optional.of(GameState.PRIME_EXPLOSION);
    }

}
