package UITestFramework.Util.restAssuredUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.restassured.AllureRestAssured;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class RestAssuredExtensionv {
    private final RequestSpecBuilder builder = new RequestSpecBuilder();
    private final String url;
    private final String method;
    private static Map<String, Double> map = new HashMap<>();
    private static Map<String, Double> time() {
        return map;
    }
    public RestAssuredExtensionv(String uri,String method, String token){
        this.url=uri;
        this.method = method;
        addToken(token);
    }

    public void addToken(String token){
        if(token!=null){
            builder.addHeader("token", token.replace("token", "").trim());
        }else{
            builder.addHeader("Authorization", token);
        }
    }

    public RequestSpecification prepareRequest() {
        return RestAssured.given().filter(new AllureRestAssured()).contentType(ContentType.JSON).spec(builder.build());
    }

  public  ResponseOptions<Response> executeApi(RequestSpecification request) {
      Response response = null;
      switch (this.method.toUpperCase()) {
          case "POST":
              response = request.post(this.url);
              break;
          case "PUT":
              response = request.put(this.url);
              break;
          case "DELETE":
              response = request.delete(this.url);
              break;
          case "GET":
              response = request.get(this.url);
              break;
      }
      map.put(this.url, response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0);
      return response;
  }

    public ResponseOptions<Response> execute() {
        RequestSpecification request = prepareRequest();
        return executeApi(request);
    }

    public ResponseOptions<Response> executeWithBody(Object body) {
        builder.setBody(body);
        return execute();
    }

    public ResponseOptions<Response> executeWithPojo(Object body) {
        builder.setBody(body);
        return execute();
    }

    public ResponseOptions<Response> executeWithQueryParams(Map<String, String> queryParams) {
        builder.addQueryParams(queryParams);
        return execute();
    }
    }
