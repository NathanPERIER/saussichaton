package fr.nperier.saussichaton.utils.io;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Utility class to interact with the content of the jar.
 */
public class JarUtils {

    private static final Logger logger = LogManager.getLogger(JarUtils.class);

    /**
     * Reads an object from a (JSON) file in the jar.
     * @param path the path of the file in the jar
     * @param type the type of the object that must be returned
     * @throws JarException if there is a problem while reading or decoding the file
     */
    public static <T> T readFromJar(final String path, final TypeReference<T> type) throws JarException {
        logger.debug("Attempting to read file at '" + path + "' in JAR");
        try (final InputStream in = JarUtils.class.getResourceAsStream(path)) {
            if(in != null) {
                try(final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    String content = reader.lines().collect(Collectors.joining());
                    return JsonEncoder.decode(content, type);
                }
            } else {
                logger.error("Could not get resource as InputStream. Initialization failed.");
                throw new JarException("Could not read file " + path + " from jar (you probably set the wrong path or forgot to include the file)");
            }
        } catch (IOException e) {
            logger.error("Unhandled IOException when mapping object to type '" + type + "'", e);
            throw new JarException("Error occurred while reading file " + path + " from jar", e);
        }
    }

    public static class JarException extends Exception {
        public JarException(String message) {
            super(message);
        }
        public JarException(String message, Throwable t) {
            super(message, t);
        }
    }

}
