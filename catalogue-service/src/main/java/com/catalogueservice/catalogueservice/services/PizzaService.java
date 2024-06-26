package com.catalogueservice.catalogueservice.services;

import java.util.List;
import java.util.Optional;

import com.catalogueservice.catalogueservice.dtos.PizzaCreateDto;
import com.catalogueservice.catalogueservice.dtos.PizzaReturnsDto;
import com.catalogueservice.catalogueservice.dtos.PizzaUpdateDto;

public interface PizzaService {

    PizzaReturnsDto createPizza(PizzaCreateDto dto);

    List<PizzaReturnsDto> findAllPizza(String filter);

    Optional<PizzaReturnsDto> findPizzaById(Long id);

    void updatePizza(Long pizzaId, PizzaUpdateDto dto);

    void deletePizza(Long pizzaId);
}
