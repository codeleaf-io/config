package io.codeleaf.config.spec;

public class InvalidSettingException extends InvalidSpecificationException {

    private final Specification.Setting setting;

    public InvalidSettingException(Specification configurationSpecification, Specification.Setting setting) {
        super(configurationSpecification);
        this.setting = setting;
    }

    public InvalidSettingException(Specification configurationSpecification, Specification.Setting setting, String message) {
        super(configurationSpecification, message);
        this.setting = setting;
    }

    public InvalidSettingException(Specification configurationSpecification, Specification.Setting setting, String message, Throwable cause) {
        super(configurationSpecification, message, cause);
        this.setting = setting;
    }

    public InvalidSettingException(Specification configurationSpecification, Specification.Setting setting, Throwable cause) {
        super(configurationSpecification, cause);
        this.setting = setting;
    }

    public Specification.Setting getSetting() {
        return setting;
    }

}
