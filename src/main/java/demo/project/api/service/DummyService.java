package demo.project.api.service;

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
	private String token;

	private final AuthService authService = new AuthService();

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
		response.then().statusCode(200).log().body()
				.body("status", Matchers.equalTo("ok"))
				.body("method", Matchers.equalTo("GET")).extract();
	}

	public void validateResponseForRequestGetUsers() {
		System.out.println("VALIDATE RESPONSE OF Dummy /users");
		response.then().statusCode(200).log().body();

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
			Assert.assertTrue(user.get("email").toString().contains("@"));

			Assert.assertNotNull(user.get("username"));
			Assert.assertNotNull(user.get("password"));

			Object addressObj = user.get("address");
			Assert.assertTrue(addressObj instanceof Map);

			@SuppressWarnings("unchecked")
			Map<String, Object> address = (Map<String, Object>) addressObj;
			Assert.assertFalse(address.get("address").toString().trim().isEmpty());
			Assert.assertFalse(address.get("city").toString().trim().isEmpty());
			Assert.assertFalse(address.get("state").toString().trim().isEmpty());
			System.out.println("Validated user: " + user.get("username"));
		}
	}

	public void sendPostRequestForAuthToken(String endpoint) {
		System.out.println("Send POST for Auth Token");
		String[] credentials = authService.captureFirstValidCredentials();
		JSONObject payload = PayloadFactory.payloadAuth(credentials[0], credentials[1]);
		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON)
				.body(payload.toString())
				.post(endpoint);
	}

	public void validateResponseForPostRequest() {
		System.out.println("Validate Auth POST Response");

		response.then().statusCode(200).log().body()
				.body("id", Matchers.instanceOf(Integer.class))
				.body("username", Matchers.notNullValue())
				.body("email", Matchers.containsString("@"))
				.body("firstName", Matchers.notNullValue())
				.body("lastName", Matchers.notNullValue())
				.body("gender", Matchers.anyOf(Matchers.is("male"), Matchers.is("female")))
				.body("accessToken", Matchers.notNullValue())
				.body("refreshToken", Matchers.notNullValue());

		token = response.jsonPath().getString("accessToken");
		System.out.println("Extracted Token: " + token);
	}

	public void sendGetRequestWithAuth(String endpoint) {
		System.out.println("REQUEST GET with token -> " + endpoint);

		token = authService.getToken();

		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.when().get(endpoint);
	}

	public void validateResponseForProducts() {
		System.out.println("Validate products");
		response.then().statusCode(200).log().body();

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
			System.out.println("Validated product: " + product.get("title"));
		}
	}

	public void sendGetRequestWithoutAuth(String endpoint) {
		System.out.println("REQUEST GET -> " + endpoint);
		response = RestAssured.given()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer invalid_token_123")
				.when().log().body()
				.get(endpoint);
	}

	public void validateResponseForProductsWithouToken() {
		System.out.println("Validate products without token");
		response.then().log().body()
				.body("message", Matchers.equalTo("Invalid/Expired Token!"))
				.extract();
	}

	public void sendPostRequestForCreatingProducts(String endpoint) {
		System.out.println("Creating product");

		token = authService.getToken();
		JSONObject productPayload = PayloadFactory.payloadProduct();

		response = RestAssured.given()
				.log().body()
				.contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + token)
				.body(productPayload.toString())
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
				.extract();
	}

	public Integer capturedId() {
		System.out.println("Capturing product ID");

		response = RestAssured.given().log().body()
				.contentType(ContentType.JSON)
				.get("/products");

		response.then().statusCode(200).log().body();
		List<Integer> ids = response.jsonPath().getList("products.id");
		Integer id = ids.get(0);

		System.out.println("Captured ID: " + id);
		return id;
	}

	public void sendGetRequestWithID(String endpoint) {
		Integer id = capturedId();
		System.out.println("REQUEST GET -> " + endpoint + "/" + id);
		response = RestAssured.given()
				.contentType(ContentType.JSON)
				.get(endpoint + "/" + id);
	}

	public void validateResponseForProductsWithId() {
		System.out.println("Validate product by ID");
		response.then().statusCode(200).log().body()
				.body("id", Matchers.instanceOf(Integer.class))
				.body("title", Matchers.instanceOf(String.class))
				.body("price", Matchers.notNullValue())
				.body("stock", Matchers.notNullValue())
				.body("rating", Matchers.notNullValue())
				.body("thumbnail", Matchers.notNullValue())
				.body("description", Matchers.instanceOf(String.class))
				.body("brand", Matchers.instanceOf(String.class))
				.body("category", Matchers.instanceOf(String.class))
				.extract();
	}

	public void validateResponseWithInvalidId() {
		System.out.println("Validate response with invalid ID");
		response.then().statusCode(404).log().body()
				.body("message", Matchers.equalTo("Product with id '0' not found"))
				.extract();
	}
}
