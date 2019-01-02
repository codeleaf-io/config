package io.codeleaf.config.util;

import io.codeleaf.config.spec.InvalidSettingException;
import io.codeleaf.config.spec.SettingNotFoundException;
import io.codeleaf.config.spec.Specification;

import java.util.List;

public final class Specifications {

    public static int parseInt(List<String> field, Specification specification) throws SettingNotFoundException, InvalidSettingException {
        try {
            return Integer.parseInt(specification.getValue(String.class, field));
        } catch (NumberFormatException cause) {
            throw new InvalidSettingException(specification, specification.getSetting(field));
        }
    }

    public static boolean parseBoolean(List<String> field, Specification specification) throws SettingNotFoundException, InvalidSettingException {
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
