import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Test2 {
    public static void main(String[] args) throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        RemoteWebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        try {
            // 2) Go to the Dino game clone
            driver.get("https://chromedino.com");
            Thread.sleep(2000);

            // 3) Click on the page to start
            driver.executeScript("document.querySelector('body').click();");

            // 4) Replace the sprite sheet with your custom image
            //    Make sure your replacement image has the same dimensions or it may look distorted.
            String newSpriteUrl = "https://i.ytimg.com/vi/zb3OtI4XIsk/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLC8Po7MXc8P4M9bqullhpi5VcEOWA";
            String script =
                    "var sprite = document.querySelector('img[src*=\"offline-sprite\"]');" +
                            "if (sprite) { sprite.src='" + newSpriteUrl + "'; }";

            driver.executeScript(script);

            // (Optional) If you want to run the game indefinitely:
            // driver.executeScript("Runner.instance_.gameOver = function(){};");

            // Keep the browser open so you can see the result
            Thread.sleep(10000);

        } finally {
            // 5) Close the browser when done
            driver.quit();
        }
    }
}

