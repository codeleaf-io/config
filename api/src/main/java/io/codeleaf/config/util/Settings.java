package io.codeleaf.config.util;

import java.util.List;

/**
 * Provides utility classes for settings.
 *
 * @author tvburger@gmail.com
 * @see io.codeleaf.config.spec.Specification.Setting
 * @since 0.1.0
 */
public final class Settings {

    public static boolean prefixMatches(List<String> fieldPrefix, List<String> field) {
        for (int i = 0; i < fieldPrefix.size(); i++) {
            if (!fieldPrefix.get(i).equals(field.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean sameField(List<String> field1, List<String> field2) {
        if (field1.size() != field2.size()) {
            return false;
        }
        for (int i = 0; i < field1.size(); i++) {
            if (!field1.get(i).equals(field2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Settings() {
    }

}
