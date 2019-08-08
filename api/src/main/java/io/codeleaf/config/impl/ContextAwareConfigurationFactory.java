package io.codeleaf.config.impl;

import io.codeleaf.config.Configuration;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.Specification;

import java.util.function.Supplier;

/**
 * This class represents a configuration factory that creates only a single type.
 *
 * @param <T> the type of the configuration that is supported
 * @param <C> the type of the context when used
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public abstract class ContextAwareConfigurationFactory<T extends Configuration, C> extends AbstractConfigurationFactory<T> {

    private final Class<C> contextTypeClass;
    private final Supplier<C> defaultContextProvider;

    /**
     * Constructs a configuration factory that is able to create a configuration of the specified type but does
     * not support a default configuration.
     *
     * @param configurationTypeClass the type of configuration that is supported
     */
    public ContextAwareConfigurationFactory(Class<T> configurationTypeClass, Class<C> contextTypeClass) {
        this(configurationTypeClass, contextTypeClass, null, null);
    }

    /**
     * Constructs a configuration factory that has a default configuration as specified by
     * <code>defaultConfiguration</code> and can create them from specification.
     * The supported configuration type class is <code>defaultConfiguration.getClass()</code>.
     *
     * @param defaultConfiguration the default configuration
     */
    @SuppressWarnings("unchecked")
    public ContextAwareConfigurationFactory(Class<C> contextTypeClass, T defaultConfiguration) {
        this((Class<T>) defaultConfiguration.getClass(), contextTypeClass, defaultConfiguration, null);
    }

    /**
     * Constructs a configuration factory that is able to create a configuration of the specified type and has
     * as the default configuration as specified by <code>defaultConfiguration</code>.
     *
     * @param configurationTypeClass the type of configuration that is supported
     * @param contextTypeClass       the type of the context
     * @param defaultConfiguration   the default configuration
     * @param defaultContextProvider the provider for the context to use, when null is specified by the caller
     */
    public ContextAwareConfigurationFactory(Class<T> configurationTypeClass, Class<C> contextTypeClass, T defaultConfiguration, Supplier<C> defaultContextProvider) {
        super(configurationTypeClass, defaultConfiguration);
        this.contextTypeClass = contextTypeClass;
        this.defaultContextProvider = defaultContextProvider;
    }

    /**
     * {@inheritDoc}
     */
    protected T parseConfiguration(Specification specification) throws InvalidSpecificationException {
        return parseConfiguration(specification, defaultContextProvider.get());
    }

    /**
     * Parses the configuration out of a specification.
     *
     * @param specification the specification to parse into a configuration
     * @param context       the context for the parsing
     * @return the configuration
     * @throws InvalidSpecificationException if the specification is invalid
     */
    protected abstract T parseConfiguration(Specification specification, C context) throws InvalidSpecificationException;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <S extends Configuration> S createConfiguration(Specification specification, Class<S> configurationTypeClass, Object context) throws InvalidSpecificationException {
        assertType(configurationTypeClass);
        if (context != null && !contextTypeClass.isInstance(context)) {
            throw new IllegalArgumentException("Invalid context type: " + context.getClass());
        }
        return configurationTypeClass.cast(parseConfiguration(specification, context == null ? defaultContextProvider.get() : (C) context));
    }

}
