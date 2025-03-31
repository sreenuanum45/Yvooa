package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reporting.Log4jManager;

/**
 * TestEnvironment is a utility class that provides access to configuration properties.
 * It ensures type safety, validation, and efficient access to environment-specific configurations.
 */
public final class TestEnvironment {

    private static final Logger logger = LoggerFactory.getLogger(TestEnvironment.class);

    // Private constructor to prevent instantiation
    private TestEnvironment() {
        throw new UnsupportedOperationException("TestEnvironment is a utility class and cannot be instantiated.");
    }

    /**
     * Retrieves the browser type from the configuration.
     *
     * @return Browser type as a String (e.g., "chrome", "firefox").
     * @throws ConfigurationException if the property is missing.
     */
    public static String getBrowser() {
        return getRequiredProperty("browser");
    }

    /**
     * Retrieves the base URL from the configuration.
     *
     * @return Base URL as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getBaseUrl() {
        return getRequiredProperty("baseUrl");
    }

    /**
     * Retrieves the Jira username from the configuration.
     *
     * @return Jira username as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getJiraUserName() {
        return getRequiredProperty("jira.username");
    }

    /**
     * Retrieves the Jira password from the configuration.
     *
     * @return Jira password as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getJiraPassword() {
        return getRequiredProperty("jira.password");
    }

    /**
     * Retrieves the Jira URL from the configuration.
     *
     * @return Jira URL as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getJiraUrl() {
        return getRequiredProperty("jira.url");
    }

    /**
     * Retrieves the Applitools API key from the configuration.
     *
     * @return Applitools API key as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getApplitoolsApiKey() {
        String apiKey = ConfigReader.getInstance().getProperty("applitools.api.key");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Applitools API key is not set in the environment variables.");
        }
        return apiKey;
    }

    /**
     * Retrieves the Excel file path from the configuration.
     *
     * @return Excel file path as a String.
     * @throws ConfigurationException if the property is missing.
     */
    public static String getExcelFilePath() {
        return getRequiredProperty("excel.file.path");
    }

    /**
     * Retrieves the implicit wait timeout value from the configuration.
     *
     * @return Implicit wait timeout in seconds as an integer.
     */
    public static int getImplicitWait() {
        String value = ConfigReader.getInstance().getProperty("implicit.wait", "10");
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid implicit.wait value '{}'. Using default of 10 seconds.", value);
            return 10;
        }
    }

    /**
     * Retrieves the explicit wait timeout value from the configuration.
     *
     * @return Explicit wait timeout in seconds as an integer.
     */
    public static int getExplicitWait() {
        String value = ConfigReader.getInstance().getProperty("explicit.wait", "20");
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit.wait value '{}'. Using default of 20 seconds.", value);
            return 20;
        }
    }

    /**
     * Retrieves the page load timeout value from the configuration.
     *
     * @return Page load timeout in seconds as an integer.
     */
    public static int getPageLoadTimeout() {
        String value = ConfigReader.getInstance().getProperty("page.load.timeout", "60");
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid page.load.timeout value '{}'. Using default of 60 seconds.", value);
            return 60;
        }
    }

    /**
     * Determines if the browser should run in headless mode based on the configuration.
     *
     * @return True if headless mode is enabled, false otherwise.
     */
    public static boolean isHeadless() {
        String value = ConfigReader.getInstance().getProperty("headless", "false").trim().toLowerCase();
        return value.equals("true") || value.equals("yes");
    }
    public static boolean isIncognito() {
        String value = ConfigReader.getInstance().getProperty("incognito", "false").trim().toLowerCase();
        return value.equals("true") || value.equals("yes");
    }

    /**
     * Determines if remote execution is enabled based on the configuration.
     *
     * @return True if remote execution is enabled, false otherwise.
     */
    public static boolean isRemoteExecution() {
        String value = ConfigReader.getInstance().getProperty("remote.execution", "false").trim().toLowerCase();
        return value.equals("true") || value.equals("yes");
    }

    /**
     * Retrieves the remote Selenium Grid URL from the configuration.
     *
     * @return Remote Grid URL as a String.
     * @throws ConfigurationException if remote execution is enabled but the URL is missing.
     */
    public static String getRemoteGridUrl() {
        if (isRemoteExecution()) {
            return getRequiredProperty("remote.grid.url");
        }
        return null;
    }

    /**
     * Retrieves the proxy address from the configuration.
     *
     * @return Proxy address as a String.
     */
    public static String getProxyAddress() {
        return ConfigReader.getInstance().getProperty("proxy.address", "").trim();
    }

    /**
     * Retrieves additional Chrome browser arguments from the configuration.
     *
     * @return Comma-separated Chrome arguments as a String.
     */
    public static String getChromeArguments() {
        return ConfigReader.getInstance().getProperty("chrome.arguments", "").trim();
    }

    /**
     * Retrieves additional Firefox browser arguments from the configuration.
     *
     * @return Comma-separated Firefox arguments as a String.
     */
    public static String getFirefoxArguments() {
        return ConfigReader.getInstance().getProperty("firefox.arguments", "").trim();
    }

    /**
     * Retrieves additional Edge browser arguments from the configuration.
     *
     * @return Comma-separated Edge arguments as a String.
     */
    public static String getEdgeArguments() {
        return ConfigReader.getInstance().getProperty("edge.arguments", "").trim();
    }

    /**
     * Retrieves the logging level from the configuration.
     *
     * @return Logging level as a String (e.g., "DEBUG", "INFO", "ERROR").
     */
    public static String getLoggingLevel() {
        return ConfigReader.getInstance().getProperty("logging.level", "INFO").trim();
    }

    // Add more getter methods as needed following the same pattern.

    /**
     * Helper method to retrieve required properties.
     *
     * @param key The configuration property key.
     * @return The trimmed property value.
     * @throws ConfigurationException if the property is missing or empty.
     */
    private static String getRequiredProperty(String key) {
        String value = ConfigReader.getInstance().getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            logger.error("Configuration property '{}' is missing or empty.", key);
            throw new ConfigurationException("Missing configuration: " + key);
        }
        return value.trim();
    }
    /**
     * Retrieves the Applitools API key from environment variables.
     *
     * @return The Applitools API key as a String.
     * @throws IllegalStateException if the API key is not set.
     */

    /**
     * Retrieves the current testing environment.
     *
     * @return The current environment as a String (e.g., "DEV", "STAGING", "PROD").
     */
    public static String getEnvironment() {
        String environment = System.getenv("TEST_ENVIRONMENT");
        if (environment == null || environment.isEmpty()) {
            // Default environment if not set
            environment = "TEST";
            Log4jManager.warn("TEST_ENVIRONMENT not set. Defaulting to DEV.");
        }
        return environment.toUpperCase();
    }











}
