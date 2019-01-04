package io.codeleaf.config.spec;

/**
 * Thrown when a specification has a specific invalid setting.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
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

    /**
     * Returns the setting that was invalid
     *
     * @return the setting that was invalid
     */
    public Specification.Setting getSetting() {
        return setting;
    }

}
