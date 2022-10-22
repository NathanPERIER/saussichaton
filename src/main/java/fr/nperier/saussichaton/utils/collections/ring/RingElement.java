package fr.nperier.saussichaton.utils.collections.ring;

public interface RingElement<T extends RingElement<?>> {

    T nextNeighbour();
    T prevNeighbour();

    default boolean isAlone() {
        return this == nextNeighbour();
    }

}
