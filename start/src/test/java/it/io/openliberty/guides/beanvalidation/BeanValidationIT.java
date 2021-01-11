package it.io.openliberty.guides.beanvalidation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.openliberty.guides.beanvalidation.Astronaut;
import io.openliberty.guides.beanvalidation.Spacecraft;

public class BeanValidationIT {

	private Client client;
	private String port;
	
	@BeforeEach
	public void setup() {
		client = ClientBuilder.newClient();
		port = System.getProperty("http.port");
	}
	
	@AfterEach
	public void teardown() {
		client.close();
	}
	
	@Test
	public void testNoFieldLevelConstraintViolations() throws Exception {
		
        Astronaut astronaut = new Astronaut();
        astronaut.setAge(25);
        astronaut.setEmail("libby@openliberty.io");
        astronaut.setName("Libby");
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setAstronaut(astronaut);
        spacecraft.setSerialNumber("Liberty1001");
        HashMap<String, Integer> destinations = new HashMap<String, Integer>();
        destinations.put("Mars", 1500);
        destinations.put("Pluto", 10000);
        spacecraft.setDestinations(destinations);

        Jsonb jsonb = JsonbBuilder.create();
        String spacecraftJSON = jsonb.toJson(spacecraft);
        Response response = postResponse(getURL(port, "validatespacecraft"),
                spacecraftJSON, false);
        String actualResponse = response.readEntity(String.class);
        String expectedResponse = "No Constraint Violations";

        assertEquals(expectedResponse, actualResponse,
                "Unexpected response when validating beans.");
	}

	@Test
	public void testFieldLevelConstraintViolation() throws Exception {
		
        Astronaut astronaut = new Astronaut();
        astronaut.setAge(25);
        astronaut.setEmail("libby");
        astronaut.setName("Libby");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setAstronaut(astronaut);
        spacecraft.setSerialNumber("Liberty123");

        HashMap<String, Integer> destinations = new HashMap<String, Integer>();
        destinations.put("Mars", -100);
        spacecraft.setDestinations(destinations);

        Jsonb jsonb = JsonbBuilder.create();
        String spacecraftJSON = jsonb.toJson(spacecraft);
        Response response = postResponse(getURL(port, "validatespacecraft"),
                spacecraftJSON, false);
        String actualResponse = response.readEntity(String.class);
        String expectedDestinationResponse = "must be greater than 0";
        assertTrue(actualResponse.contains(expectedDestinationResponse),
                "Expected response to contain: " + expectedDestinationResponse);
        String expectedEmailResponse = "must be a well-formed email address";
        assertTrue(actualResponse.contains(expectedEmailResponse),
                "Expected response to contain: " + expectedEmailResponse);
        String expectedSerialNumberResponse = "serial number is not valid";
        assertTrue(actualResponse.contains(expectedSerialNumberResponse),
                "Expected response to contain: " + expectedSerialNumberResponse);
	    
	}
	    
    @Test
    public void testNoMethodLevelConstraintViolations() throws Exception {
        String launchCode = "OpenLiberty";
        Response response = postResponse(getURL(port, "launchspacecraft"),launchCode, true);
        String actualResponse = response.readEntity(String.class);
        String expectedResponse = "launched";
        assertEquals(expectedResponse, actualResponse,
                "Unexpected response from call to launchSpacecraft");
    }
	
    @Test
    public void testMethodLevelConstraintViolation() throws Exception {
        String launchCode = "incorrectCode";
        Response response = postResponse(getURL(port, "launchspacecraft"),launchCode, true);
        String actualResponse = response.readEntity(String.class);
        assertTrue(
                actualResponse.contains("must be true"),
                "Unexpected response from call to launchSpacecraft");
    }	
    
	private Response postResponse( String url , String value , boolean isMethodLevel ) {
		if( isMethodLevel ) {
			return client.target( url ).request().post( Entity.text( value ) );
		}else {
			return client.target( url ).request().post( Entity.entity( value , MediaType.APPLICATION_JSON ) );
		}
	}
	
	private String getURL( String port , String function ) {
		return "http://localhost:" + port + "/Spacecraft/beanvalidation/" + function;	
	}
	
}
