package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.effects.AttackEffect;
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

public class PlayChoiceStateTests {

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
    }

    @Test
    public void testNoCards() {
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.DRAW, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testNoValidOptions() {
        p1.giveCard(TestData.NOPE);
        p1.giveCard(TestData.TACOCAT);
        p1.giveCard(TestData.DEFUSE);
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.DRAW, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testDrawOption() {
        p1.giveCard(TestData.TACOCAT, 2);
        p1.giveCard(TestData.ATTACK);
        c1.expectPrompt(TestPromptType.MULTILIST, List.of());
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.DRAW, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testNoTarget() {
        p1.giveCard(TestData.TACOCAT, 2);
        p1.giveCard(TestData.ATTACK);
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(2));
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.PLAY_OVER, res);
        final CardEffect pendingEffect = engine.getResolver().getNamedObject("pendingCardEffect");
        assertEquals(AttackEffect.class, pendingEffect.getClass());
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
    }

    @Test
    public void testTargetImpossibleIntoSkip() {
        p1.giveCard(TestData.TACOCAT, 2);
        p1.giveCard(TestData.ATTACK);
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(0, 1));
        c1.expectPrompt(TestPromptType.MULTILIST, List.of());
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.DRAW, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testTargetSuccess() {
        p1.giveCard(TestData.TACOCAT, 2);
        p1.giveCard(TestData.ATTACK);
        p2.giveCard(TestData.CATTERMELON, 2);
        c1.expectPrompt(TestPromptType.MULTILIST, List.of(0, 1)); // Cards
        c1.expectPrompt(TestPromptType.LIST, 0);            // Player
        final GameState res = engine.executeState(GameState.PLAY_CHOICE);
        assertEquals(GameState.PLAY_OVER, res);
        final CardEffect pendingEffect = engine.getResolver().getNamedObject("pendingCardEffect");
        assertEquals(StealRandomEffect.class, pendingEffect.getClass());
        assertSame(p1, engine.getResolver().getNamedObject("cardPlayer"));
    }

}
