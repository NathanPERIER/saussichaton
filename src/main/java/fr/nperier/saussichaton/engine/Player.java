package fr.nperier.saussichaton.engine;

import fr.nperier.saussichaton.errors.GameLogicException;
import fr.nperier.saussichaton.networking.Communicator;
import fr.nperier.saussichaton.rules.data.Card;
import fr.nperier.saussichaton.utils.collections.ring.FullRingIterable;
import fr.nperier.saussichaton.utils.collections.ring.RestOfRingIterable;
import fr.nperier.saussichaton.utils.collections.ring.RingElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Player implements RingElement<Player> {

    /** Next player around the table */
    private Player next;
    /** Previous player around the table */
    private Player prev;

    @Getter
    private final String name;
    @Getter
    private final List<Card> hand;
    private int turnsToPlay;
    @Getter
    private int turnsPlayed;
    private boolean exploded;

    private final Communicator comm;

    public Player(final String name, final Communicator comm) {
        this.name = name;
        this.comm = comm;
        this.hand = new ArrayList<>();
        this.turnsPlayed = 0;
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

    public Iterable<Player> getAllPlayers() {
        return new FullRingIterable<>(this);
    }

    public Iterable<Player> getOtherPlayers() {
        return new RestOfRingIterable<>(this);
    }

    public void introduceAtLeft(Player right) {
        next = right;
        prev = next.prev;
        next.prev = this;
        prev.next = this;
    }

    public boolean hasExploded() {
        return exploded;
    }

    public Player explode() {
        if (this.exploded) {
            throw new GameLogicException("Player " + this + " has already exploded");
        }
        this.exploded = true;
        prev.next = next;
        next.prev = prev;
        return next;
    }

    public Communicator getCommunicator() {
        return this.comm;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
