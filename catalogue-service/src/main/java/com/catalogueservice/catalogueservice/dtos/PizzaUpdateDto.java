package com.catalogueservice.catalogueservice.dtos;

import java.util.Objects;

import com.catalogueservice.catalogueservice.validators.PizzaPriceConstraint;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PizzaUpdateDto {
    
    @Size(min = 6, max = 200, message = "{catalogue.pizza.dto.errors.name.size}")
    private String name;

    @Size(min = 20, max = 2000, message = "{catalogue.pizza.dto.errors.composition.size}")
    private String composition;

    @PizzaPriceConstraint
    private Double price;

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;

        PizzaUpdateDto pizzaUpdateDto = (PizzaUpdateDto) otherObject;
        return Objects.equals(this.name, pizzaUpdateDto.getName()) &&
                Objects.equals(this.composition, pizzaUpdateDto.getComposition()) &&
                Double.compare(this.price, pizzaUpdateDto.getPrice()) == 0;
    }
}
