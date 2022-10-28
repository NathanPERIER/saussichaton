package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.effects.AttackEffect;
import fr.nperier.saussichaton.engine.effects.NopeEffect;
import fr.nperier.saussichaton.engine.effects.NopeEffectTests;
import fr.nperier.saussichaton.engine.effects.StealRandomEffect;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.networking.TestPromptType;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PlayOverStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;
    private CardEffect effect;
    private Player p1;
    private Player p2;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
        p2 = p1.nextNeighbour();
        effect = engine.initEffect(AttackEffect.class).get();
        engine.setCardPlayer(p1);
        engine.setPendingCardEffect(effect);
    }

    @Test
    public void testNoneHasCards() {
        final GameState res = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_EFFECT, res);
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
        assertSame(effect, engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testNoneCanPlay() {
        p1.giveCard(TestData.NOPE, 2);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.SKIP);
        final GameState res = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_EFFECT, res);
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
        assertSame(effect, engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testCanPlaySkips() {
        p1.giveCard(TestData.NOPE, 2);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.NOPE);
        p2.giveCard(TestData.SKIP);
        c2.expectPrompt(TestPromptType.MULTILIST, List.of());
        final GameState res = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_EFFECT, res);
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
        assertSame(effect, engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testPlaysNope() {
        p1.giveCard(TestData.NOPE, 2);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.NOPE);
        p2.giveCard(TestData.SKIP);
        c2.expectPrompt(TestPromptType.MULTILIST, List.of(1));
        final GameState res = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_OVER, res);
        assertSame(p2, engine.getResolver().getNamedObject("cardPlayer"));
        assertEquals(2, p2.getHand().size());
        final CardEffect pending = engine.getResolver().getNamedObject("pendingCardEffect");
        assertEquals(NopeEffect.class, pending.getClass());
        assertEquals("Nope", pending.getName(NopeEffectTests.NOPE_CARDS));
    }
    @Test
    public void testPlaysNopeTwice() {
        p1.giveCard(TestData.NOPE, 2);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.NOPE);
        p2.giveCard(TestData.SKIP);
        c2.expectPrompt(TestPromptType.MULTILIST, List.of(1));
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(0));
        final GameState res1 = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_OVER, res1);
        final GameState res2 = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_OVER, res2);
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
        assertEquals(1, p1.getHand().size());
        assertEquals(2, p2.getHand().size());
        final CardEffect pending = engine.getResolver().getNamedObject("pendingCardEffect");
        assertEquals(NopeEffect.class, pending.getClass());
        assertEquals("Yup", pending.getName(NopeEffectTests.NOPE_CARDS));
    }

    @Test
    public void testDoesNothing() {
        p1.giveCard(TestData.NOPE, 2);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.NOPE);
        p2.giveCard(TestData.SKIP);
        p2.giveCard(TestData.NOPE);
        c2.expectPrompt(TestPromptType.MULTILIST, List.of(1, 3));
        c2.expectInterrupt(TestPromptType.MULTILIST, 5100);
        final GameState res = engine.executeState(GameState.PLAY_OVER);
        assertEquals(GameState.PLAY_EFFECT, res);
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
        assertSame(effect, engine.getResolver().getNamedObject("pendingCardEffect"));
    }

}
