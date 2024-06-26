package com.catalogueservice.catalogueservice.dtos;

import java.util.Objects;

import com.catalogueservice.catalogueservice.validators.PizzaPriceConstraint;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PizzaCreateDto {

    @NotNull(message = "{catalogue.pizza.dto.errors.name.empty}")
    @Size(min = 6, max = 200, message = "{catalogue.pizza.dto.errors.name.size}")
    private String name;

    @NotNull(message = "{catalogue.pizza.dto.errors.composition.empty}")
    @Size(min = 20, max = 2000, message = "{catalogue.pizza.dto.errors.composition.size}")
    private String composition;

    @NotNull(message = "{catalogue.pizza.dto.errors.price.empty}")
    @PizzaPriceConstraint
    private Double price;

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;

        PizzaCreateDto pizzaCreateDto = (PizzaCreateDto) otherObject;
        return Objects.equals(this.name, pizzaCreateDto.getName()) &&
                Objects.equals(this.composition, pizzaCreateDto.getComposition()) &&
                Double.compare(this.price, pizzaCreateDto.getPrice()) == 0;
    }
}
