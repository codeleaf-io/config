package io.codeleaf.config.util;

import io.codeleaf.config.spec.InvalidSettingException;
import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    private Specifications() {
    }

}
