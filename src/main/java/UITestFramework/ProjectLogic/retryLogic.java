package UITestFramework.ProjectLogic;
import io.restassured.response.Response;
import jdk.jpackage.internal.Log;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class retryLogic implements IRetryAnalyzer {
    private int reTryCount=0;
    private int maxRetryCount=2;
    @Override
    public boolean retry(ITestResult Result) {
        if(reTryCount<maxRetryCount){
            Log.info("Retrying " + Result.getName() + " test with status "
                    + getResultStatusName(Result.getStatus()) + " for the " + (reTryCount+1) + " time(s).");
            reTryCount++;
        }
        return false;
    }
    public String getResultStatusName(int status){
        String resultName=null;
        if(status==1)
            resultName="SUCCESS";
        if(status==2)
            resultName="FAILURE";
        if(status==3)
            resultName = "SKIP";
        return resultName;
    }
    public static boolean executeWithRetry(Function<Map<String, Object>, Response> apiCall, Map<String, Object> jsonBody, Predicate<Response> condition) throws InterruptedException {
        int attempts = 0;
        do {
            if (attempts!=0){
                System.out.println("Attempted API to rehit: "+attempts);
                //Sleep for 1.2sec until next hit
                Thread.sleep(1200);
            }
            Response response = apiCall.apply(jsonBody);
            if (condition.test(response)) {
                return true;
            } else {
                attempts++;
            }
        } while (attempts < 4);
        return false;
    }
}
