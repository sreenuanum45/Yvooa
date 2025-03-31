package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class UsersPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    @CacheLookup
    @FindBy(xpath = "//span[text()='Users Directory']")
    private WebElement usersDirectoryText;
    @FindBy(id = "addNewUser")
    private WebElement addNewUserButton;
    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;
    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;
    @FindBy(id = "phoneNumberInput")
    private WebElement phoneNumberInput;
    @FindBy(id = "emailIdInput")
    private WebElement emailIdInput;
    @FindBy(id = "profileTypeSelect")
    private WebElement profileTypeSelect;
    @FindBy(name = "next")
    private WebElement nextButton;
    @FindBy(id = "confirm")
    private WebElement confirmButton;
    @FindBy(xpath = "//div[text()='User created successfully!']")
    private WebElement userCreatedSuccessMessage;
    @FindBy(xpath = "//button[text()='OK']")
    private WebElement okButton;
    @FindBy(xpath = "//span[text()='Review Personal Details']")
    private WebElement reviewPersonalDetailsText;
    @FindBy(xpath = "//section[@name='Searchbar and Options']")
    private WebElement searchbarAndOptions;


    public UsersPage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public String getTextOfUsersDirectory() {
        waitUtility.waitForElementToBeVisible(usersDirectoryText);
        return commonActions.getElementText(usersDirectoryText);
    }

    public boolean isUsersDirectoryDisplayed() {
        try {
            waitUtility.waitForElementToBeVisible(usersDirectoryText);
            commonActions.scrollToElement(usersDirectoryText);
            commonActions.scrollToElementCenter(usersDirectoryText);
            commonActions.waitForPageLoad();
            return commonActions.isElementDisplayed(usersDirectoryText);
        }catch (Exception e) {
            waitUtility.waitForElementToBeVisible(usersDirectoryText);
            return usersDirectoryText.isDisplayed();
        }
    }

    public void clickAddNewUserButton() {
        waitUtility.waitForElementToBeClickable(addNewUserButton);
        commonActions.scrollToElement(addNewUserButton);
        commonActions.waitForPageLoad();
        commonActions.clickElement(addNewUserButton);
    }

    public void enterFirstName(String firstName) {
        commonActions.typeText(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        commonActions.typeText(lastNameInput, lastName);
    }

    public void enterPhoneNumber(String phoneNumber) {
        commonActions.typeText(phoneNumberInput, phoneNumber);
    }

    public void enterEmailId(String emailId) {
        commonActions.typeText(emailIdInput, emailId);
    }

    public void selectProfileType() {
        commonActions.selectRandomFromDropdown(profileTypeSelect);
    }

    public void clickNextButton() {
        waitUtility.waitForElementToBeClickable(nextButton);
        commonActions.clickElement(nextButton);
    }

    public void clickConfirmButton() {
        waitUtility.waitForElementToBeClickable(confirmButton);
        commonActions.clickElement(confirmButton);
    }

    public String getUserCreatedSuccessMessage() {
        return commonActions.getElementText(userCreatedSuccessMessage);
    }

    public boolean isUserCreatedSuccessMessageDisplayed() {
        return commonActions.isElementDisplayed(userCreatedSuccessMessage);
    }

    public void clickOkButton() {
        waitUtility.waitForElementToBeClickable(okButton);
        commonActions.clickElement(okButton);
    }

    public boolean isReviewPersonalDetailsDisplayed() {
        waitUtility.waitForElementToBeVisible(reviewPersonalDetailsText);
        return commonActions.isElementDisplayed(reviewPersonalDetailsText);
    }
public boolean searchbarAndOptionsDisplayed() {
        waitUtility.waitForElementToBeVisible(searchbarAndOptions);
        return commonActions.isElementDisplayed(searchbarAndOptions);
    }

}
