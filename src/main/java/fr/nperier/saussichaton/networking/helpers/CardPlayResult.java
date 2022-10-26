package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.rules.data.CardPlay;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CardPlayResult {

    @Getter
    private final Player player;
    private boolean skipped;
    private boolean impossible;
    @Setter @Getter
    private CardPlay cardPlay;
    @Setter @Getter
    private CardEffect effect;
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
