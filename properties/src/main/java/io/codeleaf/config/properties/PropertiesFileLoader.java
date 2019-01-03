package io.codeleaf.config.properties;

import io.codeleaf.config.spec.Specification;
import io.codeleaf.config.spec.SpecificationNotFoundException;
import io.codeleaf.config.spec.ext.SpecificationLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesFileLoader implements SpecificationLoader {

    private static final PropertiesSpecificationParser PARSER = new PropertiesSpecificationParser();

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

    public static PropertiesFileLoader create(File parentPath) {
        requireDirectory(parentPath);
        return new PropertiesFileLoader(parentPath, PARSER);
    }

    public PropertiesFileLoader() {
        this(getDefaultDir(), PARSER);
    }

    private final File parentPath;
    private final PropertiesSpecificationParser parser;

    private PropertiesFileLoader(File parentPath, PropertiesSpecificationParser parser) {
        this.parentPath = parentPath;
        this.parser = parser;
    }

    @Override
    public Specification loadSpecification(String specificationName) throws SpecificationNotFoundException, IOException {
        if (!hasSpecification(specificationName)) {
            throw new SpecificationNotFoundException(specificationName);
        }
        Properties properties = new Properties();
        properties.load(new FileReader(getConfigurationFile(specificationName)));
        return parser.parseSpecification(properties);
    }

    @Override
    public boolean hasSpecification(String specificationName) {
        return getConfigurationFile(specificationName).exists();
    }

    private File getConfigurationFile(String specificationName) {
        return new File(parentPath.getPath(), specificationName + ".properties");
    }

}
