package io.codeleaf.config.spec.impl;

import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;

import java.util.Iterator;
import java.util.List;

public abstract class DecoratedSpecification implements Specification {

    protected final Specification specification;

    public DecoratedSpecification(Specification specification) {
        this.specification = specification;
    }

    @Override
    public List<List<String>> getDefined(List<String> fieldPrefix) {
        return specification.getDefined(fieldPrefix);
    }

    @Override
    public Iterable<Setting> getSettings(List<String> fieldPrefix) {
        return specification.getSettings(fieldPrefix);
    }

    @Override
    public boolean hasSetting(List<String> field) {
        return specification.hasSetting(field);
    }

    @Override
    public Setting getSetting(List<String> field) throws SettingNotFoundException {
        return specification.getSetting(field);
    }

    @Override
    public Iterator<Setting> iterator() {
        return specification.iterator();
    }

}
