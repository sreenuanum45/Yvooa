package config;


import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.injectors.ConstructorInjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PicoContainerConfig is responsible for configuring and managing the PicoContainer
 * instance used for dependency injection within the application.
 */
public final class PicoContainerConfig {
    private static final Logger logger = LoggerFactory.getLogger(PicoContainerConfig.class);
    private static MutablePicoContainer container;

    // Private constructor to prevent instantiation
    private PicoContainerConfig() {
        throw new UnsupportedOperationException("PicoContainerConfig is a utility class and cannot be instantiated.");
    }

    /**
     * Retrieves the singleton instance of the PicoContainer.
     * Initializes the container if it hasn't been initialized yet.
     *
     * @return MutablePicoContainer instance.
     */
    public static synchronized MutablePicoContainer getContainer() {
        if (container == null) {
            logger.info("Initializing PicoContainer.");
            container = new DefaultPicoContainer(
                    new Caching().wrap(new ConstructorInjection())
            );
            registerComponents();
            logger.info("PicoContainer initialized successfully.");
        }
        return container;
    }

    /**
     * Registers all necessary components into the PicoContainer.
     * Components are added with appropriate scopes and dependencies.
     */
    private static void registerComponents() {
        logger.info("Registering components with PicoContainer.");

        // Register utility classes
        container.addComponent(utilities.CommonActions.class);
        container.addComponent(utilities.ExcelUtility.class);
        container.addComponent(utilities.WaitUtility.class);
        logger.info("All components registered with PicoContainer.");
    }

    /**
     * Retrieves a component instance from the PicoContainer by its class type.
     *
     * @param <T>   The type of the component.
     * @param clazz The Class object of the component.
     * @return An instance of the requested component.
     * @throws ComponentNotFoundException if the component is not found in the container.
     */


    /**
     * Exception thrown when a requested component is not found in the PicoContainer.
     */
    public static class ComponentNotFoundException extends RuntimeException {
        public ComponentNotFoundException(String message) {
            super(message);
        }
    }
    // In getComponent method
    public static <T> T getComponent(Class<T> clazz) {
        T component = container.getComponent(clazz);
        if (component == null) {
            logger.error("Component {} not found in PicoContainer.", clazz.getName());
            throw new ComponentNotFoundException("Component not found: " + clazz.getName());
        }
        return component;
    }
}
