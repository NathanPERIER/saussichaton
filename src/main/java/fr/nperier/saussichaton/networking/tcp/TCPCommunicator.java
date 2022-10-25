package fr.nperier.saussichaton.networking.tcp;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.nperier.saussichaton.errors.SerialiseException;
import fr.nperier.saussichaton.networking.exceptions.CommunicationException;
import fr.nperier.saussichaton.networking.Communicator;
import fr.nperier.saussichaton.networking.exceptions.CommunicationInterrupt;
import fr.nperier.saussichaton.networking.prompt.ListPrompt;
import fr.nperier.saussichaton.networking.prompt.ListResult;
import fr.nperier.saussichaton.networking.prompt.ListResults;
import fr.nperier.saussichaton.utils.io.JsonEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TCPCommunicator implements Communicator {

    private static final Logger logger = LogManager.getLogger(TCPCommunicator.class);

    private static final CommunicationDTO DTO_OK = CommunicationDTO.forType("ack").addField("valid", true);
    private static final CommunicationDTO DTO_ERR = CommunicationDTO.forType("ack").addField("valid", false);

    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Socket sock;

    public TCPCommunicator(final Socket sock) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.writer = new PrintWriter(sock.getOutputStream());
        this.sock = sock;
    }

    protected void write(final CommunicationDTO data) {
        this.writer.println(data.toString());
        this.writer.flush();
    }

    protected <T> T read(final TypeReference<T> type) {
        try {
            while(true) {
                final String response = this.reader.readLine();
                if("INTERRUPTED".equals(response)) {
                    logger.info("Read interrupted");
                    throw new CommunicationInterrupt();
                }
                if(response == null) {
                    logger.info("Null response, user probably disconnected");
                    throw new CommunicationException("User disconnected");
                } else {
                    try {
                        return JsonEncoder.decode(response, type);
                    } catch (SerialiseException e) {
                        logger.warn("Got incorrect JSON as a response, sending ERR", e);
                        write(DTO_ERR);
                    }
                }
            }
        } catch (IOException e) {
            throw new CommunicationException("Exception while reading from TCP socket", e);
        }
    }

    @Override
    public void sendMessage(final String message) {
        write(CommunicationDTO.forType("message")
                .addField("message", message));
    }

    @Override
    public String prompt(final String message) {
        write(CommunicationDTO.forType("prompt_string")
                .addField("message", message));
        String response = read(new TypeReference<>(){});
        write(DTO_OK);
        while(response.length() == 0) {
            write(DTO_ERR);
            response = read(new TypeReference<>(){});
        }
        return response;
    }

    @Override
    public int promptInteger(final String message, final int min, final int max) {
        write(CommunicationDTO.forType("prompt_integer")
                .addField("message", message)
                .addField("min", min)
                .addField("max", max));
        int response = read(new TypeReference<>(){});
        while(response < min || response > max) {
            write(DTO_ERR);
            response = read(new TypeReference<>(){});
        }
        write(DTO_OK);
        return response;
    }

    @Override
    public boolean promptYesNo(final String message) {
        write(CommunicationDTO.forType("prompt_boolean")
                .addField("message", message));
        final boolean response = read(new TypeReference<>(){});
        write(DTO_OK);
        return response;
    }

    @Override
    public <T> ListResult<T> choice(final ListPrompt<T> prompt) {
        write(CommunicationDTO.forType("prompt_list")
                .addField("message", prompt.getMessage())
                .addField("options", prompt.getOptions())
                .addField("available", prompt.getAvailability()));
        while(true) {
            int response = read(new TypeReference<>(){});
            if(prompt.checkOption(response)) {
                write(DTO_OK);
                return prompt.getResult(response);
            }
            write(DTO_ERR);
        }
    }

    @Override
    public <T> ListResults<T> multiChoice(final ListPrompt<T> prompt, final String noneValue) {
        write(CommunicationDTO.forType("prompt_list_multi")
                .addField("message", prompt.getMessage())
                .addField("options", prompt.getOptions())
                .addField("available", prompt.getAvailability())
                .addField("none_option", noneValue));
        while(true) {
            int[] response = read(new TypeReference<>(){});
            if(noneValue != null && response.length == 1 && response[0] == prompt.getNbOptions()) {
                return new ListResults<>();
            }
            if(response.length > 0 && Arrays.stream(response).allMatch(prompt::checkOption)) {
                write(DTO_OK);
                return new ListResults<>(Arrays.stream(response)
                        .mapToObj(prompt::getResult)
                        .collect(Collectors.toList()));
            }
            write(DTO_ERR);
        }
    }

    @Override
    public void interrupt() {
        this.write(CommunicationDTO.forType("interrupt"));
    }

    @Override
    public void close() {
        this.write(CommunicationDTO.forType("terminate"));
        try {
            this.sock.close();
        } catch (IOException e) {
            logger.error("Got error while closing a communicator : ", e);
        }
    }
}
