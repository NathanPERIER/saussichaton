package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;

/**
 * Effect that allows a player to see at most three cards on top of the pile.
 */
@CardEffectRegistry.RegisterEffect("peek_three")
public class PeekThreeEffect extends PeekEffect {

    public PeekThreeEffect(final Player currentPlayer, final CommChannel channel, final DrawPile drawPile) {
        super(currentPlayer, channel, drawPile, 3);
    }

}
