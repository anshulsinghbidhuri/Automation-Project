package namsateTalk;
import Common.SharedVariables;
import Main_File.baseTest;
import UITestFramework.URL.namastTalk;
import UITestFramework.Util.restAssuredUtil.RestAssuredExtensionv;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.HashMap;
public class ChatSteps  extends baseTest {

    @Given("^User is able to signin in NamsateTalk page$")
    public void user_is_able_to_signin_in_namsatetalk_page() {
        HashMap<String ,Object>jsonBody=new HashMap<>();
        jsonBody.put("fullName","ram beta");
        jsonBody.put("email","ramsingh@gmail.com");
        jsonBody.put("password","987654");
        Response response = (Response) new RestAssuredExtensionv(namastTalk.SIGNUP.chatProject(), "POST", "Bearer " ).executeWithBody(jsonBody);
        testContext.setSharedVariable(SharedVariables.API_RESPONSE, response);
    }
}

