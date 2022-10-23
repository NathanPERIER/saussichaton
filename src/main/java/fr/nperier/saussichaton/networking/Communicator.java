package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.networking.prompt.ListResults;

public interface Communicator extends AutoCloseable {

    void sendMessage(final String message);

    String prompt(final String message);

    int promptInteger(final String message);

    boolean promptYesNo(final String message);

    <T> ListResult<T> choice(final ListPrompt<T> prompt);

    <T> ListResults<T> multiChoice(final ListPrompt<T> prompt);

    void interrupt();

    void close();

}
