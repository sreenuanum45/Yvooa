package Utility;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RangeSliderUtility {



        public HashMap<String, String> getElementState(WebElement element) {
		    HashMap<String, String> elementState = new HashMap<String, String>();

		    if (element.isDisplayed()) {


		        elementState.put("height", String.valueOf(element.getSize().getHeight()));
		        elementState.put("width", String.valueOf(element.getSize().getWidth()));
		        elementState.put("x", String.valueOf(element.getLocation().getX()));
		        elementState.put("y", String.valueOf(element.getLocation().getY()));
		        elementState.put("class", element.getAttribute("class"));
		        elementState.put("id", element.getAttribute("id"));
		        elementState.put("border", element.getCssValue("border"));
		        elementState.put("font", element.getCssValue("font"));
		        elementState.put("padding", element.getCssValue("padding"));

		        if (element.getTagName().equals("a")) {
		            elementState.put("href", element.getAttribute("href"));
		        }

		        if (element.getTagName().equals("ul") || element.getTagName().equals("ol")) {
		            elementState.put("itemcount", element.findElements(By.tagName("li")).size()+"");
		        }

		        if (element.getTagName().equals("table")) {
		            List<WebElement> tableRows = element.findElements(By.tagName("tr"));
		            elementState.put("rowcount", (tableRows.size()+""));

		        }
		    } else {
		        elementState.put("displayed", "false");
		    }

		    return elementState;

		}
    public void setRangeSliderValue(WebElement rangeSlider, int value,RemoteWebDriver driver) {
	    JavascriptExecutor js =  driver;
	    js.executeScript("arguments[0].value = arguments[1]", rangeSlider, String.valueOf(value));
	    js.executeScript("arguments[0].dispatchEvent(new Event('change'))", rangeSlider);
	}
    }


