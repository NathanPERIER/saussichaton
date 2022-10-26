package fr.nperier.saussichaton.engine.states.play;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.StateAction;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.exceptions.CommunicationInterrupt;
import fr.nperier.saussichaton.networking.helpers.CardPlayResult;
import fr.nperier.saussichaton.networking.helpers.Prompts;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.utils.concurrency.RacingRunnable;
import fr.nperier.saussichaton.utils.concurrency.ThreadRace;
import fr.nperier.saussichaton.utils.concurrency.ThreadRaceAgainstTheClock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayOverState extends StateAction {

    private static final Logger logger = LogManager.getLogger(PlayOverState.class);

    private final Player cardPlayer;
    private final GameState currentState;
    private final CardPlayTree cardPlays;
    private final CommChannel channel;
    private final GameEngine engine;

    public PlayOverState(final Player cardPlayer, final GameState currentState, final CardPlayTree cardPlays,
                         final CommChannel channel, final GameEngine engine) {
        this.cardPlayer = cardPlayer;
        this.currentState = currentState;
        this.cardPlays = cardPlays;
        this.channel = channel;
        this.engine = engine;
    }

    @Override
    public GameState execute() {
        ThreadRace<GameState> race = new ThreadRaceAgainstTheClock<>(
                GlobalConstants.NOPE_CLOCK_DELAY_MILLIS, GameState.PLAY_EFFECT
        );
        for(Player p : cardPlayer.getOtherPlayers()) {
            race.addRacer(new NopeRacingRunnable(p));
        }
        return race.go();
    }


    private class NopeRacingRunnable extends RacingRunnable<GameState> {

        private final Player player;

        public NopeRacingRunnable(final Player player) {
            this.player = player;
        }

        private CardPlayResult prompt() {
            return Prompts.promptCardPlay(
                    "What do you want to do ?",
                    player, currentState, cardPlays, engine,
                    "Don't do anything"
            );
        }

        @Override
        public void race() {
            try {
                CardPlayResult res = prompt();
                this.finished = true;
                if(res.isImpossible() || res.isSkipped()) {
                    return;
                }
                synchronized(lock) {
                    if(lock.hasValue()) {
                        return;
                    }
                    player.removeCards(res.getIndexes());
                    engine.setPendingCardEffect(res.getEffect());
                    engine.setCardPlayer(player);
                    lock.setValue(GameState.PLAY_OVER);
                    channel.broadcastOthers(player + " played " + res.getName(), player.getName());
                    player.getCommunicator().sendMessage(res.getName() + " !");
                }
            } catch(CommunicationInterrupt e) {
                logger.trace("Racing runnable communication was interrupted");
            }
        }

        @Override
        public void interrupt() {
            this.player.getCommunicator().interrupt();
        }
    }

}
