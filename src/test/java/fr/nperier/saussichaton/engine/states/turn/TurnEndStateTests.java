package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnEndStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;
    private Player p1;
    private DrawPile pile;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        pile = engine.getResolver().getService(DrawPile.class);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
    }

    @Test
    public void testNoTurnsRemaining() {
        p1.addTurns(1);
        assertEquals(0, p1.getTotalTurnsPlayed());
        assertEquals(0, p1.getTurnsPlayed());
        assertEquals(1, p1.getTurnsToPlay());
        assertEquals(1, p1.getRemainingTurns());
        final GameState res = engine.executeState(GameState.TURN_END);
        assertEquals(GameState.PLAYER_SWITCH, res);
        assertEquals(1, p1.getTotalTurnsPlayed());
        assertEquals(0, p1.getTurnsPlayed());
        assertEquals(0, p1.getTurnsToPlay());
        assertEquals(0, p1.getRemainingTurns());
    }

    @Test
    public void testTurnsRemaining() {
        p1.addTurns(2);
        assertEquals(0, p1.getTotalTurnsPlayed());
        assertEquals(0, p1.getTurnsPlayed());
        assertEquals(2, p1.getTurnsToPlay());
        assertEquals(2, p1.getRemainingTurns());
        final GameState res = engine.executeState(GameState.TURN_END);
        assertEquals(GameState.TURN_BEGIN, res);
        assertEquals(1, p1.getTotalTurnsPlayed());
        assertEquals(1, p1.getTurnsPlayed());
        assertEquals(2, p1.getTurnsToPlay());
        assertEquals(1, p1.getRemainingTurns());
    }

}
