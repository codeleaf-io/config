package io.codeleaf.config;

/**
 * Thrown when no <code>ConfigurationFactory</code> is present that can create the specified type.
 *
 * @author tvburger@gmail.com
 * @see io.codeleaf.config.ext.ConfigurationFactory
 * @since 0.1.0
 */
public class ConfigurationNotFoundException extends ConfigurationException {

    private final Class<? extends Configuration> configurationTypeClass;

    public ConfigurationNotFoundException(Class<? extends Configuration> configurationTypeClass) {
        this.configurationTypeClass = configurationTypeClass;
    }

    public ConfigurationNotFoundException(Class<? extends Configuration> configurationTypeClass, String message) {
        super(message);
        this.configurationTypeClass = configurationTypeClass;
    }

    public ConfigurationNotFoundException(Class<? extends Configuration> configurationTypeClass, String message, Throwable cause) {
        super(message, cause);
        this.configurationTypeClass = configurationTypeClass;
    }

    public ConfigurationNotFoundException(Class<? extends Configuration> configurationTypeClass, Throwable cause) {
        super(cause);
        this.configurationTypeClass = configurationTypeClass;
    }

    /**
     * Returns the type of the configuration that was not found.
     *
     * @return the type of the configuration that was not found
     */
    public Class<? extends Configuration> getConfigurationTypeClass() {
        return configurationTypeClass;
    }

}
