package PraticeProject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SigletonPratice {
    public static volatile SigletonPratice instance ;
    private  static final ThreadLocal<WebDriver> threadLocal = new ThreadLocal<>();

    private SigletonPratice() {
        System.out.println("Initializing SingletonDriver instance.");
        System.out.println("WebDriver initialized successfully.");
    }
    private void initDriver(String browser){
        switch (browser){
            case "chrome":
                System.out.println("Chrome driver initialized");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                options.addArguments("--start-maximized");
                options.setAcceptInsecureCerts(true);
                threadLocal.set(new ChromeDriver(options));
                break;
            case "firefox":
                System.out.println("Firefox driver initialized");
                ChromeOptions options1 = new ChromeOptions();
                options1.addArguments("--disable-notifications");
                options1.addArguments("--start-maximized");
                options1.setAcceptInsecureCerts(true);
                threadLocal.set(new org.openqa.selenium.firefox.FirefoxDriver());
                break;
            case "safari":

                threadLocal.set(new org.openqa.selenium.safari.SafariDriver());
                break;
                case "edge":
                    threadLocal.set(new org.openqa.selenium.edge.EdgeDriver());
                    break;
            default:
                System.out.println("Invalid browser");
        }

    }
    public static SigletonPratice getInstance(String browser){
        if (instance == null){
            synchronized (SigletonPratice.class){
                if (instance == null){
                    instance = new SigletonPratice();
                    instance.initDriver(browser);
                }
            }
        }
        if(threadLocal.get() == null){
            instance.initDriver(browser);
        }
        return instance;
    }
    public WebDriver getDriver(){
        return threadLocal.get();
    }
    public static void quitDriver(){
        if (threadLocal.get() != null){
            threadLocal.get().quit();
            threadLocal.remove();
        }
    }
}
