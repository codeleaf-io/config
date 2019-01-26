package io.codeleaf.config.spec;

import java.io.Serializable;
import java.util.*;

/**
 * This represents a specification. A specification consists of a collection of settings.
 * Each setting has a field, modeled as a <code>List&lt;String&gt;</code>, and a value, modeled as a <code>Object</code>.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public interface Specification extends Iterable<Specification.Setting> {

    /**
     * Returns an iterable over the settings starting with the given field prefix.
     *
     * @param fieldPrefix the field prefix that must match to be included in the iterable
     * @return the iterable of matching settings
     */
    Iterable<Setting> getSettings(List<String> fieldPrefix);

    /**
     * Returns an iterable over the settings starting with the given field prefix.
     *
     * @param fieldPrefix the field prefix that must match to be included in the iterable
     * @return the iterable of matching settings
     */
    default Iterable<Setting> getSettings(String... fieldPrefix) {
        return getSettings(Arrays.asList(fieldPrefix));
    }

    /**
     * Returns the fields of the corresponding settings in this specification that all start with the given field prefix.
     *
     * @param fieldPrefix the field prefix to match
     * @return the fields of the corresponding settings in this specification that all start with fieldPrefix
     */
    List<List<String>> getDefined(List<String> fieldPrefix);

    /**
     * Returns the fields of the corresponding settings in this specification that all start with the given field prefix.
     *
     * @param fieldPrefix the field prefix to match
     * @return the fields of the corresponding settings in this specification that all start with fieldPrefix
     */
    default List<List<String>> getDefined(String... fieldPrefix) {
        return getDefined(Arrays.asList(fieldPrefix));
    }

    /**
     * Returns the next level of field part names of the corresponding settings in this specification that all start with the given field prefix.
     *
     * @param fieldPrefix the field prefix to match
     * @return the next level of field part names
     */
    default List<String> getChilds(List<String> fieldPrefix) {
        Set<String> childs = new LinkedHashSet<>();
        for (List<String> defined : getDefined(fieldPrefix)) {
            if (defined.size() > fieldPrefix.size()) {
                childs.add(defined.get(fieldPrefix.size()));
            }
        }
        return new ArrayList<>(childs);
    }

    /**
     * Returns the next level of field part names of the corresponding settings in this specification that all start with the given field prefix.
     *
     * @param fieldPrefix the field prefix to match
     * @return the next level of field part names
     */
    default List<String> getChilds(String... fieldPrefix) {
        return getChilds(Arrays.asList(fieldPrefix));
    }

    /**
     * Returns <code>true</code> if the setting is present
     *
     * @param field the field identifying the setting
     * @return <code>true</code> if the setting with specified field is present
     */
    boolean hasSetting(List<String> field);

    /**
     * Returns <code>true</code> if the setting is present
     *
     * @param field the field identifying the setting
     * @return <code>true</code> if the setting with specified field is present
     */
    default boolean hasSetting(String... field) {
        return hasSetting(Arrays.asList(field));
    }

    /**
     * Returns the setting with the specified field
     *
     * @param field the field identifying the setting
     * @return the setting identified by the specified field
     * @throws SettingNotFoundException if no setting with the specified field is present in this specification
     */
    Setting getSetting(List<String> field) throws SettingNotFoundException;

    /**
     * Returns the setting with the specified field
     *
     * @param field the field identifying the setting
     * @return the setting identified by the specified field
     * @throws SettingNotFoundException if no setting with the specified field is present in this specification
     */
    default Setting getSetting(String... field) throws SettingNotFoundException {
        return getSetting(Arrays.asList(field));
    }

    /**
     * Returns the value of the setting specified by field
     *
     * @param field the field identifying the setting whose value is returned
     * @return the value of the setting identified by specified field
     * @throws SettingNotFoundException if the setting is not present in this specification
     */
    default Object getValue(List<String> field) throws SettingNotFoundException {
        return getSetting(field).getValue();
    }

    /**
     * Returns the value of the setting specified by field
     *
     * @param field the field identifying the setting whose value is returned
     * @return the value of the setting identified by specified field
     * @throws SettingNotFoundException if the setting is not present in this specification
     */
    default Object getValue(String... field) throws SettingNotFoundException {
        return getValue(Arrays.asList(field));
    }

    /**
     * Returns the value of the settings specified by field, cast as typeClass.
     *
     * @param typeClass the type class of the value to return
     * @param field     the field identifying the setting whose value is returned
     * @param <T>       the type of the value to return
     * @return the value of the setting identified by specified field
     * @throws SettingNotFoundException if the setting is not present in this specification
     * @throws InvalidSettingException  if the value can't be cast to typeClass
     */
    default <T> T getValue(Class<T> typeClass, List<String> field) throws SettingNotFoundException, InvalidSettingException {
        try {
            return typeClass.cast(getValue(field));
        } catch (ClassCastException cause) {
            throw new InvalidSettingException(this, getSetting(field));
        }
    }

    /**
     * Returns the value of the settings specified by field, cast as typeClass.
     *
     * @param typeClass the type class of the value to return
     * @param field     the field identifying the setting whose value is returned
     * @param <T>       the type of the value to return
     * @return the value of the setting identified by specified field
     * @throws SettingNotFoundException if the setting is not present in this specification
     * @throws InvalidSettingException  if the value can't be cast to typeClass
     */
    default <T> T getValue(Class<T> typeClass, String... field) throws SettingNotFoundException, InvalidSettingException {
        return getValue(typeClass, Arrays.asList(field));
    }

    /**
     * Represents a setting within a specification.
     */
    class Setting implements Serializable {

        private final List<String> field;
        private final Object value;

        public Setting(List<String> field, Object value) {
            this.field = field;
            this.value = value;
        }

        /**
         * Returns the field of the setting
         *
         * @return the field
         */
        public List<String> getField() {
            return field;
        }

        /**
         * Returns the value of the setting
         *
         * @return the value
         */
        public Object getValue() {
            return value;
        }

    }
}
