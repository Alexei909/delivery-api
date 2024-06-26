package com.feedbackservice.feedbackservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feedbackservice.feedbackservice.models.FavouritePizza;

public interface FavouritePizzaRepository extends JpaRepository<FavouritePizza, Long> {
    List<FavouritePizza> findAllByPizzaId(Long pizzaId);
}   
