package io.codeleaf.config;

import io.codeleaf.config.impl.ConfigurationCache;
import io.codeleaf.config.impl.ConfigurationServiceLoader;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;

import java.io.IOException;

/**
 * This class is used to obtain configurations.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface ConfigurationProvider {

    /**
     * Holder for a singleton, to obtain the singleton, use {@link ConfigurationProvider#get()}
     */
    final class Holder {

        private static ConfigurationProvider INSTANCE;

        static {
            init();
        }

        private static void init() {
            try {
                INSTANCE = ConfigurationCache.create(ConfigurationServiceLoader.create());
            } catch (Exception cause) {
                throw new ExceptionInInitializerError(cause);
            }
        }

        private static ConfigurationProvider get() {
            return INSTANCE;
        }

        private Holder() {
        }

    }

    /**
     * Returns a ConfigurationProvider to load configurations
     *
     * @return a ConfigurationProvider
     * @throws ExceptionInInitializerError if the Holder is lazy initialized and during initialization an exception was thrown
     */
    static ConfigurationProvider get() {
        return Holder.get();
    }

    /**
     * Returns <code>true</code> if a configuration of the specified type is present.
     *
     * @param configurationTypeClass the type class of the configuration to check for
     * @param <T>                    the type of the configuration
     * @return <code>true</code> if the configuration of the specified type is found, otherwise <code>false</code>
     */
    <T extends Configuration> boolean hasConfiguration(Class<T> configurationTypeClass);

    /**
     * Returns the configuration of the specified type.
     *
     * @param configurationTypeClass the type class of the configuration to load
     * @param <T>                    the type of the configuration
     * @return the configuration of the specified type
     * @throws ConfigurationNotFoundException when no {@link io.codeleaf.config.ext.ConfigurationFactory} is registered for the specified type
     * @throws SpecificationNotFoundException when the {@link io.codeleaf.config.spec.Specification} could not be loaded
     * @throws IOException                    when an exception occurred during opening or reading the {@link io.codeleaf.config.spec.Specification} or initialization of the specific configuration
     * @throws SpecificationFormatException   when the {@link io.codeleaf.config.spec.Specification} has an invalid format
     * @throws InvalidSpecificationException  when the {@link io.codeleaf.config.spec.Specification} has invalid fields or field values
     * @see io.codeleaf.config.ext.ConfigurationFactory
     * @see io.codeleaf.config.spec.Specification
     * @see io.codeleaf.config.spec.ext.SpecificationLoader
     */
    <T extends Configuration> T getConfiguration(Class<T> configurationTypeClass) throws ConfigurationNotFoundException, SpecificationNotFoundException, IOException, SpecificationFormatException, InvalidSpecificationException;

}
