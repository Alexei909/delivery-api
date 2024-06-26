package com.catalogueservice.catalogueservice.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PizzaPriceConstraintValidator implements ConstraintValidator<PizzaPriceConstraint, Double> {

    @Override
    public boolean isValid(Double price, ConstraintValidatorContext arg1) {
        return price > 0;
    }

}
