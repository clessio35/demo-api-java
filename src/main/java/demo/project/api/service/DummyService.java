package demo.project.api.service;

import org.hamcrest.Matchers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DummyService {

private Response response;

	public static final String TEST_ENDPOINT = "/test";

	public void accessApi() {
		System.out.println("Access api");
		RestAssured.baseURI = "https://dummyjson.com";
	}

	public void sendRequestGetForEndpoint() {
		System.out.println("REQUEST GET -> " + TEST_ENDPOINT );
		response = RestAssured.given().log().body()
				.when().contentType(ContentType.JSON)
				.get(TEST_ENDPOINT);
	}

	public void validateResponseForRequestGet() {
		System.out.println("VALIDATE REPONSE OF Dummy");
		response.then().statusCode(200).log().body()
		.body("status", Matchers.equalTo("ok"))
	    .body("method", Matchers.equalTo("GET"))
	    .extract();
	}

}
