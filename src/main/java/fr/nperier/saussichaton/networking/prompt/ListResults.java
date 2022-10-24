package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ListResults<T> {

    private final List<ListResult<T>> results;

    public ListResults() {
        this.results = List.of();
    }

    public ListResults(final List<ListResult<T>> results) {
        this.results = results;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public List<T> getValues() {
        return results.stream()
                .map(ListResult::getValue)
                .collect(Collectors.toList());
    }

    public List<Integer> getIndexes() {
        return results.stream()
                .map(ListResult::getIndex)
                .collect(Collectors.toList());
    }

}
