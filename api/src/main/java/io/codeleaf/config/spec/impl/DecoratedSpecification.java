package io.codeleaf.config.spec.impl;

import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;

import java.util.Iterator;
import java.util.List;

/**
 * Implements a decorator for a specification.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public abstract class DecoratedSpecification implements Specification {

    /**
     * The specification that is beind decorated
     */
    protected final Specification specification;

    /**
     * Constructs a decorator for the specified specification
     *
     * @param specification a decorator for the specified specification
     */
    public DecoratedSpecification(Specification specification) {
        this.specification = specification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<String>> getDefined(List<String> fieldPrefix) {
        return specification.getDefined(fieldPrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Setting> getSettings(List<String> fieldPrefix) {
        return specification.getSettings(fieldPrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSetting(List<String> field) {
        return specification.hasSetting(field);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Setting getSetting(List<String> field) throws SettingNotFoundException {
        return specification.getSetting(field);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Setting> iterator() {
        return specification.iterator();
    }

}
