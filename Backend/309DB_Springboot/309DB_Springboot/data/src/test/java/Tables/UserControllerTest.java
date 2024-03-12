package Tables;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.path.json.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @LocalServerPort
    int port;

    private static int userId;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void a_createUserTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "TestUser");
        requestParams.put("email", "testuser@example.com");
        requestParams.put("password", "password");
        requestParams.put("privilege", 1);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/users");

        assertEquals(200, response.getStatusCode());
        userId = Integer.parseInt(response.getBody().asString());
        System.out.println(userId);
    }

    @Test
    public void b_getAllUsersTest() {
        Response response = RestAssured.get("/users");

        assertEquals(200, response.getStatusCode());
        JsonPath jsonResponse = response.jsonPath();
        assertTrue(jsonResponse.getList("$").size() > 0);
    }

    @Test
    public void c_getUserByIdTest() {
        Response response = RestAssured.get("/users/" + userId);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void d_updateUserTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "UpdatedUser");
        requestParams.put("email", "updateduser@example.com");
        requestParams.put("password", "newpassword");
        requestParams.put("privilege", 2);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestParams.toString())
                .put("/users/" + userId);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void e_assignBirdTrackingInfoToUserTest() {
        int birdTrackingInfoID = 1; // replace with a valid birdTrackingInfoID

        Response response = RestAssured.given()
                .put("/users/" + userId + "/birdTrackingInfo/" + birdTrackingInfoID);

        assertEquals(200, response.getStatusCode());

        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        assertTrue(responseBody.contains("{\"message\":\"success\"}"));
    }

    @Test
    public void f_assignAnalyticToUserTest() {
        int analyticID = 1; // replace with a valid analyticID

        Response response = RestAssured.given()
                .put("/users/" + userId + "/analytic/" + analyticID);

        assertEquals(200, response.getStatusCode());

        //String responseBody = response.getBody().asString();
        //assertTrue(responseBody.contains("{\"message\":\"success\"}"));
    }

    // Test for retrieving a user's bird tracking information
    @Test
    public void g_getUserBirdTrackingInfoTest() {
        Response response = RestAssured.get("/users/" + userId + "/birdTrackingInfo");

        assertEquals(200, response.getStatusCode());
    }

    // Test for retrieving a user's analytics
    @Test
    public void h_getUserAnalyticsTest() {
        Response response = RestAssured.get("/users/" + userId + "/analytics");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void i_deleteUserTest() {
        Response response = RestAssured.delete("/users/" + userId);

        assertEquals(200, response.getStatusCode());
    }
}