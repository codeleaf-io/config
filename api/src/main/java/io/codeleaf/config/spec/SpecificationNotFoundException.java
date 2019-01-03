package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

/**
 * Thrown when the specification was not found.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class SpecificationNotFoundException extends ConfigurationException {

    private final String specificationName;

    public SpecificationNotFoundException(String specificationName) {
        this.specificationName = specificationName;
    }

    public SpecificationNotFoundException(String specificationName, String message) {
        super(message);
        this.specificationName = specificationName;
    }

    public SpecificationNotFoundException(String specificationName, String message, Throwable cause) {
        super(message, cause);
        this.specificationName = specificationName;
    }

    public SpecificationNotFoundException(String specificationName, Throwable cause) {
        super(cause);
        this.specificationName = specificationName;
    }

    /**
     * Returns the name of the specification that was not found
     *
     * @return the name of the specification that was not found
     */
    public String getSpecificationName() {
        return specificationName;
    }

}
