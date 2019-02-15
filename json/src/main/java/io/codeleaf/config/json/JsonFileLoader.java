package io.codeleaf.config.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationFormatException;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.impl.MapSpecification;
import io.codeleaf.config.spec.spi.SpecificationLoader;
import io.codeleaf.config.util.ConfigDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implements a specification loader for json files.
 *
 * @author tvburger@gmail.com
 * @see SpecificationLoader
 * @see MapSpecification
 * @see ObjectMapper
 * @since 0.1.0
 */
public final class JsonFileLoader implements SpecificationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFileLoader.class);

    private final File parentPath;
    private final ObjectMapper objectMapper;

    private JsonFileLoader(File parentPath, ObjectMapper objectMapper) {
        this.parentPath = parentPath;
        this.objectMapper = objectMapper;
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
            Map<?, ?> map = objectMapper.readValue(getConfigurationFile(specificationName), LinkedHashMap.class);
            return MapSpecification.create(MapSpecification.normalize(map));
        } catch (JsonParseException | JsonMappingException | IllegalArgumentException cause) {
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
        return new File(parentPath.getPath(), specificationName + ".json");
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Constructs a new instance that reads from the default parent directory.
     *
     * @see ConfigDirectory#getDefaultDir()
     */
    public JsonFileLoader() {
        this(ConfigDirectory.getDefaultDir(), MAPPER);
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
        return new JsonFileLoader(parentPath, MAPPER);
    }
}
