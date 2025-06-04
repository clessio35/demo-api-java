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
}
