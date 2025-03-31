package hooks;

import org.openqa.selenium.WebDriver;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;

import config.ConfigReader;
import config.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class ApplitoolsHooks {

    private static Eyes eyes;
    private static BatchInfo batch = new BatchInfo("My Test Batch");

    @Before("@visualAI")
    public void setUpApplitools(Scenario scenario) {
        // Check if driver is initialized before accessing it
        if (!DriverManager.isDriverInitialized()) {
            DriverManager.initDriver();
        }
        WebDriver driver = DriverManager.getDriver();

        eyes = new Eyes();
        String apiKey = ConfigReader.getInstance().getProperty("applitools.api.key");
        eyes.setApiKey(apiKey);

        eyes.setBatch(batch);

        eyes.open(driver,
                "My Application Name",
                scenario.getName(),
                new RectangleSize(1200, 800));

        System.out.println("[INFO] Applitools Eyes initialized for scenario: " + scenario.getName());
    }

    @After("@visualAI")
    public void tearDownApplitools(Scenario scenario) {
        if (eyes != null) {
            try {
                if (scenario.isFailed()) {
                    eyes.abortIfNotClosed();
                    System.out.println("[INFO] Applitools Eyes test aborted due to scenario failure.");
                } else {
                    eyes.closeAsync();
                    System.out.println("[INFO] Applitools Eyes test closed successfully.");
                }
            } catch (Exception e) {
                eyes.abortIfNotClosed();
                System.err.println("[ERROR] Error closing Applitools Eyes: " + e.getMessage());
            }
        }
    }

    public static Eyes getEyes() {
        return eyes;
    }
}