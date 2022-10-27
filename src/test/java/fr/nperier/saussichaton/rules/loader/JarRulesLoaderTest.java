package fr.nperier.saussichaton.rules.loader;

import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.rules.CardRegistry;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.rules.data.CardPlay;
import fr.nperier.saussichaton.test.TestData;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JarRulesLoaderTest {

    private static final String EXPLODING_KITTEN = "exploding_kitten";
    private static final String DEFUSE = "defuse";


    @TestFactory
    Collection<DynamicTest> testCountsGenerator() {
        return List.of(2, 3, 4, 5).stream()
                .map(n -> DynamicTest.dynamicTest("test" + n + "Players", () -> {
                    final JarRulesLoader loader = new JarRulesLoader(n, TestData.EXTENSIONS);
                    final CardRegistry cards = CardRegistry.create(loader);
                    final Optional<Card> explodingKitten = cards.getCard(EXPLODING_KITTEN);
                    assertTrue(explodingKitten.isPresent());
                    assertEquals(n-1, explodingKitten.get().getInitialNumber());
                    final Optional<Card> defuse = cards.getCard(DEFUSE);
                    assertTrue(defuse.isPresent());
                    assertEquals(n == 5 ? 1 : 2, defuse.get().getInitialNumber());
                    assertEquals(1, defuse.get().getGivenAtStart());
                    final List<CardPlay> plays = loader.loadCardPlays(cards);
                    assertEquals(1, plays.stream().filter(cp -> cp.canPlay(GameState.PLAY_OVER)).count());
                    assertEquals(1, plays.stream().filter(cp -> cp.canPlay(GameState.PRIME_EXPLOSION)).count());
                }))
                .collect(Collectors.toList());
    }

}
