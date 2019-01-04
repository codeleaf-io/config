package io.codeleaf.config.spec;

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
        super(configurationSpecification);
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, String message) {
        super(configurationSpecification, message);
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, String message, Throwable cause) {
        super(configurationSpecification, message, cause);
        this.settingField = settingField;
    }

    public SettingNotFoundException(Specification configurationSpecification, List<String> settingField, Throwable cause) {
        super(configurationSpecification, cause);
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

}
