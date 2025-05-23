package stepDefinitions;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.applitools.eyes.selenium.Eyes;

import config.DriverManager;
import config.TestEnvironment;
import hooks.ApplitoolsHooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.DashboardPage;
import pageObjects.LoginPage;
import utilities.CommonActions;
import utilities.EyesManager;
import utilities.VisualTestingUtil;
import utilities.WaitUtility;

public class Yoova_LoginSteps {
    // Instance variables
    private WebDriver driver;
    private static final double VISUAL_DIFF_THRESHOLD = 0.5;
    private static Eyes eyes = ApplitoolsHooks.getEyes();

    private LoginPage loginPage;

    // Public no-argument constructor required by PicoContainer
    public Yoova_LoginSteps() {
        // Initialize PageFactory elements using the current WebDriver
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @Given("open browser")
    public void open_browser() throws Exception {
        driver = DriverManager.getDriver();
        driver.manage().deleteAllCookies();
       // ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");

    }

    @Then("User launch Login page")
    public void User_launch_Login_page() throws Exception {
        Thread.sleep(2000);
        // Navigate to the login page using the base URL from configuration
        DriverManager.getDriver().get(TestEnvironment.getBaseUrl() + "/login");
        // Initialize the LoginPage page object with the injected dependencies
        loginPage = new LoginPage(new CommonActions(), new WaitUtility());

    }

    @When("User enters {string} and {string} valid credentials")
    public void user_enters_valid_credentials(String username, String password) throws Exception {


        loginPage.enterEmail(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        VisualTestingUtil.VisualComparisonResult result =
                new VisualTestingUtil(driver).captureAndCompare("dashboard_after_login");

        /*// Changed assertion
        Assert.assertTrue(
                result.getDifferencePercentage() <= VISUAL_DIFF_THRESHOLD,
                "Visual differences exceeded threshold: " + result.getMessage()
        );*/



        // Applitools visual check

        if (eyes != null) {
            EyesManager eyesManager = new EyesManager(DriverManager.getDriver(), eyes);
            eyesManager.checkWindow("Dashboard after login");
        }
    }

    @And("User is redirected to the Dashboard")
    public void user_is_redirected_to_the_dashboard() throws Exception {
       // Assert.assertEquals(driver.getCurrentUrl(), TestEnvironment.getBaseUrl() + "/dashboard");
        DashboardPage dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
        Assert.assertTrue(dashboardPage.isLogoDisplayed());
        // Visual testing
        if (eyes != null) {
            EyesManager eyesManager = new EyesManager(DriverManager.getDriver(), eyes);
            eyesManager.checkWindowWithIgnore(By.id("Yvooa Logo"), "Dashboard Ignore Dynamic");
        }
    }

    @And("User closes the browser")
    public void user_closes_the_browser() throws Exception {
        // Quit the driver and clean up resources
        DriverManager.quitDriver();
    }

    @Then("User enters invalid credentials")
    public void user_enters_invalid_credentials() throws Exception {
        String email = "sreeee@gmail.com";
        String password = "Sreenu80@";
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        Assert.assertEquals(loginPage.getIncorrectUsernameOrPassword(), "Incorrect username or password.");
    }


    @Then("User enters empty credentials")
    public void user_enters_empty_credentials() throws Exception {
        String email = "";
        String password = "";
        loginPage.enterEmail(email);

        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

    }

}


