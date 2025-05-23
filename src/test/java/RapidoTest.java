import com.twilio.Twilio;

import com.twilio.rest.api.v2010.account.Message;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.twilio.base.ResourceSet;


import java.time.Duration;

public class RapidoTest {

        public static void main(String[] args) throws InterruptedException {
            // Initialize WebDriver
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("https://m.rapido.bike/home");
            Thread.sleep(5000);
            driver.findElement(By.xpath("//input[@name='phonenumber']")).sendKeys("7416472820");
            ////button[text()='Next']
            driver.findElement(By.xpath("//button[normalize-space()='Next']")).click();

            // Step 3: Retrieve OTP via API (Example using Twilio)
            String otp = getOTPFromSMSAPI(); // Implement your API logic here
            System.out.println(otp);
//
//            // Step 4: Enter OTP
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("c_hq0s9sxawgama2d0zp3")));
            driver.findElement(By.id("otpField")).sendKeys(otp);
            driver.findElement(By.name("pickup")).sendKeys("Opposite ESI Bus Stop, Sanath Nagar, BK Guda, Erragadda, Hyderabad, Telangana 500038, India");
            driver.findElement(By.xpath("div[class='container p-3'] div:nth-child(1) div:nth-child(1) div:nth-child(2) div:nth-child(1)")).click();
            driver.findElement(By.name("drop")).sendKeys("HGH Residency, Lakdikapul, Hyderabad, Telangana 500004, India");
            driver.findElement(By.xpath("//div[@class='address1']")).click();
            driver.findElement(By.xpath("//div[@class='card-wrap selected']")).click();
            driver.findElement(By.xpath("//button[normalize-space()='Book Cab Economy']")).click();


        }

    public static String getOTPFromSMSAPI() {
        // Initialize Twilio with your credentials
        Twilio.init("ACf01f4167bd61c06ebc87e22fba1e765c", "ba3f03372265ea6978df8590ba11c650");

        // Fetch messages sent to your Twilio number
        ResourceSet<Message> messages = Message.reader().read();

        // Iterate through messages to find the latest OTP
        String otp = "";
        for (Message message : messages) {
            if (message.getTo().equals("+917416472820")) { // Your test phone number
                otp = extractOTP(message.getBody());
                break; // Stop after finding the latest OTP
            }
        }
        return otp;
    }

    // Helper method to extract OTP from SMS body
    private static String extractOTP(String smsBody) {
        return smsBody.replaceAll("[^0-9]", "").substring(0, 6); // Extract first 6 digits
    }
    }

