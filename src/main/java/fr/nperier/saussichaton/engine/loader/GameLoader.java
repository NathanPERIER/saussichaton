package fr.nperier.saussichaton.engine.loader;

import fr.nperier.saussichaton.rules.loader.RulesLoader;

import java.util.Set;

public interface GameLoader {

    int getNumPlayers();

    Set<String> getExtensions();

    RulesLoader getRulesLoader();

}
