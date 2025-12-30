package Main_File;

import Common.TestContext;

public class baseTest {
    protected TestContext testContext = new TestContext();
public String randomStraing() {
    StringBuilder sb = new StringBuilder();
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (int i = 0; i < 10; i++) {
        int index = (int) (characters.length() * Math.random());
        sb.append(characters.charAt(index));
    }
    return sb.toString();
}

public String randomNumber(String countryCode) {
    StringBuilder sb = new StringBuilder();
    String characters = "0123456789";
    for (int i = 0; i < 10; i++) {
        int index = (int) (characters.length() * Math.random());
        sb.append(characters.charAt(index));
    }
    return sb.toString();
}

public String randomEmail() {
    StringBuilder sb = new StringBuilder();
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (int i = 0; i < 10; i++) {
        int index = (int) (characters.length() * Math.random());
        sb.append(characters.charAt(index));
    }
    sb.append("@example.com");
    return sb.toString();
}



}
