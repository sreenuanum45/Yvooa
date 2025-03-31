package utilities;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;

public class EyesManager {
    private final Eyes eyes;
    private final WebDriver driver;

    public EyesManager(WebDriver driver, Eyes eyes) {
        this.driver = driver;
        this.eyes = eyes;
    }

    public void openEyes(String testName, RectangleSize viewportSize) {
        try {
            eyes.setForceFullPageScreenshot(true);
            if (viewportSize != null) {
                Eyes.setViewportSize(driver, viewportSize);
            }
            eyes.open(driver, "Your Application Name", testName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Consolidated checkWindow method
    public void checkWindow(String stepName) {
        try {
            eyes.check(stepName, Target.window());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Single checkElement implementation
    public void checkElement(WebElement element, String stepName) {
        try {
            eyes.check(stepName, Target.region(element));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Unified checkWindowWithIgnore implementation
    public void checkWindowWithIgnore(By locator, String checkpointName) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));

            eyes.check(
                    checkpointName,
                    Target.window()
                            .ignore(element)
                            .matchLevel(MatchLevel.STRICT) // or IGNORE_COLORS as needed
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeEyes() {
        try {
            eyes.close(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}