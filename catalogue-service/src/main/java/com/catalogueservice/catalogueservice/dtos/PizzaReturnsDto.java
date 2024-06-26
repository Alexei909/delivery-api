package com.catalogueservice.catalogueservice.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PizzaReturnsDto {

    private Long id;
    private String name;
    private String composition;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;

        PizzaReturnsDto pizzaReturnsDto = (PizzaReturnsDto) otherObject;
        return this.id == pizzaReturnsDto.getId() &&
                Objects.equals(this.name, pizzaReturnsDto.getName()) &&
                Objects.equals(this.composition, pizzaReturnsDto.getComposition()) &&
                Double.compare(this.price, pizzaReturnsDto.getPrice()) == 0 &&
                Objects.equals(this.createdAt, pizzaReturnsDto.getCreatedAt()) &&
                Objects.equals(this.updatedAt, pizzaReturnsDto.getUpdatedAt());
    }

    public PizzaReturnsDto(long id) {
        this.id = id;
    }
}
