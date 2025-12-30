package TestRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = {"classpath:namsateTalk/chat.feature"},
    glue = {"namsateTalk"},
    plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    // Expose the scenarios data provider so TestNG can run them individually (and in parallel if you want)
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

