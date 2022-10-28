package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Effect that allows a player to steal a card of their choice from another player, if the latter has one.
 * @see StealCardEffect
 */
@CardEffect.Targeted
@CardEffectRegistry.RegisterEffect("steal_designated")
public class StealDesignatedEffect extends StealCardEffect {

    private final CardRegistry cards;
    private final CommChannel channel;

    private Card card;

    public StealDesignatedEffect(final Player currentPlayer, final CardRegistry cards, final CommChannel channel) {
        super(currentPlayer);
        this.cards = cards;
        this.channel = channel;
    }

    /**
     * Prompts the current player for a card in addition to the target.
     * @see StealCardEffect#target
     */
    @Override
    public boolean target() {
        if(!super.target()) {
            return false;
        }
        final ListPrompt<Card> cardPrompt = ListPrompt.<Card>create("Select a card to request")
                .addAll(cards.stream()
                        .filter(c -> c.getDrawAction() == null)
                        .sorted(Card::compareTo)
                        .collect(Collectors.toList())
                ).build();
        final ListResult<Card> card = player.getCommunicator().choice(cardPrompt);
        this.card = card.getValue();
        channel.broadcastOthers(player + " wants to steal " + card + " from " + target, List.of(player.getName(), target.getName()));
        target.getCommunicator().sendMessage(player + " wants to steal " + card + " from you");
        return true;
    }

    @Override
    public Optional<GameState> execute() {
        final int index = target.getHand().indexOf(card);
        if(index >= 0) {
            player.giveCard(card);
            target.getHand().remove(index);
            channel.broadcastOthers(player + " receives " + card + " from " + target, player.getName());
            channel.broadcast("You receive " + card + " from " + target);
        } else {
            channel.broadcastOthers(target + " does not have the card, " + player + " receives nothing" , List.of(player.getName(), target.getName()));
            player.getCommunicator().sendMessage(target + " does not have the card, you receive nothing");
            target.getCommunicator().sendMessage("Since you don't have the card, you don't give anything to " + player);
        }
        return Optional.empty();
    }
}
