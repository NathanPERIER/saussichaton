package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.helpers.CardPlayResult;
import fr.nperier.saussichaton.networking.helpers.Prompts;
import fr.nperier.saussichaton.rules.CardPlayTree;

public class PlayChoiceState extends StateAction {

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
        if(res.isImpossible()) {
            currentPlayer.getCommunicator().sendMessage("You can't do anything, your only option is to draw");
            return GameState.DRAW;
        }
        if(res.isSkipped()) {
            return GameState.DRAW;
        }
        currentPlayer.removeCards(res.getIndexes());
        engine.setPendingCardEffect(res.getEffect());
        engine.setCardPlayer(currentPlayer);
        channel.broadcastOthers(currentPlayer + " plays " + res.getName(), currentPlayer.getName());
        return GameState.PLAY_OVER;
    }
}
