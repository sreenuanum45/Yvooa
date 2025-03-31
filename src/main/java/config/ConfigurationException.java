package config;

/**
 * ConfigurationException is thrown when there is an issue with loading or accessing configuration properties.
 */
public class ConfigurationException extends RuntimeException {
    /**
     * Constructs a new ConfigurationException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigurationException with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause   The cause of the exception.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
