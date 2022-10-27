package fr.nperier.saussichaton.networking;

import fr.nperier.saussichaton.networking.exceptions.CommunicationException;
import fr.nperier.saussichaton.networking.exceptions.CommunicationInterrupt;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class TestCommunicator implements Communicator {

    private final Deque<ExpectedResult> results;
    private boolean interrupted;

    public TestCommunicator() {
        this.results = new ArrayDeque<>();
        this.interrupted = false;
    }

    public void clear() {
        this.interrupted = false;
        this.results.clear();
    }

    public TestCommunicator expectPrompt(final TestPromptType type, final Object value) {
        this.results.push(new ExpectedResult(type, value, false));
        return this;
    }

    public TestCommunicator expectInterrupt(final TestPromptType type, final long beforeMillis) {
        this.results.push(new ExpectedResult(type, beforeMillis, false));
        return this;
    }

    public Object fakePrompt(final TestPromptType type) {
        if(this.results.size() == 0) {
            throw new CommunicationException("No more prompts expected");
        }
        final ExpectedResult res = this.results.pop();
        Assertions.assertEquals(type, res.type);
        if(!res.isInterrupt) {
            synchronized(this) {
                if(this.interrupted) {
                    this.interrupted = false;
                    throw new CommunicationInterrupt();
                }
            }
            return res.value;
        }
        final long delay = (Long) res.value;
        synchronized(this) {
            try {
                wait(delay);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            if(this.interrupted) {
                this.interrupted = false;
                throw new CommunicationInterrupt();
            }
            throw new CommunicationException("Expected interrupt");
        }
    }

    @Override
    public void sendMessage(final String message) { }

    @Override
    public String prompt(final String message) {
        return (String) fakePrompt(TestPromptType.STRING);
    }

    @Override
    public int promptInteger(final String message, final int min, final int max) {
        return (Integer) fakePrompt(TestPromptType.INT);
    }

    @Override
    public <T> ListResult<T> choice(ListPrompt<T> prompt) {
        final int index = (Integer) fakePrompt(TestPromptType.LIST);
        return new ListResult<>(prompt.getOptions().get(index), index);
    }

    @Override
    public <T> ListResults<T> multiChoice(ListPrompt<T> prompt, String noneValue) {
        final List<Integer> indexes = (List<Integer>) fakePrompt(TestPromptType.MULTILIST);
        return new ListResults<>(indexes.stream()
                .map(i -> new ListResult<>(prompt.getOptions().get(i), i))
                .collect(Collectors.toList()));
    }

    @Override
    public synchronized void interrupt() {
        this.interrupted = true;
        this.notify();
    }

    @Override
    public void close() { }


    private static class ExpectedResult {

        public final TestPromptType type;
        public final Object value;
        public final boolean isInterrupt;

        public ExpectedResult(final TestPromptType type, final Object value, final boolean isInterrupt) {
            this.type = type;
            this.value = value;
            this.isInterrupt = isInterrupt;
        }

    }

}
