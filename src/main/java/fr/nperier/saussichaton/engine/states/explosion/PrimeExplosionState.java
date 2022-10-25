package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.helpers.ChannelPromptOverlay;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Optional;

public class PrimeExplosionState extends StateAction {

    private final Player currentPlayer;
    private final ChannelPromptOverlay prompts;
    private final CardPlayTree cardPlays;
    private final GameState currentState;
    private final GameEngine engine;

    public PrimeExplosionState(final Player currentPlayer, final ChannelPromptOverlay prompts,
                               final CardPlayTree cardPlays, final GameState currentState,
                               final GameEngine engine) {
        this.currentPlayer = currentPlayer;
        this.prompts = prompts;
        this.cardPlays = cardPlays;
        this.currentState = currentState;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        List<Boolean> canUse = cardPlays.canPlay(currentPlayer.getHand(), currentState);
        if(canUse.stream().anyMatch(b -> b)) {
            while(true) {
                ListResults<Card> res = prompts.promptCards(
                        "You are going to explode, what do you want to do ?",
                        currentPlayer, canUse
                );
                Optional<CardPlay> cardPlay = cardPlays.get(res.getValues());
                if(cardPlay.isPresent()) {
                    currentPlayer.removeCards(res.getIndexes());
                    Optional<CardEffect> effect = engine.initEffect(cardPlay.get().getAction());
                    if(effect.isPresent()) {
                        return effect.get().execute().orElse(GameState.TURN_END);
                    }
                }
            }
        }
        return GameState.EXPLODE;
    }

}
