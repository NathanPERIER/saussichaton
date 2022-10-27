package fr.nperier.saussichaton.test;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.loader.TestGameLoader;

import java.util.Set;

public class TestData {

    public static final Set<String> EXTENSIONS = Set.of(GlobalConstants.BASE_EXTENSION);
    public static final TestGameLoader LOADER_2 = new TestGameLoader(2, EXTENSIONS);



}
