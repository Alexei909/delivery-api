package com.catalogueservice.catalogueservice.controllers;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalogueservice.catalogueservice.dtos.PizzaReturnsDto;
import com.catalogueservice.catalogueservice.dtos.PizzaUpdateDto;
import com.catalogueservice.catalogueservice.exceptions.UniquenessViolationException;
import com.catalogueservice.catalogueservice.services.PizzaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/catalogue-api/pizza/{pizzaId:\\d+}")
@RequiredArgsConstructor
public class PizzaDetailRestController {

    private final PizzaService pizzaService;

    private final MessageSource messageSource;

    @ModelAttribute("pizza")
    public Optional<PizzaReturnsDto> getPizzaById(@PathVariable("pizzaId") Long id) {
        return this.pizzaService.findPizzaById(id);
    }

    @GetMapping
    public PizzaReturnsDto findPizza(
        @ModelAttribute("pizza") PizzaReturnsDto pizza
    ) {return pizza;}

    @PutMapping
    public ResponseEntity<Void> updatePizza(
        @ModelAttribute("pizza") PizzaReturnsDto pizza,
        @Valid @RequestBody PizzaUpdateDto dto,
        BindingResult bindingResult
    ) throws BindException, UniquenessViolationException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            try {
                this.pizzaService.updatePizza(pizza.getId(), dto);
                return ResponseEntity.ok().build();
            } catch (DataIntegrityViolationException e) {
                throw new UniquenessViolationException(e.getMessage());
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePizza(
        @PathVariable("pizzaId") Long pizzaId
    ) {
        this.pizzaService.deletePizza(pizzaId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
        NoSuchElementException exception,
        Locale locale
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,    
                        this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale)));
    }
}
