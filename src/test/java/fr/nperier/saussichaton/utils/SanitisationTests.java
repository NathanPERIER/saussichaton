package fr.nperier.saussichaton.utils;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SanitisationTests {

    @Test
    public void testValidInt() {
        Optional<Integer> opt = Sanitisation.sanitisePositiveInteger("12");
        assertTrue(opt.isPresent());
        assertEquals(12, opt.get());
    }

    @Test
    public void testInvalidInt() {
        Optional<Integer> opt = Sanitisation.sanitisePositiveInteger("12a");
        assertTrue(opt.isEmpty());
    }

    @Test
    public void testEmptyString() {
        Optional<Integer> opt = Sanitisation.sanitisePositiveInteger("");
        assertTrue(opt.isEmpty());
    }

}
