package fr.nperier.saussichaton.networking.tcp;

import fr.nperier.saussichaton.utils.io.JsonEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Object used to serialise data to JSON, in order to send it on the network.
 * @see TCPCommunicator
 */
public class CommunicationDTO {

    private final Map<String, Object> fields;

    private CommunicationDTO() {
        this.fields = new HashMap<>();
    }

    /**
     * Initialises the object with a type field that will allow the client to know how to treat the message.
     */
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
