package com.feedbackservice.feedbackservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouritePizzaReturnsDto {
    private Long id;
    private Long pizzaId;
    // private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other.getClass() != this.getClass()) return false;

        FavouritePizzaReturnsDto otherDto = (FavouritePizzaReturnsDto) other;
        return this.getId() == otherDto.getId() &&
                this.getPizzaId() == otherDto.getPizzaId() &&
                this.getCreatedAt() == otherDto.getCreatedAt() &&
                this.getUpdatedAt() == otherDto.getUpdatedAt();
    }
}
