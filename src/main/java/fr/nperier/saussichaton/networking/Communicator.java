package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.prompt.ListPrompt;

import java.util.List;

public interface Communicator extends AutoCloseable {

    void sendMessage(final String message);

    String prompt(final String message);

    int promptInteger(final String message);

    boolean promptYesNo(final String message);

    <T> T choice(final ListPrompt<T> prompt);

    <T> List<T> multiChoice(final ListPrompt<T> prompt);

    void interrupt();

    void close();

}
