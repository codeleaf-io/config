package io.codeleaf.config.util;

import io.codeleaf.common.utils.StringEncoder;
import io.codeleaf.config.Configuration;
import io.codeleaf.config.ConfigurationNotFoundException;
import io.codeleaf.config.ConfigurationProvider;
import io.codeleaf.config.spec.InvalidSettingException;
import io.codeleaf.config.spec.InvalidSpecificationException;
import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.impl.MapSpecification;

import java.util.*;

/**
 * Provides utility methods for specifications.
 *
 * @author tvburger@gmail.com
 * @see Specification
 * @since 0.1.0
 */
public final class Specifications {

    public static String parseString(Specification specification, String... field) throws SettingNotFoundException {
        return parseString(specification, Arrays.asList(field));
    }

    public static String parseString(Specification specification, List<String> field) throws SettingNotFoundException {
        return Objects.toString(specification.getValue(field));
    }

    public static int parseInt(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseInt(specification, Arrays.asList(field));
    }

    public static int parseInt(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return Integer.parseInt(specification.getValue(String.class, field));
        } catch (NumberFormatException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field));
        }
    }

    public static boolean parseBoolean(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseBoolean(specification, Arrays.asList(field));
    }

    public static boolean parseBoolean(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            boolean bool;
            String value = specification.getValue(String.class, field);
            switch (value) {
                case "true":
                    bool = true;
                    break;
                case "false":
                    bool = false;
                    break;
                default:
                    throw new InvalidSettingException(specification, specification.getSetting(field));
            }
            return bool;
        } catch (NumberFormatException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field));
        }
    }

    public static Set<String> parseSet(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseSet(specification, Arrays.asList(field));
    }

    public static Set<String> parseSet(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return Collections.unmodifiableSet(new LinkedHashSet<>(
                    StringEncoder.decodeList(
                            Specifications.parseString(specification, field))));
        } catch (IllegalArgumentException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    public static List<String> parseList(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseList(specification, Arrays.asList(field));
    }

    public static List<String> parseList(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return Collections.unmodifiableList(StringEncoder.decodeList(Specifications.parseString(specification, field)));
        } catch (IllegalArgumentException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    public static Class<?> parseClass(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseClass(specification, Arrays.asList(field));
    }

    public static Class<?> parseClass(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return Class.forName(parseString(specification, field));
        } catch (ClassNotFoundException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    public static <T> Class<? extends T> parseClass(Specification specification, Class<T> baseClass, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseClass(specification, baseClass, Arrays.asList(field));
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> parseClass(Specification specification, Class<T> baseClass, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        Objects.requireNonNull(baseClass);
        Class<?> parsedClass = parseClass(specification, field);
        if (!baseClass.isAssignableFrom(parsedClass)) {
            throw new InvalidSettingException(specification, specification.getSetting(field), "Specified class is not instance of " + baseClass.getName());
        }
        return (Class<? extends T>) parsedClass;
    }

    public static Specification parseSpecification(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseSpecification(specification, Arrays.asList(field));
    }

    public static Specification parseSpecification(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return MapSpecification.create(specification, field);
        } catch (ClassCastException | IllegalArgumentException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    public static Configuration parseConfiguration(Specification specification, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseConfiguration(specification, Arrays.asList(field));
    }

    public static Configuration parseConfiguration(Specification specification, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return ConfigurationProvider.get().parseConfiguration(
                    parseClass(specification, Configuration.class, add(field, "type")),
                    parseSpecification(specification, add(field, "settings")));
        } catch (ConfigurationNotFoundException | InvalidSpecificationException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    public static Configuration parseConfiguration(Specification specification, Object context, String... field) throws SettingNotFoundException, InvalidSettingException {
        return parseConfiguration(specification, context, Arrays.asList(field));
    }

    public static Configuration parseConfiguration(Specification specification, Object context, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return ConfigurationProvider.get().parseConfiguration(
                    parseClass(specification, Configuration.class, add(field, "type")),
                    parseSpecification(specification, add(field, "settings")),
                    context);
        } catch (ConfigurationNotFoundException | InvalidSpecificationException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field), cause);
        }
    }

    private static List<String> add(List<String> list, String value) {
        List<String> newList = new ArrayList<>(list);
        newList.add(value);
        return newList;
    }

    private Specifications() {
    }

}
