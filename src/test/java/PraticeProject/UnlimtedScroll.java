package PraticeProject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class UnlimtedScroll {
    public static void main(String[] args) {
        WebDriver driver=new ChromeDriver();
        driver.get("https://www.youtube.com");
        // Customize these values
        int maxAttempts = 20; // Stop after 20 scroll attempts
        int scrollPauseSeconds = 2; // Wait 2 seconds between scrolls

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            driver.manage().window().maximize();
            // Randomize scroll distance (e.g., 80-100% of page height)
            js.executeScript("window.scrollBy(0, window.innerHeight * " + (0.8 + Math.random() * 0.2) + ");");
            unlimitedScroll(driver, maxAttempts, scrollPauseSeconds);
        } finally {
            //driver.quit();
        }
    }

    public static void unlimitedScroll(WebDriver driver, int maxAttempts, int scrollPauseSeconds) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = 0;
        int attempts = 0;

        while (attempts < maxAttempts) {
            // Scroll to the bottom
            js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");

            // Wait for new content to load
            try {
                Thread.sleep(scrollPauseSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get new page height
            long newHeight = (long) js.executeScript("return document.documentElement.scrollHeight");

            // Check if page height changed
            if (newHeight == lastHeight) {
                attempts++;
            } else {
                attempts = 0; // Reset counter if new content loaded
                lastHeight = newHeight;
            }
        }
        System.out.println("Stopped scrolling after " + maxAttempts + " attempts with no new content.");
    }
        


    }

