package Tables;

import Tables.Users.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNotEquals;

public class UsersTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://coms-309-052.class.las.iastate.edu";
        RestAssured.port = 8080;
    }



    @Test
    public void getAllUserTest()
    {
        when().request("GET","/users").then().statusCode(200);
    }
    @Test
    public void postUserTest() {
        with().body(new User("test", "test@email.com", "test", 0))
                .when()
                .request("POST", "/users")
                .then()
                .statusCode(201);
    }
    @Test
    public void getALLBirdinfoTest()
    {
        when().request("GET","/birdInfo").then().statusCode(200);
    }
    @Test
    public void postAnalyticTest() {
        with().body(new User("test", "test@email.com", "test", 0))
                .when()
                .request("POST", "/users")
                .then()
                .statusCode(201);
    }
    @Test
    public void getALLBirdTrackingInfoTest()
    {
        when().request("GET","/birdtrackinginfos").then().statusCode(200);
    }
    @Test
    public void getAllAnalyticsTest() { when().request("GET","/analytics").then().statusCode(200); }
    @Test
    public void whenValidateResponseTime_thenSuccess() {
        when().get("/users").then().time(lessThan(5000L));
    }

    @Test
    public void testUserRolesNotNull() {
        // Send a GET request to retrieve the list of users
        Response response = when().request("GET", "/users");

        List<String> l = response.jsonPath().getList("role");

        // Check if all users have roles that are not null
        for (String s : l) {
            assertNotEquals(null, s);
        }
    }

}
