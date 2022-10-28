package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.networking.TestPromptType;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StealDesignatedEffectTests {

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
    public void testNoTarget() {
        p1.giveCard(TestData.NOPE, 2);
        final Optional<CardEffect> optEffect = engine.initEffect(StealDesignatedEffect.class);
        assertFalse(optEffect.isPresent());
    }

    @Test
    public void testWithTargetNoCard() {
        p2.giveCard(TestData.OVERWEIGHT_BIKINI_CAT);
        c1.expectPrompt(TestPromptType.LIST, 0);
        c1.expectPrompt(TestPromptType.LIST, 0);
        final Optional<CardEffect> optEffect = engine.initEffect(StealDesignatedEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        final Optional<GameState> res = effect.execute();
        assertTrue(res.isEmpty());
        assertEquals(0, p1.getHand().size());
        assertEquals(List.of(TestData.OVERWEIGHT_BIKINI_CAT), p2.getHand());
    }

    @Test
    public void testWithTargetAndCard() {
        p2.giveCard(TestData.FAVOR);
        p2.giveCard(TestData.CATTERMELON);
        p2.giveCard(TestData.NOPE);
        p2.giveCard(TestData.SKIP);
        p2.giveCard(TestData.ATTACK);
        p2.giveCard(TestData.DEFUSE);
        p2.giveCard(TestData.TACOCAT);
        p2.giveCard(TestData.HAIRY_POTATO_CAT);
        p2.giveCard(TestData.RAINBOW_RALPHING_CAT);
        p2.giveCard(TestData.OVERWEIGHT_BIKINI_CAT);
        c1.expectPrompt(TestPromptType.LIST, 0);
        c1.expectPrompt(TestPromptType.LIST, 0);
        final Optional<CardEffect> optEffect = engine.initEffect(StealDesignatedEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        final Optional<GameState> res = effect.execute();
        assertTrue(res.isEmpty());
        assertEquals(1, p1.getHand().size());
        assertEquals(9, p2.getHand().size());
    }

}
