package fr.nperier.saussichaton.utils.collections;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CollectionUtilsTest {

    @Test
    public void testContainsMore() {
        List<Integer> l1 = List.of(1, 2, 3, 4, 5);
        List<Integer> l2 = List.of(1, 2, 3);
        assertTrue(CollectionUtils.containsAll(l1, l2));
    }

    @Test
    public void testContainsLess() {
        List<Integer> l1 = List.of(1, 2);
        List<Integer> l2 = List.of(1, 2, 3);
        assertFalse(CollectionUtils.containsAll(l1, l2));
    }

    @Test
    public void testSameContent() {
        List<Integer> l1 = List.of(1, 2, 3);
        List<Integer> l2 = List.of(1, 2, 3);
        assertTrue(CollectionUtils.containsAll(l1, l2));
    }

    @Test
    public void testDifferentOrder() {
        List<Integer> l1 = List.of(1, 2, 3);
        List<Integer> l2 = List.of(3, 2, 1);
        assertTrue(CollectionUtils.containsAll(l1, l2));
    }

    @Test
    public void testContainsMultipleMore() {
        List<Integer> l1 = List.of(1, 2, 2, 3, 4, 4, 4, 5);
        List<Integer> l2 = List.of(2, 2, 4, 4, 5);
        assertTrue(CollectionUtils.containsAll(l1, l2));
    }

    @Test
    public void testContainsMultipleLess() {
        List<Integer> l1 = List.of(1, 2, 3);
        List<Integer> l2 = List.of(1, 2, 2, 3);
        assertFalse(CollectionUtils.containsAll(l1, l2));
    }

}
