package fr.nperier.saussichaton.rules.loader;

import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Map;

public interface RulesLoader {

    Map<String, Card> loadCards();

    List<CardPlay> loadCardPlays(final CardRegistry cards);

}
