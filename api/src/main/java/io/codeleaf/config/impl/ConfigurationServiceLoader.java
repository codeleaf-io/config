package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.spec.*;
import io.codeleaf.config.spi.ConfigurationFactory;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Implements a configuration provider using the java service loader mechanism.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class ConfigurationServiceLoader implements ConfigurationProvider {

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
        synchronized (serviceLoader) {
            for (ConfigurationFactory factory : serviceLoader) {
                if (factory.supportsConfiguration(configurationTypeClass)) {
                    String specificationName = toSpecificationName(configurationTypeClass);
                    if (specificationProvider.hasSpecification(specificationName)) {
                        Specification specification = specificationProvider.getSpecification(toSpecificationName(configurationTypeClass));
                        return factory.createConfiguration(specification, configurationTypeClass);
                    } else if (factory.supportsDefaultConfiguration(configurationTypeClass)) {
                        return factory.createDefaultConfiguration(configurationTypeClass);
                    }
                }
            }
            throw new ConfigurationNotFoundException(configurationTypeClass);
        }
    }

    private <T extends Configuration> String toSpecificationName(Class<T> configurationTypeClass) {
        return configurationTypeClass.getName();
    }

}
