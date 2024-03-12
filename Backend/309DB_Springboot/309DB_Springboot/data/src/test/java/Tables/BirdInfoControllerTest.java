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
public class BirdInfoControllerTest {

    @LocalServerPort
    int port;

    private static int birdInfoId;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void a_getAllBirdInfoTest() {
        Response response = RestAssured.get("/birdInfo");

        //Checks Status Code
        assertEquals(200, response.getStatusCode());

        //Checks content type
        assertEquals("application/json", response.getContentType());

        // Parse the response body
        JsonPath jsonResponse = response.jsonPath();

        // Check if the response body is not empty
        assertNotNull(jsonResponse);

        // Check if response is an array
        assertTrue(jsonResponse.getList("$").size() > 0);

        // Check for a specific field in the first object of the response array (if applicable)
        String name = jsonResponse.get("[0].name");
        assertEquals("Acadian Flycatcher", name);
    }

    @Test
    public void b_getBirdInfoByIdTest() {
        Response response = RestAssured.get("/birdInfo/1");

        //Checks Status Code
        assertEquals(200, response.getStatusCode());

        //Checks content type
        assertEquals("application/json", response.getContentType());

        // Parse the response body
        JsonPath jsonResponse = response.jsonPath();

        // Check if the response body is not empty
        assertNotNull(jsonResponse);

        // Check for a specific field
        String name = jsonResponse.get("name");
        assertEquals("Acadian Flycatcher", name);
    }

    @Test
    public void c_createBirdInfoTest() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("scientificName", "Test Scientific Name");
        requestParams.put("name", "Test Name");
        requestParams.put("shortDesc", "Test Short Description");
        requestParams.put("image", "http://example.com/testimage.jpg");
        requestParams.put("rangeMap", "http://example.com/testrangemap.jpg");
        requestParams.put("callSound", "http://example.com/testcallsound.mp3");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/birdInfo");

        // Check status code
        assertEquals(200, response.getStatusCode());

        birdInfoId = Integer.parseInt(response.getBody().asString());
        System.out.println(birdInfoId);
    }

    @Test
    public void d_updateBirdInfoTest() throws JSONException {

        JSONObject requestParams = new JSONObject();
        requestParams.put("scientificName", "Updated Scientific Name");
        requestParams.put("name", "Updated Name");
        requestParams.put("shortDesc", "Updated Short Description");
        requestParams.put("image", "http://example.com/updatedimage.jpg");
        requestParams.put("rangeMap", "http://example.com/updatedrangemap.jpg");
        requestParams.put("callSound", "http://example.com/updatedcallsound.mp3");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestParams.toString())
                .put("/birdInfo/" + birdInfoId);

        assertEquals(200, response.getStatusCode());

        JsonPath jsonResponse = response.jsonPath();

        assertEquals("Updated Scientific Name", jsonResponse.get("scientificName"));
        assertEquals("Updated Name", jsonResponse.get("name"));
        assertEquals("Updated Short Description", jsonResponse.get("shortDesc"));
        assertEquals("http://example.com/updatedimage.jpg", jsonResponse.get("image"));
        assertEquals("http://example.com/updatedrangemap.jpg", jsonResponse.get("rangeMap"));
        assertEquals("http://example.com/updatedcallsound.mp3", jsonResponse.get("callSound"));
    }

    @Test
    public void e_assignBirdTrackingInfoToBirdTest() {
        int birdTrackingInfoID = 1; // Replace with a valid birdTrackingInfoID

        // Send PUT request to assign BirdTrackingInfo to BirdInfo
        Response response = RestAssured.given()
                .put("/birdInfo/" + birdInfoId + "/birdTrackingInfo/" + birdTrackingInfoID);

        // Check status code for success
        assertEquals(200, response.getStatusCode());

        // Validate the response body if necessary
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("success"));
    }

    @Test
    public void f_getBirdInfoBirdTrackingInfoTest() {

        Response response = RestAssured.get("/birdInfo/" + birdInfoId + "/birdTrackingInfo");

        assertEquals(200, response.getStatusCode());

        JsonPath jsonResponse = response.jsonPath();

        assertNotNull(jsonResponse.getList("$"));
    }

    @Test
    public void g_deleteBirdInfoTest() {

        Response response = RestAssured.delete("/birdInfo/" + birdInfoId);

        // Check status code for success
        assertEquals(200, response.getStatusCode());
    }
}