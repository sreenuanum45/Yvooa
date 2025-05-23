package Utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VideoUtility {
    public static void playVideo(WebDriver driver, WebElement videoElement) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].play();", videoElement
        );
    }

    public static void pauseVideo(WebDriver driver, WebElement videoElement) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].pause();", videoElement
        );
    }
}
