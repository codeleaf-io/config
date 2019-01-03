package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.spi.SpecificationLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Implements a specification loader for properties files.
 *
 * @author tvburger@gmail.com
 * @see SpecificationLoader
 * @see Properties
 * @since 0.1.0
 */
public final class PropertiesFileLoader implements SpecificationLoader {

    private static final PropertiesSpecificationParser PARSER = new PropertiesSpecificationParser();

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
     * @param parentPath the parent path to use for reading the properties files.
     * @return the new instance
     * @throws IllegalArgumentException if the parentPath is not a directory
     */
    public static PropertiesFileLoader create(File parentPath) {
        requireDirectory(parentPath);
        return new PropertiesFileLoader(parentPath, PARSER);
    }

    /**
     * Constructs a new instance that reads from the default parent directory.
     *
     * @see #getDefaultDir()
     */
    public PropertiesFileLoader() {
        this(getDefaultDir(), PARSER);
    }

    private final File parentPath;
    private final PropertiesSpecificationParser parser;

    private PropertiesFileLoader(File parentPath, PropertiesSpecificationParser parser) {
        this.parentPath = parentPath;
        this.parser = parser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException {
        if (!hasSpecification(specificationName)) {
            throw new SpecificationNotFoundException(specificationName);
        }
        Properties properties = new Properties();
        properties.load(new FileReader(getConfigurationFile(specificationName)));
        return parser.parseSpecification(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSpecification(String specificationName) {
        return getConfigurationFile(specificationName).exists();
    }

    private File getConfigurationFile(String specificationName) {
        return new File(parentPath.getPath(), specificationName + ".properties");
    }

}
