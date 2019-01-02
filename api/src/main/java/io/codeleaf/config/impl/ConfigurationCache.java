package io.codeleaf.config.impl;

import io.codeleaf.common.behaviors.Cache;
import io.codeleaf.common.behaviors.impl.UnlimitedCache;
import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.SpecificationFormatException;

import java.io.IOException;
import java.util.Objects;

public final class ConfigurationCache implements ConfigurationProvider {

    public static ConfigurationCache create(ConfigurationProvider provider) {
        Objects.requireNonNull(provider);
        return new ConfigurationCache(new UnlimitedCache<>(), provider);
    }

    private final Cache<Class<?>, Configuration> cache;
    private final ConfigurationProvider provider;

    private ConfigurationCache(Cache<Class<?>, Configuration> cache, ConfigurationProvider provider) {
        this.cache = cache;
        this.provider = provider;
    }

    @Override
    public <T extends Configuration> boolean hasConfiguration(Class<T> configurationTypeClass) {
        return cache.has(configurationTypeClass) || provider.hasConfiguration(configurationTypeClass);
    }

    @Override
    public <T extends Configuration> T getConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException {
        Configuration configuration;
        if (!cache.has(configurationTypeClass)) {
            configuration = provider.getConfiguration(configurationTypeClass);
            cache.put(configurationTypeClass, configuration);
        } else {
            configuration = cache.get(configurationTypeClass);
        }
        return configurationTypeClass.cast(configuration);
    }

}
