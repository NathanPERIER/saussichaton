package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

@CardEffectRegistry.RegisterEffect("prime_explosion")
public class PrimeExplosionEffect extends CardEffect {

    private final CommChannel channel;
    private final Card currentCard;

    public PrimeExplosionEffect(final Player currentPlayer, final Card currentCard, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
        this.currentCard = currentCard;
    }

    @Override
    public GameState execute() {
        StringBuilder builder = new StringBuilder();
        builder.append(player).append(" drew a");
        if("aeiouy".indexOf(Character.toLowerCase(currentCard.getName().charAt(0))) >= 0) {
            builder.append("n");
        }
        builder.append(" ").append(currentCard).append(" !");
        channel.broadcastOthers(builder.toString(), player.getName());
        return GameState.PRIME_EXPLOSION;
    }

}
