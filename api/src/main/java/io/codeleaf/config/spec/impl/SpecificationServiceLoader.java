package io.codeleaf.config.spec.impl;

import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationProvider;
import io.codeleaf.config.spec.ext.SpecificationLoader;

import java.io.IOException;
import java.util.ServiceLoader;

public final class SpecificationServiceLoader implements SpecificationProvider {

    public static SpecificationServiceLoader create() {
        return new SpecificationServiceLoader(ServiceLoader.load(SpecificationLoader.class));
    }

    private final ServiceLoader<SpecificationLoader> serviceLoader;

    private SpecificationServiceLoader(ServiceLoader<SpecificationLoader> serviceLoader) {
        this.serviceLoader = serviceLoader;
    }

    @Override
    public boolean hasSpecification(String specificationName) {
        synchronized (serviceLoader) {
            for (SpecificationLoader loader : serviceLoader) {
                if (loader.hasSpecification(specificationName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        synchronized (serviceLoader) {
            for (SpecificationLoader loader : serviceLoader) {
                if (loader.hasSpecification(specificationName)) {
                    return loader.loadSpecification(specificationName);
                }
            }
            throw new SpecificationNotFoundException(specificationName);
        }
    }

}
