package io.codeleaf.config.spec.ext;

import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;

import java.io.IOException;

public interface SpecificationLoader {

    Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException;

    boolean hasSpecification(String specificationName);

}
