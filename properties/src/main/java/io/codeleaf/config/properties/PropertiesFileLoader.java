package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.spi.SpecificationLoader;
import io.codeleaf.config.util.ConfigDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
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

    private static final PropertiesSpecificationParser PARSER = new PropertiesSpecificationParser();

    /**
     * Constructs a new instance that reads from the default parent directory.
     *
     * @see ConfigDirectory#getDefaultDir()
     */
    public PropertiesFileLoader() {
        this(ConfigDirectory.getDefaultDir(), PARSER);
    }

    /**
     * Creates a new instance reading from the parentPath.
     *
     * @param parentPath the parent path to use for reading the properties files.
     * @return the new instance
     * @throws IllegalArgumentException if the parentPath is not a directory
     */
    public static PropertiesFileLoader create(File parentPath) {
        ConfigDirectory.requireAccessibleDirectory(parentPath);
        return new PropertiesFileLoader(parentPath, PARSER);
    }
}
