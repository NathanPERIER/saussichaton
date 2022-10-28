package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AttackEffectTests {

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
    public void testAttackOneTurnLeft() {
        p1.addTurns(1);
        final Optional<CardEffect> optEffect = engine.initEffect(AttackEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        final Optional<GameState> res = effect.execute();
        assertTrue(res.isPresent());
        assertEquals(GameState.PLAYER_SWITCH, res.get());
        assertEquals(2, p2.getTurnsToPlay());
    }

    @Test
    public void testAttackSeveralTurnLeft() {
        p1.addTurns(2);
        final Optional<CardEffect> optEffect = engine.initEffect(AttackEffect.class);
        assertTrue(optEffect.isPresent());
        final CardEffect effect = optEffect.get();
        final Optional<GameState> res = effect.execute();
        assertTrue(res.isPresent());
        assertEquals(GameState.PLAYER_SWITCH, res.get());
        assertEquals(4, p2.getTurnsToPlay());
    }

}
