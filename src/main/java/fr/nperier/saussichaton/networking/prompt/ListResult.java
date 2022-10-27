package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

/**
 * A single result of a list prompt, contains a value and its index in the list.
 * @see ListPrompt
 * @see fr.nperier.saussichaton.networking.Communicator#choice(ListPrompt)
 */
@Getter
public class ListResult<T> {

    private final T value;
    private final int index;

    public ListResult(final T value, final int index) {
        this.value = value;
        this.index = index;
    }

}
