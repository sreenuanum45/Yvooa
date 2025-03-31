package Utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DivTableUtility {
	public int getChildDIVCount(WebElement dt)
	{
		int value=dt.findElements(By.xpath("child::div")).size(); //rows count in <div> tag table
		return(value);
	}
	public List<WebElement> getChildDIVs(WebElement dt)
	{
		List<WebElement> rows=dt.findElements(By.xpath("child::div"));
		return(rows);
	}	
	public String getDIVValue(RemoteWebDriver driver, WebElement area, By eleloc)
	{
		WebElement e=area.findElement(eleloc);
		String value=(String) driver.executeScript("return(arguments[0].textContent);",e);
		return(value);
	}
}
