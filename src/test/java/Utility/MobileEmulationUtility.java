package Utility;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class MobileEmulationUtility {
    public static ChromeDriver launchMobileView(ChromeDriver driver, String deviceName) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName); // e.g., "iPhone 12 Pro"

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        return new ChromeDriver(options);
    }
}
