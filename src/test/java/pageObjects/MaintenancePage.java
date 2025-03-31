package pageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utility.DataPicker;
import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class  MaintenancePage{
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    private WebDriver driver;
    private DataPicker date;

    public MaintenancePage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(),this);
    }
    @FindBy(id="addNew")
    private WebElement addNew;
    @FindBy(id="portfolioIdSelect")
    private WebElement portfolioIdSelect;
    @FindBy(id="propertySelect")
    private WebElement propertySelect;
    @FindBy(xpath="//button[text()='Confirm']")
    private WebElement confirmButton;
    @FindBy(id="requestTypeSelect")
    private WebElement requestTypeSelect;
    @FindBy(id="problemDescriptionInput")
    private WebElement problemDescriptionInput;
    @FindBy(xpath="//button[text()='Next']")
    private WebElement nextButton;
    @FindBy(id="categorySelect")
    private WebElement categorySelect;
    @FindBy(id="SubCategorySelect")
    private WebElement SubCategorySelect;
    @FindBy(id="locationInput")
    private WebElement locationInput;
    @FindBy(id="prioritySelect")
    private WebElement prioritySelect;
    @FindBy(id="assigntoSelect")
    private WebElement assignToSelect;
    @FindBy(xpath="//input[@type='file']")
    private WebElement mediaSelectFileButton;
    @FindBy(id="mediaNotesInput")
    private WebElement mediaNotesInput;
    @FindBy(id="vendorSelect")
    private WebElement vendorSelect;
    @FindBy(id="availability1DatePicker")
    private WebElement availability1DatePicker;
    @FindBy(id="availability2DatePicker")
    private WebElement availability2DatePicker;
    @FindBy(id="availability1FromTimeInput")
    private WebElement availability1FromTimeInput;
    @FindBy(id="availability1ToTimeInput")
    private WebElement availability1ToTimeInput;
    @FindBy(id="availability2FromTimeInput")
    private WebElement availability2FromTimeInput;
    @FindBy(id="availability2ToTimeInput")
    private WebElement availability2ToTimeInput;
    @FindBy(id="createRequest")
    private WebElement createRequest;
    @FindBy(xpath="(//span[@role='progressbar']//parent::div)[1]")
    private WebElement Loader;
    @FindBy(id="sucessMessage")
    private WebElement sucessMessage;
    @FindBy(id="close")
    private WebElement close;
    @FindBy(xpath="//div[starts-with(@class,'MuiClock-pin')]")
    private List<WebElement> ClockPin;
    @FindBy(xpath="//span[@role='option']")
    private List<WebElement> HoursList;
    @FindBy(xpath="(//div[starts-with(@class,'MuiDialogActions')]//button)[2]")
    private WebElement OK_Button;
    @FindBy(xpath="(//button[@aria-label='Choose date'])[1]")
    private WebElement Date1;
    @FindBy(xpath="(//button[@aria-label='Choose date'])[2]")
    private WebElement Date2;
    @FindBy(xpath="(//div[starts-with(@class,'MuiPickersCalendarHeader')])[3]")
    private WebElement CalendarHeader;
    public void waitforLoader()
    {
        waitUtility.waitForElementToBeInvisible(Loader);
    }

    public void clickAddNew()
    {
        waitUtility.waitForElementToBeClickable(addNew);
        commonActions.clickElement(addNew);
    }
    public void selectPortfolio(String portfolioname)
    {
        waitUtility.waitForElementToBeVisible(portfolioIdSelect);
        commonActions.selectByVisibleText(portfolioIdSelect,portfolioname);
    }
    public WebElement propertySelect(){
        waitUtility.waitForElementToBeVisible(propertySelect);
        return propertySelect;
    }
    public void selectProperty()
    {
        waitUtility.waitForElementToBeVisible(propertySelect());
        commonActions.selectRandomFromDropdown(propertySelect());
    }
    public void clickConfirmButton()
    {
        waitUtility.waitForElementToBeClickable(confirmButton);
        commonActions.clickElement(confirmButton);
    }
    public void selectRequestType(String RequestType)
    {
        waitUtility.waitForElementToBeVisible(requestTypeSelect);
        commonActions.selectByVisibleText(requestTypeSelect,RequestType);
    }
    public void enterProblemDescription(String problemDescription)
    {
        waitUtility.waitForElementToBeVisible(problemDescriptionInput);
        commonActions.typeText(problemDescriptionInput,problemDescription);
    }
    public void clickNextButton()
    {
        waitUtility.waitForElementToBeClickable(nextButton);
        commonActions.clickElement(nextButton);
    }
    public void selectCategory()
    {
        waitUtility.waitForElementToBeVisible(categorySelect);
        commonActions.selectRandomFromDropdown(categorySelect);
    }
    public void selectSubCategory()
    {
        waitUtility.waitForElementToBeVisible(SubCategorySelect());
        commonActions.scrollToElement(SubCategorySelect());
        commonActions.waitForPageLoad();
        //commonActions.selectRandomFromDropdown(SubCategorySelect);

        commonActions.selectByIndex(SubCategorySelect,2);


    }
    public WebElement SubCategorySelect(){
        waitUtility.waitForElementToBeVisible(SubCategorySelect);
        return SubCategorySelect;
    }
    public void enterLocation(String location)
    {
        waitUtility.waitForElementToBeVisible(locationInput);
        commonActions.typeText(locationInput,location);
    }
    public void selectPriority()
    {
        waitUtility.waitForElementToBeVisible(prioritySelect);
        commonActions.selectRandomFromDropdown(prioritySelect);
    }
    public void selectAssignTo()
    {
        waitUtility.waitForElementToBeVisible(assignToSelect);
        commonActions.selectRandomFromDropdown(assignToSelect);
    }
    public void clickMediaSelectFileButton(String mediaSelectFile)
    {
      waitUtility.waitForElementToBeVisible(mediaSelectFileButton);
      commonActions.uploadFile(mediaSelectFileButton,mediaSelectFile);
    }
    public void enterMediaNotes(String mediaNotes)
    {
        waitUtility.waitForElementToBeVisible(mediaNotesInput);
        commonActions.typeText(mediaNotesInput,mediaNotes);
    }
    public void selectVendor()
    {
        waitUtility.waitForElementToBeVisible(vendorSelect);
        commonActions.selectRandomFromDropdown(vendorSelect);
    }
    public void Available_Time_Slot_1 () throws InterruptedException {
        date =new DataPicker();
        waitUtility.waitForElementToBeVisible(Date1);
        driver=DriverManager.getDriver();
        date.dateSelector(Date1,driver,"05/25/2029");
    }
    public void Available_Time_Slot_2 () throws InterruptedException {
        date =new DataPicker();
        waitUtility.waitForElementToBeVisible(Date2);
        driver=DriverManager.getDriver();
        date.dateSelector(Date2,driver,"05/25/2029");
    }

    public List<WebElement> HoursLists()
    {
        waitUtility.waitForElementToBeVisible(HoursList);
        return HoursList;
    }
    public WebElement OKButton()
    {
        waitUtility.waitForElementToBeClickable(OK_Button);
        return OK_Button;
    }

    public void FromTime2()
    {
        waitUtility.waitForElementToBeVisible(availability2FromTimeInput);
        commonActions.clickElement(availability2FromTimeInput);
        driver=DriverManager.getDriver();
        Actions actions = new Actions(driver);
        //for hours
        actions.moveToElement(HoursLists().get(1)).build().perform();
        //for minutes
        actions.moveToElement(HoursLists().get(1)).build().perform();
        waitUtility.waitForElementToBeVisible(OKButton());
        commonActions.clickElement(OKButton());
    }
    public void ToTime2()
    {
        waitUtility.waitForElementToBeVisible(availability2ToTimeInput);
        commonActions.clickElement(availability2ToTimeInput);
        Actions actions = new Actions(driver);
        //for hours
        actions.moveToElement(HoursLists().get(3)).build().perform();
        //for minutes
        actions.moveToElement(HoursLists().get(3)).build().perform();
        waitUtility.waitForElementToBeVisible(OKButton());
        commonActions.clickElement(OKButton());
    }
    public void FromTime1()
    {

        waitUtility.waitForElementToBeVisible(availability1FromTimeInput);
        commonActions.clickElement(availability1FromTimeInput);
        /*selectTime("1:00");*/
        // In your test class
       selectTime("1:00");


    }
    public void ToTime1()
    {
        waitUtility.waitForElementToBeVisible(availability1ToTimeInput);
        commonActions.clickElement(availability1ToTimeInput);
        selectTime("4:00");
    }
    public void clickCreateRequest()
    {
        waitUtility.waitForElementToBeClickable(createRequest);
        commonActions.clickElement(createRequest);
    }
    public WebElement DisplayedSucessMessage()
    {
        waitUtility.waitForElementToBeVisible(sucessMessage);
        return sucessMessage;
    }
    public void clickClose()
    {
        waitUtility.waitForElementToBeClickable(close);
        commonActions.clickElement(close);
    }

// Reusable time selection method
    public void selectTime(String time) {
        String[] parts = time.split(":");
        selectHour(parts[0]);
        selectMinute(parts[1]);
        confirmSelection();
    }

    private void selectHour(String hour) {
        interactWithTimeElement(hour, "hours");
    }

    private void selectMinute(String minute) {
        interactWithTimeElement(minute, "minutes");
    }

    private void interactWithTimeElement(String value, String labelType) {
        By locator = By.xpath(String.format(
                "//span[text()='%s' and contains(@aria-label, '%s')]",
                value, labelType
        ));

        int attempts = 0;
        while (attempts < 3) {
            try {
                waitForMaskDisappearance();
                WebElement element = waitForClickableElement(locator);
                scrollToCenter(element);
                clickWithActions(element);
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                if(attempts == 3) {
                    forceJSclick(locator);
                    return;
                }
              WaitUtility.wait(500);
            }
        }
    }

    private void waitForMaskDisappearance() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.invisibilityOfElementLocated(
                            By.cssSelector("div.MuiClock-squareMask")
                    ));
        } catch (TimeoutException ignored) {}
    }

    private WebElement waitForClickableElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(15))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center', inline: 'center'});" +
                        "window.scrollBy(0, -window.innerHeight * 0.1);",
                element
        );
        WaitUtility.wait(300);
    }

    private void clickWithActions(WebElement element) {
        new Actions(driver)
                .moveToElement(element)
                .pause(Duration.ofMillis(200))
                .click()
                .perform();
    }

    private void forceJSclick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', {" +
                        "bubbles: true, cancelable: true, view: window" +
                        "}));",
                element
        );
    }

    private void confirmSelection() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                clickWithActions(OKButton());
                return;
            } catch (ElementClickInterceptedException e) {
                attempts++;
                forceJSclick(By.xpath("//button[contains(text(),'OK')]"));
            }
        }
    }

    }









