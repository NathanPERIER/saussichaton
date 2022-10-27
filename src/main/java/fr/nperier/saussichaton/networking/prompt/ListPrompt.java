package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Object to prompt one or several options in a list.
 * @param <T> the type of the options in the list.
 * @see fr.nperier.saussichaton.networking.Communicator#choice(ListPrompt)
 * @see fr.nperier.saussichaton.networking.Communicator#multiChoice(ListPrompt)
 */
@Getter
public class ListPrompt<T> {

    /**The message to explain the reason of the prompt*/
    protected final String message;
    /**The options in the list*/
    protected final List<T> options;
    /**For each option, a boolean indicating if it is actually available for selection or not*/
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

    public List<String> getOptionsText() {
        return options.stream()
                .map(T::toString)
                .collect(Collectors.toList());
    }

    public boolean checkOption(int i) {
        return i < options.size() && i >= 0 && availability.get(i);
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

        public Builder<T> addAll(final Iterable<T> it, final Predicate<T> pred) {
            for(T t : it) {
                options.add(t);
                availability.add(pred.test(t));
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
