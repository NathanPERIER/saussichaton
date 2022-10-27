package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;

import java.util.Optional;

/**
 * Effect that shuffles the draw pile.
 */
@CardEffectRegistry.RegisterEffect("shuffle")
public class ShuffleEffect extends CardEffect {

    private final DrawPile drawPile;
    private final CommChannel channel;

    public ShuffleEffect(final Player currentPlayer, final DrawPile drawPile, final CommChannel channel) {
        super(currentPlayer);
        this.drawPile = drawPile;
        this.channel = channel;
    }

    @Override
    public Optional<GameState> execute() {
        drawPile.shuffle();
        channel.broadcast("The draw pile has been thoroughly shuffled");
        return Optional.empty();
    }
}
