package com.feedbackservice.feedbackservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.feedbackservice.feedbackservice.dtos.FavouritePizzaReturnsDto;
import com.feedbackservice.feedbackservice.models.FavouritePizza;

@Mapper(componentModel = "spring")
public interface FavouritePizzaMapper {
    FavouritePizzaMapper INSTANSE = Mappers.getMapper(FavouritePizzaMapper.class);

    FavouritePizzaReturnsDto toDto(FavouritePizza pizza);
}
