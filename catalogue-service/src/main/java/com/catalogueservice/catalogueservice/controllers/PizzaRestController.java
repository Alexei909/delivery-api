package com.catalogueservice.catalogueservice.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.catalogueservice.catalogueservice.dtos.PizzaCreateDto;
import com.catalogueservice.catalogueservice.dtos.PizzaReturnsDto;
import com.catalogueservice.catalogueservice.exceptions.UniquenessViolationException;
import com.catalogueservice.catalogueservice.services.PizzaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalogue-api/pizza")
@RequiredArgsConstructor
public class PizzaRestController {

    private final PizzaService pizzaService;

    @PostMapping
    public ResponseEntity<PizzaReturnsDto> createPizza(
        @Valid @RequestBody PizzaCreateDto dto,
        BindingResult bindingResult,
        UriComponentsBuilder uriComponentsBuilder
    ) throws BindException, UniquenessViolationException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            try {
                PizzaReturnsDto pizza = this.pizzaService.createPizza(dto);
                return ResponseEntity
                        .created(uriComponentsBuilder
                                .replacePath("/catalogue-api/pizza/{pizzaId}")
                                .build(Map.of("pizzaId", pizza.getId())))
                        .body(pizza);
            } catch (DataIntegrityViolationException e) {
                throw new UniquenessViolationException(e.getMessage());
            }
        }
    }

    @GetMapping
    public List<PizzaReturnsDto> findAllPizza(
        @RequestParam(name = "filter", required = false) String filter
    ) {
        return this.pizzaService.findAllPizza(filter);
    }
}
