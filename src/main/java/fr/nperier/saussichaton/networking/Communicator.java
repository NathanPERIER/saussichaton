package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.prompt.ListPrompt;

import java.util.List;

public interface Communicator extends AutoCloseable {

    void sendMessage(final String message);

    String prompt(final String message);

    String choice(final ListPrompt prompt);

    List<String> multiChoice(final ListPrompt prompt);

    void interrupt();

    void close();

}
