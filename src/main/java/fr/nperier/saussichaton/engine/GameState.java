package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.engine.states.begin.DealState;
import fr.nperier.saussichaton.engine.states.begin.InitState;
import fr.nperier.saussichaton.engine.states.begin.PostDealState;
import fr.nperier.saussichaton.engine.states.begin.PreDealState;

public enum GameState {
    INIT(InitState.class),
    PRE_DEAL(PreDealState.class),
    DEAL(DealState.class),
    POST_DEAL(PostDealState.class);

    private final Class<? extends StateAction> saClass;

    GameState(Class<? extends StateAction> saClass) {
        this.saClass = saClass;
    }

    public Class<? extends StateAction> getActionClass() {
        return saClass;
    }

}
