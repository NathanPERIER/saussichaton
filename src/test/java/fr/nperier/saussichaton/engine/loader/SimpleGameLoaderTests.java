package fr.nperier.saussichaton.engine.loader;

import fr.nperier.saussichaton.GlobalConstants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

public class SimpleGameLoaderTests {

    @Test
    public void testAddBaseExtension() {
        SimpleGameLoader loader = new SimpleGameLoader(3, Set.of("imploding"));
        assertEquals(Set.of("imploding", GlobalConstants.BASE_EXTENSION), loader.getExtensions());
    }

}
