package fr.nperier.saussichaton.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Function that takes an integer and outputs an object.
 * It has an internal mapping from integer to object that it uses for known values.
 * If it can't find the integer in the keys of the mapping, it returns a default value.
 */
public class MappingFunction<T> implements Function<Integer,T> {

    private final Map<Integer, T> mapping;
    private final T defaultVal;

    /**
     * We create this function with a map where the keys are string.
     * - If the string is the representation of an integer, it is parsed and the entry is added to the mapping.
     * - Is the string is an asterisk, the value is used as the default value.
     * @throws IllegalArgumentException if a key doesn't match this description
     */
    public MappingFunction(Map<String,T> mapping) throws IllegalArgumentException {
        this.mapping = new HashMap<>();
        T defaultVal = null;
        for(Map.Entry<String,T> e : mapping.entrySet()) {
            if("*".equals(e.getKey())) {
                defaultVal = e.getValue();
            } else {
                Optional<Integer> opt = Sanitisation.sanitisePositiveInteger(e.getKey());
                if(opt.isPresent()) {
                    this.mapping.put(opt.get(), e.getValue());
                } else {
                    throw new IllegalArgumentException("Key \"" + e.getKey() + "\" does not correspond to an integer or *");
                }
            }
        }
        this.defaultVal = defaultVal;
    }

    @Override
    public T apply(Integer i) {
        return mapping.getOrDefault(i, defaultVal);
    }
}
