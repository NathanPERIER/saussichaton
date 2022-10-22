package fr.nperier.saussichaton.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MappingFunction<T> implements Function<Integer,T> {

    private static final Pattern INTEGER_REG = Pattern.compile("^-?[0-9]+$");

    private final Map<Integer, T> mapping;
    private final T defaultVal;

    public MappingFunction(Map<String,T> mapping) {
        this.mapping = new HashMap<>();
        T defaultVal = null;
        for(Map.Entry<String,T> e : mapping.entrySet()) {
            if("*".equals(e.getKey())) {
                defaultVal = e.getValue();
            } else if(INTEGER_REG.matcher(e.getKey()).matches()) {
                this.mapping.put(Integer.parseInt(e.getKey()), e.getValue());
            } else {
                throw new IllegalArgumentException("Key \"" + e.getKey() + "\" does not correspond to an integer or *");
            }
        }
        this.defaultVal = defaultVal;
    }

    @Override
    public T apply(Integer i) {
        return mapping.getOrDefault(i, defaultVal);
    }
}
