package io.codeleaf.config.spec;

import java.util.List;

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

    public List<String> getSettingField() {
        return settingField;
    }

}
