package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListPrompt<T> {

    protected final String message;
    protected final List<T> options;
    protected final List<Boolean> availability;

    protected ListPrompt(final String message, final List<T> options, final List<Boolean> availability) {
        this.message = message;
        this.options = options;
        this.availability = availability;
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
        return i <= options.size() && i >= 0 && availability.get(i);
    }

    public ListResult<T> getResult(final int i) {
        return new ListResult<>(options.get(i), i);
    }


    public static class Builder<T> {

        private final String message;
        private final List<T> options;
        private final List<Boolean> availability;

        public Builder(final String message) {
            this.message = message;
            this.options = new ArrayList<>();
            this.availability = new ArrayList<>();
        }

        public Builder<T> add(final T t) {
            options.add(t);
            availability.add(true);
            return this;
        }

        public Builder<T> add(final T t, final boolean available) {
            options.add(t);
            availability.add(available);
            return this;
        }

        public Builder<T> addAll(final Iterable<T> it) {
            for(T t : it) {
                options.add(t);
                availability.add(true);
            }
            return this;
        }

        public Builder<T> addAll(final List<T> lt, final List<Boolean> la) {
            if(lt.size() != la.size()) {
                throw new IllegalArgumentException("Both lists must have the same size");
            }
            options.addAll(lt);
            availability.addAll(la);
            return this;
        }

        public ListPrompt<T> build() {
            return new ListPrompt<>(message, options, availability);
        }

    }

}
