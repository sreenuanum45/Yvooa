import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class Test10 {

    public static void main(String[] args) throws Exception , IOException {
        // Set up WebDriver (ensure that the chromedriver path is correct)
        RemoteWebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(50, java.util.concurrent.TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(50, java.util.concurrent.TimeUnit.SECONDS);

        driver.manage().deleteAllCookies();
        FluentWait<RemoteWebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        driver.get("https://www.linkedin.com/uas/login");

        // Log in to LinkedIn
        driver.findElement(By.id("username")).sendKeys("sreenuanumandla45@gmail.com"); // Replace with your username
        driver.findElement(By.id("password")).sendKeys("Sreenu45@"); // Replace with your password
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[normalize-space(text())='Sign in']")).click();

        // Search for jobs
        driver.findElement(By.xpath("//input[@role='combobox']")).sendKeys("Software Testing, QA, Test Automation (Selenium/JIRA/Postman), Manual/Automated Testing, Agile/Scrum, CI/CD, ISTQB, SDET, QA Engineer, Performance/API Testing, Defect Tracking, DevOps, QA Analyst.", Keys.ENTER);

        // Select "Posts" filter
        List<WebElement> jobTitles = driver.findElements(By.xpath("//li[@class='search-reusables__primary-filter']//button"));
        wait.until(ExpectedConditions.visibilityOfAllElements(jobTitles));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//li[@class='search-reusables__primary-filter']//button")));
        for (WebElement jobTitle : jobTitles) {
            if (jobTitle.getText().contains("Posts")) {
                jobTitle.click();
break;
            }

        }
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Content type']"))).click();
if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Job posts']"))).isDisplayed()){
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Job posts']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Show results']//parent::button)[3]"))).click();
}
if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='searchFilter_datePosted']"))).isDisplayed()){
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='searchFilter_datePosted']"))).click();
wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='datePosted-past-24h']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Show results']//parent::button)[3]"))).click();

}
if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='searchFilter_sortBy']"))).isDisplayed()){
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='searchFilter_sortBy']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='sortBy-relevance']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Show results']//parent::button)[3]"))).click();

}
/*if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='All filters']"))).isDisplayed()){
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='ember4627']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='advanced-filter-postedBy-following']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='advanced-filter-postedBy-first']"))).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Show results']//parent::button)[3]"))).click();
}*/
        // Scroll to the bottom of the page to load more posts
        for (int i = 0; i < 5; i++) {
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
}

        // Get posts
        List<WebElement> posts = driver.findElements(By.xpath("//div[contains(@class, 'update-components-text relative update-components-update-v2__commentary')]"));

        // Get "more" buttons to load full posts
        List<WebElement> moreButtons = driver.findElements(By.xpath("//span[text()='…more']//parent::button"));


        // Initialize the PdfWriter for the output PDF file

        PdfWriter writer = new PdfWriter("D:\\batch264\\Yvooa\\src\\test\\resources\\output_text.pdf");
        // Initialize the PdfDocument using PdfWriter
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Initialize Document with PdfDocument
        Document document = new Document(pdfDoc);

        // Load a font using PdfFontFactory (using Helvetica as an example)
        PdfFont font = PdfFontFactory.createFont();

        for (int i = 0; i < posts.size(); i++) {
            if (i < moreButtons.size()) {
                try {
                    //wait.until(ExpectedConditions.elementToBeClickable(moreButtons.get(i))).click();
                    // Scroll into view and click via JavaScript
                    driver.executeScript("arguments[0].scrollIntoView({block: 'center'});", moreButtons.get(i));
                    driver.executeScript("arguments[0].click();", moreButtons.get(i));
                }catch(Exception e){
                    e.printStackTrace();
                    WebElement element = moreButtons.get(i);
                    driver.executeScript("arguments[0].scrollIntoView();", element);
                    driver.executeScript("arguments[0].click();", element);
                } // Ignore any exceptions)// Click the "more" button to load full post
            }

            // Set up the PDF document
            try {
                // Extract text from the post
                String postText = posts.get(i).getText();
                // Add the extracted text to the PDF as a paragraph with the chosen font
                document.add(new Paragraph(postText).setFont(font));
                document.add(new Paragraph("\n@==========================================================================@\n"));
                // Close the document
                System.out.println("PDF created for post " + (i) + " successfully!");


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        document.close();

        // Close the WebDriver
      //  driver.quit();
    }
}
