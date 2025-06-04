package demo.project.api.tests;

import org.junit.Before;
import org.junit.Test;

import demo.project.api.service.DummyService;

public class DummyApiTests {

    DummyService dum = new DummyService();

    @Before
    public void setup() {
        dum.accessApi();        
    }

    @Test
    public void requestGETMethodForEndpointTEST() {
        dum.sendGetRequest("/test");
        dum.validateResponseForRequestGet();
    }

    @Test
    public void requestGETMethodForEndpointUSERS() {
        dum.sendGetRequest("/users");
        dum.validateResponseForRequestGetUsers();
    }
    
    @Test
    public void requestPOSTAuthToken() {
    	dum.sendPostRequestForAuthToken("/auth/login");
    	dum.validateResponseForPostRequest();
    }
    
    @Test
    public void requestGetAuthProducts() {
    	dum.sendGetRequestWithAuth("/auth/products");
    	dum.validateResponseForProducts();
    }
    
    @Test
    public void requestGetWithouAuthProducts() {
    	dum.sendGetRequestWithoutAuth("/auth/products");
    	dum.validateResponseForProductsWithouToken();
    }
    
    @Test
    public void requestPostForCreatingProducts() {
    	dum.sendPostRequestForCreatingProducts("/products/add");
    	dum.validateResponseForProductCreated();
    }
    
    @Test
    public void requestGetForProductsCreated() {
    	dum.sendGetRequest("/products");
    	dum.validateResponseForProducts();
    }
    
    @Test
    public void requestGetForSpecificProductWithID() {
    	dum.sendGetRequestWithID("/products");
    	dum.validateResponseForProductsWithId();
    }
    
    @Test
    public void requestGetInvalidId() {
    	dum.sendGetRequest("/products/0");
    	dum.validateResponseWithInvalidId();
    }
}
