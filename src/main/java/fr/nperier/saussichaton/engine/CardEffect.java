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

public abstract class CardEffect implements Resolvable {

    private static final Logger logger = LogManager.getLogger(CardEffect.class);

    @Getter
    protected final Player player;

    public CardEffect(Player player) {
        this.player = player;
    }

    public boolean target() {
        logger.warn("Default implementation of bool target() called");
        return true;
    }

    public abstract Optional<GameState> execute();

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

    public String getName(final Map<Card, Integer> cards) {
        return constructName(cards, Card::toString);
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Targeted { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Interactive { }

}
