package fr.nperier.saussichaton.engine.loader;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.rules.loader.JarRulesLoader;
import fr.nperier.saussichaton.rules.loader.RulesLoader;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Game loader that uses a {@link JarRulesLoader} to load the rules.
 */
public class SimpleGameLoader implements GameLoader {

    private static final Logger logger = LogManager.getLogger(SimpleGameLoader.class);

    private final int nPlayers;
    @Getter
    private final Set<String> extensions;
    @Getter
    private final RulesLoader rulesLoader;

    public SimpleGameLoader(final int nPlayers, final Set<String> extensions) {
        this.nPlayers = nPlayers;
        this.extensions = new HashSet<>(extensions);
        if(this.extensions.add(GlobalConstants.BASE_EXTENSION)) {
            logger.debug("Added default extension " + GlobalConstants.BASE_EXTENSION);
        }
        this.rulesLoader = new JarRulesLoader(nPlayers, extensions);
    }

    @Override
    public int getNumPlayers() {
        return nPlayers;
    }

}
