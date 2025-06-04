package demo.project.api.service;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DummyService {

    private Response response;

    public void accessApi() {
        System.out.println("Access api");
        RestAssured.baseURI = "https://dummyjson.com";
    }

    public void sendGetRequest(String endpoint) {
        System.out.println("REQUEST GET -> " + endpoint);
        response = RestAssured.given()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .get(endpoint);
    }

    public void validateResponseForRequestGet() {
        System.out.println("VALIDATE RESPONSE OF Dummy");
        response.then().statusCode(200).log().body()
            .body("status", Matchers.equalTo("ok"))
            .body("method", Matchers.equalTo("GET"))
            .extract();
    }

    public void validateResponseForRequestGetUsers() {
        System.out.println("VALIDATE RESPONSE OF Dummy /users");
        response.then().statusCode(200).log().body().extract();

        List<Map<String, Object>> users = response.jsonPath().getList("users");
        Assert.assertFalse(users.isEmpty());

        for (Map<String, Object> user : users) {
            Assert.assertNotNull(user.get("id"));
            Assert.assertTrue(user.get("id") instanceof Integer);

            Assert.assertNotNull(user.get("firstName"));
            Assert.assertFalse(user.get("firstName").toString().trim().isEmpty());

            Assert.assertNotNull(user.get("lastName"));
            Assert.assertFalse(user.get("lastName").toString().trim().isEmpty());

            Assert.assertNotNull(user.get("age"));
            int age = Integer.parseInt(user.get("age").toString());
            Assert.assertTrue(age > 0 && age <= 99);

            Assert.assertNotNull(user.get("email"));
            String email = user.get("email").toString();
            Assert.assertTrue(email.contains("@"));

            Assert.assertNotNull(user.get("username"));
            String username = user.get("username").toString();
            Assert.assertFalse(username.trim().isEmpty());

            Assert.assertNotNull(user.get("password"));
            String password = user.get("password").toString();
            Assert.assertFalse(password.trim().isEmpty());

            System.out.println("User -> " + username + " - Password -> " + password);

            Object addressObj = user.get("address");
            Assert.assertNotNull(addressObj);
            Assert.assertTrue(addressObj instanceof Map);

            @SuppressWarnings("unchecked")
            Map<String, Object> address = (Map<String, Object>) addressObj;
            Assert.assertNotNull(address.get("address"));
            Assert.assertFalse(address.get("address").toString().trim().isEmpty());

            Assert.assertNotNull(address.get("city"));
            Assert.assertFalse(address.get("city").toString().trim().isEmpty());

            Assert.assertNotNull(address.get("state"));
            Assert.assertFalse(address.get("state").toString().trim().isEmpty());

            System.out.println("Validate user ID: " + user.get("id"));
        }
    }

}
