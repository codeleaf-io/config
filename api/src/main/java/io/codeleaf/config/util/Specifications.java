package io.codeleaf.config.util;

import io.codeleaf.common.utils.StringEncoder;
import io.codeleaf.config.spec.InvalidSettingException;
import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;

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

    public static Set<String> parseSet(Specification specification, String... field) throws SettingNotFoundException {
        return parseSet(specification, Arrays.asList(field));
    }

    public static Set<String> parseSet(Specification specification, List<String> field) throws SettingNotFoundException {
        return Collections.unmodifiableSet(new LinkedHashSet<>(
                StringEncoder.decodeList(
                        Specifications.parseString(specification, field))));
    }

    public static List<String> parseList(Specification specification, String... field) throws SettingNotFoundException {
        return parseList(specification, Arrays.asList(field));
    }

    public static List<String> parseList(Specification specification, List<String> field) throws SettingNotFoundException {
        return Collections.unmodifiableList(StringEncoder.decodeList(Specifications.parseString(specification, field)));
    }

    private Specifications() {
    }

}
