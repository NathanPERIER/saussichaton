package fr.nperier.saussichaton.engine.loader;

import fr.nperier.saussichaton.rules.loader.RulesLoader;
import fr.nperier.saussichaton.rules.loader.BufferedRulesLoader;

import java.util.Set;

public class TestGameLoader extends SimpleGameLoader {

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
