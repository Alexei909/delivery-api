package com.manager.manager.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;

import com.manager.manager.clients.PizzaRestClient;
import com.manager.manager.dtos.PizzaCreateDto;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.exceptions.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class PizzaControllerTest {
    
    @Mock
    PizzaRestClient pizzaRestClient;

    @InjectMocks
    PizzaController pizzaController;

    @Test
    void getPizzaCreatePage_ReturnsPizzaCreatePage() {
        //given
        // when
        var result = this.pizzaController.getPizzaCreatePage();

        //then
        assertEquals("/catalogue/pizza/create", result);
    }

    @Test
    void createPizza_RequestIsValid_RedirectionToPizzaPage() {
        // given
        var dto = new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doReturn(new PizzaReturnsDto(
                (long) 1, 
                "Pizza 1", 
                "More more more Mushrooms", 
                1000.0, null, null))
                .when(this.pizzaRestClient).createPizza(
                        new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0));

        // when
        var result = this.pizzaController.createPizza(dto, model, response);

        // then
        assertEquals("redirect:/catalogue/pizza/1", result);
        
        verify(this.pizzaRestClient)
                .createPizza(new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void createPizza_RequestIsInvalidZeroValues_ReturnsPageWithErrors() {
        //given
        var dto = new PizzaCreateDto();
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2", "Ошибка 3")))
                .when(this.pizzaRestClient)
                .createPizza(dto);

        //when
        var result = this.pizzaController.createPizza(dto, model, response);

        //then
        assertEquals("/catalogue/pizza/create", result);
        assertEquals(dto, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2", "Ошибка 3"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).createPizza(dto);
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void createPizza_RequestIsInvalidLengthViolation_ReturnsPageWithErrors() {
        //given
        var dto = new PizzaCreateDto("Piz", "Some", 1000.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2", "Ошибка 3")))
                .when(this.pizzaRestClient)
                .createPizza(new PizzaCreateDto("Piz", "Some", 1000.0));

        //when
        var result = this.pizzaController.createPizza(dto, model, response);

        //then
        assertEquals("/catalogue/pizza/create", result);
        assertEquals(dto, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2", "Ошибка 3"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).createPizza(dto);
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void craatePizza_RequestIsInvalidPriceNegative_ReturnsPageWithErrors() {
        //given
        var dto = new PizzaCreateDto("Pizza 1", "More more more Mushrooms", -1000.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Цена не может быть отрицателньой")))
                .when(this.pizzaRestClient)
                .createPizza(new PizzaCreateDto("Pizza 1", "More more more Mushrooms", -1000.0));
        
        //when
        var result = this.pizzaController.createPizza(dto, model, response);

        //then
        assertEquals("/catalogue/pizza/create", result);
        assertEquals(List.of("Цена не может быть отрицателньой"), model.getAttribute("errors"));
        assertEquals(dto, model.getAttribute("payload"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).createPizza(
                new PizzaCreateDto("Pizza 1", "More more more Mushrooms", -1000.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void createPizza_RequestIsInvalidUniquenessViolation_ReturnsPageWithErros() {
        //given
        var dto = new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Нарущение уникальности")))
                .when(this.pizzaRestClient)
                .createPizza(new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0));

        //when
        var result = this.pizzaController.createPizza(dto, model, response);

        //then
        assertEquals("/catalogue/pizza/create", result);
        assertEquals(List.of("Нарущение уникальности"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(dto, model.getAttribute("payload"));

        verify(this.pizzaRestClient).createPizza(
                new PizzaCreateDto("Pizza 1", "More more more Mushrooms", 1000.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void getAllPizza_ReturnsPizzaListPage() {
        //given
        var filter = "pizza";
        var model = new ConcurrentModel();
        var pizzaList = IntStream.range(1, 4)
                .mapToObj(i -> new PizzaReturnsDto((long) i, "pizza %d".formatted(i),
                        "Состав %d".formatted(i), Double.valueOf(i), null, null))
                .toList();
        
        doReturn(pizzaList).when(this.pizzaRestClient).getAllPizza(filter);

        //when
        var result = this.pizzaController.getAllPizza(model, filter);

        //then
        assertEquals("/catalogue/pizza/list", result);
        assertEquals(pizzaList, model.getAttribute("pizza"));
        assertEquals(filter, model.getAttribute("filter"));

        verify(this.pizzaRestClient).getAllPizza(filter);
        verifyNoMoreInteractions(this.pizzaRestClient);;
    }
    
}
