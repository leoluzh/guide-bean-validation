package io.openliberty.guides.beanvalidation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/")
public class BeanValidationEndpoint {

	@Inject
	private Validator validator;
	
	@Inject
	private Spacecraft bean;
	
	@POST
	@Path("/validatespacecraft")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "POST request to validate your spacecraft bean")
	public Response validateSpacecraft( @RequestBody(
			description = "Specify the values to crete the astronaut and spacecraft beans." ,
			content = @Content( 
					mediaType = MediaType.APPLICATION_JSON ,
					schema = @Schema( implementation = Spacecraft.class )
			)) Spacecraft spacecraft ) {
		
		Set<ConstraintViolation<Spacecraft>> violations = 
				validator.validate( spacecraft );
		
		if( violations.size() == 0 ) {
			return Response
					.status(Response.Status.ACCEPTED)
					.encoding(MediaType.TEXT_PLAIN)
					.entity("No constraint violations")
					.build();
		}
		
		String message = violations.stream().map( violation -> {
			return "Constraint Violation Found: " + violation.getMessage() + System.lineSeparator() ; 
		}).collect(Collectors.joining());
		
		
		return Response
				.status(Response.Status.NOT_ACCEPTABLE)
				.encoding(MediaType.TEXT_PLAIN)
				.entity(message)
				.build();
	}
	
	@POST
	@Path("/launchspacecraft")
	@Produces(MediaType.TEXT_PLAIN)
	@Operation( summary = "POST request to specify a launch code")
	public Response launchSpacecraft(
			@RequestBody( description = "Enter the launch code. Must not be null and must equal OpenLiberty for successful launch.",
			content = @Content( mediaType = MediaType.TEXT_PLAIN ))
			String launchCode ) {
		try {
			bean.launchSpacecraft(launchCode);
			return Response
					.status(Response.Status.NOT_ACCEPTABLE)
					.encoding(MediaType.TEXT_PLAIN)
					.entity( "launched" )
					.build();
		}catch( ConstraintViolationException ex ) {
			return Response
					.status(Response.Status.NOT_ACCEPTABLE)
					.encoding(MediaType.TEXT_PLAIN)
					.entity( ex.getMessage() )
					.build();
		}
	}
	
}
