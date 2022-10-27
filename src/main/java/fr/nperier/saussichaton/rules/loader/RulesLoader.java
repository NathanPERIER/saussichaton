package fr.nperier.saussichaton.rules.loader;

import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Map;

/**
 * Generic interface for an object that loads the cards and the card plays of the game.
 */
public interface RulesLoader {

    /**
     * Loads all the cards, identified by a unique identifier.
     * @see Card
     */
    Map<String, Card> loadCards();

    /**
     * Loads the card plays, using the cards previously loaded.
     * @see CardPlay
     */
    List<CardPlay> loadCardPlays(final CardRegistry cards);

}
