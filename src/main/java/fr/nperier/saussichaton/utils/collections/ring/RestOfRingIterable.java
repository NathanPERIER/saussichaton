package fr.nperier.saussichaton.utils.collections.ring;

import java.util.Iterator;

public class RestOfRingIterable<T extends RingElement<T>> implements Iterable<T> {

    private final T start;

    public RestOfRingIterable(final T start) {
        this.start = start;
    }

    @Override
    public Iterator<T> iterator() {
        return new RestOfRingIterator<T>(start);
    }


    private static class RestOfRingIterator<T extends RingElement<T>> implements Iterator<T> {

        private final T start;
        private T next;

        public RestOfRingIterator(final T start) {
            this.start = start;
            this.next = start.nextNeighbour();
        }

        @Override
        public boolean hasNext() {
            return next != start;
        }

        @Override
        public T next() {
            T res = next;
            next = next.nextNeighbour();
            return res;
        }
    }

}
