package Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class CaptchaUtility {
    public static void bypassInTestEnvironment(WebDriver driver, By captchaLocator) {
        if (isTestEnv()) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = 'FORCE_SUCCESS';",
                    driver.findElement(captchaLocator)
            );
        }
    }

    private static boolean isTestEnv() {
        return System.getProperty("env").equals("test");
    }
}
