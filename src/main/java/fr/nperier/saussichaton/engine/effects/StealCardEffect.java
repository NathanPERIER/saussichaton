package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;

public abstract class StealCardEffect extends CardEffect {

    protected Player target;

    public StealCardEffect(final Player currentPlayer) {
        super(currentPlayer);
    }

    @Override
    public boolean target() {
        final ListPrompt<Player> targetPrompt = ListPrompt.<Player>create("Select a player to target")
                .addAll(player.getOtherPlayers(), p -> (!p.hasExploded() && p.getHand().size() > 0))
                .build();
        if(targetPrompt.getAvailability().stream().noneMatch(b -> b)) {
            return false;
        }
        final ListResult<Player> target = player.getCommunicator().choice(targetPrompt);
        this.target = target.getValue();
        return true;
    }

}
