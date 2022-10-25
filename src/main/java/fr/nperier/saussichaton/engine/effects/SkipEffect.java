package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;

@CardEffectRegistry.RegisterEffect("skip")
public class SkipEffect extends CardEffect {

    private final CommChannel channel;

    public SkipEffect(final Player currentPlayer, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
    }

    @Override
    public GameState execute() {
        player.getCommunicator().sendMessage("You skipped this turn");
        channel.broadcastOthers(player + "skipped a turn", player.getName());
        return GameState.TURN_END;
    }
}
