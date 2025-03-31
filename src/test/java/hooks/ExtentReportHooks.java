package hooks;

import java.util.Base64;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import config.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import reporting.ExtentManager;

/**
 * Cucumber hooks for ExtentReports.
 * Each scenario tagged with @extent will have one test node created.
 */
public class ExtentReportHooks {

    private static final ThreadLocal<ExtentTest> scenarioTest = new ThreadLocal<>();

    @BeforeAll
    public static void setUpAll() {
        ExtentManager.getInstance();
    }

    @AfterAll
    public static void tearDownAll() {
        ExtentManager.flushReports();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        ExtentTest test = ExtentManager.getInstance().createTest(scenario.getName());
        scenarioTest.set(test);
    }
    /*@BeforeStep
    public void beforeStep(Scenario scenario) {
        ExtentTest test = scenarioTest.get();
        test.log(Status.INFO, "Starting step: " + scenario.getName());
    }
    @AfterStep
    public void afterStep(Scenario scenario) {
        ExtentTest test = scenarioTest.get();
        test.log(Status.INFO, "Ending step: " + scenario.getName());
    }*/

    @After
    public void afterScenario(Scenario scenario) {
        ExtentTest test = scenarioTest.get();
        try {
            if (scenario.isFailed()) {
                test.log(Status.FAIL, "Scenario Failed: " + scenario.getName());
                attachScreenshot(scenario, test);
            } else {
                test.log(Status.PASS, "Scenario Passed: " + scenario.getName());
            }
        } catch (Exception e) {
            test.log(Status.FAIL, "Error in afterScenario: " + e.getMessage());
        }
        // Do not flush here – flush once at the very end (@AfterAll)
    }

    /**
     * Captures a screenshot and attaches it both to the Cucumber scenario and the Extent test.
     */
    private void attachScreenshot(Scenario scenario, ExtentTest test) {
        WebDriver driver = getDriverSafely();
        if (driver == null) {
            return;
        }
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        // Attach to Cucumber report
        scenario.attach(screenshot, "image/png", "Failed Screenshot");
        // Attach to Extent report using Base64
        String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
        test.addScreenCaptureFromBase64String(base64Screenshot, "Failed Screenshot");
    }

    private WebDriver getDriverSafely() {
        try {
            return DriverManager.getDriver();
        } catch (Exception e) {
            return null;
        }
    }
}
