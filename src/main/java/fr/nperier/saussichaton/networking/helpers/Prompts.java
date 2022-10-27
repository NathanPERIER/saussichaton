package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Helper to deal with complicated or frequently used prompts
 */
public class Prompts {

    private static final Logger logger = LogManager.getLogger(Prompts.class);

    /**
     * Prompts a user for a card play.
     *
     * @param message the message that explains why the player must play cards
     * @param player the player that is targetted
     * @param state the current state of the game
     * @param cardPlays the tree holding all the possible card plays
     * @param engine the engine of the game
     * @param skipOption if the player can not play cards, a string that describes the action of doing nothing, else null
     * @return the result of the prompt
     * @see CardPlayResult
     */
    public static CardPlayResult promptCardPlay(final String message, final Player player, final GameState state,
                                                final CardPlayTree cardPlays, final GameEngine engine,
                                                final String skipOption) {
        final CardPlayResult res = new CardPlayResult(player);
        // Check which cards the player can actually play
        final List<Boolean> canUse = cardPlays.canPlay(player.getHand(), state);
        if(canUse.stream().noneMatch(b -> b)) {
            // If no cards can be played, the prompt is impossible
            res.setImpossible();
            return res;
        }
        // Create the prompt object
        final ListPrompt<Card> prompt = ListPrompt.<Card>create(message)
                .addAll(player.getHand(), canUse)
                .build();
        // While the user has not provided a satisfying answer
        while(!res.isCompleted() && !res.isSkipped()) {
            // Ask for a list of cards
            final ListResults<Card> cards = player.getCommunicator().multiChoice(prompt, skipOption);
            if(cards.isEmpty()) {
                // If skipped, return (the communicator is in charge of checking if the prompt is actually skippable)
                res.setSkipped();
                return res;
            }
            res.setIndexes(cards.getIndexes());
            // Check that the cards correspond to a card play
            final Optional<CardPlay> cardPlay = cardPlays.get(cards.getValues());
            if(cardPlay.isEmpty()) {
                player.getCommunicator().sendMessage("This is not a valid action");
                continue;
            }
            res.setCardPlay(cardPlay.get());
            // Check that the card play is playable in the current state
            if(!cardPlay.get().canPlay(state)) {
                logger.debug("Rejected card play at state " + state + ", expected one of " + cardPlay.get().getStates());
                player.getCommunicator().sendMessage("You can't do that now");
                continue;
            }
            // Attempts initialising the effect
            final Optional<CardEffect> effect = engine.initEffect(cardPlay.get().getAction());
            effect.ifPresent(res::setEffect);
        }
        return res;
    }

}
