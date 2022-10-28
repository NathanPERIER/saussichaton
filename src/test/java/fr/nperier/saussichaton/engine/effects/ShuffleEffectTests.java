package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShuffleEffectTests {

    private final TestCommunicator c1 = new TestCommunicator();
    private final TestCommunicator c2 = new TestCommunicator();
    private GameEngine engine;

    @BeforeEach
    public void init() {
        c1.clear();
        c2.clear();
        engine = TestData.getEngine(c1, c2);
    }

    @Test
    public void testShuffle() {
        final Optional<CardEffect> optEffect = engine.initEffect(ShuffleEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        final Optional<GameState> res = effect.execute();
        // We can't really test reliably that the pile has been shuffled (since random),
        // so we just test the return value ...
        assertTrue(res.isEmpty());
    }

}
