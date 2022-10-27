package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.networking.prompt.ListResults;

/**
 * Generic interface for communicating with the clients.
 */
public interface Communicator extends AutoCloseable {

    /**
     * Sends a message to the client.
     */
    void sendMessage(final String message);

    /**
     * Prompts a string to the client. The returned value is guaranteed not to be empty.
     */
    String prompt(final String message);

    /**
     * Prompts an integer in a given range to the client (bounds included).
     */
    int promptInteger(final String message, final int min, final int max);

    /**
     * Prompts the client for exactly one element in a list.
     * The returned element is guaranteed to be available, as defined by the prompt object.
     * @see ListPrompt
     */
    <T> ListResult<T> choice(final ListPrompt<T> prompt);

    /**
     * Prompts the client for some elements in the list.
     * The returned elements are guaranteed to be available, as defined by the prompt object.
     * Moreover, if the prompt is not skippable, at least one element is guaranteed to be returned.
     * @param noneValue the string that describes the action of skipping the prompt, or none if the prompt is not skippable.
     * @see ListPrompt
     */
    <T> ListResults<T> multiChoice(final ListPrompt<T> prompt, final String noneValue);

    default <T> ListResults<T> multiChoice(final ListPrompt<T> prompt) {
        return multiChoice(prompt, null);
    }

    /**
     * Interrupts the current prompt.
     */
    void interrupt();

    /**
     * Closes the communicator and frees the underlying resources.
     */
    void close();

}
