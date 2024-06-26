package com.feedbackservice.feedbackservice.dtos;

import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PizzaReviewsUpdateDto {
    @Size(max = 500, message = "{feedback.pizza.dto.errors.review.size}")
    private String review;
    @Min(value = 1, message = "{feedback.pizza.dto.errors.rating.min}")
    @Max(value = 5, message = "{feedback.pizza.dto.errors.rating.max}")
    private Integer rating;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;

        PizzaReviewsUpdateDto otherDto = (PizzaReviewsUpdateDto) other;

        return Objects.equals(this.getReview(), otherDto.getReview()) &&
                Objects.equals(this.getRating(), otherDto.getRating());
    }  
}
