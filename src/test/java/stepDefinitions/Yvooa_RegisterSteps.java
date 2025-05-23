package stepDefinitions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import config.DriverManager;
import config.TestEnvironment;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.SignUPPage;
import utilities.CommonActions;
import utilities.WaitUtility;

public class Yvooa_RegisterSteps {
    // Instance variables
   private SignUPPage signUPPage;
    public String generatedEmail;
    public static  String mail;
    public static WebDriver driver;
    public WaitUtility wait;
    String emailText;
    public Yvooa_RegisterSteps() {
        // Initialize PageFactory elements using the current WebDriver
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    @Given("User is on the Sign Up page of Yuvoo website")
    public void user_is_on_sign_up_page() throws InterruptedException,Exception{
        driver = (RemoteWebDriver) DriverManager.getDriver();
        // Navigate to the Sign Up page using the base URL from configuration
        Thread.sleep(5000);
     driver.get(TestEnvironment.getBaseUrl() + "/SignUP");
        // Initialize the page object with injected dependencies
        signUPPage = new SignUPPage(new CommonActions(), new WaitUtility());
        // Open a new tab for temporary email service
        driver.switchTo().newWindow(WindowType.TAB);
        Thread.sleep(5000);
        driver.navigate().to("https://temp-mail.org/en/");
        // Create a FluentWait instance for waiting for the email element
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        wait = new WaitUtility();

        WebElement emailElement = fluentWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("mail")));
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.id("mail")));
        fluentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("mail")));
        wait.waitForElementToBeVisible(emailElement);
        // Scroll into view and retrieve the generated email
        JavascriptExecutor js = (JavascriptExecutor) driver;
       js.executeScript("arguments[0].scrollIntoView();", emailElement);
       /* mail = emailElement.getDomAttribute("value");
        System.out.println("Generated email: " + mail);
        Thread.sleep(5000);
        emailText = emailElement.getDomAttribute("value");
         System.out.println("Email text: " + emailText);
        Assert.assertEquals(emailText, mail);*/

        //(//span[text()='Copy']//parent::button)[2]
        wait.waitForPageLoad();
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Copy']//parent::button")));
        WebElement Copy = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='btn-rds icon-btn bg-theme click-to-copy copyIconGreenBtn']")));
        Copy.click();
     mail = getClipboardContents();
        System.out.println("Copied Temporary Email: " +mail);

        // Switch back to the Sign Up tab
        Set<String> handles = driver.getWindowHandles();
        List<String> tabs = new ArrayList<>(handles);
        driver.switchTo().window(tabs.get(0));
    }

    @When("User enters valid registration details")
    public void user_enters_valid_registration_details(DataTable dataTable) {
        // Convert the DataTable to a list of maps for easy access
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        // Assuming one row of user data in the table
        Map<String, String> userData = new HashMap<>(rows.get(0));

        // Check for placeholder and generate random email if needed
        String emailValue = userData.get("Email");
        if (emailValue != null && emailValue.equals("[random]")) {
            generatedEmail = mail;
            userData.put("Email", generatedEmail);
        } else {
            generatedEmail = emailValue; // use the provided email as-is
        }

        signUPPage.enterFirstName(userData.get("First Name"));
        signUPPage.enterLastName(userData.get("Last Name"));
        signUPPage.enterEmail(mail);
        signUPPage.enterPhoneNumber(userData.get("Contact Number"));
        signUPPage.enterCompanyName(userData.get("Company Name"));
        signUPPage.enterAddress1(userData.get("Address 1"));
        signUPPage.enterAddress2(userData.get("Address 2"));
        signUPPage. enterCity(userData.get("City"));
        signUPPage.enterZipCode(userData.get("Zip Code"));
        signUPPage.enterPassword(userData.get("Password"));
        signUPPage.enterConfirmPassword(userData.get("Confirm Password"));


    }
    @And("select Dropdowns Product Type,state,country")
    public void select_dropdowns() {
        signUPPage.selectRandomProductType();
        signUPPage.selectRandomCountry();
        signUPPage.selectRandomState();

    }
    @And("User Click on the  Terms & Conditions")
    public void click_terms_conditions() {
      signUPPage.clickTermsAndConditionsCheckbox();
    }

    @And("User clicks on the Sign Up button")
    public void click_sign_up_button() {
        signUPPage.clickSignUpButton();

        if (signUPPage.isEmailCodeDisplayed()) {
            Set<String> handles = driver.getWindowHandles();
            List<String> tabs = new ArrayList<>(handles);
            driver.switchTo().window(tabs.get(1));
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(NoSuchElementException.class);
            WebElement message=fluentWait.until(ExpectedConditions.elementToBeClickable(By.linkText("Your verification code")));
           ////div[@class='inbox-empty-msg']
            if(message.isDisplayed()){
                //driver.executeScript("arguments[0].scrollIntoView();", message);
                wait.waitForElementToBeVisible(message);
                //wait.waitForElementToBeClickable(message);
                JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("arguments[0].click();", message);
            }
            WebElement OtpElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Your confirmation code is')]")));
            String OptElementText = OtpElement.getText();
            String Otp = OptElementText.replaceAll("[^0-9]", "");
            System.out.println("OTP: " + Otp);
            driver.switchTo().window(tabs.get(0));
            signUPPage.EnterEmailCode(Otp);
            signUPPage.ClickConfirmButton();
            Assert.assertEquals(fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("swal-text"))).getText(), "Verification Successful");
            fluentWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='swal-button-container']//button"))).click();
        }
    }

        @Then("User should be successfully registered on Yuvoo website")
        public void verify_successful_registration () {

        }
    // Function to get clipboard content
    public static String getClipboardContents() throws Exception {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        return (String) clipboard.getData(DataFlavor.stringFlavor);
    }
    }

