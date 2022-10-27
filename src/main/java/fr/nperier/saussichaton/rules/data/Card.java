package fr.nperier.saussichaton.rules.data;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.dto.CardEntryDTO;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

/**
 * Read-only object representing a card in the game.
 */
@Getter
public class Card {

    private static final Set<GameState> POSSIBLE_STATES = Set.of(GameState.PRE_DEAL, GameState.POST_DEAL);

    private final String id;
    private final String name;
    private final String extension;
    /**Fixed number of card of this type given to each player at the beginning of the game*/
    private final int givenAtStart;
    /**Initial number of cards inserted in draw pile*/
    private final int initialNumber;
    /**State at which the cards are inserted in the pile (basically before or after dealing the cards)*/
    private final GameState insertState;
    /**An effect to trigger when the card is drawn (may be null if no effect)*/
    private final Class<? extends CardEffect> drawAction;

    public Card(final String id, final CardEntryDTO dto, int nPlayers) throws ConfigurationException {
        this.id = id;
        this.name = dto.getName();
        this.extension = dto.getExtension();
        this.givenAtStart = dto.getGivenAtStart();
        this.initialNumber = dto.getInitialNumber(nPlayers);
        this.insertState = dto.getInsertState();
        if(!Card.POSSIBLE_STATES.contains(this.insertState)) {
            throw new ConfigurationException("Card " + this.id + " cannot be inserted at state " + this.insertState);
        }
        if(dto.getDrawAction() == null) {
            this.drawAction = null;
            return;
        }
        Optional<Class<? extends CardEffect>> opt = CardEffectRegistry.REGISTRY.getEffect(dto.getDrawAction());
        if(opt.isEmpty()) {
            throw new ConfigurationException("Unknown card effect " + dto.getDrawAction());
        }
        this.drawAction = opt.get();
    }

    /**
     * A card is greater than another if its name is lexicographically higher (used for sorting)
     */
    public int compareTo(final Card card) {
        return id.compareTo(card.id);
    }

    /**
     * A card's hash is that of its (unique) identifier.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Two cards are equal if they have the same identifier.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof Card)) {
            return false;
        }
        return ((Card)obj).id.equals(id);
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
