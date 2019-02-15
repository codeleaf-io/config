package io.codeleaf.config.spec.impl;

import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Implementation of a specification using a map.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class MapSpecification implements Specification, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapSpecification.class);

    private final Map<List<String>, Object> settingsMap;

    private MapSpecification(Map<List<String>, Object> settingsMap) {
        this.settingsMap = settingsMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Setting> iterator() {
        return new SettingIterator(Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSetting(List<String> field) {
        for (Setting setting : getSettings(field)) {
            if (Settings.sameField(setting.getField(), field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<String>> getDefined(List<String> fieldPrefix) {
        Set<String> nextLevelNames = new LinkedHashSet<>();
        for (Setting setting : getSettings(fieldPrefix)) {
            if (setting.getField().size() > fieldPrefix.size()) {
                nextLevelNames.add(setting.getField().get(fieldPrefix.size()));
            }
        }
        List<List<String>> fieldNames = new ArrayList<>();
        for (String nextLevelName : nextLevelNames) {
            List<String> defined = new ArrayList<>(fieldPrefix.size() + 1);
            defined.addAll(fieldPrefix);
            defined.add(nextLevelName);
            fieldNames.add(defined);
        }
        return fieldNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Setting> getSettings(List<String> fieldPrefix) {
        return new SettingIterable(fieldPrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Setting getSetting(List<String> field) throws SettingNotFoundException {
        for (Setting setting : getSettings(field)) {
            if (Settings.sameField(setting.getField(), field)) {
                return setting;
            }
        }
        LOGGER.warn("Setting not found: " + Settings.toString(field));
        throw new SettingNotFoundException(this, field);
    }

    /**
     * Creates a new instance containing the same settings as the specified settingsMap
     *
     * @param settingsMap the specification settings to include
     * @return the newly created instance
     */
    public static MapSpecification create(Map<List<String>, Object> settingsMap) {
        Objects.requireNonNull(settingsMap);
        Map<List<String>, Object> clonedSettingsMap = new LinkedHashMap<>();
        for (Map.Entry<List<String>, Object> entry : settingsMap.entrySet()) {
            clonedSettingsMap.put(new ArrayList<>(entry.getKey()), entry.getValue());
        }
        return new MapSpecification(Collections.unmodifiableMap(clonedSettingsMap));
    }

    /**
     * Creates a new instance containing the same settings as the specified specification
     *
     * @param specification the specification settings to include
     * @return the newly created instance
     */
    public static MapSpecification create(Specification specification) {
        Objects.requireNonNull(specification);
        Map<List<String>, Object> settingsMap = new LinkedHashMap<>();
        for (Setting setting : specification) {
            settingsMap.put(setting.getField(), setting.getValue());
        }
        return new MapSpecification(Collections.unmodifiableMap(settingsMap));
    }

    public static MapSpecification create(Specification specification, String... fieldPrefix) {
        Objects.requireNonNull(specification);
        Objects.requireNonNull(fieldPrefix);
        return create(specification, Arrays.asList(fieldPrefix));
    }

    public static MapSpecification create(Specification specification, List<String> fieldPrefix) {
        Objects.requireNonNull(specification);
        Objects.requireNonNull(fieldPrefix);
        Map<List<String>, Object> settingsMap = new LinkedHashMap<>();
        for (Setting setting : specification.getSettings(fieldPrefix)) {
            settingsMap.put(setting.getField().subList(fieldPrefix.size(), setting.getField().size()), setting.getValue());
        }
        return new MapSpecification(Collections.unmodifiableMap(settingsMap));
    }

    /**
     * Normalizes the map. That means that inner maps are removed and extra entries using a list of keys is created
     * within the new map.
     *
     * @param map the normalized map
     * @return the normalized map
     * @throws IllegalArgumentException if the map, or any inner map, does not have <code>String</code>s as keys
     * @throws NullPointerException     if map is <code>null</code>
     */
    public static Map<List<String>, Object> normalize(Map<?, ?> map) {
        Objects.requireNonNull(map);
        Map<List<String>, Object> normalizedMap = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException();
            }
            if (entry.getValue() instanceof Map) {
                Map<List<String>, Object> childMap = normalize((Map<?, ?>) entry.getValue());
                for (Map.Entry<List<String>, Object> childEntry : childMap.entrySet()) {
                    List<String> specKey = new ArrayList<>(childEntry.getKey().size() + 1);
                    specKey.add((String) entry.getKey());
                    specKey.addAll(childEntry.getKey());
                    normalizedMap.put(specKey, childEntry.getValue());
                }
            } else {
                normalizedMap.put(Collections.singletonList((String) entry.getKey()), entry.getValue());
            }
        }
        return normalizedMap;
    }

    private final class SettingIterable implements Iterable<Setting> {

        private final List<String> fieldPrefix;

        private SettingIterable(List<String> fieldPrefix) {
            this.fieldPrefix = fieldPrefix;
        }

        @Override
        public Iterator<Setting> iterator() {
            return new SettingIterator(fieldPrefix);
        }
    }

    private final class SettingIterator implements Iterator<Setting> {

        private final Iterator<Map.Entry<List<String>, Object>> iterator = settingsMap.entrySet().iterator();
        private final List<String> fieldPrefix;
        private Setting next;

        private SettingIterator(List<String> fieldPrefix) {
            this.fieldPrefix = fieldPrefix;
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                setNext();
            }
            return next != null;
        }

        @Override
        public Setting next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Setting current = next;
            setNext();
            return current;
        }

        private void setNext() {
            next = null;
            while (next == null && iterator.hasNext()) {
                Map.Entry<List<String>, Object> entry = iterator.next();
                if (Settings.prefixMatches(fieldPrefix, entry.getKey())) {
                    next = new Setting(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
