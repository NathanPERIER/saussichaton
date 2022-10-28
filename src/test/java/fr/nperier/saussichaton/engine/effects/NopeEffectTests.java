package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NopeEffectTests {

    public static final Map<Card, Integer> NOPE_CARDS = new HashMap<>();

    static {
        NOPE_CARDS.put(TestData.NOPE, 1);
    }

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;
    private CardEffect effect;
    private Player p1;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
        p1 = engine.getResolver().getNamedObject("currentPlayer");
        effect = engine.initEffect(SkipEffect.class).get();
        engine.setCardPlayer(p1);
        engine.setPendingCardEffect(effect);
    }

    @Test
    public void testNope() {
        final CardEffect nopeEffect = new NopeEffect(p1, effect);
        assertEquals("Nope", nopeEffect.getName(NOPE_CARDS));
        final Optional<GameState> res = nopeEffect.execute();
        assertTrue(res.isEmpty());
    }

    @Test
    public void testYup() {
        final CardEffect nopeEffect = new NopeEffect(p1, effect);
        final CardEffect yupEffect = new NopeEffect(p1, nopeEffect);
        assertEquals("Yup", yupEffect.getName(NOPE_CARDS));
        final Optional<GameState> res = yupEffect.execute();
        assertTrue(res.isPresent());
        assertEquals(GameState.TURN_END, res.get());
    }

}
