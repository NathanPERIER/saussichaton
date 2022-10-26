package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.rules.data.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DrawState extends StateAction {

    private static final Logger logger = LogManager.getLogger(DrawState.class);

    private final Player currentPlayer;
    private final DrawPile pile;
    private final GameEngine engine;

    public DrawState(final Player currentPlayer, final DrawPile pile, final GameEngine engine) {
        this.currentPlayer = currentPlayer;
        this.pile = pile;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        final Card card = pile.draw();
        engine.setDrawnCard(card);
        currentPlayer.getCommunicator().sendMessage("You drew " + card);
        final Optional<CardEffect> effect = engine.initEffect(card.getDrawAction());
        if(effect.isPresent()) {
            logger.trace("Executing draw action " + effect.get().getClass() + " for card " + card);
            final Optional<GameState> nextState = effect.get().execute();
            return nextState.orElse(GameState.TURN_END);
        }
        currentPlayer.giveCard(card);
        return GameState.TURN_END;
    }
}
