package io.openliberty.guides.beanvalidation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint( validatedBy = { SerialNumberValidator.class })
public @interface SerialNumber {

	String message () default "serial number is not valid." ;
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}