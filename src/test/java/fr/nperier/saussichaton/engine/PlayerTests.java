package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.networking.TestCommunicator;
import fr.nperier.saussichaton.utils.errors.GameLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerTests {

    private Player p1;

    @BeforeEach
    public void init() {
        p1 = new Player("Player 1", new TestCommunicator());
    }

    @Test
    public void testIntroduce() {
        final Player p2 = new Player("Player 2", new TestCommunicator());
        final Player p3 = new Player("Player 3", new TestCommunicator());
        p2.introduceAtLeft(p1);
        p3.introduceAtLeft(p1);
        assertSame(p1, p3.nextNeighbour());
        assertSame(p2, p3.prevNeighbour());
        assertSame(p3, p2.nextNeighbour());
        assertSame(p3, p1.prevNeighbour());
    }

    @Test
    public void testExplode() {
        final Player p2 = new Player("Player 2", new TestCommunicator());
        p1.introduceAtLeft(p2);
        assertFalse(p2.hasExploded());
        p2.explode();
        assertTrue(p2.hasExploded());
        assertTrue(p1.isAlone());
        assertSame(p1, p2.nextNeighbour());
        assertThrows(GameLogicException.class, p2::explode);
    }


}
