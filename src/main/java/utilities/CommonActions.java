package utilities;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v132.emulation.Emulation;
import org.openqa.selenium.devtools.v132.network.Network;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import reporting.Log4jManager;

/**
 * CommonActions provides utility methods to interact with web elements using Selenium WebDriver.
 * It includes actions like click, type, select from dropdowns, handle alerts, and more.
 * This class ensures robust interactions with proper waiting mechanisms and logging.
 */
public class CommonActions {

    private static final long DEFAULT_TIMEOUT = 80; // seconds
    private static final long POLLING_INTERVAL = 1; // milliseconds

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    /**
     * Constructor initializes WebDriver, WebDriverWait, and Actions instances.
     */
    public CommonActions() {
        this.driver = config.DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL));
        this.wait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
        this.actions = new Actions(driver);
    }


    /**
     * Clicks on the specified WebElement after waiting for it to be clickable.
     *
     * @param element The WebElement to be clicked.
     */
    public void clickElement(WebElement element) {
        try {
            scrollToElement(element);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            highlightElement(element);
            element.click();
            Log4jManager.info("Clicked on element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element not clickable after waiting: " + getElementDescription(element));
            takeScreenshot("screenshots/clickElement_timeout.png");
            throw e;
        } catch (ElementClickInterceptedException e) {
            Log4jManager.error("Element click intercepted: " + getElementDescription(element));
            takeScreenshot("screenshots/clickElement_intercepted.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to click on element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/clickElement_error.png");
            throw e;
        }
    }
    public void scrollToElementCenter(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
            WaitUtility.wait(100);
            Log4jManager.info("Scrolled to element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to scroll to element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollToElement_error.png");
            throw e;
        }
    }
    public void forceClickElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {" +
                "view: window," +
                "bubbles: true," +
                "cancelable: true" +
                "}))", element);
    }


    /**
     * Types the specified text into the given WebElement after waiting for its visibility.
     *
     * @param element The WebElement where text is to be entered.
     * @param text    The text to enter.
     */
    public void typeText(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            element.clear();
            element.sendKeys(text);
            Log4jManager.info("Typed text '" + text + "' into element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element not visible after waiting: " + getElementDescription(element));
            takeScreenshot("screenshots/typeText_timeout.png");
            throw e;
        } catch (ElementNotInteractableException e) {
            Log4jManager.error("Element not interactable for typing: " + getElementDescription(element));
            takeScreenshot("screenshots/typeText_notInteractable.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to type text into element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/typeText_error.png");
            throw e;
        }
    }
    //keys
    public void typeKeys(WebElement element, Keys key) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            element.sendKeys(key);
            Log4jManager.info("Typed key '" + key + "' into element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element not visible after waiting: " + getElementDescription(element));
            takeScreenshot("screenshots/typeKeys_timeout.png");
            throw e;
        } catch (ElementNotInteractableException e) {
            Log4jManager.error("Element not interactable for typing: " + getElementDescription(element));
            takeScreenshot("screenshots/typeKeys_notInteractable.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to type key into element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/typeKeys_error.png");
            throw e;
        }
    }

    /**
     * Retrieves the text from the specified WebElement after waiting for its visibility.
     *
     * @param element The WebElement from which text is to be retrieved.
     * @return The text of the element.
     */
    public String getElementText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            String text = element.getText();
            Log4jManager.info("Retrieved text '" + text + "' from element: " + getElementDescription(element));
            return text;
        } catch (TimeoutException e) {
            Log4jManager.error("Element not visible after waiting: " + getElementDescription(element));
            takeScreenshot("./screenshots/getElementText_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to get text from element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/getElementText_error.png");
            throw e;
        }
    }

    /**
     * Selects an option from a dropdown WebElement by visible text.
     *
     * @param dropdownElement The dropdown WebElement.
     * @param visibleText     The visible text of the option to select.
     */
    public void selectByVisibleText(WebElement dropdownElement, String visibleText) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            highlightElement(dropdownElement);
            Select select = new Select(dropdownElement);
            select.selectByVisibleText(visibleText);
            Log4jManager.info("Selected option '" + visibleText + "' from dropdown: " + getElementDescription(dropdownElement));
        } catch (NoSuchElementException e) {
            Log4jManager.error("Option '" + visibleText + "' not found in dropdown: " + getElementDescription(dropdownElement));
            takeScreenshot("screenshots/selectByVisibleText_noSuchElement.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to select by visible text in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectByVisibleText_error.png");
            throw e;
        }
    }
   /* //select random from select dropdown
    public void selectRandomFromDropdown(WebElement element) {
        Select select = new Select(element);
        List<WebElement> options = select.getOptions();
        int index = (int) (Math.random() * (options.size() - 1)) + 1;
        select.selectByIndex(index);
    }
  /* public void selectRandomEnabledFromDropdown(WebElement dropdown) {
       Select select = new Select(dropdown);
       List<WebElement> options = select.getOptions();

       // Collect indexes of enabled options
       List<Integer> enabledIndexes = new ArrayList<>();
       for (int i = 0; i < options.size(); i++) {
           WebElement option = options.get(i);
           if (option.isEnabled() && !option.getDomAttribute("disabled").equalsIgnoreCase("true")) {
               enabledIndexes.add(i);
           }
       }

       // Handle case with no enabled options
       if (enabledIndexes.isEmpty()) {
           throw new RuntimeException("No enabled options available in dropdown");
       }

       // Select random enabled option using original index
       int randomIndex = ThreadLocalRandom.current().nextInt(enabledIndexes.size());
       select.selectByIndex(enabledIndexes.get(randomIndex));
   }*/

    public void selectRandomFromDropdown (WebElement dropdown) {
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();
        // Collect indexes of enabled options
        List<Integer> enabledIndexes = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);
            if (option.isEnabled()) {
                enabledIndexes.add(i);
            }
        }

        // Handle cases where only the placeholder exists
        if (enabledIndexes.isEmpty()) {
            throw new RuntimeException("Dropdown has no selectable options");
        }

        // Select random enabled option using original index
        int randomIndex = ThreadLocalRandom.current().nextInt(enabledIndexes.size());
        select.selectByIndex(enabledIndexes.get(randomIndex));
    }
    // Optional: Get selected value for validation
    public String getSelectedOptionTextString(WebElement dropdown) {
        return new Select(dropdown).getFirstSelectedOption().getText();
    }
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    /**
     * Selects an option from a dropdown WebElement by value.
     *
     * @param dropdownElement The dropdown WebElement.
     * @param value           The value attribute of the option to select.
     */
    public void selectByValue(WebElement dropdownElement, String value) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            highlightElement(dropdownElement);
            Select select = new Select(dropdownElement);
            select.selectByValue(value);
            Log4jManager.info("Selected option with value '" + value + "' from dropdown: " + getElementDescription(dropdownElement));
        } catch (NoSuchElementException e) {
            Log4jManager.error("Option with value '" + value + "' not found in dropdown: " + getElementDescription(dropdownElement));
            takeScreenshot("screenshots/selectByValue_noSuchElement.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to select by value in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectByValue_error.png");
            throw e;
        }
    }

    /**
     * Selects an option from a dropdown WebElement by index.
     *
     * @param dropdownElement The dropdown WebElement.
     * @param index           The index of the option to select (0-based).
     */
    public void selectByIndex(WebElement dropdownElement, int index) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            highlightElement(dropdownElement);
            Select select = new Select(dropdownElement);
            // Wait for options to be present
            wait.until(d -> !select.getOptions().isEmpty());
            if(select.getOptions().size() > index) {
                scrollToElement(dropdownElement);
                highlightElements(select.getOptions());
            }
            select.selectByIndex(index);
            Log4jManager.info("Selected option at index '" + index + "' from dropdown: " + getElementDescription(dropdownElement));
        } catch (NoSuchElementException e) {
            Log4jManager.error("Option at index '" + index + "' not found in dropdown: " + getElementDescription(dropdownElement));
            takeScreenshot("screenshots/selectByIndex_noSuchElement.png");
            throw e;
        } catch (InvalidSelectorException  ee){
            Log4jManager.error("Failed to select by index in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + ee.getMessage());
            takeScreenshot("screenshots/selectByIndex_error.png");
            throw new RuntimeException("Invalid selector: " + ee.getMessage(), ee);
        } catch (Exception e) {
            Log4jManager.error("Failed to select by index in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectByIndex_error.png");
            throw e;
        }
    }



    /**
     * Selects an option from a dropdown WebElement using partial visible text.
     *
     * @param dropdownElement The dropdown WebElement.
     * @param partialText     The partial visible text of the option to select.
     */
    public void selectByPartialVisibleText(WebElement dropdownElement, String partialText) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            highlightElement(dropdownElement);
            Select select = new Select(dropdownElement);
            List<WebElement> options = select.getOptions();
            boolean found = false;
            for (WebElement option : options) {
                if (option.getText().contains(partialText)) {
                    select.selectByVisibleText(option.getText());
                    Log4jManager.info("Selected option containing text '" + partialText + "' from dropdown: " + getElementDescription(dropdownElement));
                    found = true;
                    break;
                }
            }
            if (!found) {
                Log4jManager.error("No option containing text '" + partialText + "' found in dropdown: " + getElementDescription(dropdownElement));
                takeScreenshot("screenshots/selectByPartialVisibleText_noMatch.png");
                throw new NoSuchElementException("No option containing text '" + partialText + "' found.");
            }
        } catch (Exception e) {
            Log4jManager.error("Failed to select by partial visible text in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectByPartialVisibleText_error.png");
            throw e;
        }
    }
    public void clickElementUsingJS(WebElement element) {
        try {
            scrollToElement(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
            Log4jManager.info("Clicked on element using JavaScript: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to click on element using JavaScript: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/clickElementUsingJS_error.png");
            throw e;
        }
    }

    /**
     * Selects an option from a dropdown WebElement using keyboard keys.
     *
     * @param dropdownElement The dropdown WebElement.
     * @param visibleText     The visible text of the option to select.
     */
    public void selectByKeyboard(WebElement dropdownElement, String visibleText) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownElement));
            highlightElement(dropdownElement);
            dropdownElement.click();
            for (char c : visibleText.toCharArray()) {
                String key = String.valueOf(c);
                dropdownElement.sendKeys(key);
                Thread.sleep(100); // Small pause between key presses
            }
            dropdownElement.sendKeys(Keys.ENTER);
            Log4jManager.info("Selected option using keyboard with text '" + visibleText + "' from dropdown: " + getElementDescription(dropdownElement));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log4jManager.error("Thread interrupted while selecting by keyboard: " + e.getMessage());
        } catch (Exception e) {
            Log4jManager.error("Failed to select by keyboard in dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectByKeyboard_error.png");
            throw e;
        }
    }

    /**
     * Retrieves the currently selected option's text from a dropdown WebElement.
     *
     * @param dropdownElement The dropdown WebElement.
     * @return The text of the currently selected option.
     */
    public String getSelectedOptionText(WebElement dropdownElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(dropdownElement));
            Select select = new Select(dropdownElement);
            WebElement selectedOption = select.getFirstSelectedOption();
            highlightElement(selectedOption);
            String selectedText = selectedOption.getText();
            Log4jManager.info("Selected option text: '" + selectedText + "' from dropdown: " + getElementDescription(dropdownElement));
            return selectedText;
        } catch (Exception e) {
            Log4jManager.error("Failed to get selected option text from dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/getSelectedOptionText_error.png");
            throw e;
        }
    }

    /**
     * Retrieves the currently selected option's value from a dropdown WebElement.
     *
     * @param dropdownElement The dropdown WebElement.
     * @return The value of the currently selected option.
     */
    public String getSelectedOptionValue(WebElement dropdownElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(dropdownElement));
            Select select = new Select(dropdownElement);
            WebElement selectedOption = select.getFirstSelectedOption();
            highlightElement(selectedOption);
            String selectedValue = selectedOption.getDomAttribute("value");
            Log4jManager.info("Selected option value: '" + selectedValue + "' from dropdown: " + getElementDescription(dropdownElement));
            return selectedValue;
        } catch (Exception e) {
            Log4jManager.error("Failed to get selected option value from dropdown: " + getElementDescription(dropdownElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/getSelectedOptionValue_error.png");
            throw e;
        }
    }

    /**
     * Hover over the specified WebElement using Actions class.
     *
     * @param element The WebElement to hover over.
     */
    public void hoverOverElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            actions.moveToElement(element).perform();
            Log4jManager.info("Hovered over element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to hover over element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/hoverOverElement_error.png");
            throw e;
        }
    }

    /**
     * Performs a right-click (context click) on the specified WebElement.
     *
     * @param element The WebElement to right-click on.
     */
    public void rightClickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            actions.contextClick(element).perform();
            Log4jManager.info("Right-clicked on element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to right-click on element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/rightClickElement_error.png");
            throw e;
        }
    }

    /**
     * Performs a double-click on the specified WebElement.
     *
     * @param element The WebElement to double-click on.
     */
    public void doubleClickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            highlightElement(element);
            actions.doubleClick(element).perform();
            Log4jManager.info("Double-clicked on element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to double-click on element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/doubleClickElement_error.png");
            throw e;
        }
    }

    /**
     * Drags the source WebElement and drops it onto the target WebElement.
     *
     * @param sourceElement The WebElement to drag.
     * @param targetElement The WebElement to drop onto.
     */
    public void dragAndDropElement(WebElement sourceElement, WebElement targetElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(sourceElement));
            wait.until(ExpectedConditions.visibilityOf(targetElement));
            highlightElement(sourceElement);
            highlightElement(targetElement);
            actions.dragAndDrop(sourceElement, targetElement).perform();
            Log4jManager.info("Dragged element: " + getElementDescription(sourceElement) +
                    " and dropped onto: " + getElementDescription(targetElement));
        } catch (Exception e) {
            Log4jManager.error("Failed to drag and drop elements. Error: " + e.getMessage());
            takeScreenshot("screenshots/dragAndDropElement_error.png");
            throw e;
        }
    }

    /**
     * Performs a drag-and-drop action using JavaScript for more reliability.
     *
     * @param source The source WebElement to drag.
     * @param target The target WebElement to drop onto.
     */
    public void dragAndDropUsingJS(WebElement source, WebElement target) {
        try {
            String script = "function createEvent(typeOfEvent) {\n" +
                    "   var event = document.createEvent(\"CustomEvent\");\n" +
                    "   event.initCustomEvent(typeOfEvent, true, true, null);\n" +
                    "   event.dataTransfer = {\n" +
                    "       data: {},\n" +
                    "       setData: function(key, value) {\n" +
                    "           this.data[key] = value;\n" +
                    "       },\n" +
                    "       getData: function(key) {\n" +
                    "           return this.data[key];\n" +
                    "       }\n" +
                    "   };\n" +
                    "   return event;\n" +
                    "}\n" +
                    "\n" +
                    "function dispatchEvent(element, event, transferData) {\n" +
                    "   if (transferData !== undefined) {\n" +
                    "       event.dataTransfer = transferData;\n" +
                    "   }\n" +
                    "   if (element.dispatchEvent) {\n" +
                    "       element.dispatchEvent(event);\n" +
                    "   } else if (element.fireEvent) {\n" +
                    "       element.fireEvent(\"on\" + event.type, event);\n" +
                    "   }\n" +
                    "}\n" +
                    "\n" +
                    "var sourceElement = arguments[0];\n" +
                    "var targetElement = arguments[1];\n" +
                    "\n" +
                    "var dragStartEvent = createEvent('dragstart');\n" +
                    "dispatchEvent(sourceElement, dragStartEvent);\n" +
                    "\n" +
                    "var dropEvent = createEvent('drop');\n" +
                    "dispatchEvent(targetElement, dropEvent, dragStartEvent.dataTransfer);\n" +
                    "\n" +
                    "var dragEndEvent = createEvent('dragend');\n" +
                    "dispatchEvent(sourceElement, dragEndEvent, dragStartEvent.dataTransfer);";
            ((JavascriptExecutor) driver).executeScript(script, source, target);
            Log4jManager.info("Performed drag and drop using JavaScript on elements: " +
                    getElementDescription(source) + " -> " + getElementDescription(target));
        } catch (Exception e) {
            Log4jManager.error("Failed to perform drag and drop using JavaScript. Error: " + e.getMessage());
            takeScreenshot("screenshots/dragAndDropUsingJS_error.png");
            throw e;
        }
    }

    /**
     * Accepts the currently displayed alert.
     */
    public void acceptAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            highlightAlert();
            alert.accept();
            Log4jManager.info("Accepted alert.");
        } catch (NoAlertPresentException e) {
            Log4jManager.error("No alert present to accept.");
            takeScreenshot("screenshots/acceptAlert_noAlert.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to accept alert. Error: " + e.getMessage());
            takeScreenshot("screenshots/acceptAlert_error.png");
            throw e;
        }
    }

    /**
     * Dismisses the currently displayed alert.
     */
    public void dismissAlert() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            highlightAlert();
            alert.dismiss();
            Log4jManager.info("Dismissed alert.");
        } catch (NoAlertPresentException e) {
            Log4jManager.error("No alert present to dismiss.");
            takeScreenshot("screenshots/dismissAlert_noAlert.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to dismiss alert. Error: " + e.getMessage());
            takeScreenshot("screenshots/dismissAlert_error.png");
            throw e;
        }
    }

    /**
     * Retrieves the text from the currently displayed alert.
     *
     * @return The text of the alert.
     */
    public String getAlertText() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            Log4jManager.info("Retrieved alert text: " + alertText);
            return alertText;
        } catch (NoAlertPresentException e) {
            Log4jManager.error("No alert present to retrieve text.");
            takeScreenshot("screenshots/getAlertText_noAlert.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to get alert text. Error: " + e.getMessage());
            takeScreenshot("screenshots/getAlertText_error.png");
            throw e;
        }
    }

    /**
     * Sends text to a prompt alert and accepts it.
     *
     * @param text The text to send to the alert.
     */
    public void sendTextToAlert(String text) {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            highlightAlert();
            alert.sendKeys(text);
            alert.accept();
            Log4jManager.info("Sent text '" + text + "' to alert and accepted it.");
        } catch (NoAlertPresentException e) {
            Log4jManager.error("No alert present to send text.");
            takeScreenshot("screenshots/sendTextToAlert_noAlert.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to send text to alert. Error: " + e.getMessage());
            takeScreenshot("screenshots/sendTextToAlert_error.png");
            throw e;
        }
    }

    /**
     * Switches to a specific frame by index.
     *
     * @param index The index of the frame to switch to.
     */
    public void switchToFrame(int index) {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
            Log4jManager.info("Switched to frame with index: " + index);
        } catch (NoSuchFrameException e) {
            Log4jManager.error("No frame found with index: " + index);
            takeScreenshot("screenshots/switchToFrame_index_noFrame.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to switch to frame by index. Error: " + e.getMessage());
            takeScreenshot("screenshots/switchToFrame_index_error.png");
            throw e;
        }
    }

    /**
     * Switches to a specific frame by name or ID.
     *
     * @param nameOrId The name or ID of the frame to switch to.
     */
    public void switchToFrame(String nameOrId) {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
            Log4jManager.info("Switched to frame with name/ID: " + nameOrId);
        } catch (NoSuchFrameException e) {
            Log4jManager.error("No frame found with name/ID: " + nameOrId);
            takeScreenshot("screenshots/switchToFrame_nameOrId_noFrame.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to switch to frame by name/ID. Error: " + e.getMessage());
            takeScreenshot("screenshots/switchToFrame_nameOrId_error.png");
            throw e;
        }
    }

    /**
     * Switches to a frame using a WebElement reference.
     *
     * @param frameElement The WebElement representing the frame to switch to.
     */
    public void switchToFrame(WebElement frameElement) {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
            highlightElement(frameElement);
            Log4jManager.info("Switched to frame using WebElement: " + getElementDescription(frameElement));
        } catch (NoSuchFrameException e) {
            Log4jManager.error("No frame found with the provided WebElement: " + getElementDescription(frameElement));
            takeScreenshot("screenshots/switchToFrame_webelement_noFrame.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to switch to frame using WebElement. Error: " + e.getMessage());
            takeScreenshot("screenshots/switchToFrame_webelement_error.png");
            throw e;
        }
    }

    /**
     * Switches back to the default content from any frame.
     */
    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            Log4jManager.info("Switched back to default content.");
        } catch (Exception e) {
            Log4jManager.error("Failed to switch back to default content. Error: " + e.getMessage());
            takeScreenshot("screenshots/switchToDefaultContent_error.png");
            throw e;
        }
    }

    /**
     * Scrolls to the specified WebElement using JavaScript.
     *
     * @param element The WebElement to scroll to.
     */
    public void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Log4jManager.info("Scrolled to element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to scroll to element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollToElement_error.png");
            throw e;
        }
    }

    /**
     * Scrolls to the specified WebElement using keyboard actions.
     *
     * @param element The WebElement to scroll to.
     */
    public void scrollToElementUsingKeyboard(WebElement element) {
        try {
            actions.moveToElement(element).sendKeys(Keys.PAGE_DOWN).perform();
            Log4jManager.info("Scrolled to element using keyboard actions: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to scroll to element using keyboard actions: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollToElementUsingKeyboard_error.png");
            throw e;
        }
    }

    /**
     * Highlights the specified WebElement with a customizable border and background using JavaScript.
     *
     * @param element          The WebElement to highlight.
     * @param borderColor      The color of the border (e.g., "3px solid blue").
     * @param backgroundColor The background color (e.g., "yellow").
     */
    public void highlightElement(WebElement element, String borderColor, String backgroundColor) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Apply custom styles
            js.executeScript("arguments[0].style.border='" + borderColor + "';", element);
            js.executeScript("arguments[0].style.backgroundColor='" + backgroundColor + "';", element);
            // Optional: Add a shadow effect
            js.executeScript("arguments[0].style.boxShadow='0 0 10px " + borderColor + "';", element);
            // Pause to visualize the highlight
            Thread.sleep(500);
            // Revert to original style
            js.executeScript("arguments[0].style.border='';", element);
            js.executeScript("arguments[0].style.backgroundColor='';", element);
            js.executeScript("arguments[0].style.boxShadow='';", element);
            Log4jManager.info("Custom highlighted element: " + getElementDescription(element));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log4jManager.error("Thread interrupted while highlighting element: " + e.getMessage());
        } catch (Exception e) {
            Log4jManager.error("Failed to custom highlight element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Highlights a WebElement with a default red border and yellow background using JavaScript.
     *
     * @param element The WebElement to highlight.
     */
    public void highlightElement(WebElement element) {
        highlightElement(element, "3px solid red", "yellow");
    }

    /**
     * Highlights the currently active alert using JavaScript.
     * Note: Selenium does not support direct manipulation of alerts via JavaScript.
     * This method can be customized if custom alert handling is implemented.
     */
    private void highlightAlert() {
        try {
            // Selenium cannot interact with native alerts via JavaScript.
            // If using custom alerts (e.g., modal dialogs), implement highlighting accordingly.
            // Example for a custom alert:
            /*
            WebElement alertElement = driver.findElement(By.id("customAlert"));
            highlightElement(alertElement);
            */
            Log4jManager.info("Attempted to highlight alert (not directly supported by Selenium).");
        } catch (Exception e) {
            Log4jManager.error("Failed to highlight alert. Error: " + e.getMessage());
        }
    }

    /**
     * Uploads a file to a file input WebElement.
     *
     * @param fileInputElement The file input WebElement.
     * @param filePath         The absolute path of the file to upload.
     */
    public void uploadFile(WebElement fileInputElement, String filePath) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(getByFromWebElement(fileInputElement)));
            highlightElement(fileInputElement);
            fileInputElement.sendKeys(filePath);
            Log4jManager.info("Uploaded file '" + filePath + "' to element: " + getElementDescription(fileInputElement));
        } catch (Exception e) {
            Log4jManager.error("Failed to upload file to element: " + getElementDescription(fileInputElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/uploadFile_error.png");
            throw e;
        }
    }

    /**
     * Uploads a file using JavaScript (useful for hidden file inputs).
     *
     * @param fileInputElement The file input WebElement.
     * @param filePath         The absolute path of the file to upload.
     */
    public void uploadFileUsingJS(WebElement fileInputElement, String filePath) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.display='block';", fileInputElement);
            fileInputElement.sendKeys(filePath);
            js.executeScript("arguments[0].style.display='none';", fileInputElement);
            Log4jManager.info("Uploaded file '" + filePath + "' to element using JavaScript: " + getElementDescription(fileInputElement));
        } catch (Exception e) {
            Log4jManager.error("Failed to upload file using JavaScript to element: " + getElementDescription(fileInputElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/uploadFileUsingJS_error.png");
            throw e;
        }
    }

    /**
     * Retrieves the locator from a WebElement's attributes.
     *
     * @param element The WebElement.
     * @return The By locator based on available attributes.
     */
    private By getByFromWebElement(WebElement element) {
        String id = element.getDomAttribute("id");
        if (id != null && !id.isEmpty()) {
            return By.id(id);
        }
        String name = element.getDomAttribute("name");
        if (name != null && !name.isEmpty()) {
            return By.name(name);
        }
        // Attempt to extract XPath from the element's description
        String elementString = element.toString();
        if (elementString.contains("xpath:")) {
            String xpath = elementString.substring(elementString.indexOf("xpath:")).split("]")[0] + "]";
            return By.xpath(xpath);
        }
        // Fallback to CSS Selector or other strategies as needed
        return By.cssSelector("");
    }

    /**
     * Takes a screenshot of the current browser window and saves it to the specified path.
     *
     * @param filePath The path where the screenshot will be saved.
     */
    public void takeScreenshot(String filePath) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            org.openqa.selenium.io.FileHandler.copy(source, destination);
            Log4jManager.info("Screenshot taken and saved to: " + filePath);
        } catch (IOException e) {
            Log4jManager.error("Failed to take screenshot. Error: " + e.getMessage());
            throw new RuntimeException("Failed to take screenshot", e);
        }
    }

    /**
     * Retrieves a description of the WebElement for logging purposes.
     *
     * @param element The WebElement to describe.
     * @return A string description of the WebElement.
     */
    private String getElementDescription(WebElement element) {
        try {
            StringBuilder description = new StringBuilder();
            description.append("Tag: ").append(element.getTagName());
            String id = element.getDomAttribute("id");
            if (id != null && !id.isEmpty()) {
                description.append(", ID: ").append(id);
            }
            String className = element.getDomAttribute("class");
            if (className != null && !className.isEmpty()) {
                description.append(", Class: ").append(className);
            }
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                description.append(", Text: '").append(text).append("'");
            }
            return description.toString();
        } catch (StaleElementReferenceException e) {
            return "Stale element reference.";
        } catch (Exception e) {
            return "Unable to describe element.";
        }
    }

    /**
     * Waits for a WebElement to be invisible.
     *
     * @param element The WebElement to wait for its invisibility.
     */
    public void waitForElementToBeInvisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
            Log4jManager.info("Element is now invisible: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element did not become invisible within timeout: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeInvisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to wait for element to be invisible: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeInvisible_error.png");
            throw e;
        }
    }

    /**
     * Selects a checkbox or radio button WebElement.
     *
     * @param element The checkbox or radio button WebElement.
     */
    public void selectCheckboxOrRadio(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            if (!element.isSelected()) {
                highlightElement(element);
                element.click();
                Log4jManager.info("Selected checkbox/radio button: " + getElementDescription(element));
            } else {
                Log4jManager.info("Checkbox/radio button already selected: " + getElementDescription(element));
            }
        } catch (Exception e) {
            Log4jManager.error("Failed to select checkbox/radio button: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/selectCheckboxOrRadio_error.png");
            throw e;
        }
    }

    /**
     * Deselects a checkbox WebElement.
     *
     * @param element The checkbox WebElement to deselect.
     */
    public void deselectCheckbox(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            if (element.isSelected()) {
                highlightElement(element);
                element.click();
                Log4jManager.info("Deselected checkbox: " + getElementDescription(element));
            } else {
                Log4jManager.info("Checkbox already deselected: " + getElementDescription(element));
            }
        } catch (Exception e) {
            Log4jManager.error("Failed to deselect checkbox: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/deselectCheckbox_error.png");
            throw e;
        }
    }

    /**
     * Waits for the page to load completely.
     */
    public void waitForPageLoad() {
        try {
            ExpectedCondition<Boolean> pageLoadCondition = drv -> ((JavascriptExecutor) drv)
                    .executeScript("return document.readyState").equals("complete");
            wait.until(pageLoadCondition);
            Log4jManager.info("Page loaded completely.");
        } catch (TimeoutException e) {
            Log4jManager.error("Page did not load completely within timeout.");
            takeScreenshot("screenshots/waitForPageLoad_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to wait for page load. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForPageLoad_error.png");
            throw e;
        }
    }
    //lighting List of elements in the page using java script
    public void highlightElements(List<WebElement> elements) {
        try {
            for (WebElement element : elements) {
                highlightElement(element);
            }
        } catch (Exception e) {
            Log4jManager.error("Failed to highlight elements. Error: " + e.getMessage());
            takeScreenshot("screenshots/highlightElements_error.png");
            throw e;
        }
    }
    public void clickElementWithRetry(WebElement element, int maxAttempts, int delayMs) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                clickElement(element);
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                if(attempts == maxAttempts) throw e;
                WaitUtility.wait(delayMs);
            }
        }
    }
    public void moveToElement(WebElement element) {
        try {
            actions.moveToElement(element).perform();
            Log4jManager.info("Moved to element: " + getElementDescription(element));
        } catch (Exception e) {
            Log4jManager.error("Failed to move to element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/moveToElement_error.png");
            throw e;
        }
    }
    /**
     * Waits for and verifies file download in specified directory
     * @param fileName Partial or full file name
     * @param directory Download directory path
     * @param timeoutSeconds Timeout in seconds
     */
    public boolean verifyFileDownload(String fileName, String directory, int timeoutSeconds) {
        File dir = new File(directory);
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000);

        while (System.currentTimeMillis() < endTime) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().contains(fileName)){
                        Log4jManager.info("File found: " + file.getAbsolutePath());
                        return true;
                    }
                }
            }
            WaitUtility.wait(1000);
        }
        Log4jManager.error("File not found after " + timeoutSeconds + " seconds: " + fileName);
        takeScreenshot("screenshots/fileDownload_timeout.png");
        return false;
    }
    /**
     * Drags and drops an element by pixel offset
     * @param element Element to drag
     * @param xOffset Horizontal offset
     * @param yOffset Vertical offset
     */
    public void dragAndDropByOffset(WebElement element, int xOffset, int yOffset) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            highlightElement(element);
            actions.dragAndDropBy(element, xOffset, yOffset).perform();
            Log4jManager.info("Dragged element by offset [" + xOffset + "," + yOffset + "]");
        } catch (Exception e) {
            Log4jManager.error("Drag and drop by offset failed. Error: " + e.getMessage());
            takeScreenshot("screenshots/dragAndDropByOffset_error.png");
            throw e;
        }
    }
    /**
     * Handles browser authentication popup using URL credentials
     * @param url URL needing authentication
     * @param username Authentication username
     * @param password Authentication password
     */
    public void handleBasicAuth(String url, String username, String password) {
        try {
            String authUrl = url.replace("://", "://" + username + ":" + password + "@");
            driver.get(authUrl);
            Log4jManager.info("Handled basic authentication for URL: " + url);
        } catch (Exception e) {
            Log4jManager.error("Basic authentication failed. Error: " + e.getMessage());
            takeScreenshot("screenshots/basicAuth_error.png");
            throw e;
        }
    }
    /**
     * Checks if element is present in DOM (doesn't check visibility)
     * @param by Locator strategy
     */
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    /**
     * Performs vertical scroll by percentage of page height
     * @param percent Scroll percentage (0-100)
     */
    public void scrollByPercentage(int percent) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, document.body.scrollHeight * " + (percent/100.0) + ")");
            Log4jManager.info("Scrolled page by " + percent + "%");
        } catch (Exception e) {
            Log4jManager.error("Scroll by percentage failed. Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollByPercentage_error.png");
            throw e;
        }
    }
    public WebElement handleStaleElement(By by, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return driver.findElement(by);
            } catch (StaleElementReferenceException e) {
                Log4jManager.warn("Stale element detected, retrying...");
                attempts++;
            }
        }
        throw new StaleElementReferenceException("Element remains stale after " + maxRetries + " attempts");
    }
    //handleStaleElement using WebElement
    public WebElement handleStaleElement(WebElement element, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return element;
            } catch (StaleElementReferenceException e) {
                Log4jManager.warn("Stale element detected, retrying...");
                attempts++;
            }
        }
        throw new StaleElementReferenceException("Element remains stale after " + maxRetries + " attempts");
    }
    /**
     * Verifies text presence in page source with timeout
     * @param text Text to verify
     * @param timeoutSeconds Timeout in seconds
     */
    public boolean verifyTextPresentInPageSource(String text, long timeoutSeconds) {
        try {
            return wait.withTimeout(Duration.ofSeconds(timeoutSeconds))
                    .until(d -> driver.getPageSource().contains(text));
        } catch (TimeoutException e) {
            Log4jManager.error("Text not found in page source: " + text);
            takeScreenshot("screenshots/textNotPresent.png");
            return false;
        }
    }
    /**
     * Gets current browser user agent
     */
    public String getUserAgent() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return navigator.userAgent;");
    }
    /**
     * Verifies CSS property value of element
     * @param element Target element
     * @param property CSS property name
     * @param expectedValue Expected CSS value
     */
    public boolean verifyCssValue(WebElement element, String property, String expectedValue) {
        try {
            String actualValue = element.getCssValue(property);
            boolean result = actualValue.equals(expectedValue);
            Log4jManager.info(String.format("CSS verification: %s - Expected: %s, Actual: %s",
                    property, expectedValue, actualValue));
            return result;
        } catch (Exception e) {
            Log4jManager.error("CSS verification failed: " + e.getMessage());
            takeScreenshot("screenshots/cssVerification_error.png");
            throw e;
        }
    }
    /**
     * Simulates pressing a specific key on an element.
     *
     * @param element The WebElement to send the key to.
     * @param key     The key to press (e.g., Keys.ENTER, Keys.TAB).
     */
    public void pressKey(WebElement element, Keys key) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            element.sendKeys(key);
            Log4jManager.info("Pressed key '" + key.name() + "' on element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element not visible after waiting: " + getElementDescription(element));
            takeScreenshot("screenshots/pressKey_timeout.png");
            throw e;
        } catch (ElementNotInteractableException e) {
            Log4jManager.error("Element not interactable for pressing key: " + getElementDescription(element));
            takeScreenshot("screenshots/pressKey_notInteractable.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to press key '" + key.name() + "' on element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/pressKey_error.png");
            throw e;
        }
    }

    /**
     * Simulates pressing a sequence of keys on an element.
     *
     * @param element The WebElement to send the keys to.
     * @param keysToSend The sequence of keys to press.
     */
    public void pressKeys(WebElement element, CharSequence... keysToSend) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            highlightElement(element);
            element.sendKeys(keysToSend);
            Log4jManager.info("Pressed keys on element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Element not visible after waiting: " + getElementDescription(element));
            takeScreenshot("screenshots/pressKeys_timeout.png");
            throw e;
        } catch (ElementNotInteractableException e) {
            Log4jManager.error("Element not interactable for pressing keys: " + getElementDescription(element));
            takeScreenshot("screenshots/pressKeys_notInteractable.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to press keys on element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/pressKeys_error.png");
            throw e;
        }
    }
    /**
     * Gets the number of rows in a table represented by a WebElement.
     *
     * @param tableElement The WebElement representing the table.
     * @return The number of rows in the table.
     */
    public int getTableRowCount(WebElement tableElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(tableElement));
            highlightElement(tableElement);
            List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
            int rowCount = rows.size();
            Log4jManager.info("Table: " + getElementDescription(tableElement) + " has " + rowCount + " rows.");
            return rowCount;
        } catch (TimeoutException e) {
            Log4jManager.error("Table element not visible after waiting: " + getElementDescription(tableElement));
            takeScreenshot("screenshots/getTableRowCount_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to get table row count for element: " + getElementDescription(tableElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/getTableRowCount_error.png");
            throw e;
        }
    }
    /**
     * Gets the number of columns in a table row represented by a WebElement.
     *
     * @param rowElement The WebElement representing the table row.
     * @return The number of columns in the row.
     */
    public int getTableCellCount(WebElement rowElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(rowElement));
            highlightElement(rowElement);
            List<WebElement> cells = rowElement.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                cells = rowElement.findElements(By.tagName("th")); // Check for header cells as well
            }
            int cellCount = cells.size();
            Log4jManager.info("Table row has " + cellCount + " cells.");
            return cellCount;
        } catch (TimeoutException e) {
            Log4jManager.error("Table row element not visible after waiting: " + getElementDescription(rowElement));
            takeScreenshot("screenshots/getTableCellCount_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to get table cell count for row: " + getElementDescription(rowElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/getTableCellCount_error.png");
            throw e;
        }
    }
    /**
     * Gets the text of a specific cell in a table.
     *
     * @param tableElement The WebElement representing the table.
     * @param row          The row index (0-based).
     * @param column       The column index (0-based).
     * @return The text content of the specified cell.
     */
    public String getTableCellText(WebElement tableElement, int row, int column) {
        try {
            wait.until(ExpectedConditions.visibilityOf(tableElement));
            highlightElement(tableElement);
            List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
            if (row < 0 || row >= rows.size()) {
                throw new IndexOutOfBoundsException("Row index out of bounds: " + row);
            }
            WebElement rowElement = rows.get(row);
            List<WebElement> cells = rowElement.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                cells = rowElement.findElements(By.tagName("th")); // Check for header cells
            }
            if (column < 0 || column >= cells.size()) {
                throw new IndexOutOfBoundsException("Column index out of bounds: " + column);
            }
            WebElement cellElement = cells.get(column);
            highlightElement(cellElement);
            String cellText = cellElement.getText();
            Log4jManager.info("Text in cell at row " + row + ", column " + column + " is: " + cellText);
            return cellText;
        } catch (TimeoutException e) {
            Log4jManager.error("Table element not visible after waiting: " + getElementDescription(tableElement));
            takeScreenshot("screenshots/getTableCellText_timeout.png");
            throw e;
        } catch (IndexOutOfBoundsException e) {
            Log4jManager.error(e.getMessage() + " for table: " + getElementDescription(tableElement));
            takeScreenshot("screenshots/getTableCellText_indexOutOfBounds.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Failed to get text from table cell. Error: " + e.getMessage());
            takeScreenshot("screenshots/getTableCellText_error.png");
            throw e;
        }
    }
    /**
     * Scrolls to the bottom of the page.
     */
    public void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Log4jManager.info("Scrolled to the bottom of the page.");
        } catch (Exception e) {
            Log4jManager.error("Failed to scroll to the bottom of the page. Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollToBottom_error.png");
            throw e;
        }
    }

    /**
     * Scrolls to the top of the page.
     */
    public void scrollToTop() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
            Log4jManager.info("Scrolled to the top of the page.");
        } catch (Exception e) {
            Log4jManager.error("Failed to scroll to the top of the page. Error: " + e.getMessage());
            takeScreenshot("screenshots/scrollToTop_error.png");
            throw e;
        }
    }
    /**
     * Helper method to get the XPath of a WebElement.
     * Note: This is a basic implementation and might not work for all complex scenarios.
     *
     * @param element The WebElement to get the XPath for.
     * @return The XPath of the element, or null if it cannot be determined.
     */
    private String getXPath(WebElement element) {
        try {
            return ((JavascriptExecutor) driver).executeScript(
                    "function getPathTo(node) {\n" +
                            "  if (node.id) return '//' + node.tagName.toLowerCase() + '[@id=\"' + node.id + '\"]';\n" +
                            "  if (node.parentNode == null) return '';\n" +
                            "  var index = 1;\n" +
                            "  var sibling = node.previousSibling;\n" +
                            "  while (sibling) {\n" +
                            "    if (sibling.nodeType == 1 && sibling.tagName == node.tagName) {\n" +
                            "      index++;\n" +
                            "    }\n" +
                            "    sibling = sibling.previousSibling;\n" +
                            "  }\n" +
                            "  var parentPath = getPathTo(node.parentNode);\n" +
                            "  return parentPath + '/' + node.tagName.toLowerCase() + '[' + index + ']';\n" +
                            "}\n" +
                            "return getPathTo(arguments[0]);", element).toString();
        } catch (WebDriverException e) {
            // Handle cases where JavaScript execution fails (e.g., due to a closed browser)
            Log4jManager.warn("WebDriverException while getting XPath: " + e.getMessage());
            return null;
        }
    }
    /**
     * Finds element within shadow DOM hierarchy
     * @param hostSelector CSS selector for shadow host
     * @param shadowPath Sequence of shadow roots and selectors
     */
    public WebElement findInShadowDOM(String hostSelector, String... shadowPath) {
        WebElement current = driver.findElement(By.cssSelector(hostSelector));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (String path : shadowPath) {
            current = (WebElement) js.executeScript(
                    "return arguments[0].shadowRoot.querySelector('" + path + "')", current);
            if(current == null) throw new NoSuchElementException("Shadow path broken at: " + path);
        }
        return current;
    }
    /**
     * Captures network requests/responses
     * @param urlPattern URL filter pattern
     * @param method HTTP method filter
     */
    public List<Map<String, Object>> captureNetworkActivity(String urlPattern, String method) {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        List<Map<String, Object>> requests = new ArrayList<>();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.requestWillBeSent(), request -> {
            if(request.getRequest().getUrl().contains(urlPattern) &&
                    request.getRequest().getMethod().equalsIgnoreCase(method)) {
                requests.add(Map.of(
                        "url", request.getRequest().getUrl(),
                        "headers", request.getRequest().getHeaders(),
                        "postData", request.getRequest().getPostData()
                ));
            }
        });
        return requests;
    }
    /**
     * Captures page performance metrics
     * @return Map of key performance indicators
     */
    public Map<String, Object> getPerformanceMetrics() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("loadEventEnd", js.executeScript(
                "return window.performance.timing.loadEventEnd - window.performance.timing.navigationStart"));

        metrics.put("resourceCount", js.executeScript(
                "return window.performance.getEntriesByType('resource').length"));

        metrics.put("memoryUsage", js.executeScript(
                "return window.performance.memory.usedJSHeapSize"));

        return metrics;
    }
    /**
     * Captures browser console logs
     * @return List of console messages
     */
    public List<String> captureConsoleLogs() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        return logEntries.getAll().stream()
                .map(LogEntry::getMessage)
                .collect(Collectors.toList());
    }
    /**
     * Overrides browser geolocation
     * @param latitude Location latitude
     * @param longitude Location longitude
     */
    public void setGeolocation(double latitude, double longitude) {
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        devTools.send(Emulation.setGeolocationOverride(
                Optional.of(latitude),
                Optional.of(longitude),
                Optional.of(100)
        ));
    }
    /**
     * Scrolls until element stops moving (for infinite scroll)
     * @param scrollableArea Scroll container element
     */
    public void scrollUntilStable(WebElement scrollableArea) {
        long previousScroll = 0;
        long currentScroll;

        do {
            previousScroll = (Long) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].scrollTop", scrollableArea);

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollBy(0, arguments[0].offsetHeight)", scrollableArea);

            WaitUtility.wait(1500);
            currentScroll = (Long) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].scrollTop", scrollableArea);

        } while (currentScroll > previousScroll);
    }




}
