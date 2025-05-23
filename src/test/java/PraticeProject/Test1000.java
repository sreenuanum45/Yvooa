package PraticeProject;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Test1000 {
    public static void main(String[] args) {
       ChromeDriver driver=new ChromeDriver()    ;
        //driver.get("https://www.google.com");
        driver.navigate().to("https://www.google.com");
        driver.findElement(By.name("q")).sendKeys("Selenium");
        driver.findElement(By.name("btnK")).click();
        driver.findElement(By.xpath("//h3[text()='Selenium']")).click();
        String title=driver.getTitle();
        System.out.println("Title of the page is "+title);
      String currentUrl=driver.getCurrentUrl();
        //driver.close();
    }
}
