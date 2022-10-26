package fr.nperier.saussichaton;

import fr.nperier.saussichaton.engine.GameEngine;
import fr.nperier.saussichaton.engine.Player;
import fr.nperier.saussichaton.engine.loader.GameLoader;
import fr.nperier.saussichaton.engine.loader.SimpleGameLoader;
import fr.nperier.saussichaton.networking.CommChannel;
import fr.nperier.saussichaton.networking.Communicator;
import fr.nperier.saussichaton.networking.tcp.TCPCommunicator;
import fr.nperier.saussichaton.utils.commandline.ArgsParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.net.*;
import java.util.Set;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    public static void verbose() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.ALL);
        ctx.updateLoggers();
        logger.trace("Verbose enabled");
    }

    public static Thread registerServerSocket(final ServerSocket serverSocket) {
        Thread hook = new Thread(() -> {
            if(!serverSocket.isClosed()) {
                logger.warn("Server socket is not closed, attempting to close");
                try {
                    serverSocket.close();
                    logger.info("Server socket closed");
                } catch (IOException e) {
                    logger.error("Got error while closing the server socket", e);
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        return hook;
    }

    public static void registerSocket(final Socket socket) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(!socket.isClosed()) {
                logger.warn("Attempting to close opened socket");
                try {
                    socket.close();
                    logger.info("Socket successfully closed");
                } catch (IOException e) {
                    logger.error("Got error while closing the server socket", e);
                }
            }
        }));
    }

    public static void main(String[] args) {
        ArgsParser parser;
        try {
            parser = new ArgsParser(args);
        } catch (ArgsParser.ArgsFormatException e) {
            logger.fatal("Got error while parsing the arguments", e);
            ArgsParser.help();
            System.exit(1);
            return;
        }
        if(parser.hasVerbose()) {
            verbose();
        }
        if(parser.getNumPlayers() < GlobalConstants.MIN_PLAYERS || parser.getNumPlayers() > GlobalConstants.MAX_PLAYERS) {
            logger.error("Incorrect number of players : " + parser.getNumPlayers());
            System.exit(1);
            return;
        }

        GameEngine engine = loadGame(parser);

        engine.start();
    }

    private static GameEngine loadGame(final ArgsParser args) {
        GameLoader loader = new SimpleGameLoader(args.getNumPlayers(), Set.of(GlobalConstants.BASE_EXTENSION));
        GameEngine engine = new GameEngine(loader);
        final Thread hook;
        logger.trace("Opening server socket in order to initialise connections");
        try(ServerSocket serverSocket = new ServerSocket()) {
            hook = registerServerSocket(serverSocket);
            SocketAddress addr = new InetSocketAddress(args.getBindAddress(), args.getPort());
            serverSocket.bind(addr, args.getNumPlayers());
            for(int i = 0; i < args.getNumPlayers(); i++) {
                acceptPlayer(serverSocket, engine);
            }
        } catch (Exception e) {
            logger.fatal("Uncaught error during the connection phase", e);
            System.exit(1);
            return null;
        }
        logger.trace("End of the connection phase, server socket is closed");
        Runtime.getRuntime().removeShutdownHook(hook);
        return engine;
    }

    private static void acceptPlayer(final ServerSocket ss, final GameEngine engine) {
        Player player = null;
        while(player == null) {
            try {
                Socket s = ss.accept();
                registerSocket(s);
                logger.trace("Received connection from " + s.getInetAddress().getHostAddress());
                Communicator comm = new TCPCommunicator(s);
                boolean accepted = false;
                Player tempPlayer = null;
                while(!accepted) {
                    String username = comm.prompt(tempPlayer == null ? "Pick a username" : "This username is already taken, please choose another one");
                    logger.trace("Picked username " + username);
                    tempPlayer = new Player(username, comm);
                    accepted = engine.addPlayer(tempPlayer);
                }
                player = tempPlayer;
            } catch(SocketException e) {
                logger.error("Server socket error (probably closed), forcing to stop", e);
                System.exit(1);
            } catch (IOException e) {
                logger.error("Got exception during a player connection", e);
            }
        }
    }

}
