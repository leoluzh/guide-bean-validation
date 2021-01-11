package io.openliberty.guides.beanvalidation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SerialNumberValidator 
    implements ConstraintValidator<SerialNumber,Object> {

    @Override
    public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
        
    	boolean isValid = false;
        
        if( Objects.isNull( arg0 ) ) {
        	return isValid;
        }
        
        String serialNumber = arg0.toString();
        isValid = serialNumber.length() == 11 && serialNumber.startsWith("Liberty");
        
        try {
        	Integer.parseInt( serialNumber.substring(7) );
        }catch( Exception ex ) {
        	isValid = false;
        }
        
        return isValid;
    }
}
