package pageObjects;

import java.util.List;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class VendorPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    @FindBy(xpath = "//button[@id='addNewVendor']")
    private WebElement addNewUserButton;
    @FindBy(xpath = "//span[text()='Vendors Directory']")
    private WebElement vendorDirectoryText;
    @FindBy(id = "companyNameInput")
    private WebElement companyNameInput;
    @FindBy(id = "contactNoInput")
    private WebElement contactNoInput;
    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;
    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;
    @FindBy(id = "emailIdInput")
    private WebElement emailIdInput;
    @FindBy(xpath = "(//select[@id='vendorTypeSelect'])[2]")
    private WebElement vendorTypeSelect;
    @FindBy(id = "address1")
    private WebElement address1;
    @FindBy(xpath = "//div[contains(@id, 'react-select')]")
    private List<WebElement> Address_option;
    @FindBy(name = "address2")
    private WebElement address2;
    @FindBy(name = "city")
    private WebElement city;
    @FindBy(name = "state")
    private WebElement state;
    @FindBy(name = "country")
    private WebElement country;
    @FindBy(name = "zipCode")
    private WebElement zipCode;
    @FindBy(id = "areaCoveredSelect")
    private WebElement areaCoveredSelect;
    @FindBy(id = "next")
    private WebElement nextButton;
    @FindBy(xpath = "//input[@type='checkbox']")
    private List<WebElement> areaCoveredCheckBox;
    @FindBy(id = "confirm")
    private WebElement confirmButton;
    @FindBy(xpath = "//span[text()='Review Vendor Details']")
    private WebElement reviewVendorDetailsText;
    @FindBy(xpath = "//div[text()='Vendor Added Successfully.']")
    private WebElement vendorAddedSuccessfullyMessage;
    @FindBy(xpath = "//button[text()='OK']")
    private WebElement okButton;
    @FindBy(xpath = "(//span[text()='Vendor Details'])[2]")
    private WebElement vendorDetailsText;

    public VendorPage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(), this);

    }

    public void clickAddNewUserButton() {
        waitUtility.waitForElementToBeRefreshed(addNewUserButton);
        commonActions.clickElement(addNewUserButton);
    }

    public String getTextOfVendorDirectory() {
        return commonActions.getElementText(vendorDirectoryText);
    }

    public boolean isVendorDirectoryPage() {
        return commonActions.isElementDisplayed(vendorDirectoryText);
    }

    public void enterCompanyName(String companyName) {
        waitUtility.waitForElementToBeVisible(companyNameInput);
        commonActions.typeText(companyNameInput, companyName);
    }

    public void enterContactNo(String contactNo) {
        waitUtility.waitForElementToBeVisible(contactNoInput);
        commonActions.typeText(contactNoInput, contactNo);
    }

    public void enterFirstName(String firstName) {
        waitUtility.waitForElementToBeVisible(firstNameInput);
        commonActions.typeText(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        waitUtility.waitForElementToBeVisible(lastNameInput);
        commonActions.typeText(lastNameInput, lastName);
    }

    public void enterEmail(String email) {
        waitUtility.waitForElementToBeVisible(emailIdInput);
        commonActions.typeText(emailIdInput, email);
    }

    public void selectVendorType(String vendorType) {
        waitUtility.waitForElementToBeClickable(vendorTypeSelect);
        commonActions.clickElement(vendorTypeSelect);
        //commonActions.selectRandomFromDropdown(portfolioTypeSelect);
        commonActions.selectByVisibleText(vendorTypeSelect,vendorType);
    }

    public List<WebElement> Address_option() {
        waitUtility.waitForElementToBeVisible(address1);
        return Address_option;
    }

    public void enterAddress1(String address1Text) {
        waitUtility.waitForElementToBeVisible(address1);
        WebDriver driver = DriverManager.getDriver();
        Actions actions = new Actions(driver);
        actions.click(address1).sendKeys(address1Text).build().perform();
        actions.click(Address_option().get(0)).build().perform();
    }

    public void enterAddress2(String address2Text) {
        waitUtility.waitForElementToBeVisible(address2);
        commonActions.typeText(address2, address2Text);
    }

    public void enterCity(String cityText) {
        waitUtility.waitForElementToBeVisible(city);
        commonActions.typeText(city, cityText);
    }

    public void enterState(String stateText) {
        waitUtility.waitForElementToBeVisible(state);
        commonActions.selectByVisibleText(state, stateText);
    }

    public void enterCountry(String countryText) {
        waitUtility.waitForElementToBeVisible(country);
        commonActions.selectByVisibleText(country, countryText);
    }

    public void enterZipCode(String zipCodeText) {
        waitUtility.waitForElementToBeVisible(zipCode);
        commonActions.typeText(zipCode, zipCodeText);
    }


    public void clickNextButton() {
        waitUtility.waitForPresenceOfElementLocated(nextButton);
        waitUtility.waitForElementToBeClickable(nextButton);
        commonActions.scrollToElement(nextButton);
        try {
            commonActions.clickElement(nextButton);
        }catch (ElementClickInterceptedException ee){
            System.out.println(ee.getMessage());
           waitUtility.waitForElementToBeRefreshed(nextButton) ;
         WebDriver d=DriverManager.getDriver();
            JavascriptExecutor js = (JavascriptExecutor) d;
            js.executeScript("arguments[0].click();", nextButton);
        }

    }


    public void selectAreaCoveredCheckBox() {
        waitUtility.waitForElementToBeVisible(areaCoveredSelect);
        commonActions.scrollToElementCenter(areaCoveredSelect);
        commonActions.clickElement(areaCoveredSelect);
        for (WebElement checkbox : areaCoveredCheckBox) {
            commonActions.scrollToElement(checkbox);
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", checkbox);
            Actions add = new Actions(DriverManager.getDriver());
            add.moveToElement(checkbox).click().build().perform();

        }

    }

    public void ClickOnConfirmButton() {
        waitUtility.waitForElementToBeVisible(confirmButton);
        commonActions.clickElement(confirmButton);
    }

    public boolean isReviewVendorDetailsPage() {
        waitUtility.waitForElementToBeVisible(reviewVendorDetailsText);
        return commonActions.isElementDisplayed(reviewVendorDetailsText);
    }

    public boolean isVendorAddedSuccessfullyMessageDisplayed() {
        waitUtility.waitForElementToBeVisible(vendorAddedSuccessfullyMessage);
        return commonActions.isElementDisplayed(vendorAddedSuccessfullyMessage);
    }

    public String getVendorAddedSuccessfullyMessage() {
        waitUtility.waitForElementToBeVisible(vendorAddedSuccessfullyMessage);
        return commonActions.getElementText(vendorAddedSuccessfullyMessage);
    }

    public void clickOkButton() {
        waitUtility.waitForElementToBeVisible(okButton);
        commonActions.clickElement(okButton);
    }

    public boolean isVendorDetailsPage() {
        waitUtility.waitForElementToBeVisible(vendorDetailsText);
        return commonActions.isElementDisplayed(vendorDetailsText);
    }


}
