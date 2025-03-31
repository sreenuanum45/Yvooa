package Utility;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DataPicker {
	public String temp;

public void dateSelector(WebElement dp, @NotNull WebDriver driver, @NotNull String ExceptedDate) throws InterruptedException {
	Thread.sleep(2000);
	String[] date=ExceptedDate.split("/");
	System.out.println(date.length);
	int ExceptedYear=Integer.parseInt(date[2]);
	String Exceptedmonth=date[0];
	Exceptedmonth=MonthConverter.convertToMonthName(Exceptedmonth);
	int day=Integer.parseInt(date[1]);
	JavascriptExecutor js=(JavascriptExecutor)driver;
	js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", dp);
	dp.click();

	while(true) {
		temp=driver.findElement(By.xpath("(//div[starts-with(@class,'MuiPickersCalendarHeader')])[3]")).getText();
		System.out.println(temp);
		int currentyear=Integer.parseInt(temp.split(" ")[1]);
		if(ExceptedYear>currentyear) {
			driver.findElement(By.xpath("//div[text()='"+temp+"']")).click();
			WebElement year=driver.findElement(By.xpath("//button[text()='"+ExceptedYear+"']"));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", year);
			js.executeScript("arguments[0].click();", year);

		}
		else if(ExceptedYear<currentyear) {
			/*driver.findElement(By.xpath("")).click();
			Thread.sleep(1000);*/
		}
		else {
			break;
		}
	}
	while(true) {
		temp=driver.findElement(By.xpath("(//div[starts-with(@class,'MuiPickersCalendarHeader')])[3]")).getText();
		WebElement headerCalender=driver.findElement(By.xpath("//div[text()='"+temp+"']"));
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", headerCalender);
		String currentmonth=driver.findElement(By.xpath("//div[text()='"+temp+"']")).getText().split(" ")[0];
		if(currentmonth.equalsIgnoreCase("January")) {
			break;
		}
		else {
			driver.findElement(By.xpath("//button[@aria-label='Previous month']")).click();
		}
	}
	while(true) {
		String currentmonth=driver.findElement(By.xpath("(//div[starts-with(@class,'MuiPickersCalendarHeader')])[3]")).getText();
		currentmonth=currentmonth.split(" ")[0];
		if(currentmonth.equalsIgnoreCase(Exceptedmonth)) {
			break;
		}
		else{
			driver.findElement(By.xpath("//button[@aria-label='Next month']")).click();
		}
	}
	driver.findElement(By.xpath("//button[text()='"+day+"']")).click();
	
}
	
	
}
