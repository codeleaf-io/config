package io.codeleaf.config.spec;

import io.codeleaf.config.spec.impl.SpecificationCache;
import io.codeleaf.config.spec.impl.SpecificationServiceLoader;

import java.io.IOException;

public interface SpecificationProvider {

    final class Singleton {

        private static final SpecificationProvider INSTANCE = SpecificationCache.create(SpecificationServiceLoader.create());

        private Singleton() {
        }

    }

    static SpecificationProvider get() {
        return Singleton.INSTANCE;
    }

    boolean hasSpecification(String specificationName);

    Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException;

}
