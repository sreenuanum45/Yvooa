package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class LoginPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    @FindBy(id="forgotPassword")
    private WebElement forgotPassword;
    @FindBy(id="signupHere")
    private WebElement signupHere;
    @FindBy(id = "customerSupportEmail")
    private WebElement customerSupportEmail;
    @FindBy(id="loginPageTitle")
    private WebElement loginPageTitle;
    @FindBy(id="emailInput")@CacheLookup
    private WebElement email;
    @FindBy(id="passwordInput")@CacheLookup
    private WebElement password;
    @FindBy(id="loginButton")@CacheLookup
    private WebElement loginButton;
    @FindBy(xpath = "//div[text()='Incorrect username or password.']")
    private WebElement incorrectUsernameOrPassword;
    public LoginPage(CommonActions commonActions, WaitUtility waitUtility)
    {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(),this);
    }
    public void clickForgotPassword()
    {
        waitUtility.waitForElementToBeClickable(forgotPassword);
        commonActions.clickElement(forgotPassword);
    }
    public void clickSignupHere()
    {
        waitUtility.waitForElementToBeClickable(signupHere);
        commonActions.clickElement(signupHere);
    }
    public void enterEmail(String emailId)
    {
        waitUtility.waitForElementToBeVisible(email);
        commonActions.typeText(email,emailId);
    }
    public void enterPassword(String passwordText)
    {
        waitUtility.waitForElementToBeVisible(password);
        commonActions.typeText(password,passwordText);
    }
    public void clickLoginButton()
    {
        waitUtility.waitForElementToBeClickable(loginButton);
        commonActions.clickElement(loginButton);
    }
public String getIncorrectUsernameOrPassword()
{
    return commonActions.getElementText(incorrectUsernameOrPassword);
}




}
