package io.codeleaf.config.json;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.impl.MapSpecification;
import io.codeleaf.config.spec.spi.SpecificationLoader;
import io.codeleaf.config.util.ConfigDirectory;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * Implements a specification loader for json files.
 *
 * @author tvburger@gmail.com
 * @see SpecificationLoader
 * @see MapSpecification
 * @see Jsonb
 * @since 0.1.0
 */
public final class JsonFileLoader implements SpecificationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFileLoader.class);

    private final File parentPath;
    private final Jsonb jsonb;

    private JsonFileLoader(File parentPath, Jsonb jsonb) {
        this.parentPath = parentPath;
        this.jsonb = jsonb;
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
        try (InputStream in = new FileInputStream(getConfigurationFile(specificationName))) {
            return doParseSpecification(in, specificationName);
        }
    }

    public Specification parseSpecification(InputStream in) throws IOException, SpecificationFormatException {
        return doParseSpecification(in, "<InputStream>");
    }

    public Specification parseSpecification(String value) throws IOException, SpecificationFormatException {
        try (InputStream in = new ByteArrayInputStream(value.getBytes())) {
            return doParseSpecification(in, "<String>");
        }
    }

    private Specification doParseSpecification(InputStream in, String specificationName) throws IOException, SpecificationFormatException {
        try {
            Map<?, ?> map = jsonb.fromJson(in, Map.class);
            return MapSpecification.create(MapSpecification.normalize(map));
        } catch (JsonbException | IllegalArgumentException cause) {
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
        File file = new File(parentPath.getPath(), specificationName + ".json");
        LOGGER.debug("Considering json file: " + file.getAbsolutePath());
        return file;
    }

    private static final Jsonb JSONB = JsonbBuilder.create();

    /**
     * Constructs a new instance that reads from the default parent directory.
     *
     * @see ConfigDirectory#getDefaultDir()
     */
    public JsonFileLoader() {
        this(ConfigDirectory.getDefaultDir(), JSONB);
    }

    /**
     * Creates a new instance reading from the parentPath.
     *
     * @param parentPath the parent path to use for reading the json files.
     * @return the new instance
     * @throws IllegalArgumentException if the parentPath is not a directory
     */
    public static JsonFileLoader create(File parentPath) {
        ConfigDirectory.requireAccessibleDirectory(parentPath);
        return new JsonFileLoader(parentPath, JSONB);
    }
}
