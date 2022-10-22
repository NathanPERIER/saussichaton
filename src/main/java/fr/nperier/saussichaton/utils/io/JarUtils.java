package fr.nperier.saussichaton.utils.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class JarUtils {

    private static final Logger logger = LogManager.getLogger(JarUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readFromJar(final String path, final TypeReference<T> type) throws JarException {
        final T result;
        logger.debug("Attempting to read path '" + path + "' into object of type '" + type + "'");
        try (final InputStream in = JarUtils.class.getResourceAsStream(path)) {
            if(in != null) {
                result = mapper.readValue(in, type);
            } else {
                logger.error("Could not get resource as InputStream. Initialization failed.");
                throw new JarException("Could not read file " + path + " from jar (you probably set the wrong path or forgot to include the file)");
            }
        } catch (IOException e) {
            logger.error("Unhandled IOException when mapping object to type '" + type + "'", e);
            throw new JarException("Could not read file " + path + " from jar (you probably set the wrong path or forgot to include the file)", e);
        }
        return result;
    }

    public static class TypeRef<T> extends TypeReference<T> { }

    public static class JarException extends Exception {
        public JarException(String message) {
            super(message);
        }
        public JarException(String message, Throwable t) {
            super(message, t);
        }
    }

}
