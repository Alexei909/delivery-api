package com.manager.manager.dtos;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PizzaCreateDto {
    private String name;
    private String composition;
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
