package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreDealStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
    }

    @Test
    public void testPreDealState() {
        final GameEngine engine = TestData.getEngine(c1, c2);
        final GameState res = engine.executeState(GameState.PRE_DEAL);
        assertEquals(res, GameState.DEAL);
        final DrawPile pile = engine.getResolver().getService(DrawPile.class);
        assertEquals(41, pile.size());
    }

}
