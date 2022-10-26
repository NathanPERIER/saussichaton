package fr.nperier.saussichaton.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MappingFunctionTests {

    @Test
    public void testComputeMapping() {
        final Map<String,Integer> mapping = new HashMap<>();
        mapping.put("1", 2);
        mapping.put("2", 3);
        mapping.put("3", 4);
        final MappingFunction<Integer> f = new MappingFunction<>(mapping);
        assertEquals(2, f.apply(1));
        assertEquals(3, f.apply(2));
        assertEquals(4, f.apply(3));
    }

    @Test
    public void testComputeDefault() {
        final Map<String,Integer> mapping = new HashMap<>();
        mapping.put("3", 5);
        mapping.put("6", 9);
        mapping.put("*", 12);
        final MappingFunction<Integer> f = new MappingFunction<>(mapping);
        assertEquals(12, f.apply(7));
    }

    @Test
    public void testBadFormat() {
        final Map<String,Integer> mapping = new HashMap<>();
        mapping.put("1", 2);
        mapping.put("*", 0);
        mapping.put("h", 3);
        assertThrows(IllegalArgumentException.class, () -> new MappingFunction<>(mapping));
    }

}
