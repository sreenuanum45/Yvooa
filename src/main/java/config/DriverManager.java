package config;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverManager is responsible for managing WebDriver instances across different threads.
 * It supports multiple browsers, remote execution, and various configurations.
 */
public final class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    // ThreadLocal to ensure thread safety for WebDriver instances
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DriverManager() {
        throw new UnsupportedOperationException("DriverManager is a utility class and cannot be instantiated.");
    }

    /**
     * Initializes the WebDriver instance based on the configuration.
     * Supports multiple browsers and remote execution.
     */
    public static void initDriver() {
        if (driverThreadLocal.get() == null) {
            String browser = TestEnvironment.getBrowser().toLowerCase();

            try {

                switch (browser) {
                    case "chrome": {
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (TestEnvironment.isHeadless()) {
                            chromeOptions.addArguments("--headless");

                        }
                        /*if (TestEnvironment.isIncognito()) {
                            chromeOptions.addArguments("--incognito");
                        }*/
                        // 3️⃣ Disable "WebDriver" detection
                        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                        chromeOptions.setExperimentalOption("useAutomationExtension", false);
                        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                        chromeOptions.setCapability("acceptInsecureCerts", true);
                        // driverThreadLocal.set(new ChromeDriver(chromeOptions));
                        chromeOptions.addArguments("--disable-notifications");
                        chromeOptions.addArguments("--disable-extensions");
                        chromeOptions.addArguments("--disable-popup-blocking");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--disable-infobars");
                        chromeOptions.addArguments("--incognito");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-animations");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        Proxy proxy= new Proxy();
                        proxy.setHttpProxy("localhost:8888");
                        DesiredCapabilities cap = new DesiredCapabilities();
                        cap.setCapability(CapabilityType.PROXY, proxy);
                        driverThreadLocal.set(new ChromeDriver(chromeOptions));
                        driverThreadLocal.get().manage().window().maximize();
                        driverThreadLocal.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(100));
                        driverThreadLocal.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(70));
                        //driverThreadLocal.get().manage().timeouts().scriptTimeout(Duration.ofSeconds(100));

                        break;
                    }
                    case "firefox": {
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        // Additional Firefox options can be configured here

                        driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                        break;
                    }
                    case "edge": {
                        EdgeOptions edgeOptions = new EdgeOptions();
                        driverThreadLocal.set(new org.openqa.selenium.edge.EdgeDriver(edgeOptions));
                        break;
                    }
                    case "safari": {
                        driverThreadLocal.set(new org.openqa.selenium.safari.SafariDriver());
                        break;
                    }
                    default:
                        logger.error("Unsupported browser: {}", browser);
                        throw new IllegalArgumentException("Unsupported browser: " + browser);
                }

                // Set implicit wait
                int implicitWait = TestEnvironment.getImplicitWait();
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
                logger.info("Set implicit wait to {} seconds", implicitWait);

                // Set page load timeout
                int pageLoadTimeout = TestEnvironment.getPageLoadTimeout();
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
                logger.info("Set page load timeout to {} seconds", pageLoadTimeout);

                // Maximize window
                getDriver().manage().window().maximize();
                logger.info("Maximized the browser window");

                logger.info("WebDriver instance initialized for thread: {}", Thread.currentThread().getName());

            } catch (Exception e) {
                logger.error("Error initializing WebDriver for browser {}: {}", browser, e.getMessage());
                throw new RuntimeException("Failed to initialize WebDriver", e);
            }
        }
    }

    /**
     * Retrieves the current thread's WebDriver instance.
     *
     * @return WebDriver instance.
     * @throws IllegalStateException if WebDriver is not initialized.
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            logger.error("WebDriver instance is not initialized for thread: {}", Thread.currentThread().getName());
            throw new IllegalStateException("WebDriver not initialized. Call DriverManager.initDriver() first.");
        }
        return driverThreadLocal.get();
    }

    /**
     * Quits the WebDriver instance and removes it from the ThreadLocal storage.
     */
    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
            logger.info("WebDriver instance quit and removed for thread: {}", Thread.currentThread().getName());
        }
    }

    /**
     * Creates a Chrome WebDriver instance with specified options.
     *
     * @return Chrome WebDriver instance.
     */
    private static WebDriver createChromeDriver() {
        logger.info("Initializing ChromeDriver");

        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();
        configureBrowserOptions(options);

        // Add browser-specific options
        if (TestEnvironment.isHeadless()) {
            options.addArguments("--headless=new");
            logger.info("Running Chrome in headless mode");
        }

        // Add additional Chrome options from configuration
        String chromeArgs = TestEnvironment.getChromeArguments();
        if (chromeArgs != null && !chromeArgs.isEmpty()) {
            for (String arg : chromeArgs.split(",")) {
                options.addArguments(arg.trim());
                logger.info("Added Chrome argument: {}", arg.trim());
            }
        }

        // Handle remote execution if enabled
        if (TestEnvironment.isRemoteExecution()) {
            try {
                URL gridUrl = new URL(TestEnvironment.getRemoteGridUrl());
                return new RemoteWebDriver(gridUrl, options);
            } catch (Exception e) {
                logger.error("Invalid remote grid URL: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize remote ChromeDriver", e);
            }
        }

        return new ChromeDriver(options);
    }

    /**
     * Creates a Firefox WebDriver instance with specified options.
     *
     * @return Firefox WebDriver instance.
     */
    private static WebDriver createFirefoxDriver() {
        logger.info("Initializing FirefoxDriver");

        FirefoxOptions options = new FirefoxOptions();
        configureBrowserOptions(options);

        if (TestEnvironment.isHeadless()) {
            options.addArguments("--headless");
            logger.info("Running Firefox in headless mode");
        }

        String firefoxArgs = TestEnvironment.getFirefoxArguments();
        if (firefoxArgs != null && !firefoxArgs.isEmpty()) {
            for (String arg : firefoxArgs.split(",")) {
                options.addArguments(arg.trim());
                logger.info("Added Firefox argument: {}", arg.trim());
            }
        }

        if (TestEnvironment.isRemoteExecution()) {
            try {
                URL gridUrl = new URL(TestEnvironment.getRemoteGridUrl());
                return new RemoteWebDriver(gridUrl, options);
            } catch (Exception e) {
                logger.error("Invalid remote grid URL: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize remote FirefoxDriver", e);
            }
        }

        return new FirefoxDriver(options);
    }

    /**
     * Creates an Edge WebDriver instance with specified options.
     *
     * @return Edge WebDriver instance.
     */
    private static WebDriver createEdgeDriver() {
        logger.info("Initializing EdgeDriver");

        EdgeOptions options = new EdgeOptions();
        configureBrowserOptions(options);

        if (TestEnvironment.isHeadless()) {
            options.addArguments("--headless=new");
            logger.info("Running Edge in headless mode");
        }

        String edgeArgs = TestEnvironment.getEdgeArguments();
        if (edgeArgs != null && !edgeArgs.isEmpty()) {
            for (String arg : edgeArgs.split(",")) {
                options.addArguments(arg.trim());
                logger.info("Added Edge argument: {}", arg.trim());
            }
        }

        if (TestEnvironment.isRemoteExecution()) {
            try {
                URL gridUrl = new URL(TestEnvironment.getRemoteGridUrl());
                return new RemoteWebDriver(gridUrl, options);
            } catch (Exception e) {
                logger.error("Invalid remote grid URL: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize remote EdgeDriver", e);
            }
        }

        return new org.openqa.selenium.edge.EdgeDriver(options);
    }

    /**
     * Creates a Safari WebDriver instance with specified options.
     *
     * @return Safari WebDriver instance.
     */
    private static WebDriver createSafariDriver() {
        logger.info("Initializing SafariDriver");

        if (TestEnvironment.isRemoteExecution()) {
            try {
                URL gridUrl = new URL(TestEnvironment.getRemoteGridUrl());
                throw new UnsupportedOperationException("Remote execution for Safari is not supported in this implementation.");
            } catch (Exception e) {
                logger.error("Invalid remote grid URL or unsupported configuration for Safari: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize remote SafariDriver", e);
            }
        }

        return new org.openqa.selenium.safari.SafariDriver();
    }

    /**
     * Configures common browser options.
     *
     * @param options Mutable capabilities object.
     */
    private static void configureBrowserOptions(org.openqa.selenium.MutableCapabilities options) {
        // Set common capabilities
        options.setCapability("pageLoadStrategy", PageLoadStrategy.NORMAL);

        // Set proxy if configured
        String proxyAddress = TestEnvironment.getProxyAddress();
        if (proxyAddress != null && !proxyAddress.isEmpty()) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyAddress);
            proxy.setSslProxy(proxyAddress);
            options.setCapability("proxy", proxy);
            logger.info("Configured proxy: {}", proxyAddress);
        }


    }

    /**
     * Provides an explicit wait instance.
     *
     * @return WebDriverWait instance with configured timeout.
     */
    public static WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(TestEnvironment.getExplicitWait()));
    }
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

}
