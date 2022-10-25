package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.helpers.ChannelMessageOverlay;

import java.util.concurrent.ThreadLocalRandom;

public class InitState extends StateAction {

    private final CommChannel channel;
    private final Player firstPlayer;
    private final GameEngine engine;

    public InitState(final CommChannel channel, final GameEngine engine, final Player currentPlayer) {
        this.channel = channel;
        this.engine = engine;
        this.firstPlayer = currentPlayer;
    }

    @Override
    public GameState execute() {
        channel.broadcast("The game is starting !");
        int rand = ThreadLocalRandom.current().nextInt(0, engine.getPlayers().size());
        Player nextPlayer = firstPlayer;
        for(int i = 0; i < rand; i++) {
            nextPlayer = firstPlayer.nextNeighbour();
        }
        channel.broadcastOthers(nextPlayer + " is the first to go", nextPlayer.getName());
        nextPlayer.getCommunicator().sendMessage("You are the first to go");
        engine.setCurrentPlayer(nextPlayer.prevNeighbour());
        return GameState.PRE_DEAL;
    }
}
