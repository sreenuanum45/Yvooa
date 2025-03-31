package Utility;


import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserLaunch {
    static RemoteWebDriver driver ;
    public static void launch (String browserChoice) throws InterruptedException {
        driver =null;
        if (browserChoice.equalsIgnoreCase("chrome")) {


            driver = new ChromeDriver();
        } else if (browserChoice.equalsIgnoreCase("firefox")) {
            // Launch Firefox
            System.setProperty("webdriver.gecko.driver", "path/to/geckodriver.exe");
            driver = new FirefoxDriver();
        } else if (browserChoice.equalsIgnoreCase("edge")) {
            // Launch Edge
            driver = new EdgeDriver();
        } else {
            System.out.println("Invalid browser choice. Please choose 'chrome', 'firefox', or 'edge'.");
            return;
        }
        Properties p=new Properties();

        driver.get(p.getProperty("url"));
        Thread.sleep(Long.parseLong(p.getProperty("waittime")));


    }
}
