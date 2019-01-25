package io.codeleaf.config.spec.impl;

import io.codeleaf.common.behaviors.Cache;
import io.codeleaf.common.behaviors.impl.UnlimitedCache;
import io.codeleaf.config.impl.ConfigurationServiceLoader;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.SpecificationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Provides a Cache implementation for the <code>SpecificationProvider</code>.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class SpecificationCache implements SpecificationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationCache.class);

    private final Cache<String, Specification> cache;
    private final SpecificationProvider provider;

    private SpecificationCache(Cache<String, Specification> cache, SpecificationProvider provider) {
        this.cache = cache;
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSpecification(String specificationName) {
        return cache.has(specificationName) || provider.hasSpecification(specificationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        Specification specification;
        if (!cache.has(specificationName)) {
            LOGGER.debug("Cache miss for: " + specificationName);
            specification = provider.getSpecification(specificationName);
            cache.put(specificationName, specification);
        } else {
            LOGGER.debug("Cache hit for: " + specificationName);
            specification = cache.get(specificationName);
        }
        return specification;
    }

    /**
     * Creates a new instance using a <code>UnlimitedCache</code> and loads cache misses from the specified provider.
     *
     * @param provider the specification provider to load cache misses
     * @return the new instance
     * @see UnlimitedCache
     */
    public static SpecificationCache create(SpecificationProvider provider) {
        Objects.requireNonNull(provider);
        return new SpecificationCache(new UnlimitedCache<>(), provider);
    }
}
