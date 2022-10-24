package fr.nperier.saussichaton.utils.commandline;

import fr.nperier.saussichaton.utils.Sanetisation;
import lombok.Getter;

import java.util.Optional;

public class ArgsParser {

    public static final int DEFAULT_PORT = 6969;
    public static final String DEFAULT_ADDR = "0.0.0.0";

    private final boolean verbose;
    @Getter
    private final int numPlayers;
    @Getter
    private final int port;
    @Getter
    private final String bindAddress;

    public ArgsParser(final String[] args) throws ArgsFormatException {
        int i = 0;
        if(args.length > 0 && ("-h".equals(args[0]) || "--help".equals(args[0]))) {
            help();
            System.exit(0);
        }
        // Verbose
        this.verbose = (args.length > i && "-v".equals(args[i]));
        if(this.verbose) { i++; }
        // Number of players
        if(args.length <= i+1 || ! "-n".equals(args[i])) {
            throw new ArgsFormatException("Expected number of players");
        }
        Optional<Integer> numPlayers = Sanetisation.sanitisePositiveInteger(args[i+1]);
        if(numPlayers.isEmpty()) {
            throw new NumberFormatException("Bad number of players : " + args[i+1]);
        }
        this.numPlayers = numPlayers.get();
        i += 2;
        // Port
        if(args.length > i && "-p".equals(args[i])) {
            i++;
            if(args.length <= i) {
                throw new ArgsFormatException("Expected value for the port argument");
            }
            Optional<Integer> port = Sanetisation.sanitisePositiveInteger(args[i]);
            if(port.isEmpty()) {
                throw new NumberFormatException("Bad port number : " + args[i]);
            }
            this.port = port.get();
            i++;
        } else {
            this.port = DEFAULT_PORT;
        }
        // Bind address
        if(args.length > i && "-b".equals(args[i])) {
            i++;
            if(args.length <= i) {
                throw new ArgsFormatException("Expected value for the bind address argument");
            }
            this.bindAddress = args[i];
            i++;
        } else {
            this.bindAddress = DEFAULT_ADDR;
        }
    }

    public boolean hasVerbose() {
        return verbose;
    }

    public static void help() {
        System.out.println("usage : [-v] -n <num_players> [-p <port>] [-b <bind_address>]");
    }

    public static class ArgsFormatException extends Exception {

        public ArgsFormatException(final String message) {
            super(message);
        }

    }

}
