package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExplodeStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
    }

    @Test
    public void testDealState() {
        final GameEngine engine = TestData.getEngine(c1, c2);
        final Player p1 = engine.getResolver().getNamedObject("currentPlayer");
        assertFalse(p1.hasExploded());
        final GameState res = engine.executeState(GameState.EXPLODE);
        assertTrue(p1.hasExploded());
        assertEquals(GameState.PLAYER_SWITCH, res);
    }

}
