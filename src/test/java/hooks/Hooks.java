package hooks;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import atu.testrecorder.ATUTestRecorder;
import config.DriverManager;
import config.PicoContainerConfig;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import reporting.Log4jManager;

/**
 * Cucumber hooks that include video recording, step-level logging,
 * screenshot capture, and browser log attachment.
 */
public class Hooks {

    public static ATUTestRecorder recorder;
    public static Instant stepStartTime;

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) throws Exception {
        Log4jManager.info("=== Starting Scenario: " + scenario.getName() + " ===");
        /* Uncomment if video recording is desired.
        try {
            String videoName = scenario.getName().replaceAll(" ", "") + "_" + System.currentTimeMillis();
            recorder = new ATUTestRecorder("videos/", videoName, false);
            recorder.start();
        } catch (Exception e) {
            ExtentCucumberAdapter.getCurrentScenario().info("Video recording failed to start: " + e.getMessage());
        }
        */
       // Reset index before each scenario outline
        ExtentCucumberAdapter.getCurrentScenario().createNode(scenario.getName());
        ExtentCucumberAdapter.addTestStepLog("Starting Scenario: " + scenario.getName());
        ExtentCucumberAdapter.getCurrentScenario().assignCategory(scenario.getSourceTagNames().toString());
        PicoContainerConfig.getContainer();

        // Initialize the WebDriver instance before the scenario starts.
        DriverManager.initDriver();
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        Log4jManager.info("=== Starting Step ===");
        stepStartTime = Instant.now();
        ExtentCucumberAdapter.getCurrentStep().createNode(scenario.getName());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        Log4jManager.info("=== Ending Step ===");

        Duration stepDuration = Duration.between(stepStartTime, Instant.now());
        String duration = stepDuration.toMillis() + " ms";
        if (stepDuration.toSeconds() > 0) {
            duration = stepDuration.toSeconds() + " sec";
        }
        if (scenario.isFailed()) {
            scenario.attach(duration.getBytes(), "text/plain", "Step Execution Time");
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            ExtentCucumberAdapter.getCurrentStep().fail("Step Failed: " + scenario.getName() + " (" + duration + ")");
        } else {
            ExtentCucumberAdapter.getCurrentStep().pass("Step Passed: " + scenario.getName() + " (" + duration + ")");
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Optional: Stop video recording if enabled.
        /*
        try {
            if (recorder != null) {
                recorder.stop();
                // Optionally, attach the video to the Cucumber report.
            }
        } catch (Exception e) {
            ExtentCucumberAdapter.getCurrentScenario().info("Error stopping video recording: " + e.getMessage());
        }
        */

        // Capture browser console logs if the WebDriver is initialized.
        if (DriverManager.isDriverInitialized()) {
            try {
                LogEntries logEntries = DriverManager.getDriver()
                        .manage()
                        .logs()
                        .get(LogType.BROWSER);
                StringBuilder logBuilder = new StringBuilder();
                for (LogEntry entry : logEntries) {
                    logBuilder.append(new Date(entry.getTimestamp()))
                            .append(" ")
                            .append(entry.getLevel())
                            .append(" ")
                            .append(entry.getMessage())
                            .append(System.lineSeparator());
                }
                if (logBuilder.length() > 0) {
                    // Attach the logs to the Cucumber report with UTF-8 encoding.
                    scenario.attach(logBuilder.toString().getBytes(StandardCharsets.UTF_8),
                            "text/plain",
                            "Browser Console Logs");
                }
            } catch (Exception e) {
                String errorMessage = "Unable to capture browser logs: " + e.getMessage();
                ExtentCucumberAdapter.getCurrentScenario().info(errorMessage);
                scenario.log(errorMessage);
            }
        }

        // Report the scenario outcome in the Extent report.
        if (scenario.isFailed()) {
            ExtentCucumberAdapter.getCurrentScenario().fail("Scenario Failed: " + scenario.getName());
        } else {
            ExtentCucumberAdapter.getCurrentScenario().pass("Scenario Passed: " + scenario.getName());
        }

        // Quit the WebDriver session.
       if (DriverManager.isDriverInitialized()){
          //  DriverManager.quitDriver();
        }
        // Reset the WebDriver instance.


        // Log the end of the scenario.
        Log4jManager.info("=== Ending Scenario: " + scenario.getName() + " ===");
    }
}
