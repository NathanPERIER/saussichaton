package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.loader.GameLoader;
import fr.nperier.saussichaton.injection.Resolver;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.helpers.ChannelMessageOverlay;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class that manages the game.
 */
public class GameEngine {

    private final Logger logger = LogManager.getLogger(GameEngine.class);

    // Dependency injection
    @Getter
    private final Resolver resolver;

    // Rules
    private final CardRegistry cards;
    private final CardPlayTree combos;

    // Draw pile
    private final DrawPile drawPile;

    // Players
    private final int nPlayers;
    private final Map<String, Player> players;
    private Player currentPlayer;

    // Communication channel
    private final CommChannel commChannel;
    private final ChannelMessageOverlay messages;

    // Game state
    private GameState currentState;


    public GameEngine(final GameLoader loader) {
        // Resolver
        this.resolver = new Resolver();
        this.resolver.addService(this);
        this.resolver.addService(this.resolver);
        // Rules
        this.cards = CardRegistry.create(loader.getRulesLoader());
        this.combos = CardPlayTree.create(loader.getRulesLoader(), this.cards);
        this.resolver.addService(this.cards);
        this.resolver.addService(this.combos);
        // Draw pile
        this.drawPile = new DrawPile();
        this.resolver.addService(this.drawPile);
        // Players
        this.nPlayers = loader.getNumPlayers();
        this.players = new HashMap<>();
        // Communication channel
        this.commChannel = new CommChannel();
        this.messages = new ChannelMessageOverlay(this.commChannel);
        this.resolver.addService(this.commChannel);
        // Game state
        this.setCurrentState(GlobalConstants.BEGIN_STATE);
    }

    /**
     * Adds a player to the game, identified by their name.
     * Caution : this method is not thread safe !
     * @return true if the player could be added, else false.
     */
    public boolean addPlayer(final Player player) throws IllegalStateException {
        if(players.size() >= nPlayers) {
            throw new IllegalStateException("Maximum number of players has already been reached");
        }
        if (players.containsKey(player.getName())) {
            return false;
        }
        logger.trace("Accepted player " + player);
        if(currentPlayer == null) {
            setCurrentPlayer(player);
        } else {
            player.introduceAtLeft(currentPlayer);
        }
        players.put(player.getName(), player);
        commChannel.addCommunicator(player.getName(), player.getCommunicator());
        messages.playerJoin(player, players.size(), nPlayers);
        return true;
    }

    /**
     * Method that runs the game.
     * Essentially, executes the action associated with the current state and sets the state to
     * the result of this action, until the action returns null and it is the end of the game.
     */
    public void start() {
        try {
            while(this.currentState != null) {
                logger.trace("Entering state " + currentState + " (" + currentState.getActionClass() + ")");
                StateAction action = resolver.resolve(currentState.getActionClass());
                GameState nextState = action.execute();
                this.setCurrentState(nextState);
            }
        } catch(Exception e) {
            logger.fatal("Uncaught exception terminated the game", e);
        }
        for(Player p : players.values()) {
            p.getCommunicator().close();
        }
    }

    /**
     * Initialises a card effect based on a (potentially null) class.
     * Will create the effect via dependency injection and will attempt setting the target if the effect is targeted.
     * @return optionally the initialised effect.
     */
    public Optional<CardEffect> initEffect(final Class<? extends CardEffect> clazz) {
        if(clazz == null) {
            return Optional.empty();
        }
        final CardEffect result = resolver.resolve(clazz);
        if(result.isTargeted()) {
            if(!result.target()) {
                return Optional.empty();
            }
        }
        return Optional.of(result);
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    public void setCurrentPlayer(final Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.resolver.setNamedObject("currentPlayer", currentPlayer);
    }

    private void setCurrentState(final GameState currentState) {
        this.currentState = currentState;
        this.resolver.setNamedObject("currentState", currentState);
    }

    public void setDrawnCard(final Card drawnCard) {
        this.resolver.setNamedObject("drawnCard", drawnCard);
    }

    public void setPendingCardEffect(final CardEffect pendingCardEffect) {
        this.resolver.setNamedObject("pendingCardEffect", pendingCardEffect);
    }

    public void setCardPlayer(final Player cardPlayer) {
        this.resolver.setNamedObject("cardPlayer", cardPlayer);
    }


    // ===== For tests basically ===========================

    public GameState executeState(final GameState state) {
        setCurrentState(state);
        final StateAction action = resolver.resolve(state.getActionClass());
        return action.execute();
    }

}
