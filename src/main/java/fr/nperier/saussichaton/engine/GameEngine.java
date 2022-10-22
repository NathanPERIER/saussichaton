package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.loader.GameLoader;
import fr.nperier.saussichaton.injection.Resolver;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardPlayTree;
import fr.nperier.saussichaton.rules.CardRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameEngine {

    private final Resolver resolver;

    // Rules
    private final CardRegistry cards;
    private final CardPlayTree combos;

    // Draw pile
    private final DrawPile drawPile;

    // Players
    private final int nPlayers;
    private final Map<String, Player> players;

    // Communication channel
    private final CommChannel commChannel;

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
        this.resolver.addService(this.commChannel);
        // Game state
        this.currentState = GlobalConstants.BEGIN_STATE;
    }

    /**
     * Adds a player to the game, identified by their name.
     * Caution : this method is not thread safe !
     * @return true if the player could be added, else false.
     */
    public boolean addPlayer(final Player player) {
        if(players.size() >= nPlayers || players.containsKey(player.getName())) {
            return false;
        }
        players.put(player.getName(), player);
        return true;
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    public void setCurrentPlayer(final Player currentPlayer) {
        this.resolver.setNamedObject("currentPlayer", currentPlayer);
    }

    public void setCardPlayer(final Player cardPlayer) {
        this.resolver.setNamedObject("cardPlayer", cardPlayer);
    }

    // TODO current card play

}
