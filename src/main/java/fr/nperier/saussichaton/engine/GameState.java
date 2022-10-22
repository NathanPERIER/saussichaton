package fr.nperier.saussichaton.engine;

import java.util.Optional;

public enum GameState {
    INIT(null);

    private final Class<? extends StateAction> saClass;

    GameState(Class<? extends StateAction> saClass) {
        this.saClass = saClass;
    }

    public Class<? extends StateAction> getActionClass() {
        return saClass;
    }

}
