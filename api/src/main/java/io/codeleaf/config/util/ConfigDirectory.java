package io.codeleaf.config.util;

import java.io.File;
import java.util.Objects;

/**
 * Provides a utility for the directory containing the specifications for configurations.
 *
 * @author tvburger@gmail.com
 * @since 0.1.0
 */
public final class ConfigDirectory {

    private ConfigDirectory() {
    }

    /**
     * Gets the default parent directory for the specification files. This will be read from the <code>config.dir</code>
     * system property. If this property is not defined, it will fallback to the <code>user.dir</code> system property.
     *
     * @return the default parent directory for reading the specification files.
     * @see System#getProperties()
     */
    public static File getDefaultDir() {
        File file = new File(System.getProperty("config.dir", System.getProperty("user.dir")));
        requireAccessibleDirectory(file);
        return file;
    }

    /**
     * Requires file to be a directory in which we can read specification files.
     *
     * @param specDir the directory containing the specification files
     * @throws IllegalArgumentException if specDir is not a directory or not executable
     * @throws NullPointerException if specDir is <code>null</code>
     */
    public static void requireAccessibleDirectory(File specDir) {
        Objects.requireNonNull(specDir);

        if (!specDir.isDirectory() || !specDir.canExecute()) {
            throw new IllegalArgumentException("Not a directory: " + specDir.getAbsolutePath());
        }
    }
}
