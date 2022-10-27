package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.rules.data.CardPlay;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Object resulting of a prompt for a card play.
 */
public class CardPlayResult {

    @Getter
    private final Player player;
    /**Indicates if the player chose to not play anything (if available)*/
    private boolean skipped;
    /**Set to true if the player can't actually play any cards*/
    private boolean impossible;
    /**The card play chosen by the player, if any*/
    @Setter @Getter
    private CardPlay cardPlay;
    /**The initialised effect for the card play, if any*/
    @Setter @Getter
    private CardEffect effect;
    /**The indexes of the cards in the hand of the player*/
    @Getter
    private List<Integer> indexes;

    public CardPlayResult(final Player player) {
        this.player = player;
        this.skipped = false;
        this.impossible = false;
        this.effect = null;
    }

    public void setIndexes(final List<Integer> indexes) {
        this.indexes = new ArrayList<>(indexes);
    }

    public void setSkipped() {
        skipped = true;
    }

    public void setImpossible() {
        impossible = true;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public boolean isImpossible() {
        return impossible;
    }

    public boolean isCompleted() {
        return effect != null;
    }

    public String getName() {
        if(effect == null) {
            return null;
        }
        return effect.getName(cardPlay.getCards());
    }

}
