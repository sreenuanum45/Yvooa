
package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.Region;
import com.applitools.eyes.locators.TextRegionSettings;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

import config.DriverManager;
import reporting.Log4jManager;


/**
 * AIIntegrationUtility integrates Applitools Eyes for advanced visual testing.
 * It manages Eyes lifecycle, configurations, and provides methods to perform various visual checks.
 */

public class AIIntegrationUtility {

    private Eyes eyes;
    private VisualGridRunner runner;
    private BatchInfo batch;
    private boolean isBatchConfigured;


/**
     * Constructor initializes Applitools Eyes with necessary configurations.
     * It sets up the batch information and initializes the VisualGridRunner for parallel execution.
     */

    public AIIntegrationUtility() {
        initializeRunner();
        initializeEyes();
    }


/**
     * Initializes the VisualGridRunner for parallel test execution.
     */

    private void initializeRunner() {
        // Configure the VisualGridRunner with desired concurrency
        RunnerOptions runnerOptions = new RunnerOptions().testConcurrency(5);
        runner = new VisualGridRunner(runnerOptions);

        // Set up a batch to group multiple tests
        batch = new BatchInfo("My BDD Project - " + config.TestEnvironment.getEnvironment());
        isBatchConfigured = false;
    }


/**
     * Initializes Applitools Eyes with API key and desired configurations.
     */

    private void initializeEyes() {
        eyes = new Eyes(runner);
        eyes.setApiKey(config.TestEnvironment.getApplitoolsApiKey());

        // Set batch information if not already set
        if (!isBatchConfigured) {
            eyes.setBatch(batch);
            isBatchConfigured = true;
        }

        // Configure Visual Grid options
        com.applitools.eyes.selenium.Configuration config = eyes.getConfiguration();
        config.setBatch(batch);
        // Add different browser and device configurations
        config.addBrowser(1200, 800, BrowserType.CHROME);
        config.addBrowser(1200, 800, BrowserType.FIREFOX);
        config.addBrowser(1200, 800, BrowserType.SAFARI);
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.LANDSCAPE);
        // Optionally, set match level
        config.setMatchLevel(MatchLevel.STRICT);
        eyes.setConfiguration(config);
    }


/**
     * Opens an Applitools Eyes test session.
     *
     * @param testName The name of the test.
     * @param viewport The size of the viewport (e.g., new RectangleSize(1200, 800)).
     */

    public void openEyes(String testName, RectangleSize viewport) {
        WebDriver driver = config.DriverManager.getDriver(); // Corrected DriverManager usage
        if (viewport == null) {
            throw new IllegalArgumentException("Viewport cannot be null for Applitools Eyes");
        }
        eyes.setExplicitViewportSize(viewport);
        try {
            eyes.open(driver, "My BDD Project", testName, viewport);
            Log4jManager.info("Opened Eyes for test: " + testName);
        } catch (Exception e) {
            Log4jManager.error("Failed to open Eyes for test: " + testName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesOpenError_" + sanitizeFileName(testName) + ".png");
            throw e;
        }
    }


/**
     * Performs a full-page visual checkpoint.
     *
     * @param checkpointName The name of the checkpoint.
     */

    public void checkWindow(String checkpointName) {
        try {
            eyes.checkWindow(checkpointName);
            Log4jManager.info("Performed Eyes checkWindow: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform checkWindow: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCheckWindowError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }


/**
     * Performs a visual checkpoint on a specific WebElement.
     *
     * @param element        The WebElement to check.
     * @param checkpointName The name of the checkpoint.
     */

    public void checkElement(WebElement element, String checkpointName) {
        try {
            eyes.check(checkpointName, com.applitools.eyes.selenium.fluent.Target.region(element));
            Log4jManager.info("Performed Eyes checkElement: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform checkElement: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCheckElementError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }


/**
     * Performs a region-based visual checkpoint.
     *
     * @param region         The region to check.
     * @param checkpointName The name of the checkpoint.
     */

    public void checkRegion(Region region, String checkpointName) {
        try {
            eyes.check(checkpointName, com.applitools.eyes.selenium.fluent.Target.region(region));
            Log4jManager.info("Performed Eyes checkRegion: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform checkRegion: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCheckRegionError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }


/**
     * Closes the Eyes test session and collates the results.
     */

    public void closeEyes() {
        try {
            eyes.closeAsync();
            Log4jManager.info("Closed Eyes session asynchronously.");
        } catch (Exception e) {
            Log4jManager.error("Failed to close Eyes session. Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCloseError.png");
            throw e;
        }
    }


/**
     * Aborts the Eyes test session if not already closed.
     * This should be called in test teardown to ensure resources are freed.
     */

    public void abortEyesIfNotClosed() {
        try {
            eyes.abortIfNotClosed();
            Log4jManager.info("Aborted Eyes session if not already closed.");
        } catch (Exception e) {
            Log4jManager.error("Failed to abort Eyes session. Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesAbortError.png");
            // Do not rethrow to prevent masking original exceptions
        }
    }


/**
     * Finalizes the VisualGridRunner and retrieves all test results.
     * This should be called after all tests have been executed.
     */

    public void finalizeRunner() {
        try {
            runner.getAllTestResults(false);
            Log4jManager.info("Finalized VisualGridRunner and retrieved all test results.");
        } catch (Exception e) {
            Log4jManager.error("Failed to get all test results from VisualGridRunner.", e);
            takeScreenshot("screenshots/eyesFinalizeRunnerError.png");
            throw new RuntimeException("Failed to get all test results from VisualGridRunner.", e);
        }
    }


/**
     * Takes a screenshot of the current browser window and saves it to the specified path.
     *
     * @param filePath The path where the screenshot will be saved.
     */

    private void takeScreenshot(String filePath) {
        try {
            // Ensure the directory exists
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                Log4jManager.info("Created screenshots directory at: " + screenshotDir.getAbsolutePath());
            }

            WebDriver driver = DriverManager.getDriver();
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileHandler.copy(source, destination);
            Log4jManager.info("Screenshot taken and saved to: " + filePath);
        } catch (IOException e) {
            Log4jManager.error("Failed to take screenshot. Error: " + e.getMessage());
            // Depending on your framework's design, you might want to throw a runtime exception here.
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }


/**
     * Retrieves a description of the WebElement for logging purposes.
     *
     * @param element The WebElement to describe.
     * @return A string description of the WebElement.
     */

    private String getElementDescription(WebElement element) {
        try {
            StringBuilder description = new StringBuilder();
            description.append("Tag: ").append(element.getTagName());
            String id = element.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                description.append(", ID: ").append(id);
            }
            String className = element.getAttribute("class");
            if (className != null && !className.isEmpty()) {
                description.append(", Class: ").append(className);
            }
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                description.append(", Text: '").append(text).append("'");
            }
            return description.toString();
        } catch (StaleElementReferenceException e) {
            return "Stale element reference.";
        } catch (Exception e) {
            return "Unable to describe element.";
        }
    }


/**
     * Sanitizes file names by removing or replacing invalid characters.
     *
     * @param name The original file name.
     * @return A sanitized file name safe for use in file systems.
     */

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    // ---------------------- Advanced Features ----------------------


/**
     * Sets a specific match level before performing a window check and resets it afterwards.
     *
     * @param checkpointName The name of the checkpoint.
     */

    public void checkWindowWithContentMatch(String checkpointName) {
        eyes.setMatchLevel(MatchLevel.CONTENT);
        checkWindow(checkpointName);
        eyes.setMatchLevel(MatchLevel.STRICT); // Reset to default
    }


/**
     * Performs an accessibility check on a specific WebElement.
     *
     * @param element        The WebElement to check.
     * @param checkpointName The name of the checkpoint.
     */

    public void checkElementAccessibility(WebElement element, String checkpointName) {
        try {
            
            eyes.checkElement(element, checkpointName);
            eyes.extractTextRegions(new TextRegionSettings(element.getText(),checkpointName));

            Log4jManager.info("Performed Eyes accessibility check: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform accessibility check: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesAccessibilityCheckError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }


/**
     * Handles multiple browser windows or tabs by switching to each and performing visual checks.
     */

    public void handleMultipleWindows() {
        WebDriver driver = DriverManager.getDriver();
        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            driver.switchTo().window(windowHandle);
            // Perform visual checks in the new window
            checkWindow("New Window Check");
        }
    }


/**
     * Adds an ignore region to a WebElement, performs a visual check, and removes the ignore regions afterwards.
     *
     * @param element        The WebElement to add as an ignore region.
     * @param checkpointName The name of the checkpoint.
     */

    public void addIgnoreRegion(WebElement element, String checkpointName) {
        try {
            eyes.addProperty(element.toString(), "true");
            eyes.checkElement(element, checkpointName);

            // Capture the entire page (excluding the ignored region)
            eyes.checkWindow("Full Page with Ignore Region - " + checkpointName);

            // Alternatively, capture a specific element (excluding the ignored region)
            // This might require a different method depending on your library version
            // eyes.checkElement(someElement, Target.excluding(region(element)));

            eyes.extractTextRegions(new TextRegionSettings(element.getText(),checkpointName));

        // Clean up after checks
            Log4jManager.info("Performed Eyes check with ignore region: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform Eyes check with ignore region: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCheckIgnoreRegionError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }


/**
     * Performs a visual check on a dynamically defined region.
     *
     * @param region         The region to check.
     * @param checkpointName The name of the checkpoint.
     */

    public void checkDynamicRegion(Region region, String checkpointName) {
        try {
            eyes.check(checkpointName, com.applitools.eyes.selenium.fluent.Target.region(region));
            Log4jManager.info("Performed Eyes checkDynamicRegion: " + checkpointName);
        } catch (Exception e) {
            Log4jManager.error("Failed to perform checkDynamicRegion: " + checkpointName + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/eyesCheckDynamicRegionError_" + sanitizeFileName(checkpointName) + ".png");
            throw e;
        }
    }
}

