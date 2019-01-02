package io.codeleaf.config.ext;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;

public interface ConfigurationFactory {

    <T extends Configuration> boolean supportsConfiguration(Class<T> configurationTypeClass);

    <T extends Configuration> T createConfiguration(Specification specification, Class<T> configurationTypeClass) throws InvalidSpecificationException;

    <T extends Configuration> boolean supportsDefaultConfiguration(Class<T> configurationTypeClass);

    <T extends Configuration> T createDefaultConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException;

}
