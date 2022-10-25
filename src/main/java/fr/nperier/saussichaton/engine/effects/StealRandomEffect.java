package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@CardEffect.Targeted
@CardEffectRegistry.RegisterEffect("steal_random")
public class StealRandomEffect extends StealCardEffect {

    private final CommChannel channel;

    public StealRandomEffect(final Player currentPlayer, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
    }

    @Override
    public Optional<GameState> execute() {
        int index = ThreadLocalRandom.current().nextInt(0, target.getHand().size());
        final Card card = target.getHand().remove(index);
        player.giveCard(card);
        channel.broadcastOthers(target + " gave a card to " + player, List.of(target.getName(), player.getName()));
        player.getCommunicator().sendMessage(target + " gave you " + card);
        target.getCommunicator().sendMessage("You gave " + card + " to " + player);
        return Optional.empty();
    }
}
