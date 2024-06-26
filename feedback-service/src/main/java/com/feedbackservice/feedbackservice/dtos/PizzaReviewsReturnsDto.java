package com.feedbackservice.feedbackservice.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PizzaReviewsReturnsDto {
    private Long id;
    private String review;
    private Integer rating;
    private Long pizzaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;

        PizzaReviewsReturnsDto otherDto = (PizzaReviewsReturnsDto) other;

        return this.getId() == otherDto.getId() &&
                Objects.equals(this.getReview(), otherDto.getReview()) &&
                Objects.equals(this.getRating(), otherDto.getRating()) &&
                Objects.equals(this.getPizzaId(), otherDto.getPizzaId()) &&
                Objects.equals(this.getCreatedAt(), otherDto.getCreatedAt()) &&
                Objects.equals(this.getUpdatedAt(),otherDto.getUpdatedAt());
    }   
}
