package example;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(AllureTestNg.class)
public class AllureTestNGExample {

    @Test
    @Feature("Login")
    @Story("Valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    public void loginTest() {
        // attach plain text
        Allure.addAttachment("Request", "POST /login {user: alice}");

        // attach bytes (e.g., fake screenshot bytes for demo)
        byte[] fakePng = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47};
        Allure.addAttachment("Screenshot", "image/png", new java.io.ByteArrayInputStream(fakePng), ".png");

        Assert.assertTrue(true, "Demo assertion for Allure report");
    }
}
