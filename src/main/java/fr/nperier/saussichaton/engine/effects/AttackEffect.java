package fr.nperier.saussichaton.engine.effects;

import fr.nperier.saussichaton.engine.CardEffect;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.rules.CardEffectRegistry;

import java.util.Optional;

@CardEffectRegistry.RegisterEffect("attack")
public class AttackEffect extends CardEffect {

    private final CommChannel channel;

    public AttackEffect(final Player currentPlayer, final CommChannel channel) {
        super(currentPlayer);
        this.channel = channel;
    }

    @Override
    public Optional<GameState> execute() {
        channel.broadcastOthers(player + " attacks and skips all remaining turns", player.getName());
        player.getCommunicator().sendMessage("You attack and skip all remaining turns");
        player.nextNeighbour().addTurns(
                (player.getTurnsToPlay() == 1) ? 2 : (player.getRemainingTurns() + 2)
        );
        return Optional.of(GameState.PLAYER_SWITCH);
    }

}
