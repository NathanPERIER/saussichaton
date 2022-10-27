package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.List;
import java.util.Optional;

/**
 * Effect that allows a player to steal a card from another player, but the target can choose which card.
 * @see StealCardEffect
 */
@CardEffect.Targeted
@CardEffect.Interactive
@CardEffectRegistry.RegisterEffect("steal_chosen")
public class StealChosenEffect extends StealCardEffect {

    private final CommChannel channel;

    public StealChosenEffect(final Player currentPlayer, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
    }

    @Override
    public Optional<GameState> execute() {
        ListPrompt<Card> prompt = ListPrompt.<Card>create("Which card do you want to give to " + player + " ?")
                .addAll(target.getHand())
                .build();
        ListResult<Card> res = target.getCommunicator().choice(prompt);
        player.giveCard(target.getHand().get(res.getIndex()));
        player.getCommunicator().sendMessage(target + " gave you " + res.getValue());
        channel.broadcastOthers(target + " gave a card to " + player, List.of(player.getName(), target.getName()));
        return Optional.empty();
    }
}
