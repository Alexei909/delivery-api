package com.feedbackservice.feedbackservice.services;

import java.util.List;
import java.util.Optional;

import com.feedbackservice.feedbackservice.dtos.PizzaReviewsCreateDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsReturnsDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsUpdateDto;

public interface PizzaReviewsService {
    List<PizzaReviewsReturnsDto> getAllReviews();
    List<PizzaReviewsReturnsDto> getAllReviewsByPizzaId(Long pizzaId);
    Optional<PizzaReviewsReturnsDto> getReviewById(Long reviewId);
    PizzaReviewsReturnsDto createReview(PizzaReviewsCreateDto dto);
    void updateReview(Long reviewId, PizzaReviewsUpdateDto dto);
    void deleteReview(Long pizzaId);
}
