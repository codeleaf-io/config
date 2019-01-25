package io.codeleaf.config.spec;

import io.codeleaf.config.spec.impl.SpecificationCache;
import io.codeleaf.config.spec.impl.SpecificationServiceLoader;

import java.io.IOException;

/**
 * This class is used to obtain specifications.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface SpecificationProvider {

    /**
     * Returns a SpecificationProvider to load configurations
     *
     * @return a SpecificationProvider
     * @throws ExceptionInInitializerError if the Holder is lazy initialized and during initialization an exception was thrown
     */
    static SpecificationProvider get() {
        return Holder.INSTANCE;
    }

    /**
     * Returns <code>true</code> if the provider contains the specified specification
     *
     * @param specificationName the name of specification
     * @return <code>true</code> if the provider contains the specified specification
     */
    boolean hasSpecification(String specificationName);

    /**
     * Returns the specification with the specified name
     *
     * @param specificationName the name of the specification
     * @return the specification
     * @throws SpecificationNotFoundException when no specification is found with the given name
     * @throws IOException                    when an exception occurred during reading the configuration
     * @throws SpecificationFormatException   if the specification has the wrong format
     * @see io.codeleaf.config.spec.spi.SpecificationLoader
     */
    Specification getSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException;

    /**
     * Holder for a singleton, to obtain the singleton, use {@link SpecificationProvider#get()}
     */
    final class Holder {

        private Holder() {
        }

        private static SpecificationProvider INSTANCE;

        static {
            init();
        }

        private static void init() {
            try {
                INSTANCE = SpecificationCache.create(SpecificationServiceLoader.create());
            } catch (Exception cause) {
                throw new ExceptionInInitializerError(cause);
            }
        }

        private static SpecificationProvider get() {
            return INSTANCE;
        }
    }
}
