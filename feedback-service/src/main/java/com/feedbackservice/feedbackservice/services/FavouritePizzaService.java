package com.feedbackservice.feedbackservice.services;

import java.util.List;
import java.util.Optional;

import com.feedbackservice.feedbackservice.dtos.FavouritePizzaCreateDto;
import com.feedbackservice.feedbackservice.dtos.FavouritePizzaReturnsDto;

public interface FavouritePizzaService {
    List<FavouritePizzaReturnsDto> getAllFavouritePizza();
    List<FavouritePizzaReturnsDto> getAllFavouritePizzaByPizzaId(Long pizzaId);
    // List<FavouritePizzaReturnsDto> getAllFavouritePizzaByUserId();
    Optional<FavouritePizzaReturnsDto> getFavouritePizzaById(Long id);
    FavouritePizzaReturnsDto createFavouritePizza(FavouritePizzaCreateDto dto);
    void deleteFavouritePizza(Long id);
}
