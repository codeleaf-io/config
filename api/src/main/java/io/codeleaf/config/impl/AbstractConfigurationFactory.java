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

    /**
     * Constructs a configuration factory that is able to create a configuration of the specified type
     *
     * @param configurationTypeClass the type of configuration that is supported
     */
    public AbstractConfigurationFactory(Class<T> configurationTypeClass) {
        this.configurationTypeClass = configurationTypeClass;
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
     * Returns <code>true</code> if a default configuration is support. The provided behavior does not provide
     * this, and thus returns <code>false</code>.
     *
     * @return <code>false</code> if the method has not been overwritten
     */
    protected boolean supportsDefaultConfiguration() {
        return false;
    }

    /**
     * Returns the default configuration. The provided behavior does not provide this, and thus throws an exception.
     *
     * @return never unless the method has been overwritten
     * @throws ConfigurationNotFoundException if the method has not been overwritten
     */
    protected T getDefaultConfiguration() throws ConfigurationNotFoundException {
        throw new ConfigurationNotFoundException(configurationTypeClass);
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
