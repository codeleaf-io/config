package io.codeleaf.config;

/**
 * Thrown when any exception related to the Configuration library occurred.
 * When the caller is not interested in exact reason of an exception, the caller can just catch them all
 * using a single catch block, by catching this type.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class ConfigurationException extends Exception {

    public ConfigurationException() {
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}
