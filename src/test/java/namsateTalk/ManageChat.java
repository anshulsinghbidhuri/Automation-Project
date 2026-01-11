package namsateTalk;
import Main_File.baseTest;
import UITestFramework.URL.namastTalk;
import UITestFramework.Util.restAssuredUtil.RestAssuredExtensionv;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class ManageChat extends baseTest {

    @Getter
    @Setter
   public static class ChatInfo {
        private String userName;
        private String userEmail;
        private String token;
    }

    public static Response singUp() {
        HashMap<String ,Object> jsonBody=new HashMap<>();
        jsonBody.put("fullName","Automation User"+ randomString());
        jsonBody.put("email",randomEmail());
        jsonBody.put("password","123456");
        return (Response) new RestAssuredExtensionv(namastTalk.SIGNUP.chatProject(), "POST", "Bearer ").executeWithBody(jsonBody);
    }
    public static  Response logIn(ChatInfo chatInfo) {
        HashMap<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("email",chatInfo.userEmail );
        jsonBody.put("password", "123456");
        return (Response) new RestAssuredExtensionv(namastTalk.LOGIN.chatProject(), "POST", "Bearer").executeWithBody(jsonBody);
    }

    public static Response me(){
        return (Response) new RestAssuredExtensionv(namastTalk.ME.chatProject(), "GET", "Bearer").execute();
    }

    public static Response logout(){
        return (Response) new RestAssuredExtensionv(namastTalk.LOGOUT.chatProject(), "POST", "Bearer").execute();
    }

    public  static  Response delete(){
        return (Response) new RestAssuredExtensionv(namastTalk.DELETE.chatProject(), "DELETE", "Bearer").execute();
    }

    public static Response onBoarding(ChatInfo chatInfo){
        HashMap<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("fullName",chatInfo.userName);
        jsonBody.put("email",chatInfo.userEmail);
        jsonBody.put("bio","Developer");
        jsonBody.put("nativeLanguage","English");
        jsonBody.put("learningLanguage","Hindi");
        jsonBody.put("location","India");
        return (Response) new RestAssuredExtensionv(namastTalk.ON_BOARDING.chatProject(), "POST",chatInfo.getToken()).executeWithBody(jsonBody);
    }
}
