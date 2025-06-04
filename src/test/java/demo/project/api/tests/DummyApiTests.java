package demo.project.api.tests;

import org.junit.Before;
import org.junit.Test;

import demo.project.api.service.DummyService;

public class DummyApiTests {

	DummyService dum = new DummyService();
	
	@Before
	public void requestApi() {
		dum.accessApi();		
	}
	
	@Test
	public void requestGETMethodForEndpointTEST() {
		dum.sendRequestGetForEndpoint();
		dum.validateResponseForRequestGet();
	}
	
	@Test
	public void requestGETMethodForEndpointUSERS() {
		dum.sendRequestGetForEndpointUsers();
		dum.validateResponseForRequestGetUsers();
	}

	
	
	
	
}
