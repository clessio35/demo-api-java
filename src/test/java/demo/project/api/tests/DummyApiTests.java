package demo.project.api.tests;

import org.junit.Test;

import demo.project.api.service.DummyService;

public class DummyApiTests {

	DummyService dum = new DummyService();
	
	@Test
	public void requestGETMethodForEndpointTEST() {
		dum.accessApi();
		dum.sendRequestGetForEndpoint();
		dum.validateResponseForRequestGet();
	}
	

	
	
	
	
}
