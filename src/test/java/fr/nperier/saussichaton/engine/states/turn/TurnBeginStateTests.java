package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnBeginStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;
    private Player p1;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
    }

    @Test
    public void testOneTurnToPlay() {
        p1.addTurns(1);
        final GameState res = engine.executeState(GameState.TURN_BEGIN);
        assertEquals(GameState.PLAY_CHOICE, res);
    }

    @Test
    public void testSeveralTurnsToPlay() {
        p1.addTurns(2);
        final GameState res = engine.executeState(GameState.TURN_BEGIN);
        assertEquals(GameState.PLAY_CHOICE, res);
    }

}
