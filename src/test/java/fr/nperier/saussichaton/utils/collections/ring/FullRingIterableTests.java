package fr.nperier.saussichaton.utils.collections.ring;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullRingIterableTests {

    @Test
    public void testOneElement() {
        TestRingElement elt = TestRingElement.makeRing(1);
        assertTrue(elt.isAlone());
        List<Integer> values = TestRingElement.elementsToValues(new FullRingIterable<>(elt));
        assertEquals(List.of(0), values);
    }

    @Test
    public void testMultipleElement() {
        TestRingElement elt = TestRingElement.makeRing(5);
        assertFalse(elt.isAlone());
        List<Integer> values = TestRingElement.elementsToValues(new FullRingIterable<>(elt));
        assertEquals(List.of(0,1,2,3,4), values);
    }

}
