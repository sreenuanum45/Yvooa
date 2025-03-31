package stepDefinitions;

import java.util.Objects;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.github.javafaker.Faker;

import config.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import pageObjects.DashboardPage;
import pageObjects.MaintenancePage;
import pageObjects.TenantsPage;
import pageObjects.UsersPage;
import pageObjects.VendorPage;
import utilities.CommonActions;
import utilities.WaitUtility;

public class DirectoryStepDefination {
    private DashboardPage dashboardPage;
    private MaintenancePage maintenancePage;
    private UsersPage usersPage;
    private TenantsPage tenantsPage;
    private VendorPage vendorPage;

    public DirectoryStepDefination() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @When("User clicks on the Directories button")
    public void user_clicks_on_the_directories_button() throws InterruptedException {
        dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
        dashboardPage.MoveToDirectories();
    }

    @When("User Select Directory {string}")
    public void user_select_directory(String directoryName) {
        if (directoryName.equalsIgnoreCase("Users Directory")) {
            dashboardPage.selectUsersDirectory();
        } else if (directoryName.equalsIgnoreCase("Tenant Directory")) {
            dashboardPage.selectTenantDirectory();
        } else if (directoryName.equalsIgnoreCase("Vendors Directory")) {
            dashboardPage.selectVendorDirectory();
        }
    }

    @When("User navigates to the selected directory {string}")
    public void user_navigates_to_the_selected_directory(String directoryName) {
        usersPage = new UsersPage(new CommonActions(), new WaitUtility());
        tenantsPage = new TenantsPage(new CommonActions(), new WaitUtility());
        vendorPage = new VendorPage(new CommonActions(), new WaitUtility());
        WebDriver driver = DriverManager.getDriver();
        if (Objects.requireNonNull(driver.getCurrentUrl()).contains("https://pm-uat.yvooa.com/directories/tenants")) {
            Assert.assertEquals(tenantsPage.getTenantDirectoryText(), directoryName);
        } else if (driver.getCurrentUrl().contains("https://pm-uat.yvooa.com/directories/vendor")) {
            Assert.assertEquals(vendorPage.getTextOfVendorDirectory(), directoryName);
        } else if (driver.getCurrentUrl().contains("https://pm-uat.yvooa.com/directories/users")) {
            Assert.assertEquals(usersPage.getTextOfUsersDirectory(), directoryName);
        }
    }

    @When("Fill the Personal Information form with the details {string} {string} {string} {string}")
    public void fill_the_personal_information_form_with_the_details(String firstName, String lastName, String phoneNumber, String emailId) {
        if (firstName.equalsIgnoreCase("{random}")) {
            firstName = new Faker().name().firstName();
            usersPage.enterFirstName(firstName);
        }
        usersPage.enterLastName(lastName);
        if (phoneNumber.equalsIgnoreCase("{random}")) {
            usersPage.enterPhoneNumber(generatePhoneNumber());
        } else {
            usersPage.enterPhoneNumber(phoneNumber);
        }
        if (emailId.equalsIgnoreCase("{random}")) {
            usersPage.enterEmailId(generateRandomEmail());
        } else {
            usersPage.enterEmailId(emailId);
        }
        usersPage.selectProfileType();
        usersPage.clickNextButton();
    }
    @When("Click on the AddNew button")
    public void click_on_the_add_new_button() {
      WebDriver driver=DriverManager.getDriver();
        if (Objects.requireNonNull(driver.getCurrentUrl()).contains("https://pm-uat.yvooa.com/directories/users")) {
                usersPage.clickAddNewUserButton();
            } else if (driver.getCurrentUrl().contains("https://pm-uat.yvooa.com/directories/vendor")) {

                vendorPage.clickAddNewUserButton();
            }
    }
    @And("Verify the created successfully message And Click on the OK button")
    public void Verify_the_created_successfully_message_And_Click_on_the_OK_button() {
        if (usersPage.isUserCreatedSuccessMessageDisplayed()) {
            Assert.assertEquals(usersPage.getUserCreatedSuccessMessage(), "User created successfully!");
            usersPage.clickOkButton();
        }
    }

    @And("Click on the Confirm button")
    public void click_on_the_confirm_button() {
       WebDriver driver=DriverManager.getDriver();
       if((Objects.requireNonNull(driver.getCurrentUrl())).contains("https://pm-uat.yvooa.com/directories/vendor")){
              vendorPage.ClickOnConfirmButton();
       } else if (driver.getCurrentUrl().contains("https://pm-uat.yvooa.com/directories/users")) {
            usersPage.clickConfirmButton();

       }
    }

    @And("Fill Vendor Details form  And {string} Click on the Next button")
    public void fill_vendor_details_form_and_click_on_the_next_button(String vendorType) {
        Assert.assertTrue(vendorPage.isVendorDirectoryPage());
        vendorPage.enterCompanyName("Test Company");
        vendorPage.enterContactNo(generatePhoneNumber());
        vendorPage.enterFirstName("John");
        vendorPage.enterLastName("Doe");
        vendorPage.enterEmail("aHxkF@example.com");
        vendorPage.selectVendorType(vendorType);
        vendorPage.enterAddress1("123 Main St");
        vendorPage.enterAddress2("Apt 4");
        vendorPage.enterCity("New York");
        vendorPage.enterState("Arkansas");
        vendorPage.enterZipCode("10001");
        vendorPage.enterCountry("United States");
        vendorPage.selectAreaCoveredCheckBox();
        vendorPage.clickNextButton();
    }

    public static String generateRandomEmail() {
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }

    public static String generatePhoneNumber() {
        Random rand = new Random();

        // Area code: 200-999 (cannot start with 0 or 1)
        int areaCode = 200 + rand.nextInt(800);

        // Central office code: 555 (reserved for fictional use)
        int centralOfficeCode = 555;

        // Line number: 0000-9999
        int lineNumber = rand.nextInt(10000);

        return String.format(
                "%03d-%03d-%04d",
                areaCode,
                centralOfficeCode,
                lineNumber
        );
    }


}
