package fr.nperier.saussichaton.utils.collections.ring;

import java.util.Iterator;

/**
 * Utility to iterate over a ring in the natural order.
 * The iterable yields all the elements in the ring, including the element given as an argument (in the first position).
 */
public class FullRingIterable<T extends RingElement<T>> implements Iterable<T> {

    private final T start;

    public FullRingIterable(final T start) {
        this.start = start;
    }

    @Override
    public Iterator<T> iterator() {
        return new FullRingIterator<T>(start);
    }


    private static class FullRingIterator<T extends RingElement<T>> implements Iterator<T> {

        private final T start;
        private T next;
        private boolean looped;

        public FullRingIterator(final T start) {
            this.start = start;
            this.next = start;
            this.looped = false;
        }

        @Override
        public boolean hasNext() {
            return !looped;
        }

        @Override
        public T next() {
            T res = next;
            next = next.nextNeighbour();
            if(next == start) {
                looped = true;
            }
            return res;
        }
    }

}
