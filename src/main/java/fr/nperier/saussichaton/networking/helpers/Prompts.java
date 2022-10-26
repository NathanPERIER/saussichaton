package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Optional;

public class Prompts {

    public static CardPlayResult promptCardPlay(final String message, final Player player, final GameState state,
                                                final CardPlayTree cardPlays, final GameEngine engine,
                                                final String skipOption) {
        final CardPlayResult res = new CardPlayResult(player);
        final List<Boolean> canUse = cardPlays.canPlay(player.getHand(), state);
        if(canUse.stream().noneMatch(b -> b)) {
            res.setImpossible();
            return res;
        }
        final ListPrompt<Card> prompt = ListPrompt.<Card>create(message)
                .addAll(player.getHand(), canUse)
                .build();
        while(!res.isCompleted() || !res.isSkipped()) {
            final ListResults<Card> cards = player.getCommunicator().multiChoice(prompt, skipOption);
            if(cards.isEmpty()) {
                res.setSkipped();
                return res;
            }
            res.setIndexes(cards.getIndexes());
            final Optional<CardPlay> cardPlay = cardPlays.get(cards.getValues());
            if(cardPlay.isEmpty()) {
                player.getCommunicator().sendMessage("You can't do that now");
                continue;
            }
            res.setCardPlay(cardPlay.get());
            if(cardPlay.get().canPlay(state)) {
                player.getCommunicator().sendMessage("This is not a valid action");
                continue;
            }
            final Optional<CardEffect> effect = engine.initEffect(cardPlay.get().getAction());
            effect.ifPresent(res::setEffect);
        }
        return res;
    }

}
