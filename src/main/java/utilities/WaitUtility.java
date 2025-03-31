package utilities;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.DriverManager;
import reporting.Log4jManager;

/**
 * WaitUtility provides advanced waiting mechanisms for Selenium WebDriver.
 * It includes dynamic waits, fluent waits, and various wait conditions to handle
 * different synchronization challenges in web applications.
 */
public class WaitUtility {

    private WebDriver driver;
    private WebDriverWait defaultWait;
    private static final int DEFAULT_TIMEOUT = 100; // seconds
    private static final int DEFAULT_POLLING_INTERVAL = 1; // milliseconds

    /**
     * Constructor initializes WebDriver and default WebDriverWait instances.
     */
    public WaitUtility() {
        this.driver = DriverManager.getDriver();
        this.defaultWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.defaultWait.pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL));
        this.defaultWait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
    }

    /**
     * Creates a custom WebDriverWait with specified timeout and polling interval.
     *
     * @param timeoutSeconds    The maximum time to wait in seconds.
     * @param pollingIntervalMs The polling interval in milliseconds.
     * @return A configured WebDriverWait instance.
     */
    public WebDriverWait createCustomWait(int timeoutSeconds, int pollingIntervalMs) {
        return (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalMs))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);
    }

    /**
     * Waits for the specified WebElement to be clickable using default wait settings.
     *
     * @param element The WebElement to wait for.
     * @return The clickable WebElement.
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            WebElement clickableElement = defaultWait.until(ExpectedConditions.elementToBeClickable(element));
            Log4jManager.info("Element is clickable: " + getElementDescription(element));
            return clickableElement;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be clickable: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeClickable_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be clickable: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeClickable_error.png");
            throw e;
        }
    }

    //waitForElementToBeClickable using By class method parameter
    public WebElement waitForElementToBeClickable(By locator) {
        try {
            WebElement clickableElement = defaultWait.until(ExpectedConditions.elementToBeClickable(locator));
            Log4jManager.info("Element is clickable: " + locator.toString());
            return clickableElement;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be clickable: " + locator.toString());
            takeScreenshot("screenshots/waitForElementToBeClickable_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be clickable: " + locator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeClickable_error.png");
            throw e;
        }
    }

    // Add small pause after scrolling
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Log4jManager.error("Error waiting for " + milliseconds + " milliseconds. Error: " + e.getMessage());
        }
    }


    /**
     * Waits for the specified WebElement to be visible using default wait settings.
     *
     * @param element The WebElement to wait for.
     * @return The visible WebElement.
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        try {
            WebElement visibleElement = defaultWait.until(ExpectedConditions.visibilityOf(element));
            Log4jManager.info("Element is visible: " + getElementDescription(element));
            return visibleElement;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be visible: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeVisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be visible: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeVisible_error.png");
            throw e;
        }
    }

    /**
     * Waits for the specified WebElement to be present in the DOM using default wait settings.
     *
     * @param locator The By locator of the WebElement.
     * @return The located WebElement.
     */
    public WebElement waitForElementPresence(By locator) {
        try {
            WebElement element = defaultWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            Log4jManager.info("Element present in DOM: " + locator.toString());
            return element;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element presence: " + locator.toString());
            takeScreenshot("screenshots/waitForElementPresence_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element presence: " + locator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementPresence_error.png");
            throw e;
        }
    }

    /**
     * Waits for all elements matching the locator to be visible using default wait settings.
     *
     * @param locator The By locator of the WebElements.
     * @return The list of visible WebElements.
     */
    public List<WebElement> waitForAllElementsToBeVisible(By locator) {
        try {
            List<WebElement> elements = defaultWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            Log4jManager.info("All elements located by " + locator.toString() + " are visible.");
            return elements;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for all elements to be visible: " + locator.toString());
            takeScreenshot("screenshots/waitForAllElementsToBeVisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for all elements to be visible: " + locator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAllElementsToBeVisible_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific text to be present in a WebElement using default wait settings.
     *
     * @param element The WebElement to check.
     * @param text    The text to be present.
     */
    public void waitForTextToBePresentInElement(WebElement element, String text) {
        try {
            defaultWait.until(ExpectedConditions.textToBePresentInElement(element, text));
            Log4jManager.info("Text '" + text + "' is present in element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for text '" + text + "' to be present in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForTextToBePresentInElement_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for text in element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForTextToBePresentInElement_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific attribute of a WebElement to have a given value using default wait settings.
     *
     * @param element       The WebElement to check.
     * @param attribute     The attribute to inspect.
     * @param expectedValue The expected value of the attribute.
     */
    public void waitForAttributeToBe(WebElement element, String attribute, String expectedValue) {
        try {
            defaultWait.until(ExpectedConditions.attributeToBe(element, attribute, expectedValue));
            Log4jManager.info("Attribute '" + attribute + "' of element: " + getElementDescription(element) +
                    " is now '" + expectedValue + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for attribute '" + attribute + "' to be '" + expectedValue +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForAttributeToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for attribute in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAttributeToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific CSS property of a WebElement to have a given value using default wait settings.
     *
     * @param element       The WebElement to check.
     * @param cssProperty   The CSS property to inspect.
     * @param expectedValue The expected value of the CSS property.
     */
    public void waitForCssPropertyToBe(WebElement element, String cssProperty, String expectedValue) {
        try {
            defaultWait.until(ExpectedConditions.attributeToBe(element, cssProperty, expectedValue));
            Log4jManager.info("CSS property '" + cssProperty + "' of element: " + getElementDescription(element) +
                    " is now '" + expectedValue + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for CSS property '" + cssProperty + "' to be '" + expectedValue +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForCssPropertyToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for CSS property in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForCssPropertyToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for the entire page to load completely.
     */
    public void waitForPageLoad() {
        try {
            ExpectedCondition<Boolean> pageLoadCondition = drv -> ((JavascriptExecutor) drv)
                    .executeScript("return document.readyState").equals("complete");
            defaultWait.until(pageLoadCondition);
            Log4jManager.info("Page loaded completely.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for page to load completely.");
            takeScreenshot("screenshots/waitForPageLoad_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for page load. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForPageLoad_error.png");
            throw e;
        }
    }

    /**
     * Waits for a custom JavaScript condition to be true.
     *
     * @param script         The JavaScript code that returns a boolean value.
     * @param timeoutSeconds The maximum time to wait in seconds.
     */
    public void waitForCustomJavaScriptCondition(String script, int timeoutSeconds) {
        try {
            WebDriverWait customWait = createCustomWait(timeoutSeconds, DEFAULT_POLLING_INTERVAL);
            customWait.until(driver -> ((JavascriptExecutor) driver).executeScript(script).equals(true));
            Log4jManager.info("Custom JavaScript condition met.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for custom JavaScript condition.");
            takeScreenshot("screenshots/waitForCustomJSCondition_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for custom JavaScript condition. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForCustomJSCondition_error.png");
            throw e;
        }
    }

    /**
     * Waits for all elements matching the locator to be present in the DOM.
     *
     * @param locator The By locator of the WebElements.
     * @return The list of located WebElements.
     */
    public List<WebElement> waitForAllElementsPresence(By locator) {
        try {
            List<WebElement> elements = defaultWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            Log4jManager.info("All elements present in DOM by locator: " + locator.toString());
            return elements;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for all elements to be present: " + locator.toString());
            takeScreenshot("screenshots/waitForAllElementsPresence_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for all elements presence: " + locator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAllElementsPresence_error.png");
            throw e;
        }
    }

    /**
     * Waits for a WebElement to be invisible using default wait settings.
     *
     * @param element The WebElement to wait for its invisibility.
     */
    public void waitForElementToBeInvisible(WebElement element) {
        try {
            defaultWait.until(ExpectedConditions.invisibilityOf(element));
            Log4jManager.info("Element is now invisible: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be invisible: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeInvisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be invisible: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeInvisible_error.png");
            throw e;
        }
    }

    /**
     * Waits for the URL to contain a specific substring using default wait settings.
     *
     * @param urlFraction The substring to be present in the URL.
     */
    public void waitForUrlToContain(String urlFraction) {
        try {
            defaultWait.until(ExpectedConditions.urlContains(urlFraction));
            Log4jManager.info("URL contains: " + urlFraction);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for URL to contain: " + urlFraction);
            takeScreenshot("screenshots/waitForUrlToContain_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for URL to contain: " + urlFraction + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForUrlToContain_error.png");
            throw e;
        }
    }

    /**
     * Waits for the title of the page to contain a specific substring using default wait settings.
     *
     * @param titleFraction The substring to be present in the title.
     */
    public void waitForTitleToContain(String titleFraction) {
        try {
            defaultWait.until(ExpectedConditions.titleContains(titleFraction));
            Log4jManager.info("Page title contains: " + titleFraction);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for title to contain: " + titleFraction);
            takeScreenshot("screenshots/waitForTitleToContain_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for title to contain: " + titleFraction + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForTitleToContain_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific element to have a specific text using a custom condition.
     *
     * @param element The WebElement to check.
     * @param text    The exact text to be present in the element.
     */
    public void waitForElementTextToBe(WebElement element, String text) {
        try {
            defaultWait.until(ExpectedConditions.textToBePresentInElement(element, text));
            Log4jManager.info("Element text is now: '" + text + "' for element: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element text to be: '" + text + "' for element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementTextToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element text to be: '" + text + "' for element: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementTextToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific element's attribute to contain a substring using a custom condition.
     *
     * @param element   The WebElement to check.
     * @param attribute The attribute to inspect.
     * @param substring The substring to be present in the attribute's value.
     */
    public void waitForAttributeToContain(WebElement element, String attribute, String substring) {
        try {
            defaultWait.until(ExpectedConditions.attributeContains(element, attribute, substring));
            Log4jManager.info("Attribute '" + attribute + "' of element: " + getElementDescription(element) +
                    " contains substring: '" + substring + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for attribute '" + attribute + "' to contain substring: '" + substring +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForAttributeToContain_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for attribute to contain substring in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAttributeToContain_error.png");
            throw e;
        }
    }

    /**
     * Waits for a list of WebElements to be clickable using default wait settings.
     *
     * @param elements The list of WebElements to wait for.
     * @return The list of clickable WebElements.
     */
    public List<WebElement> waitForElementsToBeClickable(List<WebElement> elements) {
        try {
            List<WebElement> clickableElements = new ArrayList<>();
            for (WebElement element : elements) {
                clickableElements.add(defaultWait.until(ExpectedConditions.elementToBeClickable(element)));
                Log4jManager.info("Element is clickable: " + getElementDescription(element));
            }
            return clickableElements;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for one or more elements to be clickable.");
            takeScreenshot("screenshots/waitForElementsToBeClickable_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for elements to be clickable. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementsToBeClickable_error.png");
            throw e;
        }
    }

    public void waitForElementToBeVisible(List<WebElement> elements) {
        try {
            defaultWait.until(ExpectedConditions.visibilityOfAllElements(elements));
            Log4jManager.info("All elements are visible.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for all elements to be visible.");
            takeScreenshot("screenshots/waitForElementsToBeVisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for elements to be visible. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementsToBeVisible_error.png");
            throw e;
        }
    }

    /**
     * Waits for an element to have a specific CSS value using a custom condition.
     *
     * @param element       The WebElement to check.
     * @param cssProperty   The CSS property to inspect.
     * @param expectedValue The expected value of the CSS property.
     */
    public void waitForElementCssValue(WebElement element, String cssProperty, String expectedValue) {
        try {
            defaultWait.until(driver -> {
                String actualValue = element.getCssValue(cssProperty);
                return actualValue.equals(expectedValue);
            });
            Log4jManager.info("CSS property '" + cssProperty + "' of element: " + getElementDescription(element) +
                    " is now '" + expectedValue + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for CSS property '" + cssProperty + "' to be '" + expectedValue +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementCssValue_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for CSS property in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementCssValue_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific number of windows/tabs to be present.
     *
     * @param numberOfWindows The expected number of windows/tabs.
     */
    public void waitForNumberOfWindowsToBe(int numberOfWindows) {
        try {
            defaultWait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
            Log4jManager.info("Number of windows/tabs is now: " + numberOfWindows);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for number of windows/tabs to be: " + numberOfWindows);
            takeScreenshot("screenshots/waitForNumberOfWindowsToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for number of windows/tabs. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForNumberOfWindowsToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific element to be selected (useful for checkboxes and radio buttons).
     *
     * @param element The WebElement to check.
     */
    public void waitForElementToBeSelected(WebElement element) {
        try {
            defaultWait.until(ExpectedConditions.elementToBeSelected(element));
            Log4jManager.info("Element is selected: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be selected: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeSelected_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be selected: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeSelected_error.png");
            throw e;
        }
    }

    /**
     * Waits for an element to be deselected (useful for checkboxes).
     *
     * @param element The WebElement to check.
     */
    public void waitForElementToBeDeselected(WebElement element) {
        try {
            defaultWait.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(element)));
            Log4jManager.info("Element is deselected: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be deselected: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeDeselected_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be deselected: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeDeselected_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific frame to be available and switches to it using default wait settings.
     *
     * @param frameLocator The By locator of the frame.
     */
    public void waitForFrameAndSwitchToIt(By frameLocator) {
        try {
            defaultWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
            Log4jManager.info("Switched to frame: " + frameLocator.toString());
        } catch (NoSuchFrameException e) {
            Log4jManager.error("No frame found with locator: " + frameLocator.toString());
            takeScreenshot("screenshots/waitForFrameAndSwitchToIt_noFrame.png");
            throw e;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for frame: " + frameLocator.toString());
            takeScreenshot("screenshots/waitForFrameAndSwitchToIt_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for frame: " + frameLocator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForFrameAndSwitchToIt_error.png");
            throw e;
        }
    }

    /**
     * Waits for an element to be invisible using a custom wait.
     *
     * @param locator        The By locator of the WebElement.
     * @param timeoutSeconds The maximum time to wait in seconds.
     */
    public void waitForElementToBeInvisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = createCustomWait(timeoutSeconds, DEFAULT_POLLING_INTERVAL);
            customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            Log4jManager.info("Element located by " + locator.toString() + " is now invisible.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be invisible: " + locator.toString());
            takeScreenshot("screenshots/waitForElementToBeInvisible_custom_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be invisible: " + locator.toString() + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeInvisible_custom_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific element to have a specific property value using a custom condition.
     *
     * @param element  The WebElement to check.
     * @param property The JavaScript property to inspect.
     * @param value    The expected value of the property.
     */
    public void waitForElementPropertyToBe(WebElement element, String property, String value) {
        try {
            defaultWait.until(driver -> {
                String actualValue = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0]." + property, element);
                return actualValue.equals(value);
            });
            Log4jManager.info("Property '" + property + "' of element: " + getElementDescription(element) +
                    " is now '" + value + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for property '" + property + "' to be '" + value +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementPropertyToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for property in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementPropertyToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for an AJAX call to complete using a custom JavaScript condition.
     * This assumes that jQuery is used in the application.
     */
    public void waitForAjaxToComplete() {
        try {
            String script = "return jQuery.active == 0;";
            defaultWait.until(driver -> ((JavascriptExecutor) driver).executeScript(script).equals(true));
            Log4jManager.info("All AJAX calls have completed.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for AJAX calls to complete.");
            takeScreenshot("screenshots/waitForAjaxToComplete_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for AJAX calls to complete. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAjaxToComplete_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific number of elements to be present in the DOM using a custom condition.
     *
     * @param locator       The By locator of the WebElements.
     * @param expectedCount The expected number of elements.
     */
    public void waitForNumberOfElementsToBe(By locator, int expectedCount) {
        try {
            defaultWait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
            Log4jManager.info("Number of elements located by " + locator.toString() + " is now: " + expectedCount);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for number of elements to be: " + expectedCount + " for locator: " + locator.toString());
            takeScreenshot("screenshots/waitForNumberOfElementsToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for number of elements to be: " + expectedCount + " for locator: " + locator.toString() +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForNumberOfElementsToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific element to contain a given CSS class using a custom condition.
     *
     * @param element   The WebElement to check.
     * @param className The CSS class name to be present.
     */
    public void waitForElementToContainClass(WebElement element, String className) {
        try {
            defaultWait.until(driver -> element.getAttribute("class").contains(className));
            Log4jManager.info("Element: " + getElementDescription(element) + " now contains class: '" + className + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to contain class: '" + className + "' for element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToContainClass_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to contain class: '" + className + "' for element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToContainClass_error.png");
            throw e;
        }
    }

    /**
     * Waits for the URL to match exactly a given string using default wait settings.
     *
     * @param url The exact URL to wait for.
     */
    public void waitForUrlToBe(String url) {
        try {
            defaultWait.until(ExpectedConditions.urlToBe(url));
            Log4jManager.info("URL is now exactly: " + url);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for URL to be exactly: " + url);
            takeScreenshot("screenshots/waitForUrlToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for URL to be exactly: " + url + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForUrlToBe_error.png");
            throw e;
        }
    }

    public void highlightElements(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", element);
    }

    /**
     * Waits for the page's title to be exactly a given string using default wait settings.
     *
     * @param title The exact title to wait for.
     */
    public void waitForTitleToBe(String title) {
        try {
            defaultWait.until(ExpectedConditions.titleIs(title));
            Log4jManager.info("Page title is now exactly: " + title);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for page title to be exactly: " + title);
            takeScreenshot("screenshots/waitForTitleToBe_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for page title to be exactly: " + title + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForTitleToBe_error.png");
            throw e;
        }
    }

    /**
     * Waits for a WebElement to have a specific CSS value using a custom condition.
     *
     * @param element     The WebElement to check.
     * @param cssProperty The CSS property to inspect.
     * @param value       The expected value of the CSS property.
     */
    public void waitForCssValue(WebElement element, String cssProperty, String value) {
        try {
            defaultWait.until(driver -> element.getCssValue(cssProperty).equals(value));
            Log4jManager.info("CSS property '" + cssProperty + "' of element: " + getElementDescription(element) +
                    " is now '" + value + "'.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for CSS property '" + cssProperty + "' to be '" + value +
                    "' in element: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForCssValue_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for CSS property in element: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForCssValue_error.png");
            throw e;
        }
    }

    /**
     * Waits for an element to become stale (no longer attached to the DOM).
     *
     * @param element The WebElement to wait for staleness.
     */
    public void waitForElementToBeStale(WebElement element) {
        try {
            defaultWait.until(ExpectedConditions.stalenessOf(element));
            Log4jManager.info("Element is now stale: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to become stale: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeStale_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to become stale: " + getElementDescription(element) +
                    ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeStale_error.png");
            throw e;
        }
    }

    /**
     * Waits for an element's visibility based on a custom condition.
     *
     * @param condition The custom condition function.
     * @return The WebElement once the condition is met.
     */
    public WebElement waitForElementBasedOnCondition(Function<WebDriver, WebElement> condition) {
        try {
            WebElement element = defaultWait.until(condition);
            Log4jManager.info("Custom condition met for element: " + getElementDescription(element));
            return element;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for custom condition.");
            takeScreenshot("screenshots/waitForElementBasedOnCondition_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for custom condition. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementBasedOnCondition_error.png");
            throw e;
        }
    }

    /**
     * Takes a screenshot of the current browser window and saves it to the specified path.
     *
     * @param filePath The path where the screenshot will be saved.
     */
    private void takeScreenshot(String filePath) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            org.openqa.selenium.io.FileHandler.copy(source, destination);
            Log4jManager.info("Screenshot taken and saved to: " + filePath);
        } catch (IOException e) {
            Log4jManager.error("Failed to take screenshot. Error: " + e.getMessage());
            // Depending on your framework's design, you might want to throw a runtime exception here.
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
            String id = element.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                description.append(", ID: ").append(id);
            }
            String className = element.getAttribute("class");
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

    public void waitForPresenceOfElementLocated(WebElement element) {
        try {
            defaultWait.until(ExpectedConditions.visibilityOf(element));
            Log4jManager.info("Element is visible: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element to be visible: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementToBeVisible_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element to be visible: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementToBeVisible_error.png");
            throw e;

        }
    }

    /**
     * Waits for an alert to be present and returns it.
     *
     * @return The Alert instance.
     */
    public Alert waitForAlertToBePresent() {
        try {
            Alert alert = defaultWait.until(ExpectedConditions.alertIsPresent());
            Log4jManager.info("Alert is present.");
            return alert;
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for alert to be present.");
            takeScreenshot("screenshots/waitForAlertToBePresent_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for alert. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForAlertToBePresent_error.png");
            throw e;
        }
    }

    /**
     * Waits for a frame to be available using a WebElement and switches to it.
     *
     * @param frameElement The WebElement representing the frame.
     */
    public void waitForFrameAndSwitchToIt(WebElement frameElement) {
        try {
            defaultWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
            Log4jManager.info("Switched to frame: " + getElementDescription(frameElement));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for frame: " + getElementDescription(frameElement));
            takeScreenshot("screenshots/waitForFrameAndSwitchToIt_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error switching to frame: " + getElementDescription(frameElement) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForFrameAndSwitchToIt_error.png");
            throw e;
        }
    }

    /**
     * Waits for element's position to stabilize (useful for animated elements).
     *
     * @param element          The WebElement to monitor.
     * @param maxPositionDelta The maximum allowed position change in pixels.
     * @param timeoutSeconds   Maximum time to wait.
     */
    public void waitForElementPositionToStabilize(WebElement element, int maxPositionDelta, int timeoutSeconds) {
        try {
            WebDriverWait customWait = createCustomWait(timeoutSeconds, 500);
            Point[] previousPosition = {null};

            customWait.until(driver -> {
                Point currentPosition = element.getLocation();
                if (previousPosition[0] == null) {
                    previousPosition[0] = currentPosition;
                    return false;
                }
                boolean stabilized = Math.abs(currentPosition.getX() - previousPosition[0].getX()) <= maxPositionDelta &&
                        Math.abs(currentPosition.getY() - previousPosition[0].getY()) <= maxPositionDelta;
                previousPosition[0] = currentPosition;
                return stabilized;
            });
            Log4jManager.info("Element position stabilized: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element position to stabilize: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementPositionToStabilize_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element position stabilization: " + getElementDescription(element) + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementPositionToStabilize_error.png");
            throw e;
        }
    }

    /**
     * Waits for a new window to appear and switches to it.
     *
     * @param initialWindowCount The number of windows before the action.
     */
    public void waitForNewWindowAndSwitch(int initialWindowCount) {
        try {
            defaultWait.until(driver -> driver.getWindowHandles().size() > initialWindowCount);
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
            Log4jManager.info("Switched to new window.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for new window.");
            takeScreenshot("screenshots/waitForNewWindow_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for new window. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForNewWindow_error.png");
            throw e;
        }
    }

    /**
     * Waits for the URL to change from a specified value.
     *
     * @param originalUrl The URL that should be changed.
     */
    public void waitForUrlToChangeFrom(String originalUrl) {
        try {
            defaultWait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(originalUrl)));
            Log4jManager.info("URL changed from: " + originalUrl);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for URL to change from: " + originalUrl);
            takeScreenshot("screenshots/waitForUrlToChangeFrom_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for URL change. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForUrlToChangeFrom_error.png");
            throw e;
        }
    }

    /**
     * Waits for a file to appear in the specified directory.
     *
     * @param downloadDir     Path to download directory.
     * @param fileNamePattern Filename pattern to match (supports wildcards).
     * @param timeoutSeconds  Maximum time to wait.
     * @return The downloaded File object.
     */
    public File waitForFileToDownload(String downloadDir, String fileNamePattern, int timeoutSeconds) throws InterruptedException {
        try {
            File dir = new File(downloadDir);
            File[] files;
            long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

            while (System.currentTimeMillis() < endTime) {
                files = dir.listFiles((dir1, name) -> name.matches(fileNamePattern.replace("*", ".*")));
                if (files != null && files.length > 0) {
                    Log4jManager.info("File found: " + files[0].getName());
                    return files[0];
                }
                Thread.sleep(500);
            }
            throw new TimeoutException("File not found with pattern: " + fileNamePattern);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for file download: " + fileNamePattern);
            takeScreenshot("screenshots/waitForFileDownload_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for file download. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForFileDownload_error.png");
            throw e;
        }
    }

    /**
     * Creates a fluent wait with customizable parameters.
     *
     * @param timeoutSeconds    Maximum wait time.
     * @param pollingIntervalMs Polling interval.
     * @param ignoredExceptions Exceptions to ignore.
     * @return Configured FluentWait instance.
     */
    public FluentWait<WebDriver> createFluentWait(int timeoutSeconds, int pollingIntervalMs, Class<? extends Throwable>... ignoredExceptions) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalMs))
                .ignoreAll(List.of(ignoredExceptions));
    }

    /**
     * Waits for element presence with custom timeout.
     *
     * @param locator        Element locator.
     * @param timeoutSeconds Custom timeout.
     * @return Located WebElement.
     */
    public WebElement waitForElementPresence(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = createCustomWait(timeoutSeconds, DEFAULT_POLLING_INTERVAL);
            return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element presence: " + locator);
            takeScreenshot("screenshots/waitForElementPresenceCustom_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element presence: " + locator + ". Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementPresenceCustom_error.png");
            throw e;
        }
    }

    /**
     * Waits for element to reach specified enabled state.
     *
     * @param element WebElement to check.
     * @param enabled True to wait for enabled, false for disabled.
     */
    public void waitForElementToBeEnabled(WebElement element, boolean enabled) {
        try {
            defaultWait.until(driver -> element.isEnabled() == enabled);
            Log4jManager.info("Element enabled state: " + enabled + " for: " + getElementDescription(element));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element enabled state: " + enabled);
            takeScreenshot("screenshots/waitForElementEnabledState_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element enabled state. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementEnabledState_error.png");
            throw e;
        }
    }

    /**
     * Waits for a specific option to be selected in a dropdown.
     *
     * @param selectElement  The Select dropdown element.
     * @param expectedOption The expected selected option text.
     */
    public void waitForSelectOptionToBe(WebElement selectElement, String expectedOption) {
        try {
            defaultWait.until(driver -> {
                Select select = new Select(selectElement);
                return select.getFirstSelectedOption().getText().equals(expectedOption);
            });
            Log4jManager.info("Dropdown selection is now: " + expectedOption);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for dropdown option: " + expectedOption);
            takeScreenshot("screenshots/waitForSelectOption_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for dropdown option. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForSelectOption_error.png");
            throw e;
        }
    }

    /**
     * Waits for a loading spinner to disappear.
     *
     * @param spinnerLocator Locator for the loading spinner.
     */
    public void waitForLoadingSpinnerToDisappear(By spinnerLocator) {
        try {
            WebDriverWait customWait = createCustomWait(30, 500);
            customWait.until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
            Log4jManager.info("Loading spinner disappeared.");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for spinner to disappear: " + spinnerLocator);
            takeScreenshot("screenshots/waitForSpinnerDisappear_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for spinner. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForSpinnerDisappear_error.png");
            throw e;
        }
    }

    /**
     * Waits for element to be refreshed in the DOM (handles staleness).
     *
     * @param element Element to refresh.
     * @return The refreshed WebElement.
     */
    public WebElement waitForElementToBeRefreshed(WebElement element) {
        try {
            return defaultWait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for element refresh: " + getElementDescription(element));
            takeScreenshot("screenshots/waitForElementRefresh_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for element refresh. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForElementRefresh_error.png");
            throw e;
        }
    }

    /**
     * Waits for JavaScript variable to equal specified value.
     *
     * @param variableName  JavaScript variable name.
     * @param expectedValue Expected variable value.
     */
    public void waitForJavaScriptVariable(String variableName, String expectedValue) {
        try {
            String script = "return " + variableName + ";";
            defaultWait.until(driver -> {
                Object value = ((JavascriptExecutor) driver).executeScript(script);
                return value != null && value.toString().equals(expectedValue);
            });
            Log4jManager.info("JavaScript variable " + variableName + " = " + expectedValue);
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for JS variable: " + variableName);
            takeScreenshot("screenshots/waitForJsVariable_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for JS variable. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForJsVariable_error.png");
            throw e;
        }
    }

    /**
     * Waits for scroll completion by checking scroll position stability.
     *
     * @param timeoutSeconds Maximum time to wait.
     */
    public void waitForScrollCompletion(int timeoutSeconds) {
        try {
            WebDriverWait customWait = createCustomWait(timeoutSeconds, 200);
            Long[] previousPosition = {null};

            customWait.until(driver -> {
                Long currentPosition = (Long) ((JavascriptExecutor) driver)
                        .executeScript("return window.pageYOffset;");
                if (previousPosition[0] == null) {
                    previousPosition[0] = currentPosition;
                    return false;
                }
                boolean stabilized = previousPosition[0].equals(currentPosition);
                previousPosition[0] = currentPosition;
                return stabilized;
            });
            Log4jManager.info("Scroll position stabilized");
        } catch (TimeoutException e) {
            Log4jManager.error("Timeout waiting for scroll completion");
            takeScreenshot("screenshots/waitForScrollCompletion_timeout.png");
            throw e;
        } catch (Exception e) {
            Log4jManager.error("Error waiting for scroll completion. Error: " + e.getMessage());
            takeScreenshot("screenshots/waitForScrollCompletion_error.png");
            throw e;
        }
    }
}


