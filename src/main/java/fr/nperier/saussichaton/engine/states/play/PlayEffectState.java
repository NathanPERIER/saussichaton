package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.exceptions.CommunicationInterrupt;
import fr.nperier.saussichaton.networking.helpers.CardPlayResult;
import fr.nperier.saussichaton.networking.helpers.Prompts;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.utils.concurrency.RacingRunnable;
import fr.nperier.saussichaton.utils.concurrency.ThreadRace;
import fr.nperier.saussichaton.utils.concurrency.ThreadRaceAgainstTheClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayEffectState extends StateAction {

    private final CardEffect pendingCardEffect;
    private final GameEngine engine;

    public PlayEffectState(final CardEffect pendingCardEffect, final GameEngine engine) {
        this.pendingCardEffect = pendingCardEffect;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        engine.setPendingCardEffect(null);
        engine.setCardPlayer(null);
        return pendingCardEffect.execute().orElse(GameState.PLAY_CHOICE);
    }

}
