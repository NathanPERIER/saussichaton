package fr.nperier.saussichaton.networking.helpers;

import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.List;

public class ChannelPromptOverlay {

    private final CommChannel channel;

    public ChannelPromptOverlay(final CommChannel channel) {
        this.channel = channel;
    }

    public static ListResults<Card> promptCards(final String message, final Player player) {
        ListPrompt<Card> prompt = ListPrompt.<Card>create(message)
                .addAll(player.getHand())
                .build();
        return player.getCommunicator().multiChoice(prompt);
    }

    public static ListResults<Card> promptCards(final String message, final Player player, final List<Boolean> canUse) {
        ListPrompt<Card> prompt = ListPrompt.<Card>create(message)
                .addAll(player.getHand(), canUse)
                .build();
        return player.getCommunicator().multiChoice(prompt);
    }

    public static ListResults<Card> promptCards(final String message, final Player player, final List<Boolean> canUse,
                                         final String skipOption) {
        ListPrompt<Card> prompt = ListPrompt.<Card>create(message)
                .addAll(player.getHand(), canUse)
                .build();
        return player.getCommunicator().multiChoice(prompt, skipOption);
    }

}
