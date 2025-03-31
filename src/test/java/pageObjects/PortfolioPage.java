package pageObjects;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.github.javafaker.Faker;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class PortfolioPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    @FindBy(id="addNewPortfolio")
    private WebElement addNewPortfolio;
    @FindBy(id="portfolioNameInput")
    private WebElement portfolioNameInput;
    @FindBy(id="portfolioTypeSelect")
    private WebElement portfolioTypeSelect;
    @FindBy(id="einSsnInput")
    private WebElement einSsnInput;
    @FindBy(id="emailInput")
    private WebElement emailInput;
    @FindBy(xpath="//div[@id='address1']")
    private WebElement address1;
    @FindBy(id="undefinedAddress2Input")
    private WebElement address2;
    @FindBy(name="city")
    private WebElement city;
    @FindBy(xpath="//input[@id='phoneNumberInput']")
    private WebElement phoneNumber;

    @FindBy(name="state")
    private WebElement state;
    @FindBy(xpath="//div[contains(@id, 'react-select')][2]")
    private List<WebElement> option;
    @FindBy(name="country")
    private WebElement country;
    @FindBy(name="zipCode")
    private WebElement zipCode;
    @FindBy(id="next")
    private WebElement nextButton;
    @FindBy(id="back")
    private WebElement backButton;
    @FindBy(id="confirm")
    private WebElement confirmButton;
    @FindBy(xpath="//div[text()='New Portfolio Created']")
    private WebElement newPortfolioCreatedMessage;
    @FindBy(xpath="//div[text()='Success']")
    private WebElement successMessage;
    @FindBy(xpath="//button[text()=\"OK\"]")
    private WebElement okButton;
    @FindBy(xpath="//h6[text()='Loading...']//parent::div")
    private WebElement loading;
    @FindBy(id="portfolioTable-rows-per-page-select")
    private WebElement rowsPerPage;
    @FindBy(xpath = "//td[@id='portfolioName']")
    List<WebElement>ListofPortfoliosNames;
    @FindBy(xpath="//p[@class='MuiTablePagination-displayedRows css-wqp0ve']")
    private WebElement displayedRows;
    @FindBy(id="portfolioTable")
    private WebElement table;
    @FindBy(xpath = "//button[@id='portfolioTable-next-button']//*[name()='svg']")
    private WebElement nextButtonTable;

    public void LoaderDisapear(){
        waitUtility.waitForElementToBeInvisible(loading);
    }
    public void enterPortfolioName(String portfolioName){
        if(portfolioName.equalsIgnoreCase("{random}")){
            portfolioName = "Portfolio"+System.currentTimeMillis();
            waitUtility.waitForElementToBeVisible(portfolioNameInput);
            commonActions.typeText(portfolioNameInput, portfolioName);
        }
        else {
            waitUtility.waitForElementToBeVisible(portfolioNameInput);
            commonActions.typeText(portfolioNameInput, portfolioName);
        }
    }
    public void enterEmail(String email){
        if(email.equalsIgnoreCase("{random}")){
          email= new Faker().internet().emailAddress();
            waitUtility.waitForElementToBeVisible(emailInput);
            commonActions.typeText(emailInput,email);
        }
        else{
            waitUtility.waitForElementToBeVisible(emailInput);
            commonActions.typeText(emailInput,email);
        }

    }
    public void clickOnOkButton(){
        waitUtility.waitForElementToBeVisible(okButton);
        commonActions.clickElement(okButton);
    }
    public String getSuccessMessage(){
        waitUtility.waitForElementToBeVisible(successMessage);
        return commonActions.getElementText(successMessage);
    }
    public String getNewPortfolioCreatedMessage(){
        waitUtility.waitForElementToBeVisible(newPortfolioCreatedMessage);
        return commonActions.getElementText(newPortfolioCreatedMessage);
    }
    public void clickOnConfirmButton(){
        waitUtility.waitForElementToBeVisible(confirmButton);
        commonActions.clickElement(confirmButton);
    }
    public void clickOnNextButton(){
        waitUtility.waitForElementToBeVisible(nextButton);
        commonActions.clickElement(nextButton);
    }
    public void enterZipCode(String zip){
        waitUtility.waitForElementToBeVisible(zipCode);
        commonActions.typeText(zipCode,zip);
    }
    public void enterPhoneNumber(String phone){
        if(phone.equalsIgnoreCase("{random}")){
            phone = new Faker().phoneNumber().cellPhone();
            waitUtility.waitForElementToBeVisible(phoneNumber);
            commonActions.typeText(phoneNumber,phone);
        }
        else{
            waitUtility.waitForElementToBeVisible(phoneNumber);
            commonActions.typeText(phoneNumber,phone);
        }

    }

    public WebElement GetAddress1()
    {
        return address1;
    }
    public void enterState(String statename){
        waitUtility.waitForElementToBeVisible(state);
        commonActions.selectByVisibleText(state,statename);
    }
    public void enterCountry(String countryname){
        waitUtility.waitForElementToBeVisible(country);
        commonActions.selectByVisibleText(country,countryname);
    }
    public void enterCity(String city){
        waitUtility.waitForElementToBeVisible(this.city);
        commonActions.typeText(this.city,city);
    }
    public void enterAddress1(String address){
        waitUtility.waitForElementToBeVisible(address1);
        WebDriver driver = DriverManager.getDriver();
        Actions actions=new Actions(driver);
        actions.click(GetAddress1()).sendKeys(address).build().perform();
        actions.click(Address_option().get(0)).build().perform();
    }
public List<WebElement> Address_option(){
        return option;
}
public void enterAddress2(String address){
        waitUtility.waitForElementToBeVisible(address2);
        commonActions.typeText(address2,address);
}
    public void entereinSsnInput(String einSsn){
        waitUtility.waitForElementToBeVisible(einSsnInput);
        commonActions.typeText(einSsnInput,einSsn);
    }

    public WebElement GetPortfolioTypeSelect()
    {
        return portfolioTypeSelect;
    }


    public void SelectPortfolioType(String portfolioType)
    {
        waitUtility.waitForElementToBeClickable(portfolioTypeSelect);
        commonActions.clickElement(portfolioTypeSelect);
        //commonActions.selectRandomFromDropdown(portfolioTypeSelect);
        commonActions.selectByVisibleText(portfolioTypeSelect,portfolioType);


    }
    public void clickAddNewPortfolio()
    {
        waitUtility.waitForElementToBeClickable(addNewPortfolio);
        commonActions.scrollToElement(addNewPortfolio);
        commonActions.clickElementUsingJS(addNewPortfolio);
    }
    public PortfolioPage(CommonActions commonActions, WaitUtility waitUtility)
    {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(),this);
    }
    public WebElement GetRowsPerPage()
    {
        return rowsPerPage;
    }
    public void ClickOnTheRowsPerPage()
    {
        waitUtility.waitForElementToBeInvisible(loading);
        commonActions.scrollToBottom();
        commonActions.scrollToElement(rowsPerPage);
        JavascriptExecutor js=(JavascriptExecutor)DriverManager.getDriver();
        js.executeScript("arguments[0].click();",rowsPerPage);
        Select select = new Select(GetRowsPerPage());
        select.selectByVisibleText("100");
    }
    public List<WebElement> ListOfPortfoliosNames()
    {
        waitUtility.waitForElementToBeInvisible(loading);
        return ListofPortfoliosNames;
    }
    public WebElement GetDisplayedRows()
    {
        return displayedRows;
    }
    public int  GetDisplayedRowsCount()
    {
        waitUtility.waitForElementToBeInvisible(loading);
        int y= Math.round((float) Integer.parseInt(displayedRows.getText().split("of ")[1].trim()) /100);
        return y;
    }
    public WebElement GetTable()
    {
        return table;
    }
    public void waitforTableToLoad()
    {
        waitUtility.waitForElementToBeInvisible(loading);
        waitUtility.waitForElementToBeVisible(table);
    }
    public void ClickOnNextButtonTable() throws InterruptedException {

        try {
            commonActions.scrollToElement(nextButtonTable);
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            js.executeScript("arguments[0].scrollIntoView();", nextButtonTable);
            if(nextButtonTable.isDisplayed()){
            nextButton.click();
            }
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }

    }
    public boolean isDisplayedNextButtonTable()
    {
        return nextButtonTable.isDisplayed();
    }
}
