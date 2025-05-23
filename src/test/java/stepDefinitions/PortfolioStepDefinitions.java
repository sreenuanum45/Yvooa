package stepDefinitions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import config.TestEnvironment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.applitools.eyes.selenium.Eyes;

import config.DriverManager;
import hooks.ApplitoolsHooks;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.annotations.DataProvider;
import pageObjects.DashboardPage;
import pageObjects.LoginPage;
import pageObjects.PortfolioPage;
import utilities.CommonActions;
import utilities.ExcelUtility;
import utilities.WaitUtility;

import static stepDefinitions.Yvooa_RegisterSteps.driver;

public class PortfolioStepDefinitions {
    private static int scenarioInstanceCount = 0;
    private static final Object lock = new Object(); // For thread safety

    private static final double VISUAL_DIFF_THRESHOLD = 0.5;
    private static Eyes eyes = ApplitoolsHooks.getEyes();
    private  List<Map<String, String>> testData;
    private PortfolioPage portfolioPage;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    public Scenario currentScenario;
    public int dataRowIndex = 0;
    static int pages;

    // Public no-argument constructor required by PicoContainer
    public PortfolioStepDefinitions() {

        // Initialize PageFactory elements using the current WebDriver
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {

        currentScenario = scenario;
        if (testData != null && !testData.isEmpty()) {
            // Assume each scenario in outline corresponds to one row
            String scenarioName = scenario.getName();
            int scenarioIndex = Integer.parseInt(scenarioName.split(" - ")[1]); // e.g., "Portfolio Creation - 0"
            if (scenarioIndex < testData.size()) {
                dataRowIndex = scenarioIndex;
            } else {
                throw new RuntimeException("No matching test data found for scenario: " + scenarioName);
            }
        }


    }

    @Then("User enters valid credentials")
    public void user_enters_valid_credentials() {
        if (testData != null && dataRowIndex < testData.size()) {
            Map<String, String> data = testData.get(dataRowIndex);
            loginPage = new LoginPage(new CommonActions(), new WaitUtility());
            loginPage.enterEmail(data.get("username"));
            loginPage.enterPassword(data.get("password"));
            loginPage.clickLoginButton();
        } else {
            throw new RuntimeException("No more test data found for scenario: " + currentScenario.getName());
        }
    }

    @And("User is redirected to the Dashboard And User clicks on the Portfolio button")
    public void user_clicks_portfolio_button() {
        dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
        dashboardPage.ClickOnPortfolio();
    }

    @Then("User is redirected to the Portfolio page")
    public void verify_portfolio_page() {
        portfolioPage = new PortfolioPage(new CommonActions(), new WaitUtility());
     driver = DriverManager.getDriver();
        Assert.assertEquals(driver.getCurrentUrl(), "https://pm-uat.yvooa.com/portfolio");
        portfolioPage.waitforTableToLoad();

    }

    @Then("user Click on the Confirm button")
    public void user_click_confirm_button() {

        portfolioPage.clickOnConfirmButton();
    }

    @And("User Fills the Portfolio Details form with {string}, {string}, {string}, {string}, {string}")
    public void user_Fills_the_Portfolio_Details_form_with(String portfolioName, String portfolioType, String ein, String email, String phone) {

        portfolioPage.clickAddNewPortfolio();
        portfolioPage.LoaderDisapear();
        portfolioPage.enterPhoneNumber(phone);
        portfolioPage.enterPortfolioName(portfolioName);
        portfolioPage.SelectPortfolioType(portfolioType); // Fixed method case
        portfolioPage.entereinSsnInput(ein); // Fixed method name and spacing
        portfolioPage.enterEmail(email);

    }

    @And("User Fills the Portfolio Details form")
    public void user_fills_the_portfolio_details_form() {
        if (testData != null && dataRowIndex < testData.size()) {
            Map<String, String> data = testData.get(dataRowIndex);
            portfolioPage.clickAddNewPortfolio();
            portfolioPage.LoaderDisapear();
            portfolioPage.enterPhoneNumber(data.get("Portfoilo_Owner_Contact_Number"));
            portfolioPage.enterPortfolioName(data.get("portfolioName"));
            portfolioPage.SelectPortfolioType(data.get("portfolioType"));
            portfolioPage.entereinSsnInput(data.get("EIN No"));
            portfolioPage.enterEmail(data.get("Portfolio Owner Email ID"));
        } else {
            throw new RuntimeException("No more test data found for scenario: " + currentScenario.getName());
        }
    }

    @Then("User Fills the Business Address details form with {string}, {string}, {string}, {string}, {string}, {string}")
    public void fill_business_address(String address1, String address2, String city,
                                      String state, String zip, String country) {
        portfolioPage.enterAddress1(address1);
        portfolioPage.enterAddress2(address2);
        portfolioPage.enterCity(city);
        portfolioPage.enterState(state);
        portfolioPage.enterZipCode(zip);
        portfolioPage.enterCountry(country);
    }

    @Then("user Fills the Business Address details form")
    public void user_fills_the_business_address_details_form() {
        if (testData != null && dataRowIndex < testData.size()) {
            Map<String, String> data = testData.get(dataRowIndex);
            portfolioPage.enterAddress1(data.get("Address 1"));
            portfolioPage.enterAddress2(data.get("Address 2"));
            portfolioPage.enterCity(data.get("City"));
            portfolioPage.enterState(data.get("State"));
            portfolioPage.enterZipCode(data.get("Zip Code"));
            portfolioPage.enterCountry(data.get("Country"));
        } else {
            throw new RuntimeException("No more test data found for scenario: " + currentScenario.getName());
        }
    }

    @And("User Click on the Next button") // Must match feature file EXACTLY
    public void click_next_button() {
        portfolioPage.clickOnNextButton();
    }

    @And("user Display the New Portfolio Created and Click on the OK button")
    public void verify_portfolio_creation_and_click_ok() {
        Assert.assertEquals(portfolioPage.getNewPortfolioCreatedMessage(), "New Portfolio Created");
        Assert.assertEquals(portfolioPage.getSuccessMessage(), "Success");
        portfolioPage.clickOnOkButton();
    }
    @io.cucumber.java.After(order = 1)
    public void afterScenario() {

        if (testData != null) {
            if (dataRowIndex < testData.size() - 1) {
                dataRowIndex++;
            } else {
                dataRowIndex = 0;
            }
        }
    }

    @Given("load the data from {string} and {string} excel sheet")
    public void load_the_data_from_and_excel_sheet(String File, String string2) {
        try {
            String sheet = string2;
            String scenarioName = currentScenario.getName();
            String filePath = File;
            //testData = ExcelUtility.getData(filePath, scenarioName);
            testData = ExcelUtility.getData(filePath, sheet);
            if (testData == null) {
                throw new RuntimeException("No test data found for scenario: " + scenarioName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*@Given("load the data from excel sheet")
    public void load_the_data_from_excel_sheet(){
        getPortfolioData();
    }*/

    @Then("Click on the {string} button")
    public void click_on_the_Selected_Portfolio_button(String portfolioName) throws InterruptedException {

        portfolioPage.ClickOnTheRowsPerPage();
        portfolioPage.waitforTableToLoad();
        pages = portfolioPage.GetDisplayedRowsCount();
       aa: for (int i = 0; i < pages; i++) {

            List<WebElement> portfolisName = portfolioPage.ListOfPortfoliosNames();
            portfolioPage.waitforTableToLoad();
           BB: for (WebElement element : portfolisName) {
                if (element.getText().equalsIgnoreCase(portfolioName)) {
                    element.click();
                    break aa;
                }
            }
            if (portfolioPage.isDisplayedNextButtonTable()) {
                portfolioPage.ClickOnNextButtonTable();
            }


        }

    }
    @DataProvider(name = "portfolioData")
    public Object[][] getPortfolioData() throws IOException {
        String filePath = "D:\\batch264\\Yvooa\\src\\test\\resources\\testdata.xlsx";
        List<Map<String, String>> dataList = ExcelUtility.getData(filePath, "Portfolio Creation");

        // Convert List<Map> to Object[][]
        Object[][] testData = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            testData[i][0] = dataList.get(i);
        }
        return testData;
    }
    @Given("load the data from the file {string} and {string} excel sheet")
    public void load_the_data_from_the_file_and_excel_sheet(String string, String string2) throws IOException, InterruptedException {
//        driver = DriverManager.getDriver();
        driver.manage().deleteAllCookies();
        DriverManager.getDriver().get(TestEnvironment.getBaseUrl() + "/login");
        List<Map<String, String>> excelData = ExcelUtility.getData(string, string2);

        for (Map<String, String> rowData : excelData) {
            System.out.println("Running for: " + rowData);

            loginPage = new LoginPage(new CommonActions(), new WaitUtility());
            Thread.sleep(2000);
            loginPage = new LoginPage(new CommonActions(), new WaitUtility());
            loginPage.enterEmail(rowData.get("username"));
            loginPage.enterPassword(rowData.get("password"));
            loginPage.clickLoginButton();
            dashboardPage = new DashboardPage(new CommonActions(), new WaitUtility());
            dashboardPage.ClickOnPortfolio();
            portfolioPage = new PortfolioPage(new CommonActions(), new WaitUtility());
//            driver = DriverManager.getDriver();
            Assert.assertEquals(driver.getCurrentUrl(), "https://pm-uat.yvooa.com/portfolio");
            portfolioPage.waitforTableToLoad();
            portfolioPage.clickAddNewPortfolio();
            portfolioPage.LoaderDisapear();
            portfolioPage.enterPhoneNumber(rowData.get("Portfoilo_Owner_Contact_Number"));
            portfolioPage.enterPortfolioName(rowData.get("portfolioName"));
            portfolioPage.SelectPortfolioType(rowData.get("portfolioType"));
            portfolioPage.entereinSsnInput(rowData.get("EIN No"));
            portfolioPage.enterEmail(rowData.get("Portfolio Owner Email ID"));
            portfolioPage.clickOnNextButton();
            portfolioPage.enterAddress1(rowData.get("Address 1"));
            portfolioPage.enterAddress2(rowData.get("Address 2"));
            portfolioPage.enterCity(rowData.get("City"));
            portfolioPage.enterState(rowData.get("State"));
            portfolioPage.enterZipCode(rowData.get("Zip Code"));
            portfolioPage.enterCountry(rowData.get("Country"));
            portfolioPage.clickOnNextButton();
            portfolioPage.clickOnConfirmButton();
            Assert.assertEquals(portfolioPage.getNewPortfolioCreatedMessage(), "New Portfolio Created");
            Assert.assertEquals(portfolioPage.getSuccessMessage(), "Success");
            portfolioPage.clickOnOkButton();
            // Quit the driver and clean up resources

    }
        DriverManager.quitDriver();
    }

}

