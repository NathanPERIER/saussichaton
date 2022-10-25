package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.networking.prompt.ListResults;

public interface Communicator extends AutoCloseable {

    void sendMessage(final String message);

    String prompt(final String message);

    int promptInteger(final String message, final int min, final int max);

    boolean promptYesNo(final String message);

    <T> ListResult<T> choice(final ListPrompt<T> prompt);

    <T> ListResults<T> multiChoice(final ListPrompt<T> prompt, final String noneValue);

    default <T> ListResults<T> multiChoice(final ListPrompt<T> prompt) {
        return multiChoice(prompt, null);
    }

    void interrupt();

    void close();

}
