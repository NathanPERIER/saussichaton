package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.helpers.ChannelMessageOverlay;

import java.util.concurrent.ThreadLocalRandom;

public class InitState extends StateAction {

    private final ChannelMessageOverlay messages;
    private final Player firstPlayer;
    private final GameEngine engine;

    public InitState(final ChannelMessageOverlay messages, final GameEngine engine, final Player currentPlayer) {
        this.messages = messages;
        this.engine = engine;
        this.firstPlayer = currentPlayer;
    }

    @Override
    public GameState execute() {
        messages.gameStart();
        int rand = ThreadLocalRandom.current().nextInt(0, engine.getPlayers().size());
        Player nextPlayer = firstPlayer;
        for(int i = 0; i < rand; i++) {
            nextPlayer = firstPlayer.nextNeighbour();
        }
        engine.setCurrentPlayer(nextPlayer);
        return GameState.PRE_DEAL;
    }
}
