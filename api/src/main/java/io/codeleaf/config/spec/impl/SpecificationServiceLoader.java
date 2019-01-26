package io.codeleaf.config.spec.impl;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.SpecificationProvider;
import io.codeleaf.config.spec.spi.SpecificationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Implements a specification provider using the java service loader mechanism.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class SpecificationServiceLoader implements SpecificationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificationServiceLoader.class);

    private final ServiceLoader<SpecificationLoader> serviceLoader;

    private SpecificationServiceLoader(ServiceLoader<SpecificationLoader> serviceLoader) {
        this.serviceLoader = serviceLoader;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        synchronized (serviceLoader) {
            for (SpecificationLoader loader : serviceLoader) {
                if (loader.hasSpecification(specificationName)) {
                    LOGGER.debug("Loading specification: " + specificationName);
                    return loader.loadSpecification(specificationName);
                }
            }
            LOGGER.debug("Specification not found: " + specificationName);
            throw new SpecificationNotFoundException(specificationName);
        }
    }

    /**
     * Creates a new instance that leverages the java service loader.
     *
     * @return the new instance
     * @see ServiceLoader#load(Class)
     */
    public static SpecificationServiceLoader create() {
        return new SpecificationServiceLoader(ServiceLoader.load(SpecificationLoader.class));
    }
}
