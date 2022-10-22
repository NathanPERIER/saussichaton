package fr.nperier.saussichaton.utils.collections.ring;

import java.util.ArrayList;
import java.util.List;

public class TestRingElement implements RingElement<TestRingElement> {

    private TestRingElement next;
    private TestRingElement prev;

    private final int value;

    private TestRingElement(final int i) {
        this.value = i;
    }

    public static TestRingElement makeRing(final int size) {
        TestRingElement res = new TestRingElement(0);
        res.next = res;
        res.prev = res;
        for(int i = 1; i < size; i++) {
            TestRingElement elt = new TestRingElement(i);
            elt.next = res;
            elt.prev = res.prev;
            res.prev = elt;
            elt.prev.next = elt;
        }
        return res;
    }

    public static List<Integer> elementsToValues(Iterable<TestRingElement> it) {
        List<Integer> res = new ArrayList<>();
        for(TestRingElement elt : it) {
            res.add(elt.value);
        }
        return res;
    }

    @Override
    public TestRingElement nextNeighbour() {
        return next;
    }

    @Override
    public TestRingElement prevNeighbour() {
        return prev;
    }

}
