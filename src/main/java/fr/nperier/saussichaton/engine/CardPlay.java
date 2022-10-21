package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;
import lombok.Getter;

public abstract class CardPlay implements Resolvable {

    @Getter
    protected final Player player;

    public CardPlay(Player player) {
        this.player = player;
    }

    public abstract State play();

}
