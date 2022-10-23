package fr.nperier.saussichaton.utils.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    public static <T> boolean containsAll(final List<T> l1, final List<T> l2) {
        Map<T,Integer> counts = new HashMap<>();
        for(T t : l2) {
            counts.compute(t, (k, v) -> v == null ? 1 : v+1);
        }
        for(T t : l1) {
            counts.computeIfPresent(t, (k, v) -> v-1);
        }
        return counts.values().stream().allMatch(v -> v <= 0);
    }

}
