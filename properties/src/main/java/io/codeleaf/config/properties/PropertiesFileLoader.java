package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.spi.SpecificationLoader;
import io.codeleaf.config.util.ConfigDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileLoader.class);

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
    public Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException, SpecificationFormatException {
        LOGGER.debug("Specification location: " + getConfigurationFile(specificationName).getAbsolutePath());
        if (!hasSpecification(specificationName)) {
            throw new SpecificationNotFoundException(specificationName);
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(getConfigurationFile(specificationName)));
            return parser.parseSpecification(properties);
        } catch (IllegalArgumentException cause) {
            LOGGER.debug("Specification loading error: " + cause.getMessage());
            throw new SpecificationFormatException(specificationName, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSpecification(String specificationName) {
        return getConfigurationFile(specificationName).exists();
    }

    private File getConfigurationFile(String specificationName) {
        File file = new File(parentPath.getPath(), specificationName + ".properties");
        LOGGER.debug("Considering properties file: " + file.getAbsolutePath());
        return file;
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
