package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ext.ConfigurationFactory;
import io.codeleaf.config.spec.*;

import java.io.IOException;
import java.util.ServiceLoader;

public final class ConfigurationServiceLoader implements ConfigurationProvider {

    public static ConfigurationServiceLoader create() {
        return new ConfigurationServiceLoader(ServiceLoader.load(ConfigurationFactory.class), SpecificationProvider.get());
    }

    private final ServiceLoader<ConfigurationFactory> serviceLoader;
    private final SpecificationProvider specificationProvider;

    private ConfigurationServiceLoader(ServiceLoader<ConfigurationFactory> serviceLoader, SpecificationProvider specificationProvider) {
        this.serviceLoader = serviceLoader;
        this.specificationProvider = specificationProvider;
    }

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
