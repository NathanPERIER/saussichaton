package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListPrompt<T> {

    protected final String message;
    protected final List<T> options;

    protected ListPrompt(final String message, final List<T> options) {
        this.message = message;
        this.options = options;
    }

    public static <T> Builder<T> create(final String message) {
        return new Builder<>(message);
    }

    public int getNbOptions() {
        return options.size();
    }

    public T getOption(final int i) {
        return options.get(i);
    }

    public boolean checkOption(int i) {
        return i <= options.size() && i >= 0;
    }


    public static class Builder<T> {

        private final String message;
        private final List<T> options;

        public Builder(final String message) {
            this.message = message;
            this.options = new ArrayList<>();
        }

        public Builder<T> add(final T t) {
            options.add(t);
            return this;
        }

        public Builder<T> addAll(final Iterable<T> it) {
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
