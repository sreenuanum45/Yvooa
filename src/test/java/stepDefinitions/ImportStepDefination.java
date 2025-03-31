package stepDefinitions;

import java.awt.AWTException;

import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import io.cucumber.java.en.And;
import pageObjects.DashboardPage;
import pageObjects.ImportCSVPage;
import pageObjects.MaintenancePage;
import pageObjects.TenantsPage;
import pageObjects.UsersPage;
import pageObjects.VendorPage;
import utilities.CommonActions;
import utilities.WaitUtility;

public class ImportStepDefination {
    private DashboardPage dashboardPage;
    private MaintenancePage maintenancePage;
    private UsersPage usersPage;
    private TenantsPage tenantsPage;
    private VendorPage vendorPage;
    private ImportCSVPage importCSVPage;

    public ImportStepDefination() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @And("User is redirected to the Dashboard And User clicks on the import button")
    public void user_is_redirected_to_the_dashboard_and_user_clicks_on_the_import_button() {
        dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
        dashboardPage.Clickonimports();
    }

    @And("User select {string} And {string} And {string} And {string} Fields")
    public void user_select_and_fields(String category, String portfolioname,String PropertyName ,String file) throws AWTException, InterruptedException {
        importCSVPage=new ImportCSVPage(new CommonActions(),new WaitUtility());
        importCSVPage.selectCategoryAndFileUpload(category,portfolioname,PropertyName,file);
    }

    @And("user click on the uploadFinalFile button")
    public void user_click_on_the_upload_final_file_button() {
        importCSVPage.clickUploadFinalFile();
    }
}
