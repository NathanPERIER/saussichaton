package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;
import fr.nperier.saussichaton.rules.data.Card;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Effect triggered when playing cards or drawing a card.
 * Typically instantiated by the engine via dependency injection.
 */
public abstract class CardEffect implements Resolvable {

    private static final Logger logger = LogManager.getLogger(CardEffect.class);

    @Getter
    protected final Player player;

    public CardEffect(Player player) {
        this.player = player;
    }

    /**
     * Method executed if the effect class is marked with {@link Targeted}.
     * Allows the player to select options before executing the effect.
     * @return true if it is possible to select the options, else false
     */
    public boolean target() {
        logger.warn("Default implementation of bool target() called");
        return true;
    }

    /**
     * Method executed when the effect is applied.
     * @return optionally a game state to modify the logical flow of the game.
     */
    public abstract Optional<GameState> execute();

    /**
     * Method called when the effect is cancelled.
     * @return optionally a game state to modify the logical flow of the game.
     */
    public Optional<GameState> cancel() {
        return Optional.empty();
    }

    public boolean isTargeted() {
        return this.getClass().isAnnotationPresent(Targeted.class);
    }

    protected String constructName(final Map<Card, Integer> cards, final Function<Card,String> namer) {
        final List<String> cards_repr = cards.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map(e -> namer.apply(e.getKey()) + (e.getValue() > 1 ? " x" + e.getValue() : ""))
                .collect(Collectors.toList());
        return String.join(" + ", cards_repr);
    }

    /**
     * Name for this effect to be displayed.
     * @param cards the cards that were used to trigger the effect.
     */
    public String getName(final Map<Card, Integer> cards) {
        return constructName(cards, Card::toString);
    }


    /**
     * Annotation for card effects that are targeted (require execution of the {@link CardEffect#target()} method)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Targeted { }

    /**
     * Annotation for the card effects that are interactive (require a prompt during execution).
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Interactive { }

}
