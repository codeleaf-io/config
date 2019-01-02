package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.impl.MapSpecification;

import java.util.*;

public final class PropertiesSpecificationParser {

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
