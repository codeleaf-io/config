package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ext.ConfigurationFactory;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;

import java.util.Objects;

public abstract class AbstractConfigurationFactory<T extends Configuration> implements ConfigurationFactory {

    private final Class<T> configurationTypeClass;

    public AbstractConfigurationFactory(Class<T> configurationTypeClass) {
        this.configurationTypeClass = configurationTypeClass;
    }

    protected abstract T parseConfiguration(Specification specification) throws InvalidSpecificationException;

    protected boolean supportsDefaultConfiguration() {
        return false;
    }

    protected T getDefaultConfiguration() throws ConfigurationNotFoundException {
        throw new ConfigurationNotFoundException(configurationTypeClass);
    }

    @Override
    public final <S extends Configuration> boolean supportsConfiguration(Class<S> configurationTypeClass) {
        return Objects.equals(this.configurationTypeClass, configurationTypeClass);
    }

    @Override
    public final <S extends Configuration> S createConfiguration(Specification specification, Class<S> configurationTypeClass) throws InvalidSpecificationException {
        assertType(configurationTypeClass);
        return configurationTypeClass.cast(parseConfiguration(specification));
    }

    @Override
    public final <S extends Configuration> boolean supportsDefaultConfiguration(Class<S> configurationTypeClass) {
        assertType(configurationTypeClass);
        return supportsDefaultConfiguration();
    }

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
