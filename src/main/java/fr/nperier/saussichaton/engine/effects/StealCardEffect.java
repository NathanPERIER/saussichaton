package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.Map;

/**
 * Generic targeted effect that allows a player to steal cards from another player.
 */
public abstract class StealCardEffect extends CardEffect {

    protected Player target;

    public StealCardEffect(final Player currentPlayer) {
        super(currentPlayer);
    }

    @Override
    public String getName(final Map<Card,Integer> cards) {
        return super.getName(cards) + " on " + target;
    }

    /**
     * Targets a player to steal cards from.
     * @return true if at least one player has cards to steal, else false.
     */
    @Override
    public boolean target() {
        final ListPrompt<Player> targetPrompt = ListPrompt.<Player>create("Select a player to target")
                .addAll(player.getOtherPlayers(), p -> (!p.hasExploded() && p.getHand().size() > 0))
                .build();
        if(targetPrompt.getAvailability().stream().noneMatch(b -> b)) {
            player.getCommunicator().sendMessage("You can't play this since there isn't any other player with cards");
            return false;
        }
        final ListResult<Player> target = player.getCommunicator().choice(targetPrompt);
        this.target = target.getValue();
        return true;
    }

}
