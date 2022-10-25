package fr.nperier.saussichaton.rules.data;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.dto.CardEntryDTO;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

@Getter
public class Card {

    private static final Set<GameState> POSSIBLE_STATES = Set.of(GameState.PRE_DEAL, GameState.POST_DEAL);

    private final String id;
    private final String name;
    private final String extension;
    private final int givenAtStart;
    private final int initialNumber;
    private final GameState insertState;
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

    public int compareTo(final Card card) {
        return id.compareTo(card.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

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
