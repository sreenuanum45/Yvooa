package utilities;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaginationUtils {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By rowsPerPageSelector = By.cssSelector("div.rows-per-page select");
    private final By paginationText = By.cssSelector("div.pagination-text");
    private final By nextButton = By.cssSelector("button.next-page");
    private final By previousButton = By.cssSelector("button.previous-page");
    private final By tableBody = By.cssSelector("table tbody");

    public PaginationUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void setRowsPerPage(int rows) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(rowsPerPageSelector));
        scrollToElement(dropdown);
        new Select(dropdown).selectByVisibleText(String.valueOf(rows));
        waitForTableReload();
    }

    public void goToNextPage() {
        if (isNextButtonEnabled()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            scrollToElement(nextBtn);
            nextBtn.click();
            waitForTableReload();
        }
    }

    public void goToPreviousPage() {
        if (isPreviousButtonEnabled()) {
            WebElement prevBtn = wait.until(ExpectedConditions.elementToBeClickable(previousButton));
            scrollToElement(prevBtn);
            prevBtn.click();
            waitForTableReload();
        }
    }

    public void goToPage(int targetPage) {
        int currentPage = getCurrentPageNumber();
        while (currentPage < targetPage && isNextButtonEnabled()) {
            goToNextPage();
            currentPage++;
        }
        while (currentPage > targetPage && isPreviousButtonEnabled()) {
            goToPreviousPage();
            currentPage--;
        }
    }

    public int getTotalItems() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(paginationText)).getText();
        return Integer.parseInt(text.split("of ")[1].trim());
    }

    public int getTotalPages() {
        int totalItems = getTotalItems();
        int rowsPerPage = getCurrentRowsPerPage();
        return (int) Math.ceil((double) totalItems / rowsPerPage);
    }

    public int getCurrentRowsPerPage() {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(rowsPerPageSelector));
        return Integer.parseInt(new Select(dropdown).getFirstSelectedOption().getText());
    }

    public int getCurrentPageNumber() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(paginationText)).getText();
        return Integer.parseInt(text.split("-")[1].split(" ")[0].trim());
    }

    public boolean isNextButtonEnabled() {
        return !driver.findElement(nextButton).getAttribute("class").contains("disabled");
    }

    public boolean isPreviousButtonEnabled() {
        return !driver.findElement(previousButton).getAttribute("class").contains("disabled");
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    private void waitForTableReload() {
        // Wait for previous data to clear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("table tbody tr.loading-row")));

        // Wait for new data to load
        wait.until(d -> {
            try {
                return driver.findElement(tableBody).findElements(By.tagName("tr")).size() > 0;
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    // Example usage:
    public void performPaginatedOperation(PaginationAction action) {
        int totalPages = getTotalPages();
        int currentRows = getCurrentRowsPerPage();

        for (int page = 1; page <= totalPages; page++) {
            action.execute(page);
            if (page < totalPages) {
                goToNextPage();
            }
        }
    }

    @FunctionalInterface
    public interface PaginationAction {
        void execute(int currentPage);
    }
}
