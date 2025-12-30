package UITestFramework.Util.restAssuredUtil;
import io.restassured.response.Response;
import java.util.Map;

public class ResponseValidator {
    public static void matchAllKeys(Response response, Map<String, Object> expectedValues) {
        Map<String, Object> actualValues = response.jsonPath().getMap("$");
        for (String key : expectedValues.keySet()) {
            if (key.equals("ignore")) {
                continue;
            }
            if (!actualValues.containsKey(key)) {
                throw new AssertionError("Expected key '" + key + "' not found in response.");
            }
            if (!expectedValues.get(key).equals(actualValues.get(key))) {
                throw new AssertionError("Expected value for key '" + key + "' was '" + expectedValues.get(key) + "', but actual value was '" + actualValues.get(key) + "'.");
            }
        }
    }
}
