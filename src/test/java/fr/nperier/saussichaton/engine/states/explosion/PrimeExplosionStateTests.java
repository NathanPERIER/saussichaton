package fr.nperier.saussichaton.engine.states.explosion;

import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.networking.TestPromptType;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimeExplosionStateTests {

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
        engine.getResolver().setNamedObject("drawnCard", TestData.EXPLODING_KITTEN);
        pile = engine.getResolver().getService(DrawPile.class);
        pile.push(TestData.CATTERMELON);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
    }

    @Test
    public void testWithDefuse() {
        p1.getHand().addAll(List.of(
                TestData.NOPE,
                TestData.DEFUSE,
                TestData.ATTACK,
                TestData.DEFUSE,
                TestData.TACOCAT
        ));
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(1, 3));
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(3));
        c1.expectPrompt(TestPromptType.INT, 1);
        final GameState res = engine.executeState(GameState.PRIME_EXPLOSION);
        assertEquals(TestData.EXPLODING_KITTEN, pile.get(1));
        assertEquals(List.of(TestData.NOPE,TestData.DEFUSE,TestData.ATTACK,TestData.TACOCAT), p1.getHand());
        assertEquals(GameState.TURN_END, res);
    }

    @Test
    public void testWithoutDefuse() {
        p1.getHand().add(TestData.NOPE);
        final GameState res = engine.executeState(GameState.PRIME_EXPLOSION);
        assertEquals(GameState.EXPLODE, res);
    }

    @Test
    public void testOnlyDefuse() {
        p1.getHand().add(TestData.DEFUSE);
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(0));
        c1.expectPrompt(TestPromptType.INT, 2);
        final GameState res = engine.executeState(GameState.PRIME_EXPLOSION);
        assertEquals(TestData.EXPLODING_KITTEN, pile.get(0));
        assertTrue(p1.getHand().isEmpty());
        assertEquals(GameState.TURN_END, res);
    }

}
