package Utility;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ActionsUtility {
    public Actions act;
    public FluentWait<RemoteWebDriver> wait;
    //Constructor
    public ActionsUtility(RemoteWebDriver driver, FluentWait<RemoteWebDriver> wait)
    {
        act=new Actions(driver);
        this.wait=wait;
    }
    //Operational methods
    public void clickOnElement(WebElement e)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.click(e).perform();
    }


    public void clickOnElementAtLocation(WebElement e, int xoffset, int yoffset)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.moveToElement(e).pause(Duration.ofSeconds(3)).moveByOffset(xoffset, yoffset)
                .pause(Duration.ofSeconds(3)).click().build().perform();
    }

    public void doubleClickOnElement(WebElement e)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.doubleClick(e).perform();
    }

    public void dragDrop(RemoteWebDriver driver, WebElement e1, WebElement e2)
    {
        wait.until(ExpectedConditions.visibilityOf(e1));
        wait.until(ExpectedConditions.visibilityOf(e2));
        driver.executeScript("arguments[0].scrollIntoView();",e1); //to avoid intercepted exception
        act.dragAndDrop(e1,e2).perform();
    }

    public void fillElement(WebElement e, String data)
    {
        wait.until(ExpectedConditions.visibilityOf(e));
        act.sendKeys(e,data).perform();
    }

    public void focusOnElementCenter(WebElement e)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.moveToElement(e).perform();
    }

    public void focusOnElementLocation(WebElement e, int xoffset, int yoffset)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.moveToElement(e,xoffset,yoffset).perform();
    }

    public String getDynamicToolTipText(WebElement e)
    {
        wait.until(ExpectedConditions.visibilityOf(e));
        act.moveToElement(e).pause(5000).clickAndHold(e).pause(5000).build().perform();
        WebElement tt=e.findElement(By.xpath("child::span"));
        String temp=tt.getText();
        act.release(e).perform();
        return(temp);
    }

    public String getStaticToolTipText(WebElement e)
    {
        //tool-tip is value of "title" attribute of an element
        wait.until(ExpectedConditions.visibilityOf(e));
        String temp=e.getAttribute("title");
        return(temp);
    }

    public void moveSlider(WebElement e, int xoffset, int yoffset)
    {
        wait.until(ExpectedConditions.visibilityOf(e));
        act.dragAndDropBy(e,xoffset,yoffset).pause(5000).build().perform();
    }

    public void rightClickOnElement(WebElement e)
    {
        wait.until(ExpectedConditions.elementToBeClickable(e));
        act.contextClick(e).perform();
    }
}
