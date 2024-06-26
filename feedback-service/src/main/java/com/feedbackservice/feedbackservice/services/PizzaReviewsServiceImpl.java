package com.feedbackservice.feedbackservice.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feedbackservice.feedbackservice.dtos.PizzaReviewsCreateDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsReturnsDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsUpdateDto;
import com.feedbackservice.feedbackservice.mappers.PizzaReviewsMapper;
import com.feedbackservice.feedbackservice.models.PizzaReviews;
import com.feedbackservice.feedbackservice.repositories.PizzaReviewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PizzaReviewsServiceImpl implements PizzaReviewsService {

    private final PizzaReviewsRepository reviewsRepository;
    private final PizzaReviewsMapper reviewsMapper;

    @Override
    public List<PizzaReviewsReturnsDto> getAllReviews() {
        List<PizzaReviews> reviews = this.reviewsRepository.findAll();
        return reviews.stream()
                .map(this.reviewsMapper::toDto)
                .toList();
    }

    @Override
    public List<PizzaReviewsReturnsDto> getAllReviewsByPizzaId(Long pizzaId) {
        List<PizzaReviews> reviews = this.reviewsRepository.findAllByPizzaId(pizzaId);
        return reviews.stream()
                .map(this.reviewsMapper::toDto)
                .toList();
    }

    @Override
    public Optional<PizzaReviewsReturnsDto> getReviewById(Long reviewId) {
        Optional<PizzaReviews> review = this.reviewsRepository.findById(reviewId);

        if (review.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(this.reviewsMapper.toDto(review.get()));
        }
    }

    @Override
    public PizzaReviewsReturnsDto createReview(PizzaReviewsCreateDto dto) {
        PizzaReviews review = this.reviewsRepository.save(
                new PizzaReviews(
                        null, dto.getReview(), dto.getRating(), dto.getPizzaId(), null, null));
        return this.reviewsMapper.toDto(review);
    }

    @Transactional
    @Override
    public void updateReview(Long reviewId, PizzaReviewsUpdateDto dto) {
        this.reviewsRepository.findById(reviewId)
                .ifPresentOrElse(review -> {
                        if (dto.getRating() != null) review.setRating(dto.getRating());
                        if (dto.getReview() != null) review.setReview(dto.getReview());
                }, () -> {throw new NoSuchElementException();});
    }

    @Override
    public void deleteReview(Long pizzaId) {
        this.reviewsRepository.deleteById(pizzaId);
    }

}
