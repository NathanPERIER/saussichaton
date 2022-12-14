package fr.nperier.saussichaton.rules.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.nperier.saussichaton.utils.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import fr.nperier.saussichaton.rules.dto.CardEntryDTO;
import fr.nperier.saussichaton.rules.dto.CardPlayDTO;
import fr.nperier.saussichaton.utils.io.JarUtils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rules loader that uses JSON files contained in the JAR to load the rules.
 *
 * @see RulesLoader
 * @see CardEntryDTO
 * @see CardPlayDTO
 */
public class JarRulesLoader implements RulesLoader {

    private static final String CARDS_PATH = "/game/cards.json";
    private static final String CARD_PLAYS_PATH = "/game/card_plays.json";

    private final int nPlayers;
    private final Set<String> extensions;

    public JarRulesLoader(final int nPlayers, final Set<String> extensions) {
        this.nPlayers = nPlayers;
        this.extensions = extensions;
    }

    private <T> T loadFromJar(String path, final TypeReference<T> type) throws ConfigurationException {
        try {
            return JarUtils.readFromJar(path, type);
        } catch (JarUtils.JarException e) {
            throw new ConfigurationException("Problem occurred while loading resources from " + path, e);
        }
    }

    @Override
    public Map<String, Card> loadCards() throws ConfigurationException {
        final Map<String, CardEntryDTO> dtos = loadFromJar(CARDS_PATH, new TypeReference<>(){});
        return dtos.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(
                        e.getKey(),
                        new Card(e.getKey(), e.getValue(), nPlayers))
                )
                .filter(e -> extensions.contains(e.getValue().getExtension()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<CardPlay> loadCardPlays(CardRegistry cards) throws ConfigurationException {
        final List<CardPlayDTO> dtos = loadFromJar(CARD_PLAYS_PATH, new TypeReference<>(){});
        return dtos.stream()
                .map(dto -> new CardPlay(dto, cards))
                .filter(c -> extensions.contains(c.getExtension()))
                .collect(Collectors.toList());
    }

}
