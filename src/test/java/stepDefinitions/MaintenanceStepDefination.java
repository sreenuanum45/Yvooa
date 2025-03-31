package stepDefinitions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import config.DriverManager;
import io.cucumber.java.en.And;
import pageObjects.DashboardPage;
import pageObjects.MaintenancePage;
import utilities.CommonActions;
import utilities.WaitUtility;

public class MaintenanceStepDefination {
    private DashboardPage dashboardPage;
    private MaintenancePage maintenancePage;
    public MaintenanceStepDefination() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

   @And("User clicks on the maintenance button")
    public void user_clicks_on_the_maintenance_button() {
        dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
      dashboardPage.ClickOnMaintenance();
    }

    @And("User is redirected to the Maintenance page")
    public void user_is_redirected_to_the_maintenance_page() throws InterruptedException {
        maintenancePage = new MaintenancePage(new CommonActions(), new WaitUtility());
        Thread.sleep(1000);
        maintenancePage.clickAddNew();
    }

    @And("Fill the Create Maintenance Ticket {string}")
    public void fill_the_create_maintenance_ticket(String portfolioName) {
        maintenancePage.selectPortfolio(portfolioName);
        maintenancePage.waitforLoader();
        maintenancePage.selectProperty();
        maintenancePage.clickConfirmButton();
    }

    @And("Fill the Add Maintenance Request {string} And {string} And {string} And {string} And {string}")
    public void fill_the_add_maintenance_request(String Request_Type,String problemDescription,String location,String Notes,String MediaSelectFile) throws InterruptedException {
       if(Request_Type.equals("Maintenance Request-Property")) {
           maintenancePage.selectRequestType(Request_Type);
           maintenancePage.enterProblemDescription(problemDescription);
       }
         else if(Request_Type.equals("Turnover Request")) {
           maintenancePage.selectRequestType(Request_Type);
           maintenancePage.enterProblemDescription(problemDescription);
       }
       maintenancePage.clickNextButton();
         maintenancePage.selectCategory();
            maintenancePage.selectSubCategory();
            maintenancePage.enterLocation(location);
            maintenancePage.selectPriority();
            maintenancePage.selectAssignTo();
            maintenancePage.enterMediaNotes(Notes);
        //maintenancePage.clickMediaSelectFileButton(MediaSelectFile);
        maintenancePage.selectVendor();
        maintenancePage.Available_Time_Slot_1 ();
       // maintenancePage.Available_Time_Slot_2 ();
        maintenancePage.FromTime1();
        maintenancePage.ToTime1();
        /*maintenancePage.FromTime2();
        maintenancePage.ToTime2();*/
      maintenancePage.clickCreateRequest();
    }

    @And("Verify the A Maintenance Work Order has been successfully Created! message")
    public void verify_the_a_maintenance_work_order_has_been_successfully_created_message() {
        WebElement element = maintenancePage.DisplayedSucessMessage();
        Assert.assertTrue(element.isDisplayed());
        maintenancePage.clickClose();
    }

}


