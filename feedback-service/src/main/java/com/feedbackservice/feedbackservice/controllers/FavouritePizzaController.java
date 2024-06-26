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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.feedbackservice.feedbackservice.dtos.FavouritePizzaCreateDto;
import com.feedbackservice.feedbackservice.dtos.FavouritePizzaReturnsDto;
import com.feedbackservice.feedbackservice.services.FavouritePizzaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favourite-pizza")
@RequiredArgsConstructor
public class FavouritePizzaController {

    private final FavouritePizzaService favouritePizzaService;

    @GetMapping
    public List<FavouritePizzaReturnsDto> getAllFavouritePizza() {
        return this.favouritePizzaService.getAllFavouritePizza();
    }

    @GetMapping("/by-pizza-id/{pizzaId:\\d+}")
    public List<FavouritePizzaReturnsDto> getAllFavouritePizzaByPizzaId(
        @PathVariable("pizzaId") Long pizzaId
    ) {
        return this.favouritePizzaService.getAllFavouritePizzaByPizzaId(pizzaId);
    }

    @GetMapping("/{favouriteId:\\d+}")
    public FavouritePizzaReturnsDto getFavouritePizzaById(
        @PathVariable("favouriteId") Long id
    ) {
        return this.favouritePizzaService.getFavouritePizzaById(id)
                .orElseThrow(() -> new NoSuchElementException("feedback.errors.404.favourite.not_found"));
    }

    @PostMapping
    public ResponseEntity<FavouritePizzaReturnsDto> createFavouritePizza(
        @Valid @RequestBody FavouritePizzaCreateDto dto,
        BindingResult bindingResult,
        UriComponentsBuilder uriComponentsBuilder
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            FavouritePizzaReturnsDto pizza = this.favouritePizzaService.createFavouritePizza(dto);
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/api/favourite-pizza/{favouriteId}")
                            .build(Map.of("favouriteId", pizza.getId())))
                    .body(pizza);
        }
    }

    @DeleteMapping("/{favouriteId:\\d+}")
    public ResponseEntity<?> deleteFavouritePizza(
        @PathVariable("favouriteId") Long id
    ) {
        this.favouritePizzaService.deleteFavouritePizza(id);
        return ResponseEntity.ok().build();
    }

}
