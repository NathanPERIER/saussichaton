package fr.nperier.saussichaton.utils;

import java.util.Optional;
import java.util.regex.Pattern;

public class Sanetisation {

    private static final Pattern POSITIVE_INTEGER_REG = Pattern.compile("^-?[0-9]+$");

    public static Optional<Integer> sanitisePositiveInteger(final String repr) {
        if(POSITIVE_INTEGER_REG.matcher(repr).matches()) {
            return Optional.of(Integer.parseInt(repr));
        }
        return Optional.empty();
    }

}
