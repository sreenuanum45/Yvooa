import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Test1 {
    public static void main(String[] args) {


        // 2) Initialize Chrome options
        ChromeOptions options = new ChromeOptions();
        // (Optional) disable automation info bars
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        // 3) Create a new RemoteWebDriver instance
        RemoteWebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        try {
            // 4) Navigate to an online clone of the Chrome Dino game
            driver.get("https://chromedino.com");
            Thread.sleep(2000);

            // 5) Start the game by simulating a click on the page
            driver.executeScript("document.querySelector('body').click();");

            // 6) Replace the sprite sheet with your custom image
            //    Make sure your replacement image is the same size or it may look distorted.
            String newSpriteUrl = "https://yt3.ggpht.com/yti/ANjgQV_iRLnoSVAxan8bUgbjuHbMX3inVCAeMbLf3xzdNTH356PH=s88-c-k-c0x00ffffff-no-rj";
            String script =
                    "var sprite = document.querySelector('img[src*=\"offline-sprite\"]');" +
                            "if (sprite) { sprite.src='" + newSpriteUrl + "'; }";
            driver.executeScript(script);

            // 7) (Optional) Disable the Game Over so you can run infinitely
            driver.executeScript("Runner.instance_.gameOver = function(){};");

            // 8) Create an Actions instance for simulating key presses
            Actions actions = new Actions(driver);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 9) Continuously check for obstacles and jump if they're too close
            while (true) {
                // Get the x-position of the first obstacle, if any
                Object obstacleObj = js.executeScript(
                        "return Runner.instance_.horizon.obstacles.length > 0 "
                                + "? Runner.instance_.horizon.obstacles[0].xPos : null;"
                );

                if (obstacleObj != null) {
                    Number obstacleDistance = (Number) obstacleObj;
                    // If the obstacle is within 100px, jump
                    if (obstacleDistance.doubleValue() < 100) {
                        actions.sendKeys(Keys.SPACE).perform();
                        System.out.println("Jump! obstacle at xPos = " + obstacleDistance);
                    }
                }

                // Short sleep so we don't hammer the CPU (tweak as needed)
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        } finally {
            // (Optional) Close the browser when done
            // driver.quit();
        }
    }
}
