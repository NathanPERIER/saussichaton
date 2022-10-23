package fr.nperier.saussichaton.rules.loader;

import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;

import java.util.List;
import java.util.Map;

public class BufferedRulesLoader implements RulesLoader {

    private final RulesLoader wrapped;

    private Map<String, Card> cards;
    private List<CardPlay> cardPlays;

    public BufferedRulesLoader(final RulesLoader wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Map<String, Card> loadCards() {
        if(cards == null) {
            cards = wrapped.loadCards();
        }
        return cards;
    }

    @Override
    public List<CardPlay> loadCardPlays(final CardRegistry cards) {
        if(cardPlays == null) {
            cardPlays = wrapped.loadCardPlays(cards);
        }
        return cardPlays;
    }
}
