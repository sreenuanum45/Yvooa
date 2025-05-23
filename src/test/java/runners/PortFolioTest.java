package runners;

import config.PicoContainerConfig;
import io.cucumber.testng.Pickle;
import io.cucumber.testng.TestNGCucumberRunner;

import org.picocontainer.MutablePicoContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;
import utilities.ExcelUtility;
import utilities.TestContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CucumberOptions(
        features = "src/test/resources/features/YvooaPortFlio.feature",
        glue = {"stepDefinitions", "hooks"},

        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "timeline:test-output-thread/",
                "rerun:target/failedrerun.txt"
        },
        monochrome = true
)
public class PortFolioTest  extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true) // Enable parallel execution of scenarios
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
