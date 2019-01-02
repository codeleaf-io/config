package io.codeleaf.config.spec;

import io.codeleaf.config.ConfigurationException;

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

    public String getSpecificationName() {
        return specificationName;
    }

}
