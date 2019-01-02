package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

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

    public String getSpecificationName() {
        return specificationName;
    }

}
