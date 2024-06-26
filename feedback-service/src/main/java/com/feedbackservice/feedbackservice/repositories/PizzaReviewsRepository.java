package com.feedbackservice.feedbackservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feedbackservice.feedbackservice.models.PizzaReviews;

public interface PizzaReviewsRepository extends JpaRepository<PizzaReviews, Long> {
    List<PizzaReviews> findAllByPizzaId(Long pizzaId);
}
