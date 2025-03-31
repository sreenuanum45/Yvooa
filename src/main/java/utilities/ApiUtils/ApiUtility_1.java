
package utilities.ApiUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility_1 {
    private static final String BASE_URI = "https://api.example.com";

    public static Response getUserData(String userId) {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .basePath("/users/" + userId)
                .get();
    }

    public static Response createUserData(String jsonPayload) {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .basePath("/users")
                .header("Content-Type", "application/json")
                .body(jsonPayload)
                .post();
    }

    public static Response updateUserData(String userId, String jsonPayload) {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .basePath("/users/" + userId)
                .header("Content-Type", "application/json")
                .body(jsonPayload)
                .put();
    }

    public static Response deleteUserData(String userId) {
        return RestAssured.given()
                .baseUri(BASE_URI)
                .basePath("/users/" + userId)
                .delete();
    }

}
