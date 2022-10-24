package fr.nperier.saussichaton.networking.tcp;

import fr.nperier.saussichaton.utils.io.JsonEncoder;

import java.util.HashMap;
import java.util.Map;

public class CommunicationDTO {

    private final Map<String, Object> fields;

    private CommunicationDTO() {
        this.fields = new HashMap<>();
    }

    public static CommunicationDTO forType(final String type) {
        return new CommunicationDTO().addField("type", type);
    }

    public CommunicationDTO addField(final String name, final Object obj) {
        this.fields.put(name, obj);
        return this;
    }

    @Override
    public String toString() {
        return JsonEncoder.encode(fields);
    }

}
