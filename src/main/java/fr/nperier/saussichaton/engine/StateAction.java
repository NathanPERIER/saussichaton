package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;

public abstract class StateAction implements Resolvable {

    public abstract GameState execute();

}
