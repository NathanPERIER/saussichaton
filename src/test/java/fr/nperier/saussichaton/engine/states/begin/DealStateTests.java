package fr.nperier.saussichaton.engine.states.begin;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DealStateTests {

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
        final DrawPile pile = engine.getResolver().getService(DrawPile.class);
        final Player currentPlayer = engine.getResolver().getNamedObject("currentPlayer");
        pile.push(TestData.NOPE, 70);
        final GameState res = engine.executeState(GameState.DEAL);
        assertEquals(GameState.POST_DEAL, res);
        for(Player p : currentPlayer.getAllPlayers()) {
            assertEquals(8, p.getHand().size());
            assertEquals(1, p.getHand().stream().filter(TestData.DEFUSE::equals).count());
        }
    }

}
