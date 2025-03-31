package PraticeProject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DemoTest1 {
    private WebDriver driver;
    @BeforeClass
    @Parameters({"Browser"})
    public void setUp( @Optional("Chrome")String Browser){
       driver= SigletonPratice.getInstance(Browser).getDriver();
    }
    @Test
    public void testSingletonPattern(){
        System.out.println("Test to demonstrate Singleton pattern");
        driver.get("https://www.google.com");
driver.findElement(By.name("q")).sendKeys("addual kalam");
        System.out.println(Thread.currentThread().getName()+" "+driver.getTitle());
        Assert.assertEquals(driver.getTitle(),"Google");
    }
    @AfterClass
    public void tearDown(){
      SigletonPratice.quitDriver();
    }


}
