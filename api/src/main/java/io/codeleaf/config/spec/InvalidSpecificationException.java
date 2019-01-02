package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

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

    public Specification getSpecification() {
        return specification;
    }

}
