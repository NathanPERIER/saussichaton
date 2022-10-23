package fr.nperier.saussichaton.networking.prompt;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PartialListPrompt<T> extends ListPrompt<T> {

    protected final List<Boolean> availability;

    private PartialListPrompt(final String message, final List<T> options, final List<Boolean> availability) {
        super(message, options);
        this.availability = availability;
    }

    public static <T> Builder<T> createPartial(final String message) {
        return new Builder<>(message);
    }

    public boolean checkOption(int i) {
        return super.checkOption(i) && availability.get(i);
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

        public Builder<T> add(final T t, final boolean available) {
            options.add(t);
            availability.add(available);
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

        public PartialListPrompt<T> build() {
            return new PartialListPrompt<>(message, options, availability);
        }

    }

}
