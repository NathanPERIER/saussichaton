package fr.nperier.saussichaton.networking;

import java.util.HashMap;
import java.util.Map;

public class CommChannel {

    private final Map<String, Communicator> communicators;

    public CommChannel() {
        this.communicators = new HashMap<>();
    }

    public void addCommunicator(final String name, final Communicator comm) {
        this.communicators.put(name, comm);
    }

    public void broadcast(final String message) {
        for(Communicator comm : communicators.values()) {
            comm.sendMessage(message);
        }
    }

    public void broadcastOthers(final String message, final String commName) {
        communicators.entrySet().stream()
                .filter(e -> !commName.equals(e.getKey()))
                .forEach(e -> e.getValue().sendMessage(message));
    }

}
