package fr.nperier.saussichaton.rules;

import fr.nperier.saussichaton.utils.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.loader.RulesLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Read-only data structure that holds one instance of each card that exists in the game (in the loaded extensions).
 */
public class CardRegistry implements Iterable<Card> {

    private final Map<String, Card> entries;

    public CardRegistry(final Map<String, Card> entries) {
        this.entries = new HashMap<>(entries);
    }

    public static CardRegistry create(final RulesLoader loader) throws ConfigurationException {
        return new CardRegistry(loader.loadCards());
    }

    @Override
    public Iterator<Card> iterator() {
        return entries.values().iterator();
    }

    public Stream<Card> stream() {
        return entries.values().stream();
    }

    public Optional<Card> getCard(final String id) {
        return Optional.ofNullable(entries.get(id));
    }

}
