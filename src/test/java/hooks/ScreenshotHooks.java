package hooks;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import config.DriverManager;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;



/**
 * Hook class to capture screenshots when a scenario fails.
 */
public class ScreenshotHooks {

    /**
     * This hook runs after every scenario. If the scenario has failed, it attempts to capture a
     * screenshot using the active WebDriver instance and attaches it to the scenario.
     *
     * @param scenario The current running scenario provided by Cucumber.
     */
    @AfterStep
    public void takeScreenshotOnFail(Scenario scenario) {
        // Check if the scenario failed
        if (scenario.isFailed()) {
            System.out.println("[INFO] Scenario Failed: " + scenario.getName());

            // Retrieve the WebDriver from DriverManager
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                try {
                    // Capture the screenshot as a byte array
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                    // Attach the screenshot to the scenario in Cucumber's report
                    scenario.attach(screenshot, "image/png", "Failed Step Screenshot");
                    System.out.println("[INFO] Screenshot captured and attached for scenario: " + scenario.getName());

                } catch (Exception e) {
                    // Log any exception that occurs during screenshot capture
                    System.err.println("[ERROR] Failed to capture screenshot: " + e.getMessage());
                }
            } else {
                // If the driver is null, we cannot capture a screenshot
                System.err.println("[WARNING] WebDriver is null. Cannot capture screenshot for scenario: "
                        + scenario.getName());
            }
        } else {
            System.out.println("[INFO] Scenario Passed: " + scenario.getName());
        }
    }
}
