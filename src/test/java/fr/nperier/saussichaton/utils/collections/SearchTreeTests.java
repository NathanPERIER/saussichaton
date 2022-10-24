package fr.nperier.saussichaton.utils.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;
import java.util.Optional;

public class SearchTreeTests {

    private SearchTree<Integer, String> tree;

    @BeforeEach
    public void init() {
        tree = new SearchTree<>();
        tree.set(List.of(), "Hello");
        tree.set(List.of(3, 5), "Demat");
        tree.set(List.of(6, 9), "Bonjour");
        tree.set(List.of(9, 7, 4), "Buenos dias");
    }

    @Test
    public void testGetRoot() {
        Optional<String> res = tree.get(List.of());
        assertTrue(res.isPresent());
        assertEquals("Hello", res.get());
    }

    @Test
    public void testGetBranch() {
        Optional<String> res = tree.get(List.of(3, 5));
        assertTrue(res.isPresent());
        assertEquals("Demat", res.get());
    }

    @Test
    public void testGetAbsent() {
        Optional<String> res = tree.get(List.of(4, 2));
        assertFalse(res.isPresent());
    }

    @Test
    public void testSetNew() {
        Optional<String> old = tree.set(List.of(1, 2, 3), "Yes");
        assertFalse(old.isPresent());
        Optional<String> res = tree.get(List.of(1, 2, 3));
        assertTrue(res.isPresent());
        assertEquals("Yes", res.get());
    }

    @Test
    public void testSetExisting() {
        Optional<String> old = tree.set(List.of(), "World");
        assertTrue(old.isPresent());
        assertEquals("Hello", old.get());
        Optional<String> res = tree.get(List.of());
        assertTrue(res.isPresent());
        assertEquals("World", res.get());
    }

    @Test
    public void testListVectors() {
        List<List<Integer>> res = tree.listVectors(s -> s.startsWith("B"));
        assertEquals(2, res.size());
        assertTrue(res.contains(List.of(6, 9)));
        assertTrue(res.contains(List.of(9, 7, 4)));
    }

}
