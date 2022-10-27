package fr.nperier.saussichaton.utils.collections.ring;

/**
 * This is an interface for an element that is part of a ring.
 * We will define a ring as a special case of the chained list, where the last element is linked to the first one.
 * A ring can be iterated over in two directions :
 *  - the natural order (or direct order) by going to the next element
 *  - the reverse order by going to the previous element
 */
public interface RingElement<T extends RingElement<?>> {

    T nextNeighbour();
    T prevNeighbour();

    default boolean isAlone() {
        return this == nextNeighbour();
    }

}
