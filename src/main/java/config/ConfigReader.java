package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private Properties properties;
    private String environment;

    // Private constructor to prevent instantiation
    private ConfigReader() {
        loadEnvironment();
        loadProperties();
        validateProperties();
    }

    // Bill Pugh Singleton for thread safety
    private static class ConfigReaderHolder {
        private static final ConfigReader INSTANCE = new ConfigReader();
    }

    public static ConfigReader getInstance() {
        return ConfigReaderHolder.INSTANCE;
    }

    // Load environment with better handling
    private void loadEnvironment() {
        environment = System.getProperty("env", "dev");
        logger.info("Loading configuration for environment: {}", environment);
    }

    // Load properties efficiently
    private void loadProperties() {
        rwLock.writeLock().lock();
        try {
            properties = new Properties();

            // Load base config
            loadConfigFile("config.properties");

            // Load environment-specific config
            loadConfigFile(String.format("config-%s.properties", environment));

        } finally {
            rwLock.writeLock().unlock();
        }
    }

    // Helper method to load properties
    private void loadConfigFile(String fileName) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (stream != null) {
                properties.load(stream);
                logger.info("Loaded configuration from {}", fileName);
            } else {
                logger.warn("Configuration file '{}' not found in classpath", fileName);
            }
        } catch (IOException e) {
            logger.error("Error loading configuration file: {}", fileName, e);
        }
    }

    // Validate essential properties
    private void validateProperties() {
        validateRequiredProperty("baseUrl");
    }

    // Helper method to validate required properties
    private void validateRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            logger.error("Essential property '{}' is missing", key);
            throw new IllegalStateException("Missing essential configuration: " + key);
        }
    }

    // Get property with thread safety
    public String getProperty(String key) {
        rwLock.readLock().lock();
        try {
            return properties.getProperty(key);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    // Get property with a default value
    public String getProperty(String key, String defaultValue) {
        rwLock.readLock().lock();
        try {
            return properties.getProperty(key, defaultValue);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    // Reload properties dynamically
    public void reload() {
        logger.info("Reloading configuration...");
        loadProperties();
        validateProperties();
    }

    // Get the current environment
    public String getEnvironment() {
        return environment;
    }

    // Allow environment to be set dynamically (for testing or runtime updates)
    public void setEnvironment(String env) {
        if (env == null || env.trim().isEmpty()) {
            throw new IllegalArgumentException("Environment cannot be null or empty");
        }
        this.environment = env;
        reload();
    }
}
