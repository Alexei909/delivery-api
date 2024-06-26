package com.feedbackservice.feedbackservice.controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.feedbackservice.feedbackservice.dtos.PizzaReviewsCreateDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsReturnsDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsUpdateDto;
import com.feedbackservice.feedbackservice.services.PizzaReviewsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class PizzaReviewsController {

    private final PizzaReviewsService reviewsService;

    @GetMapping
    public List<PizzaReviewsReturnsDto> getAllReviews() {
        return this.reviewsService.getAllReviews();
    }

    @GetMapping("by-pizza-id/{pizzaId:\\d+}")
    public List<PizzaReviewsReturnsDto> getAllReviewsByPizzaId(
        @PathVariable("pizzaId") Long pizzaId
    ) {
        return this.reviewsService.getAllReviewsByPizzaId(pizzaId);
    }

    @GetMapping("{reviewId:\\d+}")
    public PizzaReviewsReturnsDto getReviewById(
        @PathVariable("reviewId") Long reviewId
    ) {
        return this.reviewsService.getReviewById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("feedback.errors.404.not_found"));
    }

    @PostMapping
    public ResponseEntity<PizzaReviewsReturnsDto> createReview(
        @Valid @RequestBody PizzaReviewsCreateDto dto,
        BindingResult bindingResult,
        UriComponentsBuilder uriComponentsBuilder
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } 
        PizzaReviewsReturnsDto review = this.reviewsService.createReview(dto);
        return ResponseEntity.created(uriComponentsBuilder
                        .replacePath("/api/reviews/{reviewId}")
                        .build(Map.of("reviewId", review.getId())))
                .body(review);
    }

    @PutMapping("/{reviewId:\\d+}")
    public ResponseEntity<?> updateReview(
        @PathVariable("reviewId") Long reviewId,
        @Valid @RequestBody PizzaReviewsUpdateDto dto,
        BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            try {
                this.reviewsService.updateReview(reviewId, dto);
                return ResponseEntity.ok().build();
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("feedback.errors.404.not_found");
            }
        }
    }

    @DeleteMapping("/{reviewId:\\d+}")
    public ResponseEntity<?> deleteReview(
        @PathVariable("reviewId") Long reviewId
    ) {
        this.reviewsService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

}
