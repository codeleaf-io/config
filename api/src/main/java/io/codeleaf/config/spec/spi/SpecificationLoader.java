package io.codeleaf.config.spec.spi;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;

import java.io.IOException;

/**
 * The service provider interface to introduce new specification loaders.
 */
public interface SpecificationLoader {

    /**
     * Loads the specification with the given name
     *
     * @param specificationName the name of the specification to load
     * @return the loaded specification
     * @throws SpecificationNotFoundException if this loader does not contain a specification with the given name
     * @throws IOException                    if the loader fails to read the specification
     * @throws SpecificationFormatException   if the specification is in the wrong format
     */
    Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException;

    /**
     * Returns <code>true</code> if this loader contains a specification with the given name, otherwise <code>false</code>
     *
     * @param specificationName the name of the specification
     * @return <code>true</code> if this loader contains a specification with the given name, otherwise <code>false</code>
     */
    boolean hasSpecification(String specificationName);

}
