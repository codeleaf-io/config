package io.codeleaf.config.impl;

import io.codeleaf.common.behaviors.Cache;
import io.codeleaf.common.behaviors.impl.UnlimitedCache;
import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;

import java.io.IOException;
import java.util.Objects;

/**
 * Provides a Cache implementation for the <code>ConfigurationProvider</code>.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class ConfigurationCache implements ConfigurationProvider {

    /**
     * Creates a new instance using a <code>UnlimitedCache</code> and loads cache misses from the specified provider.
     *
     * @param provider the configuration provider to load cache misses
     * @return the new instance
     * @see UnlimitedCache
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> boolean hasConfiguration(Class<T> configurationTypeClass) {
        return cache.has(configurationTypeClass) || provider.hasConfiguration(configurationTypeClass);
    }

    /**
     * {@inheritDoc}
     */
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
