package pageObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class ImportCSVPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    WebDriver driver = DriverManager.getDriver();
    JavascriptExecutor js = (JavascriptExecutor) driver;
    @FindBy(id = "categorySelect")
    private WebElement categorySelectOption;
    @FindBy(id = "uploadFileInput")
    private WebElement uploadFileInput;
    @FindBy(xpath = "//button[@id='uploadFileSelectFileButton']")
    private WebElement chooseFile;
    @FindBy(id = "uploadFinalFile")
    private WebElement uploadFinalFile;
    @FindBy(id = "portfolioSelect")
    private WebElement portfolioSelect;
    @FindBy(id = "propertySelect")
    private WebElement propertySelect;
    @FindBy(xpath = "//*[local-name()='circle' and contains(@class,'MuiCircularProgress')]")
    private WebElement loader;

    public ImportCSVPage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public void selectCategoryAndFileUpload(String category, String PortfolioName, String PropertyName, String file) throws AWTException, InterruptedException {
        waitUtility.waitForElementToBeVisible(categorySelectOption);
        String propery = PropertyName;
        if (category.equalsIgnoreCase("Portfolio")) {
            waitUtility.waitForElementToBeInvisible(loader());
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);
            waitUtility.waitForElementToBeInvisible(loader());

        } else if (category.equalsIgnoreCase("Property Single Family")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);
            js.executeScript("arguments[0].scrollIntoView();", portfolioSelect());
            js.executeScript("arguments[0].click();", portfolioSelect());

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                Thread.sleep(7000);
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }

        } else if (category.equalsIgnoreCase("Property Multi Family")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);
            System.out.println("PortfolioName" + PortfolioName);

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                Thread.sleep(3000);
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }
            commonActions.waitForElementToBeInvisible(loader());

        } else if (category.equalsIgnoreCase("Unit")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                portfolioSelect().click();
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                js.executeScript("arguments[0].scrollIntoView();", portfolioSelect());
                js.executeScript("arguments[0].click();", portfolioSelect());
                Thread.sleep(10000);
                waitUtility.waitForElementToBeInvisible(loader());
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }
            commonActions.waitForElementToBeInvisible(loader());
            commonActions.clickElement(propertySelect());
            waitUtility.waitForElementToBeInvisible(loader());
            if (PropertyName.equalsIgnoreCase("{random}") | PropertyName.equalsIgnoreCase("NA") | PropertyName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(propertySelect());
            } else {

                Thread.sleep(2000);
                commonActions.selectByVisibleText(propertySelect(), PropertyName);
            }


        } else if (category.equalsIgnoreCase("Single Family Tenant")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                js.executeScript("arguments[0].scrollIntoView();", portfolioSelect());
                js.executeScript("arguments[0].click();", portfolioSelect());
                Thread.sleep(10000);
                waitUtility.waitForElementToBeInvisible(loader());
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }

        } else if (category.equalsIgnoreCase("Multi-Family Tenant")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                js.executeScript("arguments[0].scrollIntoView();", portfolioSelect());
                js.executeScript("arguments[0].click();", portfolioSelect());
                Thread.sleep(10000);
                waitUtility.waitForElementToBeInvisible(loader());
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }
            commonActions.waitForElementToBeInvisible(loader());
            commonActions.clickElement(propertySelect());
            waitUtility.waitForElementToBeInvisible(loader());
            if (PropertyName.equalsIgnoreCase("{random}") | PropertyName.equalsIgnoreCase("NA") | PropertyName.equalsIgnoreCase("")) {

                commonActions.selectRandomFromDropdown(propertySelect());
            } else {

                Thread.sleep(2000);
                commonActions.selectByVisibleText(propertySelect(), PropertyName);
            }

        } else if (category.equalsIgnoreCase("Utility Charges")) {
            js.executeScript("arguments[0].scrollIntoView();", categorySelectOption);
            js.executeScript("arguments[0].click();", categorySelectOption);
            commonActions.selectByVisibleText(categorySelectOption, category);

            if (PortfolioName.equalsIgnoreCase("{random}") | PortfolioName.equalsIgnoreCase("NA") | PortfolioName.equalsIgnoreCase("")) {
                commonActions.selectRandomFromDropdown(portfolioSelect());
            } else {
                js.executeScript("arguments[0].scrollIntoView();", portfolioSelect());
                js.executeScript("arguments[0].click();", portfolioSelect());
                Thread.sleep(10000);
                waitUtility.waitForElementToBeInvisible(loader());
                commonActions.selectByVisibleText(portfolioSelect(), PortfolioName);
            }
            commonActions.waitForElementToBeInvisible(loader());
            commonActions.clickElement(propertySelect());
            waitUtility.waitForElementToBeInvisible(loader());
            if (PropertyName.equalsIgnoreCase("{random}") | PropertyName.equalsIgnoreCase("NA") | PropertyName.equalsIgnoreCase("")) {

                commonActions.selectRandomFromDropdown(propertySelect());
            } else {

                Thread.sleep(2000);
                commonActions.selectByVisibleText(propertySelect(), PropertyName);
            }
        } else if (category.equalsIgnoreCase("Rental Transactions")) {

        }
        waitUtility.waitForElementToBeInvisible(loader());
        if (chooseFile.isDisplayed()) {
            if (chooseFile.isEnabled()) {
                chooseFile.click();
            }
        }
        StringSelection filePath = new StringSelection(file);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);
        Robot robot = new Robot();
        // Paste the file path and press Enter
        Thread.sleep(1000);
        robot.delay(30000); // Wait for the file upload dialog to appear
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void clickUploadFinalFile() {
        waitUtility.waitForElementToBeVisible(uploadFinalFile);
        js.executeScript("arguments[0].scrollIntoView();", uploadFinalFile);
        js.executeScript("arguments[0].click();", uploadFinalFile);
    }

    public WebElement portfolioSelect() {
        return portfolioSelect;
    }

    public WebElement propertySelect() {
        return propertySelect;
    }

    public WebElement loader() {
        return loader;
    }
}
