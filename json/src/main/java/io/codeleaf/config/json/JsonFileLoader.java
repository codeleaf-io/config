package io.codeleaf.config.json;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.spi.SpecificationLoader;

import java.io.File;
import java.io.IOException;

public final class JsonFileLoader implements SpecificationLoader {

    /**
     * Gets the default parent directory for the properties files. This will be read from the <code>config.dir</code>
     * system property. If this property is not defined, it will fallback to the <code>user.dir</code> system property.
     *
     * @return the default parent directory for reading the properties files.
     * @see System#getProperties()
     */
    public static File getDefaultDir() {
        File file = new File(System.getProperty("config.dir", System.getProperty("user.dir")));
        requireDirectory(file);
        return file;
    }

    private static void requireDirectory(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + file.getAbsolutePath());
        }
    }

    /**
     * Creates a new instance reading from the parentPath.
     *
     * @param parentPath the parent path to use for reading the json files.
     * @return the new instance
     * @throws IllegalArgumentException if the parentPath is not a directory
     */
    public static JsonFileLoader create(File parentPath) {
        requireDirectory(parentPath);
        return new JsonFileLoader(parentPath);
    }

    public JsonFileLoader(File parentPath) {

    }

    @Override
    public Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        return null;
    }

    @Override
    public boolean hasSpecification(String specificationName) {
        return false;
    }
}
