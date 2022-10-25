package fr.nperier.saussichaton.rules;

import fr.nperier.saussichaton.engine.CardEffect;
import org.reflections.Reflections;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.*;

public class CardEffectRegistry {

    public static final CardEffectRegistry REGISTRY = new CardEffectRegistry();

    private static final String EFFECTS_PACKAGE = "fr.nperier.saussichaton.engine.effects";

    private final Map<String, Class<? extends CardEffect>> effects;

    @SuppressWarnings("unchecked")
    private CardEffectRegistry() {
        Reflections reflections = new Reflections(EFFECTS_PACKAGE);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RegisterEffect.class);
        this.effects = classes.stream()
                .filter(CardEffect.class::isAssignableFrom) // The cast is actually checked here
                .collect(Collectors.toMap(
                        c -> c.getAnnotation(RegisterEffect.class).value(),
                        c -> (Class<? extends CardEffect>) c)
                );
    }

    public Optional<Class<? extends CardEffect>> getEffect(final String name) {
        return Optional.ofNullable(effects.get(name));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface RegisterEffect {
        String value();
    }

}
