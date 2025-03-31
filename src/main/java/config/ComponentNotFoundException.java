package config;

/**
 * ComponentNotFoundException is thrown when a requested component is not found in the PicoContainer.
 */
public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException(String message) {
        super(message);
    }
}
