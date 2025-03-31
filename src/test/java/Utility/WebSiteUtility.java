package Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.io.Files;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class WebSiteUtility {
    private static RemoteWebDriver driver;

    public String captureScreenshot(RemoteWebDriver driver) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
        Date dt = new Date();
        String fn = System.getProperty("user.dir") + "\\target\\" + sf.format(dt) + ".png";
        File dest = new File(fn); //create a new file in HDD
        File src = driver.getScreenshotAs(OutputType.FILE);
        Files.copy(src, dest);
        return (dest.getAbsolutePath());
    }

    public void closeSite(RemoteWebDriver driver) {
        driver.quit();
    }

    public FluentWait<RemoteWebDriver> defineWait(RemoteWebDriver driver) throws Exception {
        String temp1 = PropertiesFileUtility.getValueInPropertiesFile("maxwait");
        int value1 = Integer.parseInt(temp1);
        String temp2 = PropertiesFileUtility.getValueInPropertiesFile("interval");
        int value2 = Integer.parseInt(temp2);
        FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver);
        wait.withTimeout(Duration.ofSeconds(value1));
        wait.pollingEvery(Duration.ofMillis(value2));
        return (wait);
    }

    public String fullPageScreenshot(RemoteWebDriver driver) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
        Date dt = new Date();
        String fn = System.getProperty("user.dir") + "\\target\\" + sf.format(dt) + ".png";
        File dest = new File(fn); //create a new file in HDD
        AShot as = new AShot();
        ShootingStrategy shs = ShootingStrategies.viewportPasting(1000);
        Screenshot ss = as.shootingStrategy(shs).takeScreenshot(driver);
        ImageIO.write(ss.getImage(), "PNG", dest);
        return (dest.getAbsolutePath());
    }

    public void launchSite(RemoteWebDriver driver) throws Exception {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.get(PropertiesFileUtility.getValueInPropertiesFile("baseURL"));

    }

    public RemoteWebDriver openBrowser(String browsername) {
        RemoteWebDriver driver;
        if (browsername.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browsername.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browsername.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else {
            driver = new InternetExplorerDriver();
        }
        return (driver);
    }

    public static RemoteWebDriver getDriver() {
        if (driver == null) {
            String browser = PropertiesFileUtility.getProperty("browser");
            switch (browser.toLowerCase()) {
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "ie":
                    driver = new InternetExplorerDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid browser: " + browser);
            }
        }
        return driver;
    }
}
