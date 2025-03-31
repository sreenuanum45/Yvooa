package PraticeProject;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.rmi.Remote;

public class Test111 {
    //launch the google website
    //search for the keyword "Selenium"
    //click on the first link
    //verify the title of the page
    RemoteWebDriver driver=new ChromeDriver();
    public void launchGoogle(){
        driver.get("https://www.google.com");
        driver.findElement(By.name("q")).sendKeys("Selenium");
        driver.findElement(By.name("btnK")).click();
        driver.findElement(By.xpath("//h3[text()='Selenium']")).click();
        String title=driver.getTitle();
        System.out.println("Title of the page is "+title);
        //driver.close();

    }

}
