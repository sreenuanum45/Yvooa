package Utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class StorageUtility {
    public static void setLocalStorageItem(WebDriver driver, String key, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.setItem(arguments[0], arguments[1]);", key, value
        );
    }
}
