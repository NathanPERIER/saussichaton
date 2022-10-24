package fr.nperier.saussichaton.rules;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.utils.collections.CollectionUtils;
import fr.nperier.saussichaton.utils.collections.SearchTree;
import fr.nperier.saussichaton.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import fr.nperier.saussichaton.rules.loader.RulesLoader;

import java.util.*;
import java.util.stream.Collectors;

public class CardPlayTree {

    private final SearchTree<Card, CardPlay> tree;
    private final Map<GameState, Map<Card,Integer>> minCountsCache;

    public CardPlayTree() {
        tree = new SearchTree<>();
        minCountsCache = new HashMap<>();
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
        for(GameState state : entry.getStates()) {
            Map<Card,Integer> minCounts = minCountsCache.get(state);
            if(minCounts == null) {
                minCountsCache.put(state, new HashMap<>(entry.getCards()));
            } else {
                for(Map.Entry<Card,Integer> e : entry.getCards().entrySet()) {
                    Integer min = minCounts.get(e.getKey());
                    if(min == null || min > e.getValue()) {
                        minCounts.put(e.getKey(), e.getValue());
                    }
                }
            }
        }
    }

    public Optional<CardPlay> get(final Collection<Card> cards) {
        final List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Card::compareTo);
        return tree.get(sorted);
    }

    public List<Boolean> canPlay(final List<Card> cards, final GameState state) {
        final Map<Card,Integer> counts = new HashMap<>();
        final Map<Card,Integer> minCounts = minCountsCache.get(state);
        if(minCounts == null) {
            return cards.stream()
                    .map(c -> false)
                    .collect(Collectors.toList());
        }
        for(Card c : cards) {
            counts.compute(c, (k,v) -> v == null ? 1 : v+1);
        }
        return cards.stream()
                .map(c -> minCounts.containsKey(c) && counts.get(c) >= minCounts.get(c))
                .collect(Collectors.toList());
    }

}
