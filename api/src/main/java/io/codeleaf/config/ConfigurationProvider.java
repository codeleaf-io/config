package io.codeleaf.config;

import io.codeleaf.config.impl.ConfigurationCache;
import io.codeleaf.config.impl.ConfigurationServiceLoader;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.SpecificationFormatException;

import java.io.IOException;

public interface ConfigurationProvider {

    final class Singleton {

        private static final ConfigurationProvider INSTANCE = ConfigurationCache.create(ConfigurationServiceLoader.create());

        private Singleton() {
        }

    }

    static ConfigurationProvider get() {
        return Singleton.INSTANCE;
    }

    <T extends Configuration> boolean hasConfiguration(Class<T> configurationTypeClass);

    <T extends Configuration> T getConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException;

}
