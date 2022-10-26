package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.helpers.CardPlayResult;
import fr.nperier.saussichaton.networking.helpers.Prompts;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Optional;

public class PrimeExplosionState extends StateAction {

    private final Player currentPlayer;
    private final CardPlayTree cardPlays;
    private final GameState currentState;
    private final GameEngine engine;

    public PrimeExplosionState(final Player currentPlayer, final CardPlayTree cardPlays, final GameState currentState,
                               final GameEngine engine) {
        this.currentPlayer = currentPlayer;
        this.cardPlays = cardPlays;
        this.currentState = currentState;
        this.engine = engine;
    }

    private CardPlayResult prompt() {
        return Prompts.promptCardPlay(
                "You are going to explode, what do you want to do ?",
                currentPlayer, currentState, cardPlays, engine,
                null
        );
    }

    @Override
    public GameState execute() {
        CardPlayResult res = prompt();
        if(res.isImpossible()) {
            return GameState.EXPLODE;
        }
        currentPlayer.removeCards(res.getIndexes());
        return res.getEffect().execute().orElse(GameState.TURN_END);
    }

}
