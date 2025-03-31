package Utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebTableUtility {
	public List<WebElement> getCellChilds(WebElement wt, int rownum, int colnum, By loc)
	{
		List<WebElement> eles=wt.findElement(
				              By.xpath("child::tbody/tr["+rownum+"]/td["+colnum+"]"))
				                .findElements(loc);

		return(eles);
	}	
	
    public String getCellValue(RemoteWebDriver driver, WebElement wt, int rownum, int colnum)
	{
		WebElement cell=wt.findElement(By.xpath("child::tbody/tr["+rownum+"]/td["+colnum+"]"));

		String value=(String)driver.executeScript("return(arguments[0].textContent);",cell);

		return(value);

	}
	
	public int getColumnsCountInRow(WebElement wt, int rownum)
	{
		int value=wt.findElements(By.xpath("child::tbody/tr["+rownum+"]/td")).size();
		return(value);
	}	
	
	public List<WebElement> getHeaders(WebElement wt)
	{
		List<WebElement> headers=wt.findElements(By.xpath("child::thead/tr/th"));
		return(headers);
	}
	
	public List<WebElement> getRows(WebElement wt)
	{
		List<WebElement> rows=wt.findElements(By.xpath("child::tbody/tr"));

		return(rows);
	}
	
	public int getRowsCount(WebElement wt)
	{

		int value=wt.findElements(By.xpath("child::tbody/tr")).size();

		return(value);
	}
}
