package io.codeleaf.config;

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

    public Class<? extends Configuration> getConfigurationTypeClass() {
        return configurationTypeClass;
    }

}
