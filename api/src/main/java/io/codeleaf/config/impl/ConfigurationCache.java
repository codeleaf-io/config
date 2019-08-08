package io.codeleaf.config.impl;

import io.codeleaf.common.behaviors.Cache;
import io.codeleaf.common.behaviors.impl.UnlimitedCache;
import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Provides a Cache implementation for the <code>ConfigurationProvider</code>.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class ConfigurationCache implements ConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCache.class);

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
        return doGetConfiguration(configurationTypeClass, false, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> T getConfiguration(Class<T> configurationTypeClass, Object context) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException {
        return doGetConfiguration(configurationTypeClass, true, context);
    }

    private <T extends Configuration> T doGetConfiguration(Class<T> configurationTypeClass, boolean withContext, Object context) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException {
        Objects.requireNonNull(configurationTypeClass);
        Configuration configuration;
        if (!cache.has(configurationTypeClass)) {
            LOGGER.debug("Cache miss for: " + configurationTypeClass);
            if (withContext) {
                configuration = provider.getConfiguration(configurationTypeClass);
            } else {
                configuration = provider.getConfiguration(configurationTypeClass, context);
            }
            cache.put(configurationTypeClass, configuration);
        } else {
            LOGGER.debug("Cache hit for: " + configurationTypeClass);
            configuration = cache.get(configurationTypeClass);
        }
        return configurationTypeClass.cast(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> T parseConfiguration(Class<T> configurationTypeClass, Specification specification) throws ConfigurationNotFoundException, InvalidSpecificationException {
        return provider.parseConfiguration(configurationTypeClass, specification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Configuration> T parseConfiguration(Class<T> configurationTypeClass, Specification specification, Object context) throws ConfigurationNotFoundException, InvalidSpecificationException {
        return provider.parseConfiguration(configurationTypeClass, specification, context);
    }

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
}
