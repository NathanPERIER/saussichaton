package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

@Getter
public class ListResult<T> {

    private final T value;
    private final int index;

    public ListResult(final T value, final int index) {
        this.value = value;
        this.index = index;
    }

}
