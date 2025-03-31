package runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/ directories.feature",
        glue = {"stepDefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                // Allure report plugin:
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                // Extent report adapter plugin (note the trailing colon):
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "timeline:test-output-thread/",
                "rerun:target/failedrerun.txt"
        },
        monochrome = true
)
public class DirectoriesTest extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true) // Enable parallel execution of scenarios
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
