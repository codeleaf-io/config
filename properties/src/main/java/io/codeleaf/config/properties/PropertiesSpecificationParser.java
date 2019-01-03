package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.impl.MapSpecification;

import java.util.*;

/**
 * Implements a parser that parses a specification out of a properties file.
 *
 * @author tvburger@gmail.com
 * @see Properties
 * @see Specification
 * @since 0.1.0
 */
public final class PropertiesSpecificationParser {

    /**
     * Parses a specification from the specified properties.
     *
     * @param properties the properties to parse
     * @return the resulting specification
     */
    public Specification parseSpecification(Properties properties) {
        Map<List<String>, Object> settingsMap = new LinkedHashMap<>();
        for (String stringPropertyName : properties.stringPropertyNames()) {
            settingsMap.put(createField(stringPropertyName), properties.getProperty(stringPropertyName));
        }
        return MapSpecification.create(settingsMap);
    }

    private List<String> createField(String stringPropertyName) {
        return Arrays.asList(stringPropertyName.split("\\."));
    }

}
