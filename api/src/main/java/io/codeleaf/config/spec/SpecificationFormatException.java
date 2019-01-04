package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

/**
 * Thrown if a specification is in an invalid format, and thus settings can't be read.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class SpecificationFormatException extends ConfigurationException {

    private final String specificationName;

    public SpecificationFormatException(String specificationName) {
        this.specificationName = specificationName;
    }

    public SpecificationFormatException(String specificationName, String message) {
        super(message);
        this.specificationName = specificationName;
    }

    public SpecificationFormatException(String specificationName, String message, Throwable cause) {
        super(message, cause);
        this.specificationName = specificationName;
    }

    public SpecificationFormatException(String specificationName, Throwable cause) {
        super(cause);
        this.specificationName = specificationName;
    }

    /**
     * Returns the name of the specification that was in an invalid format
     *
     * @return the name of the specification that was in an invalid format
     */
    public String getSpecificationName() {
        return specificationName;
    }

}
