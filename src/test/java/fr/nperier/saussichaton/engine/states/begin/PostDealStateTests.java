package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostDealStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
    }

    @Test
    public void testPostDealState() {
        final GameEngine engine = TestData.getEngine(c1, c2);
        final GameState res = engine.executeState(GameState.POST_DEAL);
        assertEquals(GameState.PLAYER_SWITCH, res);
        final DrawPile pile = engine.getResolver().getService(DrawPile.class);
        assertEquals(3, pile.size());
    }

}
