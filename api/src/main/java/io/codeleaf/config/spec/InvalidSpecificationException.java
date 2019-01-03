package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

/**
 * Thrown when the specification contains invalid settings.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class InvalidSpecificationException extends ConfigurationException {

    private final Specification specification;

    public InvalidSpecificationException(Specification specification) {
        this.specification = specification;
    }

    public InvalidSpecificationException(Specification specification, String message) {
        super(message);
        this.specification = specification;
    }

    public InvalidSpecificationException(Specification specification, String message, Throwable cause) {
        super(message, cause);
        this.specification = specification;
    }

    public InvalidSpecificationException(Specification specification, Throwable cause) {
        super(cause);
        this.specification = specification;
    }

    /**
     * Returns the specification that contains invalid settings
     *
     * @return the specification that contains invalid settings
     */
    public Specification getSpecification() {
        return specification;
    }

}
