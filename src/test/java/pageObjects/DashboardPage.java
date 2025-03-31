
package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class DashboardPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;

    public DashboardPage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @FindBy(id = "home")
    private WebElement home;
    @FindBy(id = "portfolio")
    private WebElement portfolio;
    @FindBy(id = "maintenance")
    private WebElement maintenance;
    @FindBy(id = "reports")
    private WebElement reports;
    @FindBy(id = "documents")
    private WebElement documents;
    @FindBy(id = "import")
    private WebElement imports;
    @FindBy(id = "directories")
    private WebElement directories;
    @FindBy(id = "notifications")
    private WebElement notifications;
    @FindBy(id = "accounting")
    private WebElement accounting;
    @FindBy(xpath = "//div[@name='Profile']")
    private WebElement profile;
    @FindBy(id = "Yvooa Logo")
    private WebElement logo;
    @FindBy(id = "home-prof-img")
    private WebElement profileImage;
    @FindBy(id="usersDirectory")
    private WebElement usersDirectory;
    @FindBy(id="tenantDirectory")
    private WebElement tenantDirectory;
    @FindBy(id="vendorDirectory")
    private WebElement vendorDirectory;


    public boolean isProfileImageDisplayed() {
        return waitUtility.waitForElementToBeVisible(profileImage).isDisplayed();
    }

    public boolean isLogoDisplayed() {
        return waitUtility.waitForElementToBeVisible(logo).isDisplayed();
    }

    public void ClickOnPortfolio() {
        waitUtility.waitForElementToBeClickable(portfolio);
        commonActions.clickElement(portfolio);

    }

    public void ClickOnMaintenance() {
        waitUtility.waitForElementToBeClickable(maintenance);
        commonActions.clickElement(maintenance);
    }
    public void ClickOnreports(){
        waitUtility.waitForElementToBeClickable(reports);
        commonActions.clickElement(reports);
    }
    public void MoveToDirectories() throws InterruptedException {
        waitUtility.waitForElementToBeVisible(directories);
        commonActions.moveToElement(directories);

        //commonActions.wait();
        commonActions.clickElement(directories);
    }
    public void selectUsersDirectory(){
        waitUtility.waitForElementToBeClickable(usersDirectory);
        commonActions.moveToElement(usersDirectory);
        commonActions.clickElement(usersDirectory);
    }
    public void selectTenantDirectory(){
        waitUtility.waitForElementToBeClickable(tenantDirectory);
        commonActions.moveToElement(tenantDirectory);
        commonActions.waitForPageLoad();
        commonActions.scrollToElement(tenantDirectory);
        commonActions.clickElement(tenantDirectory);
    }
    public void selectVendorDirectory(){
        waitUtility.waitForElementToBeClickable(vendorDirectory);
        commonActions.moveToElement(vendorDirectory);
        commonActions.waitForPageLoad();
        commonActions.scrollToElement(vendorDirectory);
        commonActions.clickElement(vendorDirectory);
    }
    public void Clickonimports(){
        waitUtility.waitForElementToBeClickable(imports);
        commonActions.clickElement(imports);
    }


}

