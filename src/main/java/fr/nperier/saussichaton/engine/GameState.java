package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;

public abstract class GameState implements Resolvable {

    public abstract State execute();

}
