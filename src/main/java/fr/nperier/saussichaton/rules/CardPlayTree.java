package fr.nperier.saussichaton.rules;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.utils.collections.SearchTree;
import fr.nperier.saussichaton.utils.errors.ConfigurationException;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import fr.nperier.saussichaton.rules.loader.RulesLoader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A special kind of read-only tree used to hold the card plays and browse them efficiently
 *
 * @see SearchTree
 */
public class CardPlayTree {

    private final SearchTree<Card, CardPlay> tree;
    /**
     * Used for caching, contains the minimum count required for a card to be playable at a certain game state.
     */
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

    /**
     * Adds a card play to the tree. A card play is identified by a vector of cards, sorted by name.
     *
     * @throws ConfigurationException if there is already a card play associated with the card vector
     */
    protected void add(final CardPlay entry) throws ConfigurationException {
        // Create the vector of cards and sort it
        final List<Card> cards = new ArrayList<>();
        for(Map.Entry<Card, Integer> e : entry.getCards().entrySet()) {
            for(int i = 0; i < e.getValue(); i++) {
                cards.add(e.getKey());
            }
        }
        cards.sort(Card::compareTo);
        // Check if a card play is already associated with the vector
        Optional<CardPlay> opt = tree.set(cards, entry);
        if(opt.isPresent()) {
            throw new ConfigurationException("Duplicate card play detected");
        }
        // Update the minimum counts for all the states
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

    /**
     * Retrieves a card play based on a vector of cards.
     *
     * @param cards the vector of cards (doesn't have to be sorted)
     */
    public Optional<CardPlay> get(final Collection<Card> cards) {
        final List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Card::compareTo);
        return tree.get(sorted);
    }

    /**
     * Based on a list of card (typically the hand of the player) and a game state, computes
     * a list of booleans indicating if the card at the same index may be played during the state.
     */
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
