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

public class DrawStateTests {

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
    public void testHarmlessCard() {
        final Card c = TestData.CATTERMELON;
        p1.giveCard(TestData.TACOCAT);
        pile.push(c);
        final GameState res = engine.executeState(GameState.DRAW);
        assertEquals(GameState.TURN_END, res);
        assertEquals(0, pile.size());
        assertEquals(List.of(TestData.TACOCAT, c), p1.getHand());
        final Card drawn = engine.getResolver().getNamedObject("drawnCard");
        assertEquals(c, drawn);
    }

    @Test
    public void testExplosionCard() {
        final Card c = TestData.EXPLODING_KITTEN;
        p1.giveCard(TestData.TACOCAT);
        pile.push(c);
        final GameState res = engine.executeState(GameState.DRAW);
        assertEquals(GameState.PRIME_EXPLOSION, res);
        assertEquals(0, pile.size());
        assertEquals(List.of(TestData.TACOCAT), p1.getHand());
        final Card drawn = engine.getResolver().getNamedObject("drawnCard");
        assertEquals(c, drawn);
    }

}
