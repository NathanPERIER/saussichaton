package fr.nperier.saussichaton.rules;

import fr.nperier.saussichaton.utils.collections.SearchTree;
import fr.nperier.saussichaton.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import fr.nperier.saussichaton.rules.loader.RulesLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardPlayTree {

    private final SearchTree<Card, CardPlay> tree;

    public CardPlayTree() {
        tree = new SearchTree<>();
    }

    public static CardPlayTree create(final RulesLoader loader, final CardRegistry cards) throws ConfigurationException {
        CardPlayTree result = new CardPlayTree();
        for(CardPlay entry : loader.loadCardPlays(cards)) {
            result.add(entry);
        }
        return result;
    }

    protected void add(final CardPlay entry) throws ConfigurationException {
        final List<Card> cards = new ArrayList<>();
        for(Map.Entry<Card, Integer> e : entry.getCards().entrySet()) {
            for(int i = 0; i < e.getValue(); i++) {
                cards.add(e.getKey());
            }
        }
        cards.sort(Card::compareTo);
        Optional<CardPlay> opt = tree.set(cards, entry);
        if(opt.isPresent()) {
            throw new ConfigurationException("Duplicate card combo detected");
        }
    }

    public Optional<CardPlay> get(final Collection<Card> cards) {
        final List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Card::compareTo);
        return tree.get(sorted);
    }

}
