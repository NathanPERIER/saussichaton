package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;

public class TurnBeginState extends StateAction {

    private final Player currentPlayer;
    private final CommChannel channel;

    public TurnBeginState(final Player currentPlayer, final CommChannel channel) {
        this.currentPlayer = currentPlayer;
        this.channel = channel;
    }

    @Override
    public GameState execute() {
        final String turnProgression;
        if(currentPlayer.getTurnsToPlay() > 1) {
            turnProgression = " (" + (currentPlayer.getTurnsPlayed() + 1)
                    + "/" + currentPlayer.getTurnsToPlay() + ")";
        } else {
            turnProgression = "";
        }
        currentPlayer.getCommunicator().sendMessage("Your turn to play" + turnProgression);
        channel.broadcastOthers(currentPlayer + "'s turn to play" + turnProgression, currentPlayer.getName());
        return GameState.TURN_CHOICE;
    }

}
