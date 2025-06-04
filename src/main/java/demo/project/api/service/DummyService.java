package demo.project.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DummyService {

	private Response response;
	private String username;
	private String password;
	private String token;

	public void accessApi() {
		System.out.println("Access api");
		RestAssured.baseURI = "https://dummyjson.com";
	}

	public void sendGetRequest(String endpoint) {
		System.out.println("REQUEST GET -> " + endpoint);
		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON)
				.when().get(endpoint);
	}

	public void validateResponseForRequestGet() {
		System.out.println("VALIDATE RESPONSE OF Dummy");
		response.then().statusCode(200).log().body().body("status", Matchers.equalTo("ok"))
				.body("method", Matchers.equalTo("GET")).extract();
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

	public void captureTokenData() {
	    response = RestAssured.given().log().body()
	            .when().contentType(ContentType.JSON)
	            .get("/users");
	    response.then().statusCode(200);
	    String userJson = response.jsonPath().getString("users.username");
	    String passJson = response.jsonPath().getString("users.password");
	    userJson = userJson.replaceAll("[\\[\\]\\s]", "");
	    passJson = passJson.replaceAll("[\\[\\]\\s]", "");
	    String[] usernames = userJson.split(",");
	    String[] passwords = passJson.split(",");
	    username = usernames[0];
        password = passwords[0];
	    System.out.println("Username: " + username + " | Password: " + password);
	}

	
	public JSONObject payloadAuth() {
		captureTokenData();
		HashMap<String, Object> user = new HashMap<String, Object>();
		user.put("username", username);
		user.put("password", password);
		JSONObject json = new JSONObject(user);
		return json;
	}

	public void sendPostRequest(String endpoint) {
		System.out.println("Send post request");
		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON).when()
				.body(payloadAuth().toString())
				.post(endpoint);
	}

	public void validateResponseForPostRequest() {
		System.out.println("Validate post request");
		 response.then().statusCode(200).log().body()
	        .body("id", Matchers.instanceOf(Integer.class))
	        .body("username", Matchers.notNullValue())
	        .body("email", Matchers.containsString("@"))
	        .body("firstName", Matchers.notNullValue())
	        .body("lastName", Matchers.notNullValue())
	        .body("gender", Matchers.anyOf(Matchers.is("male"), Matchers.is("female")))
	        .body("accessToken", Matchers.notNullValue())
	        .body("refreshToken", Matchers.notNullValue());
		 String accessToken = response.jsonPath().getString("accessToken");
		 token = accessToken;
		 System.out.println("Extracted Token: " + token);
	}

	public void tokenCaptured() {
		System.out.println("token in process");
		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON).when()
				.body(payloadAuth().toString())
				.post("/auth/login");
		response.then().statusCode(200).log().body();
		 String accessToken = response.jsonPath().getString("accessToken");
		 token = accessToken;
		 System.out.println("Extracted Token: " + token);
	}
	public void sendGetRequestWithAuth(String endpoint) {
			System.out.println("REQUEST GET -> " + endpoint);
			tokenCaptured();
			response = RestAssured.given().log().body()
					.contentType(ContentType.JSON)
					.header("Authorization", "Bearer "+token)
					.when()
					.get(endpoint);
	}

	public void validateResponseForProducts() {
		System.out.println("Validate products");
		response.then().log().body().extract();
		List<Map<String, Object>> products = response.jsonPath().getList("products");
		
		Assert.assertFalse(products.isEmpty());
		for (Map<String, Object> product : products) {
			Assert.assertNotNull(product.get("id"));
			Assert.assertNotNull(product.get("title"));
			Assert.assertNotNull(product.get("description"));
			Assert.assertNotNull(product.get("price"));
			Assert.assertNotNull(product.get("discountPercentage"));
			Assert.assertNotNull(product.get("rating"));
			Assert.assertNotNull(product.get("stock"));
			Assert.assertNotNull(product.get("tags"));
			Assert.assertNotNull(product.get("thumbnail"));
			Assert.assertNotNull(product.get("images"));			
			Object imagesObj = product.get("images");
			Assert.assertNotNull(imagesObj);
		}
	}

}
