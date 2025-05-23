package Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;

public class ChaosEngineeringUtility {
    private WebDriver driver;
    private Random random;

    // Method to simulate random chaos like page crashes, network issues, or other failures
    private void simulateRandomFailure() {
        // Randomly decide whether to simulate a failure
        int failureType = random.nextInt(6);

        switch (failureType) {
            case 0:
                simulatePageCrash();
                break;
            case 1:
                simulateNetworkFailure();
                break;
            case 2:
                simulateSlowdown();
                break;
            case 3:
                simulateKeyboardFailure();
                break;
            case 4:
                simulateMouseDisruption();
                break;
            case 5:
                simulateUnexpectedQuit();
                break;
        }
    }

    // Simulate a browser crash (by closing the browser randomly)
    private void simulatePageCrash() {
        if (random.nextInt(10) == 0) { // 10% chance of triggering a crash
            System.out.println("Simulating page crash...");
            driver.quit(); // Close the browser randomly
            System.exit(0); // Exit the test, as if the browser crashed
        }
    }

    // Simulate network failure (simply wait for some time to simulate a delay)
    private void simulateNetworkFailure() {
        if (random.nextInt(10) == 0) { // 10% chance of network failure
            System.out.println("Simulating network failure...");
            try {
                Thread.sleep(5000); // Wait for 5 seconds to simulate a network delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulate random slowdowns by adding delays
    private void simulateSlowdown() {
        if (random.nextInt(5) == 0) { // 20% chance of slowing down actions
            System.out.println("Simulating slowdowns...");
            try {
                Thread.sleep(2000); // 2 seconds delay to simulate slow response
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulate a keyboard failure (by randomly typing an incorrect key or not typing)
    private void simulateKeyboardFailure() {
        if (random.nextInt(10) == 0) { // 10% chance of keyboard failure
            System.out.println("Simulating keyboard failure...");
            // Simulating a failed attempt to type
            try {
                WebElement searchBox = driver.findElement(By.name("q"));
                searchBox.sendKeys("Selenium Chaos");
                Thread.sleep(1000); // Simulate a failed key press by adding a pause
                searchBox.clear(); // Clear the input box as if a failure happened
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulate a mouse disruption (clicking somewhere randomly or failing to click)
    private void simulateMouseDisruption() {
        if (random.nextInt(10) == 0) { // 10% chance of mouse disruption
            System.out.println("Simulating mouse disruption...");
            Actions actions = new Actions(driver);
            WebElement randomElement = driver.findElement(By.name("q"));
            actions.moveToElement(randomElement).click().perform(); // Simulate click
            try {
                Thread.sleep(1000); // Simulate a pause before clicking (failure)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulate an unexpected quit of the browser (to test the system's ability to recover)
    private void simulateUnexpectedQuit() {
        if (random.nextInt(15) == 0) { // 6.67% chance of unexpected quit
            System.out.println("Simulating unexpected browser quit...");
            driver.quit(); // Close the browser randomly as if it unexpectedly quit
            System.exit(0); // Exit the test to simulate a crash
        }
    }


}
