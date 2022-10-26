package fr.nperier.saussichaton.rules.data;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.CardEffectRegistry;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.dto.CardPlayDTO;
import lombok.Getter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CardPlay {

    private static final Set<GameState> POSSIBLE_STATES = Set.of(
            GameState.PLAY_CHOICE,
            GameState.PLAY_OVER,
            GameState.PRIME_EXPLOSION
    );

    private final String extension;
    private final Map<Card, Integer> cards;
    private final List<GameState> states;
    private final Class<? extends CardEffect> action;

    public CardPlay(final CardPlayDTO dto, final CardRegistry cards) throws ConfigurationException {
        this.extension = dto.getExtension();
        this.cards = dto.getCards().entrySet().stream()
                .map(e -> {
                    Optional<Card> opt = cards.getCard(e.getKey());
                    if(opt.isEmpty()) {
                        throw new ConfigurationException("Unknown card " + e.getKey());
                    }
                    return new AbstractMap.SimpleEntry<>(opt.get(), e.getValue());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.states = dto.getStates();
        for(GameState state : this.states) {
            if(!POSSIBLE_STATES.contains(state)) {
                throw new ConfigurationException("Card play cannot occur at state " + state);
            }
        }
        Optional<Class<? extends CardEffect>> opt = CardEffectRegistry.REGISTRY.getEffect(dto.getAction());
        if(opt.isEmpty()) {
            throw new ConfigurationException("Unknown card effect " + dto.getAction());
        }
        this.action = opt.get();
    }

    public boolean canPlay(final GameState state) {
        return states.contains(state);
    }

}
