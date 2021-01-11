package io.openliberty.guides.beanvalidation;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Astronaut implements Serializable {

	private static final long serialVersionUID = 1L;

	@ToString.Include
	@NotBlank
	private String name;
	
	@Min(18)
	@Max(120)
	private Integer age;
	
	@ToString.Include
	@EqualsAndHashCode.Include
	@Email
	private String email;
	
}
