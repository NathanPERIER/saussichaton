package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;
import lombok.Getter;

public abstract class CardEffect implements Resolvable {

    @Getter
    protected final Player player;

    public CardEffect(Player player) {
        this.player = player;
    }

    public abstract GameState play();

}
