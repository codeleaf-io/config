package io.codeleaf.config.spec;

import io.codeleaf.config.util.Settings;

import java.util.List;

/**
 * Thrown when a specification does not contain the a specific setting.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public class SettingNotFoundException extends InvalidSpecificationException {

    private final List<String> settingField;

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField) {
        super(configurationSpecification, createMessage(settingField, null));
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, String message) {
        super(configurationSpecification, createMessage(settingField, message));
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, String message, Throwable cause) {
        super(configurationSpecification, createMessage(settingField, message), cause);
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, Throwable cause) {
        super(configurationSpecification, createMessage(settingField, cause == null ? null : cause.getMessage()), cause);
        this.settingField = settingField;
    }

    /**
     * Returns the field of the setting that was not present in the specification
     *
     * @return the field of the setting that was not present in the specification
     */
    public List<String> getSettingField() {
        return settingField;
    }

    private static String createMessage(List<String> settingField, String message) {
        StringBuilder sb = new StringBuilder("No setting found: ");
        if (settingField.isEmpty()) {
            sb.append("<null>");
        } else {
            sb.append(Settings.toString(settingField));
        }
        if (message != null) {
            sb.append(": " + message);
        }
        return sb.toString();
    }

}
