package Utility;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CSSPropertiesList{
    public  String cssPropertiesOfEachElement(WebElement e, RemoteWebDriver driver){

        return (String) driver.executeScript(
                "var s = ''; " +
                        "var o = window.getComputedStyle(arguments[0]); " +
                        "for (var i = 0; i < o.length; i++) { " +
                        "  s = s + o[i] + ':' + o.getPropertyValue(o[i]) + ','; " +
                        "} " +
                        "return s;", e);
    }
}
