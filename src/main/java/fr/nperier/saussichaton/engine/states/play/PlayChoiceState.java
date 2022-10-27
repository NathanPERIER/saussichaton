package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.helpers.CardPlayResult;
import fr.nperier.saussichaton.networking.helpers.Prompts;
import fr.nperier.saussichaton.rules.CardPlayTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * State that enables the player to play cards during their turn.
 * Automatically skips to the drawing state if the player can't play.
 */
public class PlayChoiceState extends StateAction {

    private static final Logger logger = LogManager.getLogger(PlayChoiceState.class);

    private final Player currentPlayer;
    private final CardPlayTree cardPlays;
    private final GameState currentState;
    private final CommChannel channel;
    private final GameEngine engine;

    public PlayChoiceState(final Player currentPlayer, final CardPlayTree cardPlays, final GameState currentState,
                           final CommChannel channel, final GameEngine engine) {
        this.currentPlayer = currentPlayer;
        this.cardPlays = cardPlays;
        this.currentState = currentState;
        this.channel = channel;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        CardPlayResult res = Prompts.promptCardPlay(
                "What do you want to do ?",
                currentPlayer, currentState, cardPlays, engine,
                "End turn and draw card"
        );
        if(res.isImpossible()) { // If player can't do anything
            try {
                Thread.sleep(2000); // Sleep for readability
            } catch(InterruptedException e) {
                logger.warn("Got unexpectedly interrupted", e);
            }
            currentPlayer.getCommunicator().sendMessage("You can't do anything, your only option is to draw");
            return GameState.DRAW;
        }
        if(res.isSkipped()) { // If player wants to end turn
            return GameState.DRAW;
        }
        currentPlayer.removeCards(res.getIndexes());
        engine.setPendingCardEffect(res.getEffect()); // Stages the card effect for execution (an maybe nopes)
        engine.setCardPlayer(currentPlayer);
        channel.broadcastOthers(currentPlayer + " plays " + res.getName(), currentPlayer.getName());
        return GameState.PLAY_OVER;
    }
}
