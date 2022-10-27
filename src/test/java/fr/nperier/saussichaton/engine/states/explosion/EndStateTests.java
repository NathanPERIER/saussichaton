package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class EndStateTests {

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
        p1.nextNeighbour().explode();
        final GameState res = engine.executeState(GameState.END);
        assertNull(res);
    }

}
