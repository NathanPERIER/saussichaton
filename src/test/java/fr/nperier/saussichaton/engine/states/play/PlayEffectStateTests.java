package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.DrawPile;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.effects.PeekThreeEffect;
import fr.nperier.saussichaton.engine.effects.SkipEffect;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayEffectStateTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        final Player p1 = engine.getResolver().getNamedObject("currentPlayer");
        engine.setCardPlayer(p1);
    }

    @Test
    public void testNoFlowModification() {
        final DrawPile pile = engine.getResolver().getService(DrawPile.class);
        pile.push(TestData.CATTERMELON);
        pile.push(TestData.ATTACK);
        pile.push(TestData.EXPLODING_KITTEN);
        final Optional<CardEffect> optEffect = engine.initEffect(PeekThreeEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        engine.setPendingCardEffect(effect);
        final GameState res = engine.executeState(GameState.PLAY_EFFECT);
        assertEquals(GameState.PLAY_CHOICE, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

    @Test
    public void testFlowModification() {
        final Optional<CardEffect> optEffect = engine.initEffect(SkipEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        engine.setPendingCardEffect(effect);
        final GameState res = engine.executeState(GameState.PLAY_EFFECT);
        assertEquals(GameState.TURN_END, res);
        assertNull(engine.getResolver().getNamedObject("cardPlayer"));
        assertNull(engine.getResolver().getNamedObject("pendingCardEffect"));
    }

}
