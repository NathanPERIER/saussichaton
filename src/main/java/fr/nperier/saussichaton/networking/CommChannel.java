package fr.nperier.saussichaton.networking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Channel that allows to broadcast messages to communicators identified by a unique name.
 */
public class CommChannel {

    private final Map<String, Communicator> communicators;

    public CommChannel() {
        this.communicators = new HashMap<>();
    }

    public void addCommunicator(final String name, final Communicator comm) {
        this.communicators.put(name, comm);
    }

    /**
     * Sends a message to all known communicators.
     */
    public void broadcast(final String message) {
        for(Communicator comm : communicators.values()) {
            comm.sendMessage(message);
        }
    }

    /**
     * Sends a message to all but one communicator.
     */
    public void broadcastOthers(final String message, final String commName) {
        communicators.entrySet().stream()
                .filter(e -> !commName.equals(e.getKey()))
                .forEach(e -> e.getValue().sendMessage(message));
    }

    /**
     * Sends a message to all communicators that are not specified in the list.
     */
    public void broadcastOthers(final String message, final List<String> commNames) {
        communicators.entrySet().stream()
                .filter(e -> !commNames.contains(e.getKey()))
                .forEach(e -> e.getValue().sendMessage(message));
    }

}
