package io.codeleaf.config.spec.impl;

import io.codeleaf.common.behaviors.Cache;
import io.codeleaf.common.behaviors.impl.UnlimitedCache;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationProvider;

import java.io.IOException;
import java.util.Objects;

public final class SpecificationCache implements SpecificationProvider {

    public static SpecificationCache create(SpecificationProvider provider) {
        Objects.requireNonNull(provider);
        return new SpecificationCache(new UnlimitedCache<>(), provider);
    }

    private final Cache<String, Specification> cache;
    private final SpecificationProvider provider;

    private SpecificationCache(Cache<String, Specification> cache, SpecificationProvider provider) {
        this.cache = cache;
        this.provider = provider;
    }

    @Override
    public boolean hasSpecification(String specificationName) {
        return cache.has(specificationName) || provider.hasSpecification(specificationName);
    }

    @Override
    public Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        Specification specification;
        if (!cache.has(specificationName)) {
            specification = provider.getSpecification(specificationName);
            cache.put(specificationName, specification);
        } else {
            specification = cache.get(specificationName);
        }
        return specification;
    }

}
