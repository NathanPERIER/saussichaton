package fr.nperier.saussichaton.engine.loader;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.rules.loader.RulesLoader;
import fr.nperier.saussichaton.rules.loader.BufferedRulesLoader;

import java.util.Set;

public class TestGameLoader extends SimpleGameLoader {

    private static final Set<String> EXTENSIONS = Set.of(GlobalConstants.BASE_EXTENSION);
    public static final TestGameLoader LOADER_2 = new TestGameLoader(2, EXTENSIONS);
    public static final TestGameLoader LOADER_3 = new TestGameLoader(3, EXTENSIONS);
    public static final TestGameLoader LOADER_4 = new TestGameLoader(4, EXTENSIONS);
    public static final TestGameLoader LOADER_5 = new TestGameLoader(5, EXTENSIONS);

    private final RulesLoader actualRulesLoader;

    public TestGameLoader(final int nPlayers, final Set<String> extensions) {
        super(nPlayers, extensions);
        actualRulesLoader = new BufferedRulesLoader(super.getRulesLoader());
    }

    @Override
    public RulesLoader getRulesLoader() {
        return actualRulesLoader;
    }

}
