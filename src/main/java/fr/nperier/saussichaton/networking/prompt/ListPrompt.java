package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListPrompt<T> {

    private final String message;
    private final List<T> options;

    private ListPrompt(final String message, final List<T> options) {
        this.message = message;
        this.options = options;
    }

    public static <T> ListPromptBuilder<T> create(final String message) {
        return new ListPromptBuilder<>(message);
    }

    public int getNbOptions() {
        return options.size();
    }

    public T getOption(final int i) {
        return options.get(i);
    }


    private static class ListPromptBuilder<T> {

        private final String message;
        private final List<T> options;

        public ListPromptBuilder(final String message) {
            this.message = message;
            this.options = new ArrayList<>();
        }

        public ListPromptBuilder<T> add(final T t) {
            options.add(t);
            return this;
        }

        public ListPromptBuilder<T> addAll(final Iterable<T> it) {
            for(T t : it) {
                options.add(t);
            }
            return this;
        }

        public ListPrompt<T> build() {
            return new ListPrompt<>(message, options);
        }

    }

}
