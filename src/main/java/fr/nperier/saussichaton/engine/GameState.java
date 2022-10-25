package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.engine.states.begin.DealState;
import fr.nperier.saussichaton.engine.states.begin.InitState;
import fr.nperier.saussichaton.engine.states.begin.PostDealState;
import fr.nperier.saussichaton.engine.states.begin.PreDealState;
import fr.nperier.saussichaton.engine.states.turn.PlayerSwitchState;
import fr.nperier.saussichaton.engine.states.turn.TurnBeginState;

public enum GameState {
    INIT(InitState.class),
    PRE_DEAL(PreDealState.class),
    DEAL(DealState.class),
    POST_DEAL(PostDealState.class),
    PLAYER_SWITCH(PlayerSwitchState.class),
    TURN_BEGIN(TurnBeginState.class),
    TURN_CHOICE(null),   // TODO
    PLAY(null),          // TODO
    PLAY_OVER(null),     // TODO
    DRAW(null),          // TODO
    PRIME_EXPLOSION(null), // TODO
    EXPLODE(null),       // TODO
    TURN_END(null),      // TODO
    END(null);           // TODO

    private final Class<? extends StateAction> saClass;

    GameState(Class<? extends StateAction> saClass) {
        this.saClass = saClass;
    }

    public Class<? extends StateAction> getActionClass() {
        return saClass;
    }

}
