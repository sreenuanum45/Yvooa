package pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class TenantsPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    @FindBy(xpath = "//span[text()='Tenant Directory']")
    private WebElement tenantDirectoryText;
    public TenantsPage(CommonActions commonActions, WaitUtility waitUtility) {
        this.commonActions = commonActions;
        this.waitUtility = waitUtility;
        PageFactory.initElements(DriverManager.getDriver(), this);
    }
    public boolean isTenantDirectoryPage() {
        waitUtility.waitForElementToBeVisible(tenantDirectoryText);
        return commonActions.isElementDisplayed(tenantDirectoryText);
    }
    public String getTenantDirectoryText() {
        waitUtility.waitForElementToBeVisible(tenantDirectoryText);
        return commonActions.getElementText(tenantDirectoryText);
    }

}
