package fr.nperier.saussichaton.test;

import fr.nperier.saussichaton.GlobalConstants;
import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.GameState;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.loader.TestGameLoader;
import fr.nperier.saussichaton.networking.Communicator;
import fr.nperier.saussichaton.rules.data.Card;

import java.util.Map;
import java.util.Set;

public class TestData {

    public static final Set<String> EXTENSIONS = Set.of(GlobalConstants.BASE_EXTENSION);
    public static final TestGameLoader LOADER = new TestGameLoader(2, EXTENSIONS);

    public static final Card EXPLODING_KITTEN;
    public static final Card DEFUSE;
    public static final Card ATTACK;
    public static final Card FAVOR;
    public static final Card NOPE;
    public static final Card SHUFFLE;
    public static final Card SKIP;
    public static final Card SEE_THE_FUTURE;
    public static final Card HAIRY_POTATO_CAT;
    public static final Card CATTERMELON;
    public static final Card RAINBOW_RALPHING_CAT;
    public static final Card TACOCAT;
    public static final Card OVERWEIGHT_BIKINI_CAT;

    static {
        final Map<String, Card> cards = LOADER.getRulesLoader().loadCards();
        EXPLODING_KITTEN = cards.get("exploding_kitten");
        DEFUSE = cards.get("defuse");
        ATTACK = cards.get("attack");
        FAVOR = cards.get("favor");
        NOPE = cards.get("nope");
        SHUFFLE = cards.get("shuffle");
        SKIP = cards.get("skip");
        SEE_THE_FUTURE = cards.get("see_the_future");
        HAIRY_POTATO_CAT = cards.get("hairy_potato_cat");
        CATTERMELON = cards.get("cattermelon");
        RAINBOW_RALPHING_CAT = cards.get("rainbow_ralphing_cat");
        TACOCAT = cards.get("tacocat");
        OVERWEIGHT_BIKINI_CAT = cards.get("overweight_bikini_cat");
    }

    public static GameEngine getEngine(final Communicator c1, final Communicator c2) {
        final Player p1 = new Player("Player 1", c1);
        final Player p2 = new Player("Player 2", c2);
        final GameEngine res = new GameEngine(LOADER);
        res.addPlayer(p1);
        res.addPlayer(p2);
        return res;
    }

}
