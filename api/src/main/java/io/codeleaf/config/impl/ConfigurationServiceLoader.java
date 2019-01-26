package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.spec.*;
import io.codeleaf.config.spi.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Implements a configuration provider using the java service loader mechanism.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class ConfigurationServiceLoader implements ConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationServiceLoader.class);

    private final ServiceLoader<ConfigurationFactory> serviceLoader;
    private final SpecificationProvider specificationProvider;

    private ConfigurationServiceLoader(ServiceLoader<ConfigurationFactory> serviceLoader, SpecificationProvider specificationProvider) {
        this.serviceLoader = serviceLoader;
        this.specificationProvider = specificationProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> boolean hasConfiguration(Class<T> configurationTypeClass) {
        synchronized (serviceLoader) {
            for (ConfigurationFactory factory : serviceLoader) {
                if (factory.supportsConfiguration(configurationTypeClass)) {
                    return factory.supportsDefaultConfiguration(configurationTypeClass) || specificationProvider.hasSpecification(toSpecificationName(configurationTypeClass));
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> T getConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException {
        Objects.requireNonNull(configurationTypeClass);
        synchronized (serviceLoader) {
            for (ConfigurationFactory factory : serviceLoader) {
                if (factory.supportsConfiguration(configurationTypeClass)) {
                    String specificationName = toSpecificationName(configurationTypeClass);
                    if (specificationProvider.hasSpecification(specificationName)) {
                        Specification specification = specificationProvider.getSpecification(toSpecificationName(configurationTypeClass));
                        return factory.createConfiguration(specification, configurationTypeClass);
                    } else {
                        LOGGER.debug("No specification found for: " + specificationName);
                        if (factory.supportsDefaultConfiguration(configurationTypeClass)) {
                            LOGGER.debug("Creating default configuration for: " + configurationTypeClass);
                            return factory.createDefaultConfiguration(configurationTypeClass);
                        }
                    }
                }
            }
            LOGGER.warn("Configuration not found: " + configurationTypeClass);
            throw new ConfigurationNotFoundException(configurationTypeClass);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> T parseConfiguration(Class<T> configurationTypeClass, Specification specification) throws ConfigurationNotFoundException, InvalidSpecificationException {
        Objects.requireNonNull(configurationTypeClass);
        Objects.requireNonNull(specification);
        synchronized (serviceLoader) {
            for (ConfigurationFactory factory : serviceLoader) {
                if (factory.supportsConfiguration(configurationTypeClass)) {
                    return factory.createConfiguration(specification, configurationTypeClass);
                }
            }
        }
        LOGGER.warn("Configuration not found: " + configurationTypeClass);
        throw new ConfigurationNotFoundException(configurationTypeClass);
    }

    private <T extends Configuration> String toSpecificationName(Class<T> configurationTypeClass) {
        return configurationTypeClass.getName();
    }

    /**
     * Creates a new instance that read the specifications from the singleton service provider.
     *
     * @return the new instance
     * @see ServiceLoader#load(Class)
     * @see SpecificationProvider#get()
     */
    public static ConfigurationServiceLoader create() {
        return new ConfigurationServiceLoader(ServiceLoader.load(ConfigurationFactory.class), SpecificationProvider.get());
    }
}
