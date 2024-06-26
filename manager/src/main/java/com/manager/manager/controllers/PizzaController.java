package com.manager.manager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.manager.manager.clients.PizzaRestClient;
import com.manager.manager.dtos.PizzaCreateDto;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogue/pizza")
public class PizzaController {

    private final PizzaRestClient pizzaRestClient;

    @GetMapping("list")
    public String getAllPizza(
        Model model,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        model.addAttribute("pizza", this.pizzaRestClient.getAllPizza(filter));
        model.addAttribute("filter", filter);
        return "/catalogue/pizza/list";
    }

    @GetMapping("create")
    public String getPizzaCreatePage() {
        return "/catalogue/pizza/create";
    }

    @PostMapping("create")
    public String createPizza(
        PizzaCreateDto dto,
        Model model,
        HttpServletResponse response
    ) {
        try {
            PizzaReturnsDto pizza = this.pizzaRestClient.createPizza(dto);
            return "redirect:/catalogue/pizza/%d".formatted(pizza.getId());
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", dto);
            model.addAttribute("errors", exception.getErrors());
            return "/catalogue/pizza/create";
        }    
    }
}
