package com.catalogueservice.catalogueservice.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PizzaPriceConstraintValidator.class)
public @interface PizzaPriceConstraint {
    String message() default "{catalogue.pizza.dto.errors.price.negative}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
