package TestRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "classpath:",
    glue = {"namsateTalk"},
    plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    // TestNG runner for Cucumber. No additional code required.
}
