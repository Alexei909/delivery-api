package com.feedbackservice.feedbackservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.feedbackservice.feedbackservice.dtos.PizzaReviewsReturnsDto;
import com.feedbackservice.feedbackservice.models.PizzaReviews;

@Mapper(componentModel = "spring")
public interface PizzaReviewsMapper {
    PizzaReviewsMapper INSTANSE = Mappers.getMapper(PizzaReviewsMapper.class);

    PizzaReviewsReturnsDto toDto(PizzaReviews pizzaReviews);
}
