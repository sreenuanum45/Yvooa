package designpatterns;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.DriverManager;


/**
 * SingletonDriver ensures that only one instance of SingletonDriver exists throughout the application.
 * It provides a global access point to the WebDriver instance managed by DriverManager.
 *
 * <p><strong>Note:</strong> Using SingletonDriver in conjunction with DriverManager's ThreadLocal
 * can lead to unexpected behaviors in multi-threaded environments. It's recommended to use either
 * SingletonDriver for single-threaded scenarios or DriverManager with ThreadLocal for multi-threaded
 * scenarios, but not both simultaneously.</p>
 */
public final class SingletonDriver {

    private static final Logger logger = LoggerFactory.getLogger(SingletonDriver.class);
    private static volatile SingletonDriver instance = null;
    private WebDriver driver;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the WebDriver using DriverManager.
     */
    private SingletonDriver() {
        try {
            logger.info("Initializing SingletonDriver instance.");
            DriverManager.initDriver();
            driver = DriverManager.getDriver();
            logger.info("WebDriver initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver in SingletonDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver in SingletonDriver", e);
        }
    }

    /**
     * Provides the global access point to the SingletonDriver instance.
     *
     * @return SingletonDriver instance.
     */
    public static SingletonDriver getInstance() {
        if (instance == null) {
            synchronized (SingletonDriver.class) {
                if (instance == null) {
                    instance = new SingletonDriver();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the WebDriver instance.
     *
     * @return WebDriver instance.
     */
    public WebDriver getDriver() {
        if (driver == null) {
            logger.error("WebDriver instance is null. Re-initializing DriverManager.");
            DriverManager.initDriver();
            driver = DriverManager.getDriver();
        }
        return driver;
    }

    /**
     * Quits the WebDriver instance and cleans up resources.
     * Should be called at the end of the test execution.
     */
    public void quitDriver() {
        if (driver != null) {
            logger.info("Quitting WebDriver instance.");
            DriverManager.quitDriver();
            driver = null;
            logger.info("WebDriver instance quit successfully.");
        } else {
            logger.warn("WebDriver instance is already null.");
        }
    }
}
