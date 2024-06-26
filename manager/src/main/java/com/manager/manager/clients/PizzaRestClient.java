package com.manager.manager.clients;

import java.util.List;
import java.util.Optional;

import com.manager.manager.dtos.PizzaCreateDto;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.dtos.PizzaUpdateDto;

public interface PizzaRestClient {

    List<PizzaReturnsDto> getAllPizza(String filter);

    PizzaReturnsDto createPizza(PizzaCreateDto dto);

    Optional<PizzaReturnsDto> getPizzaById(Long pizzaId);

    void deletePizzaById(Long pizzaId);

    void editPizza(Long id, PizzaUpdateDto dto);
}
