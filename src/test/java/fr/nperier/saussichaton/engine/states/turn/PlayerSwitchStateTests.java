package fr.nperier.saussichaton.engine.states.turn;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PlayerSwitchStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;
    private Player p1;
    private Player p2;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
        p2 = p1.nextNeighbour();
        p1.addTurns(1);
    }

    @Test
    public void testNoTurnsInCounter() {
        p1.endTurn();
        assertEquals(0, p1.getRemainingTurns());
        assertEquals(0, p2.getTurnsPlayed());
        assertEquals(0, p2.getTurnsToPlay());
        assertEquals(0, p2.getRemainingTurns());
        final GameState res = engine.executeState(GameState.PLAYER_SWITCH);
        assertEquals(GameState.TURN_BEGIN, res);
        assertEquals(0, p1.getTurnsPlayed());
        assertEquals(0, p1.getTurnsToPlay());
        assertEquals(0, p1.getRemainingTurns());
        final Player current = engine.getResolver().getNamedObject("currentPlayer");
        assertSame(p2, current);
        assertEquals(0, current.getTurnsPlayed());
        assertEquals(1, current.getTurnsToPlay());
        assertEquals(1, current.getRemainingTurns());
    }

    @Test
    public void testTurnsInCounter() {
        p1.endTurn();
        p2.addTurns(2);
        assertEquals(0, p1.getRemainingTurns());
        assertEquals(0, p2.getTurnsPlayed());
        assertEquals(2, p2.getTurnsToPlay());
        assertEquals(2, p2.getRemainingTurns());
        final GameState res = engine.executeState(GameState.PLAYER_SWITCH);
        assertEquals(GameState.TURN_BEGIN, res);
        assertEquals(0, p1.getTurnsPlayed());
        assertEquals(0, p1.getTurnsToPlay());
        assertEquals(0, p1.getRemainingTurns());
        final Player current = engine.getResolver().getNamedObject("currentPlayer");
        assertSame(p2, current);
        assertEquals(0, current.getTurnsPlayed());
        assertEquals(2, current.getTurnsToPlay());
        assertEquals(2, current.getRemainingTurns());
    }

    @Test
    public void testLastPlayerAlive() {
        p1.explode();
        final GameState res = engine.executeState(GameState.PLAYER_SWITCH);
        assertEquals(GameState.END, res);
        final Player current = engine.getResolver().getNamedObject("currentPlayer");
        assertSame(p2, current);
    }

}
