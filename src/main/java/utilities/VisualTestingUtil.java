package utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

public class VisualTestingUtil {
    private static final String BASELINE_PATH = "screenshots/baseline/";
    private static final String ACTUAL_PATH = "screenshots/actual/";
    private static final String DIFF_PATH = "screenshots/diff/";
    private static final long ELEMENT_TIMEOUT_SECONDS = 10;
    private final WebDriver driver;
    private static final double DIFFERENCE_THRESHOLD = 0.1; // 0.1% maximum allowed difference

    public VisualTestingUtil(WebDriver driver) {
        this.driver = driver;
        createDirectories();
    }

    private void createBaseline(Screenshot screenshot, File baselineFile) throws IOException {
        writeImage(screenshot.getImage(), baselineFile);
    }
    private void writeImage(BufferedImage image, File file) throws IOException {
        ImageWriter writer = null;
        try {
            writer = ImageIO.getImageWritersByFormatName("png").next();
            try (ImageOutputStream output = ImageIO.createImageOutputStream(file)) {
                writer.setOutput(output);
                IIOImage iioImage = new IIOImage(image, null, null);
                writer.write(null, iioImage, writer.getDefaultWriteParam());
            }
        } finally {
            if (writer != null) {
                writer.dispose();
            }
        }
    }
    private VisualComparisonResult processComparisonResult(ImageDiff diff, String diffPath) throws IOException {
        double differencePercentage = calculateDifferencePercentage(diff);

        if (differencePercentage > DIFFERENCE_THRESHOLD) {
            writeImage(diff.getMarkedImage(), new File(diffPath));
            return new VisualComparisonResult(
                    false,
                    differencePercentage,
                    String.format("Visual differences exceed threshold (%.2f%% > %.2f%%)",
                            differencePercentage, DIFFERENCE_THRESHOLD)
            );
        }
        return new VisualComparisonResult(true, differencePercentage, "Differences within acceptable range");
    }

    private void createDirectories() {

        createDirectory(BASELINE_PATH);
        createDirectory(ACTUAL_PATH);
        createDirectory(DIFF_PATH);
    }

    private void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + path);
            }
        }
    }

    public VisualComparisonResult captureAndCompare(String screenshotName) throws IOException {
        return captureAndCompare(screenshotName, null);
    }

    public VisualComparisonResult captureAndCompare(String screenshotName, WebElement element) throws IOException {
        validateDriverState();
        WebElement targetElement = handleElementState(element);

        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String baselineFileName = BASELINE_PATH + screenshotName + ".png";
        String actualFileName = ACTUAL_PATH + screenshotName + "_" + timestamp + ".png";
        String diffFileName = DIFF_PATH + screenshotName + "_diff_" + timestamp + ".png";

        Screenshot screenshot = captureScreenshot(targetElement);
        saveActualScreenshot(screenshot, new File(actualFileName));

        return handleComparison(screenshot, baselineFileName, diffFileName);
    }

    private WebElement handleElementState(WebElement element) {
        if (element != null) {
            waitForElementVisibility(element);
            if (!element.isDisplayed()) {
                throw new IllegalStateException("Element exists but is not visible");
            }
        }
        return element;
    }

    private void waitForElementVisibility(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(ELEMENT_TIMEOUT_SECONDS))
                .until(ExpectedConditions.visibilityOf(element));
    }

    private Screenshot captureScreenshot(WebElement element) {
        AShot aShot = new AShot().coordsProvider(new WebDriverCoordsProvider());
        return (element == null)
                ? aShot.takeScreenshot(driver)
                : aShot.takeScreenshot(driver, element);
    }

   /* private void saveActualScreenshot(Screenshot screenshot, String path) throws IOException {
        ImageIO.write(screenshot.getImage(), "PNG", new File(path));
    }
*/
    private VisualComparisonResult handleComparison(Screenshot screenshot, String baselinePath, String diffPath)
            throws IOException {

        File baselineFile = new File(baselinePath);

        if (!baselineFile.exists()) {
            createBaseline(screenshot, baselineFile);
            return new VisualComparisonResult(true, 0, "Baseline created successfully");
        }

        BufferedImage baselineImage = ImageIO.read(baselineFile);
        ImageDiff diff = new ImageDiffer().makeDiff(baselineImage, screenshot.getImage());

        if (diff.hasDiff()) {
            return handleDifferences(diff, diffPath);
        }

        return new VisualComparisonResult(true, 0, "No visual differences detected");
    }

    private void saveActualScreenshot(Screenshot screenshot, File baselineFile) throws IOException {
        ImageIO.write(screenshot.getImage(), "PNG", baselineFile);
    }

    private VisualComparisonResult handleDifferences(ImageDiff diff, String diffPath) throws IOException {
        double differencePercentage = calculateDifferencePercentage(diff);
        ImageIO.write(diff.getMarkedImage(), "PNG", new File(diffPath));

        return new VisualComparisonResult(
                false,
                differencePercentage,
                String.format("Visual differences detected (%.2f%%)", differencePercentage)
        );
    }

    private double calculateDifferencePercentage(ImageDiff diff) {
        int totalPixels = diff.getDiffImage().getWidth() * diff.getDiffImage().getHeight();
        return (diff.getDiffSize() * 100.0) / totalPixels;
    }

    private void validateDriverState() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver instance is null");
        }

        try {
            driver.getTitle(); // Simple check to verify driver is active
        } catch (Exception e) {
            throw new IllegalStateException("WebDriver is not in a valid state", e);
        }
    }


    // Static nested class for comparison results
    public static class VisualComparisonResult {
        private final boolean isMatch;
        private final double differencePercentage;
        private final String message;

        public VisualComparisonResult(boolean isMatch, double differencePercentage, String message) {
            this.isMatch = isMatch;
            this.differencePercentage = differencePercentage;
            this.message = message;
        }

        // Getters
        public boolean isMatch() {
            return isMatch;
        }

        public double getDifferencePercentage() {
            return differencePercentage;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return String.format("Match: %s | Difference: %.2f%% | Message: %s",
                    isMatch, differencePercentage, message);
        }
    }
}