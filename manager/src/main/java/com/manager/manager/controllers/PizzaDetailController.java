package com.manager.manager.controllers;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manager.manager.clients.PizzaRestClient;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.dtos.PizzaUpdateDto;
import com.manager.manager.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/catalogue/pizza/{pizzaId:\\d+}")
@RequiredArgsConstructor
public class PizzaDetailController {

    private final PizzaRestClient pizzaRestClient;

    private final MessageSource messageSource;

    @ModelAttribute("pizza")
    public PizzaReturnsDto getPizzaById(
        @PathVariable("pizzaId") Long pizzaId
    ) {
        return this.pizzaRestClient.getPizzaById(pizzaId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.404.not_found"));
    }

    @GetMapping
    public String getPizzaPage() {
        return "/catalogue/pizza/pizza";
    }

    @PostMapping("/delete")
    public String deletePizzaById(
        @ModelAttribute("pizza") PizzaReturnsDto pizza
    ) {
        this.pizzaRestClient.deletePizzaById(pizza.getId());
        return "redirect:/catalogue/pizza/list";
    }

    @GetMapping("/edit")
    public String getEditPizzaPage() {
        return "/catalogue/pizza/edit";
    }

    @PostMapping("/edit")
    public String editPizza(
        @ModelAttribute("pizza") PizzaReturnsDto pizza,
        PizzaUpdateDto dto,
        Model model,
        HttpServletResponse response
    ) {
        try {
            this.pizzaRestClient.editPizza(pizza.getId(), dto);
            return "redirect:/catalogue/pizza/%d".formatted(pizza.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", exception.getErrors());
            return "/catalogue/pizza/edit";
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(
        NoSuchElementException exception,
        Model model,
        HttpServletResponse response,
        Locale locale
    ) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", 
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        
        return "/errors/404";
    }
}
