package reporting;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import config.DriverManager;

/**
 * Implements TestNG’s ITestListener and integrates ExtentReports.
 * Ensures that each test is created only once and logs are added to the corresponding test node.
 */
public class ReportListener implements ITestListener {

    private static ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());
        extentTestThreadLocal.set(test);
        Log4jManager.info("Started Test: " + result.getMethod().getMethodName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            String methodName = result.getMethod().getMethodName();
            String logText = "<b>TEST CASE: " + methodName.toUpperCase() + " PASSED</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
            test.pass(m);
            test.log(Status.PASS, result.getName() + " has passed.");
            Log4jManager.info("Test Passed: " + methodName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            // Log the exception details in an expandable section
            String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
            test.fail("<details>"
                    + "<summary><b><font color=\"red\">Exception Occurred: Click to see</font></b></summary>"
                    + exceptionMessage.replaceAll(",", "<br>")
                    + "</details>\n");

            // Capture and add a screenshot to the report
            try {
                String screenshotPath = getScreenshotPath(result.getMethod().getMethodName());
                test.addScreenCaptureFromPath(screenshotPath, result.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String failureLog = "TEST CASE FAILED";
            Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
            test.log(Status.FAIL, m);
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());

            // Optionally attach an additional screenshot using the common method
            ExtentManager.attachScreenshot(result.getMethod().getMethodName() + "_Failure_Screenshot", Status.FAIL);
            Log4jManager.error("Test Failed: " + result.getMethod().getMethodName(), result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
            String methodName = result.getMethod().getMethodName();
            String logText = "<b>TEST CASE: " + methodName + " SKIPPED</b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
            test.skip(m);
            ExtentManager.attachScreenshot(result.getMethod().getMethodName() + "_Skipped_Screenshot", Status.SKIP);
            Log4jManager.warn("Test Skipped: " + methodName + " - " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Log4jManager.warn("Test Failed but within success percentage: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        Log4jManager.info("Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flushReports();
        Log4jManager.info("Test Suite finished: " + context.getName());
        try {
            Desktop.getDesktop().browse(new File(System.getProperty("user.dir") + "/target/extentReport/extent-report.html").toURI());
        } catch (IOException e) {
            throw new RuntimeException("Failed to open the report", e);
        }
    }

    /**
     * Utility to capture a screenshot and return its file path.
     */
    private String getScreenshotPath(String testCaseName) throws IOException {
        File source = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotName = testCaseName + "_" + timestamp + ".jpg";
        File dest = new File(System.getProperty("user.dir") + "/Reports/Screenshots/" + screenshotName);
        FileUtils.copyFile(source, dest);
        return dest.getAbsolutePath();
    }
}
