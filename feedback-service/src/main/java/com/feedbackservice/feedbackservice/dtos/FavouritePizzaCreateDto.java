package com.feedbackservice.feedbackservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouritePizzaCreateDto {
    @NotNull(message = "{feedback.pizza.dto.errors.pizza-id}")
    @Min(value = 1, message = "{feedback.pizza.dto.errors.pizza-id.min}")
    private Long pizzaId;
    // private Long userId;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other.getClass() != this.getClass()) return false;

        FavouritePizzaCreateDto otherDto = (FavouritePizzaCreateDto) other;
        return this.getPizzaId() == otherDto.getPizzaId();
    }
}
