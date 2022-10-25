package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.injection.Resolvable;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

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

    public boolean isInteractive() {
        return this.getClass().isAnnotationPresent(Interactive.class);
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Targeted { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Interactive { }

}
