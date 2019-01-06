package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spi.ConfigurationFactory;

import java.util.Objects;

/**
 * This class represents a configuration factory that creates only a single type.
 *
 * @param <T> the type of the configuration that is supported
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public abstract class AbstractConfigurationFactory<T extends Configuration> implements ConfigurationFactory {

    private final Class<T> configurationTypeClass;
    private final T defaultConfiguration;

    /**
     * Constructs a configuration factory that is able to create a configuration of the specified type but does
     * not support a default configuration.
     *
     * @param configurationTypeClass the type of configuration that is supported
     */
    public AbstractConfigurationFactory(Class<T> configurationTypeClass) {
        this(configurationTypeClass, null);
    }

    /**
     * Constructs a configuration factory that has a default configuration as specified by
     * <code>defaultConfiguration</code> and can create them from specification.
     * The supported configuration type class is <code>defaultConfiguration.getClass()</code>.
     *
     * @param defaultConfiguration the default configuration
     */
    @SuppressWarnings("unchecked")
    public AbstractConfigurationFactory(T defaultConfiguration) {
        this((Class<T>) defaultConfiguration.getClass(), defaultConfiguration);
    }

    /**
     * Constructs a configuration factory that is able to create a configuration of the specified type and has
     * as the default configuration as specified by <code>defaultConfiguration</code>.
     *
     * @param configurationTypeClass the type of configuration that is supported
     * @param defaultConfiguration   the default configuration
     */
    public AbstractConfigurationFactory(Class<T> configurationTypeClass, T defaultConfiguration) {
        this.configurationTypeClass = configurationTypeClass;
        this.defaultConfiguration = defaultConfiguration;
    }

    /**
     * Parses the configuration out of a specification.
     *
     * @param specification the specification to parse into a configuration
     * @return the configuration
     * @throws InvalidSpecificationException if the specification is invalid
     */
    protected abstract T parseConfiguration(Specification specification) throws InvalidSpecificationException;

    /**
     * Returns <code>true</code> if a default configuration was specified in the constructor.
     *
     * @return <code>true</code> if a default configuration is support
     */
    protected boolean supportsDefaultConfiguration() {
        return defaultConfiguration != null;
    }

    /**
     * Returns the default configuration when provided in the constructor.
     *
     * @return the default configuration
     * @throws ConfigurationNotFoundException if no default configuration was specified
     */
    protected T getDefaultConfiguration() throws ConfigurationNotFoundException {
        if (!supportsDefaultConfiguration()) {
            throw new ConfigurationNotFoundException(configurationTypeClass);
        }
        return defaultConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <S extends Configuration> boolean supportsConfiguration(Class<S> configurationTypeClass) {
        return Objects.equals(this.configurationTypeClass, configurationTypeClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <S extends Configuration> S createConfiguration(Specification specification, Class<S> configurationTypeClass) throws InvalidSpecificationException {
        assertType(configurationTypeClass);
        return configurationTypeClass.cast(parseConfiguration(specification));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <S extends Configuration> boolean supportsDefaultConfiguration(Class<S> configurationTypeClass) {
        assertType(configurationTypeClass);
        return supportsDefaultConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <S extends Configuration> S createDefaultConfiguration(Class<S> configurationTypeClass) throws ConfigurationNotFoundException {
        assertType(configurationTypeClass);
        return configurationTypeClass.cast(getDefaultConfiguration());
    }

    private <S extends Configuration> void assertType(Class<S> configurationTypeClass) {
        if (!supportsConfiguration(configurationTypeClass)) {
            throw new IllegalArgumentException();
        }
    }

}
