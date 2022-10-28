package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.utils.errors.GameLogicException;
import fr.nperier.saussichaton.networking.Communicator;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.utils.collections.ring.FullRingIterable;
import fr.nperier.saussichaton.utils.collections.ring.RestOfRingIterable;
import fr.nperier.saussichaton.utils.collections.ring.RingElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that hold the data relevant to a player.
 * The player is part of a ring of player (as an analogy to how players typically arrange themselves around a table).
 * @see RingElement
 */
public class Player implements RingElement<Player> {

    /** Next player around the table */
    private Player next;
    /** Previous player around the table */
    private Player prev;

    @Getter
    private final String name;
    @Getter
    private final List<Card> hand;
    @Getter
    private int turnsToPlay;
    @Getter
    private int remainingTurns;
    @Getter
    private int totalTurnsPlayed;
    private boolean exploded;

    private final Communicator comm;

    public Player(final String name, final Communicator comm) {
        this.name = name;
        this.comm = comm;
        this.hand = new ArrayList<>();
        this.totalTurnsPlayed = 0;
        this.turnsToPlay = 0;
        this.remainingTurns = 0;
        this.exploded = false;
        this.next = this;
        this.prev = this;
    }

    public Player nextNeighbour() {
        return this.next;
    }

    public Player prevNeighbour() {
        return this.prev;
    }

    public void giveCard(final Card c) {
        this.hand.add(c);
    }

    public void giveCard(final Card c, int n) {
        for(int i = 0; i < n; i++) {
            this.hand.add(c);
        }
    }

    public void removeCards(final List<Integer> indexes) {
        indexes.stream()
                .sorted(Collections.reverseOrder())
                .forEach(i -> hand.remove(i.intValue()));
    }

    public Iterable<Player> getAllPlayers() {
        return new FullRingIterable<>(this);
    }

    public Iterable<Player> getOtherPlayers() {
        return new RestOfRingIterable<>(this);
    }

    /**
     * Introduces the player in an existing ring for which we know one element,
     * such as the current player will be as far as possible from this element
     * according to the ring natural order
     *
     * @param right the player that will after the current player in the ring
     */
    public void introduceAtLeft(Player right) {
        next = right;
        prev = next.prev;
        next.prev = this;
        prev.next = this;
    }

    // ===== Turns logic =====================================================================

    public void addTurns(int n) {
        this.turnsToPlay += n;
        this.remainingTurns += n;
    }

    /**
     * Method called at the end of a turn.
     */
    public void endTurn() {
        this.totalTurnsPlayed++;
        this.remainingTurns--;
        if(this.remainingTurns == 0) {
            this.turnsToPlay = 0;
        }
    }

    /**
     * Method called at the end of all the turns, resets the temporary counters.
     */
    public void clearTurns() {
        this.turnsToPlay = 0;
        this.remainingTurns = 0;
    }

    /**
     * Retrieves the turns played within the current set of turns the player has to take
     * (NOT the total number of turns since the beginning, this is {@link Player#getTotalTurnsPlayed()}
     */
    public int getTurnsPlayed() {
        return turnsToPlay - remainingTurns;
    }

    // ===== Explosion logic =============================================================

    /**
     * Method to call when the player loses the game.
     * (btw I lost)
     *
     * @return the next player to play
     */
    public Player explode() {
        if (this.exploded) {
            throw new GameLogicException("Player " + this + " has already exploded");
        }
        this.exploded = true;
        prev.next = next;
        next.prev = prev;
        return next;
    }

    public boolean hasExploded() {
        return exploded;
    }

    public Communicator getCommunicator() {
        return this.comm;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
