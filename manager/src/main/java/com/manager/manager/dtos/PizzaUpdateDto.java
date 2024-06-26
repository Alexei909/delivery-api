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
public class PizzaUpdateDto {
    private String name;
    private String composition;
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
