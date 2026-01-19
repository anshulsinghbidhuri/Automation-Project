package namsateTalk;
import Common.SharedVariables;
import Main_File.baseTest;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class ChatSteps extends baseTest {
    ManageChat.ChatInfo chatInfo = new ManageChat.ChatInfo();

    @Given("^User is able to signin in NamsateTalk page$")
    public void user_is_able_to_signin_in_namsatetalk_page() {
       Response response= ManageChat.singUp();
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(201, response.getStatusCode());
        Assert.assertEquals("User created successfully!", response.jsonPath().getString("message"));
        chatInfo.setUserName(response.jsonPath().get("user.fullName"));
        chatInfo.setUserEmail(response.jsonPath().get("user.email"));

    }

    @When("^User enters Valid Email and Password for Login$")
    public void user_enters_valid_email_and_password_for_login() throws InterruptedException {
        Response response = ManageChat.logIn(chatInfo);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(200, response.getStatusCode());
        Assert .assertEquals("Login successful!", response.jsonPath().getString("message"));
        chatInfo.setToken(response.getCookie("jwt"));
    }

    @Then("^User checks the status for Onboarding$")
    public void user_checks_the_status_for_onboarding() {
        Response response = ManageChat.me(chatInfo);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(200, response.getStatusCode());
    }

@And ("^User Proceeds for Onboarding resgetartion")
    public void user_proceeds_for_onboarding_resgetartion() {
        Response response = ManageChat.onBoarding(chatInfo);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Then ("^User logs out from the application$")
    public void user_logs_out_from_the_application() {
        Response response = ManageChat.logout(chatInfo);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(200, response.getStatusCode());
    }
    @And("^User Deletes the account the application")
    public void user_deletes_the_account() {
        Response response = ManageChat.delete(chatInfo);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
        Assert.assertEquals(200, response.getStatusCode());
    }
}

