package fr.nperier.saussichaton.utils.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonEncoder {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String encode(Object o) throws SerialiseException {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new SerialiseException("Error occurred while encoding an object to JSON", e);
        }
    }

    public static <T> T decode(String s, final TypeReference<T> type) throws SerialiseException {
        try {
            return mapper.readValue(s, type);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new SerialiseException("Error occurred while decoding a JSON object", e);
        }
    }

}
