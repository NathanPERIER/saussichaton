package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.Optional;

@CardEffect.Interactive
@CardEffectRegistry.RegisterEffect("defuse")
public class DefuseEffect extends CardEffect {

    private final Card drawnCard;
    private final DrawPile drawPile;
    private final CommChannel channel;

    public DefuseEffect(final Player currentPlayer, final Card drawnCard, final DrawPile drawPile,
                        final CommChannel channel) {
        super(currentPlayer);
        this.drawnCard = drawnCard;
        this.drawPile = drawPile;
        this.channel = channel;
    }

    @Override
    public Optional<GameState> execute() {
        int index = player.getCommunicator().promptInteger("You successfully defused the bomb, where do you want to replace it ?",
                0, drawPile.size() + 1);
        drawPile.add(index, drawnCard);
        channel.broadcastOthers(player + " defused the bomb and replaced it somewhere in the pile...", player.getName());
        return Optional.empty();
    }


}
