package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.engine.states.begin.DealState;
import fr.nperier.saussichaton.engine.states.begin.InitState;
import fr.nperier.saussichaton.engine.states.begin.PostDealState;
import fr.nperier.saussichaton.engine.states.begin.PreDealState;
import fr.nperier.saussichaton.engine.states.explosion.EndState;
import fr.nperier.saussichaton.engine.states.explosion.ExplodeState;
import fr.nperier.saussichaton.engine.states.explosion.PrimeExplosionState;
import fr.nperier.saussichaton.engine.states.turn.PlayerSwitchState;
import fr.nperier.saussichaton.engine.states.turn.TurnBeginState;
import fr.nperier.saussichaton.engine.states.turn.TurnEndState;

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
    PRIME_EXPLOSION(PrimeExplosionState.class),
    EXPLODE(ExplodeState.class),
    TURN_END(TurnEndState.class),
    END(EndState.class);

    private final Class<? extends StateAction> saClass;

    GameState(Class<? extends StateAction> saClass) {
        this.saClass = saClass;
    }

    public Class<? extends StateAction> getActionClass() {
        return saClass;
    }

}
