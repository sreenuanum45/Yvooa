package reporting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

import config.DriverManager;
import config.TestEnvironment;

/**
 * Advanced ExtentManager class to manage and customize ExtentReports.
 * <p>
 * This class provides methods to:
 * <ul>
 *   <li>Initialize and configure the ExtentReports instance with custom styling and system info.</li>
 *   <li>Create test nodes (and child nodes) using a ThreadLocal for thread safety.</li>
 *   <li>Capture and attach screenshots (both file-based and Base64) with unique file names.</li>
 *   <li>Log steps and exceptions with screenshots in one call.</li>
 *   <li>Retrieve the generated report file path.</li>
 * </ul>
 * </p>
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();
    private static String reportFile;

    /**
     * Creates and initializes the ExtentReports instance with advanced configuration.
     *
     * @return the ExtentReports instance
     */
    public static synchronized ExtentReports createInstance() {
        if (extent == null) {
            try {
                // Create a unique report file name using a timestamp
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                reportFile = System.getProperty("user.dir") + "/target/extentReport/extent-report-" + timeStamp + ".html";

                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
                sparkReporter.config().setTheme(Theme.DARK);
                sparkReporter.config().setDocumentTitle("Automation Test Report");
                sparkReporter.config().setReportName("My BDD Project - Test Execution Report");
                sparkReporter.config().setEncoding("utf-8");
                sparkReporter.config().setProtocol(Protocol.HTTPS);
                sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
                sparkReporter.config().setTimelineEnabled(true);
                sparkReporter.config().setJs("js-string");

                // Custom CSS for branding and styling (update the logo path as needed)
                String customCSS = ".brand-logo { " +
                        "background-image: url('C:\\Users\\sreenu\\Downloads\\DALL·E 2025-02-09 16.51.30 - A sleek and professional logo design for 'Anumandla Sreenu', featuring modern, bold typography with a futuristic and innovative feel. The initials 'AS.webp\"'); " +
                        "background-repeat: no-repeat; " +
                        "background-position: left center; " +
                        "padding-left: 60px; " +
                        "height: 60px; " +
                        "}";
                sparkReporter.config().setCss(customCSS);

                // Optionally load external XML configuration if exists
                File xmlConfig = new File("src/main/resources/extent-config.xml");
                if (xmlConfig.exists()) {
                    sparkReporter.loadXMLConfig(xmlConfig);
                }

                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);
                // Set initial system information for the report
                extent.setSystemInfo("yuooa", "My BDD Project");
                extent.setSystemInfo("Environment", TestEnvironment.getEnvironment());
                extent.setSystemInfo("Browser", "Chrome");
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));

                Log4jManager.info("Initialized ExtentReports with SparkReporter at: " + reportFile);
            } catch (Exception e) {
                Log4jManager.error("Failed to create ExtentReports instance.", e);
            }
        }
        return extent;
    }

    /**
     * Returns the current ExtentReports instance, creating one if necessary.
     *
     * @return the ExtentReports instance
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Creates a new test in the report and stores it in a ThreadLocal variable.
     *
     * @param testName the name of the test or scenario.
     */
    public static void createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        extentTestThread.set(test);
        Log4jManager.info("Created test: " + testName);
    }

    /**
     * Creates a child node (sub-test) under the current test.
     *
     * @param nodeName the name of the node.
     * @return the created ExtentTest node.
     */
    public static ExtentTest createNode(String nodeName) {
        ExtentTest parentTest = getTest();
        if (parentTest == null) {
            Log4jManager.error("Parent test is null. Please create a test before adding a node.");
            return null;
        }
        ExtentTest node = parentTest.createNode(nodeName);
        Log4jManager.info("Created node: " + nodeName);
        return node;
    }

    /**
     * Retrieves the current thread's ExtentTest instance.
     *
     * @return the current ExtentTest.
     */
    public static ExtentTest getTest() {
        return extentTestThread.get();
    }

    /**
     * Adds custom system information to the report.
     *
     * @param key   the system info key.
     * @param value the system info value.
     */
    public static void addSystemInfo(String key, String value) {
        getInstance().setSystemInfo(key, value);
        Log4jManager.info("Added system info: " + key + " = " + value);
    }

    /**
     * Flushes the ExtentReports instance and clears the ThreadLocal test.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            Log4jManager.info("Flushed ExtentReports.");
            extent = null; // Reset to force reinitialization on next run.
            extentTestThread.remove(); // Clear ThreadLocal
        }
    }

    /**
     * Returns the file path of the generated report.
     *
     * @return the report file path.
     */
    public static String getReportFilePath() {
        return reportFile;
    }

    /**
     * Captures a screenshot and attaches it to the current test node.
     *
     * @param screenshotName the base name for the screenshot file.
     * @param status         the log status (e.g., PASS, FAIL, INFO).
     */
    public static void attachScreenshot(String screenshotName, Status status) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            Log4jManager.error("WebDriver instance is null. Cannot capture screenshot.");
            getTest().warning("WebDriver instance is null. Screenshot not captured.");
            return;
        }

        // Ensure the screenshots directory exists
        String screenshotsDir = System.getProperty("user.dir") + "/screenshots/";
        File dir = new File(screenshotsDir);
        if (!dir.exists() && !dir.mkdirs()) {
            Log4jManager.error("Failed to create screenshots directory at: " + screenshotsDir);
            getTest().warning("Failed to create screenshots directory. Screenshot not captured.");
            return;
        }

        // Append a timestamp to the screenshot name to ensure uniqueness
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String finalScreenshotName = screenshotName + "_" + timeStamp;
        String destPath = screenshotsDir + finalScreenshotName + ".png";

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(destPath));
            getTest().log(status, "Screenshot attached:",
                    MediaEntityBuilder.createScreenCaptureFromPath(destPath, finalScreenshotName).build());
            Log4jManager.info("Attached screenshot: " + destPath);
        } catch (IOException e) {
            Log4jManager.error("Failed to attach screenshot: " + destPath, e);
            getTest().fail("Failed to attach screenshot: " + destPath);
        }
    }

    /**
     * Overloaded method using INFO status by default.
     *
     * @param screenshotName the base name for the screenshot file.
     */
    public static void attachScreenshot(String screenshotName) {
        attachScreenshot(screenshotName, Status.INFO);
    }

    /**
     * Captures a Base64-encoded screenshot and attaches it to the current test node.
     *
     * @param screenshotName the name for the screenshot.
     * @param status         the log status.
     */
    public static void attachBase64Screenshot(String screenshotName, Status status) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            Log4jManager.error("WebDriver instance is null. Cannot capture screenshot.");
            getTest().warning("WebDriver instance is null. Base64 screenshot not captured.");
            return;
        }
        try {
            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            getTest().log(status, "Base64 Screenshot attached:",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, screenshotName).build());
            Log4jManager.info("Attached Base64 screenshot: " + screenshotName);
        } catch (Exception e) {
            Log4jManager.error("Error capturing Base64 screenshot for: " + screenshotName, e);
            getTest().fail("Error capturing Base64 screenshot for: " + screenshotName);
        }
    }

    /**
     * Logs a test step along with a screenshot.
     *
     * @param message        the log message.
     * @param screenshotName the base name for the screenshot file.
     * @param status         the log status.
     */
    public static void logStepWithScreenshot(String message, String screenshotName, Status status) {
        getTest().log(status, message);
        attachScreenshot(screenshotName, status);
    }

    /**
     * Logs an exception with an attached screenshot.
     *
     * @param e              the exception to log.
     * @param screenshotName the base name for the screenshot file.
     */
    public static void logExceptionWithScreenshot(Exception e, String screenshotName) {
        getTest().fail("Exception occurred: " + e.getMessage());
        attachScreenshot(screenshotName, Status.FAIL);
        Log4jManager.error("Exception occurred in test.", e);
    }
}
