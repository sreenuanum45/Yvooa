package pageObjects;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class SignUPPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    public SignUPPage(CommonActions commonActions, WaitUtility waitUtility)
    {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(),this);
    }
    @FindBy(id="firstNameInput")
    private WebElement firstName;
    @FindBy(id="lastNameInput")
    private WebElement lastName;
    @FindBy(id="signupEmailInput")
    private WebElement email;
    @FindBy(id="phoneNumberInput")
    private WebElement phoneNumber;
    @FindBy(id="companyNameInput")
    private WebElement companyName;
    @FindBy(id="taxIdInput")
    private WebElement taxId;
    @FindBy(id="productTypeSelect")
    private WebElement productType;
    @FindBy(id="signupAddress2Input")
    private WebElement address2;
@FindBy(id="signupCountrySelect")
    private WebElement country;
@FindBy(id="signupZipcodeInput")
    private WebElement zipCode;
@FindBy(id="signupPasswordInput")
    private WebElement password;
@FindBy(id="confirmSignUpPasswordInput")
    private WebElement confirmPassword;
@FindBy(id="termsAndConditionsCheckbox")
    private WebElement termsAndConditionsCheckbox;
@FindBy(xpath="//div[@id='address1']")
    private WebElement address1;
@FindBy(id="signupButton")
    private WebElement signUpButton;
@FindBy(id="signupCityInput")
private WebElement city;
@FindBy(id="emailCode")
private WebElement emailCode;
@FindBy(id="confirmBtn")
private WebElement confirmButton;
@FindBy(id="signupStateSelect")
private WebElement state;


@FindBy(xpath="//div[contains(@id, 'react-select')][2]")
private List<WebElement> option;

public WebElement EmailCode(){
    return emailCode;
}
public void EnterEmailCode(String emailCodeText){
    waitUtility.waitForElementToBeVisible(emailCode);
    commonActions.typeText(emailCode,emailCodeText);
}
    public List<WebElement> Address_option(){
        return option;
    }




public void ClickConfirmButton(){
    waitUtility.waitForElementToBeClickable(confirmButton);
    commonActions.clickElement(confirmButton);
}
public boolean isEmailCodeDisplayed(){
    //waitUtility.waitForElementToBeVisible(emailCode);
    //commonActions.waitForPageLoad();
   return commonActions.isElementDisplayed(emailCode);
    //return emailCode.isDisplayed();
}


public void enterCity(String cityText){
    waitUtility.waitForElementToBeVisible(city);
    commonActions.typeText(city,cityText);
}
public void enterFirstName(String firstNameText)
    {
        waitUtility.waitForElementToBeVisible(firstName);
        commonActions.typeText(firstName,firstNameText);
    }
    public void enterLastName(String lastNameText)
    {
        waitUtility.waitForElementToBeVisible(lastName);
        commonActions.typeText(lastName,lastNameText);
    }
    public void enterEmail(String emailText)
    {
        waitUtility.waitForElementToBeVisible(email);
        commonActions.typeText(email,emailText);
    }
    public void enterPhoneNumber(String phoneNumberText)
    {
        waitUtility.waitForElementToBeVisible(phoneNumber);
        commonActions.typeText(phoneNumber,phoneNumberText);
    }
    public void enterCompanyName(String companyNameText)
    {
        waitUtility.waitForElementToBeVisible(companyName);
        commonActions.typeText(companyName,companyNameText);
    }
    public void enterTaxId(String taxIdText)
    {
        waitUtility.waitForElementToBeVisible(taxId);
        commonActions.typeText(taxId,taxIdText);
    }
    public void selectProductType(String productTypeText)
    {
        waitUtility.waitForElementToBeVisible(productType);
        commonActions.selectByVisibleText(productType,productTypeText);
    }

    //pick random
    public void selectRandomProductType()
    {
        waitUtility.waitForElementToBeVisible(productType);
        commonActions.selectRandomFromDropdown(productType);
    }
    public void enterAddress2(String address2Text)
    {
        waitUtility.waitForElementToBeVisible(address2);
        commonActions.typeText(address2,address2Text);
    }
    public void selectCountry(String countryText)
    {
        waitUtility.waitForElementToBeVisible(country);
        commonActions.selectByVisibleText(country,countryText);
    }
    public void enterZipCode(String zipCodeText)
    {
        waitUtility.waitForElementToBeVisible(zipCode);
        commonActions.typeText(zipCode,zipCodeText);
    }
    public void enterPassword(String passwordText)
    {
        waitUtility.waitForElementToBeVisible(password);
        commonActions.typeText(password,passwordText);
    }
    public void enterConfirmPassword(String confirmPasswordText)
    {
        waitUtility.waitForElementToBeVisible(confirmPassword);
        commonActions.typeText(confirmPassword,confirmPasswordText);
    }
    public void clickTermsAndConditionsCheckbox()
    {
        waitUtility.waitForElementToBeClickable(termsAndConditionsCheckbox);
        commonActions.clickElement(termsAndConditionsCheckbox);
    }
    public void clickSignUpButton()
    {
        waitUtility.waitForElementToBeClickable(signUpButton);
        commonActions.clickElement(signUpButton);
    }
    public void selectRandomCountry()
    {
        waitUtility.waitForElementToBeVisible(country);
        commonActions.selectRandomFromDropdown(country);
    }
    public WebElement state(){
        return state;
    }
    public void selectRandomState()
    {
        waitUtility.waitForElementToBeVisible(state);
        commonActions.selectRandomFromDropdown(state);
    }
    public void enterAddress1(String address1Text)
    {
        Actions actions=new Actions(DriverManager.getDriver());
        actions.moveToElement(address1).perform();

        actions.click(address1).sendKeys(address1Text).build().perform();

        actions.sendKeys(Keys.ENTER).build().perform();
        actions.click(Address_option().get(0)).build().perform();
       /* waitUtility.waitForElementToBeVisible(address1);
        commonActions.typeText(address1,address1Text);
        commonActions. typeKeys(address1, Keys.ENTER);*/
    }






}
