package io.codeleaf.config.spi;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;

/**
 * The service provider interface for introducing new configuration types.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface ConfigurationFactory {

    /**
     * Returns <code>true</code> if the specified type is supported by this factory, otherwise <code>false</code>
     *
     * @param configurationTypeClass the type class of a configuration
     * @param <T>                    the type of a configuration
     * @return <code>true</code> if the specified type is supported, otherwise <code>false</code>
     */
    <T extends Configuration> boolean supportsConfiguration(Class<T> configurationTypeClass);

    /**
     * Creates a configuration based on the specified specification.
     *
     * @param specification          the specification to use
     * @param configurationTypeClass the type class of the configuration to create
     * @param <T>                    the type of the configuration to create
     * @return the newly created configuration
     * @throws InvalidSpecificationException if the specification is invalid for the requested configuration type
     */
    <T extends Configuration> T createConfiguration(Specification specification, Class<T> configurationTypeClass) throws InvalidSpecificationException;

    <T extends Configuration> T createConfiguration(Specification specification, Class<T> configurationTypeClass, Object context) throws InvalidSpecificationException;

    /**
     * Returns <code>true</code> if the specified type is supported by this factory and has a default configuration, otherwise <code>false</code>.
     * The default configuration is called when supported and no specification has been found.
     *
     * @param configurationTypeClass the type class of a configuration
     * @param <T>                    the type of a configuration
     * @return <code>true</code> if the specified type is supported and has a default configuration, otherwise <code>false</code>
     */
    <T extends Configuration> boolean supportsDefaultConfiguration(Class<T> configurationTypeClass);

    /**
     * Returns the default configuration. The default configuration is called when supported and no specification has been found.
     *
     * @param configurationTypeClass the type class of the configuration
     * @param <T>                    the type of the configuration
     * @return the default configuration
     * @throws ConfigurationNotFoundException if <code>supportsDefaultConfiguration</code> would return <code>false</code> for the given configuration type
     */
    <T extends Configuration> T createDefaultConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException;

}
