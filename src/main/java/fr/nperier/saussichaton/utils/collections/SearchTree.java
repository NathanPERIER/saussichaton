package fr.nperier.saussichaton.utils.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Data structure that indexes objects by vectors.
 * @param <S> the type of the objects in the vectors
 * @param <T> the type of the indexed objects
 */
public class SearchTree<S,T> {

    private final Map<S,SearchTree<S,T>> children;
    private T value;

    public SearchTree() {
        children = new HashMap<>();
    }

    /**
     * Sets the value of this vector and return the old value, if any.
     */
    public Optional<T> set(final List<S> path, final T val) {
        return set(path, 0, val);
    }

    private Optional<T> set(final List<S> path, final int index, final T val) {
        if(index >= path.size()) {
            T old = value;
            value = val;
            return Optional.ofNullable(old);
        }
        S elt = path.get(index);
        if(!children.containsKey(elt)) {
            children.put(elt, new SearchTree<>());
        }
        return children.get(elt).set(path, index+1, val);
    }

    /**
     * Retrieves the object associated with this vector, if any
     */
    public Optional<T> get(final List<S> path) {
        return get(path, 0);
    }

    private Optional<T> get(final List<S> path, final int index) {
        if(index >= path.size()) {
            return Optional.ofNullable(value);
        }
        S elt = path.get(index);
        if(!children.containsKey(elt)) {
            return Optional.empty();
        }
        return children.get(elt).get(path, index+1);
    }

    /**
     * Lists all the vectors for which the value matches a certain predicate
     */
    public List<List<S>> listVectors(final Predicate<T> predicate) {
        List<List<S>> res = new ArrayList<>();
        this.listVectors(predicate, new ArrayDeque<>(), res);
        return res;
    }

    private void listVectors(final Predicate<T> predicate, final Deque<S> path, final List<List<S>> res) {
        if(value != null && predicate.test(value)) {
            res.add(new ArrayList<>(path));
        }
        for(Map.Entry<S,SearchTree<S,T>> e : children.entrySet()) {
            path.addLast(e.getKey());
            e.getValue().listVectors(predicate, path, res);
            path.removeLast();
        }
    }

}
