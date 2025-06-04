package demo.project.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;

import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
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

	public void sendPostRequestForAuthToken(String endpoint) {
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
					.header("Authorization", "Bearer "+ token)
					.when()
					.get(endpoint);
	}

	public void validateResponseForProducts() {
		System.out.println("Validate products");
		response.then().statusCode(200).log().body().extract();
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
			System.out.println("Validated!");
		}
	}

	public void sendGetRequestWithoutAuth(String endpoint) {
		System.out.println("REQUEST GET -> " + endpoint);
		response = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer 2132435565464646")
				.when().log().body()
				.get(endpoint);
	}

	public void validateResponseForProductsWithouToken() {
		System.out.println("Validate products without token");
		response.then().log().body()
			.body("message", Matchers.equalTo("Invalid/Expired Token!"))
			.extract().body();
	}
	
	public JSONObject payloadProduct() {
		Faker faker = new Faker();
		HashMap<String, Object> product = new HashMap<String, Object>();
		product.put("title", faker.commerce().productName());
        product.put("description", faker.lorem().sentence());
        product.put("price", faker.number().numberBetween(10, 100));
        product.put("discountPercentage", faker.number().randomDouble(1, 5, 15));
        product.put("rating", faker.number().randomDouble(2, 3, 5));
        product.put("stock", faker.number().numberBetween(10, 100));
        product.put("brand", faker.company().name());
        product.put("category", "fragrances");
        product.put("thumbnail", faker.internet().url() + ".png");
        JSONObject json = new JSONObject(product);
        return json;
	}

	public void sendPostRequestForCreatingProducts(String endpoint) {
		System.out.println("Creating products");
		response = RestAssured.given()
				.log().body()
				.contentType(ContentType.JSON).when()
				.body(payloadProduct().toString())
				.post(endpoint);
	}

	public void validateResponseForProductCreated() {
		System.out.println("Validate product created");
		response.then().statusCode(201).log().body()
			.body("id", Matchers.instanceOf(Integer.class))
			.body("title", Matchers.instanceOf(String.class))
			.body("price", Matchers.instanceOf(Integer.class))
			.body("stock", Matchers.instanceOf(Integer.class))
			.body("rating", Matchers.notNullValue())
			.body("thumbnail", Matchers.instanceOf(String.class))
			.body("description", Matchers.instanceOf(String.class))
			.body("brand", Matchers.instanceOf(String.class))
			.body("category", Matchers.instanceOf(String.class))
			.extract().body();
	}

	public Integer capturedId() {
		System.out.println("product id");
		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON).when()
				.get("/products");
		response.then().statusCode(200).log().body();
		List<Integer> ids = response.jsonPath().getList("products.id");
		Integer id = ids.get(0);
		System.out.println("id: " + id);
		return id;
	}
	public void sendGetRequestWithID(String endpoint) {
		Integer id = capturedId();
		System.out.println("REQUEST GET -> " + endpoint + " id: " + id);
		response = RestAssured.given()
				.contentType(ContentType.JSON)
				.when().get(endpoint + "/" + id);
	}

	public void validateResponseForProductsWithId() {
		System.out.println("Validate product with id");
		response.then()
			.log().body()
			.body("id", Matchers.instanceOf(Integer.class))
			.body("title", Matchers.instanceOf(String.class))
			.body("price", Matchers.notNullValue())
			.body("stock", Matchers.notNullValue())
			.body("rating", Matchers.notNullValue())
			.body("thumbnail", Matchers.notNullValue())
			.body("description", Matchers.instanceOf(String.class))
			.body("brand", Matchers.instanceOf(String.class))
			.body("category", Matchers.instanceOf(String.class)).extract().response();
	}


}
