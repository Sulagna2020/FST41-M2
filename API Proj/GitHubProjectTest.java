
package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class GitHubProjectTest {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    int idSSH;
    String KeySSH = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCTwZgskuRsR9GgReKTxGT6VEVFAxpWHPrgckXqQFBadfaLT+qoPx9X2498YzYClQfyUaTQdi6eUGAmHvuaZabw19b7gO0Gjs/Z56mDckkX6do5yKqgrstnIwontp7f3+WZrYbMdzfq82SbIAHh07UGUuqA7tHFTYfC+udRWThci0Z7AKcenyqKvfxL53yefEjb0tewjFIxx4GFZwIsLmD4QE3a4FTK4v73w1GJGT/fJpgDNB8ihz72+O0k94vNE08+KD5Ri7hTTPTsMOSVdnYofJijoNoMff8/2+5cEOOu2bJ5Qpl1+U6tFat3HHME9heSWOJtM1b45XNG9IMIfo2B";

    @BeforeClass
    public void setUp() {
        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://api.github.com").
                setContentType(ContentType.JSON).addHeader("Authorization", "token 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").
                build();

        responseSpec = new ResponseSpecBuilder().
                build();
    }


    @Test(priority=1)
    public void addSshKey()
    {
//Request Body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key", KeySSH);

        Response response = given().log().all().spec(requestSpec).body(reqBody).when().post("/user/keys");
        System.out.println(response.getBody().asPrettyString());

        idSSH = response.then().extract().path("id");
        System.out.println(idSSH);

        response.then().log().all().spec(responseSpec).statusCode(201);
    }

    @Test(priority=2)
    public void getRequestSSH() {
        given().log().all().spec(requestSpec).pathParam("id", idSSH).
                when().get("/user/keys/{id}").
                then().log().all().spec(responseSpec).statusCode(200);

    }

    @Test(priority=3)
    public void deleteRequestSSH() {
        given().log().all().spec(requestSpec).pathParam("id", idSSH).
                when().delete("/user/keys/{id}").
                then().log().all().spec(responseSpec).statusCode(204);
    }
}