package demo.project.api.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthService {

    private String token;

    public String[] captureFirstValidCredentials() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/users");
        response.then().statusCode(200);
        List<Map<String, Object>> users = response.jsonPath().getList("users");
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("No user data returned");
        }
        Map<String, Object> firstUser = users.get(0);
        String username = firstUser.get("username").toString();
        String password = firstUser.get("password").toString();
        System.out.println("Username: " + username + " | Password: " + password);
        return new String[]{username, password};
    }

    public String getToken() {
        if (token != null && !token.isEmpty()) {
            return token;
        }
        String[] credentials = captureFirstValidCredentials();
        JSONObject payload = PayloadFactory.payloadAuth(credentials[0], credentials[1]);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload.toString())
                .post("/auth/login");
        response.then().statusCode(200);
        token = response.jsonPath().getString("accessToken");
        System.out.println("Access Token captured: " + token);
        return token;
    }
}
