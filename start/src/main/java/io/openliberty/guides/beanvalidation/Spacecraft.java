package io.openliberty.guides.beanvalidation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Named
@RequestScoped



@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder()

public class Spacecraft implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Valid
	@EqualsAndHashCode.Include
	@ToString.Include
	private Astronaut astronaut;
	
	private Map<@NotBlank String, @Positive Integer> destinations; 
	
	@SerialNumber
	@EqualsAndHashCode.Include
	@ToString.Include
	private String serialNumber;
	
	public Spacecraft() {
		destinations = new HashMap<>();
	}
	
	@AssertTrue
	public boolean launchSpacecraft( @NotNull String launchCode ) {
		return "OpenLiberty".equals( launchCode );
	}

}
