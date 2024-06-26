package com.manager.manager.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;

import com.manager.manager.clients.PizzaRestClient;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.dtos.PizzaUpdateDto;
import com.manager.manager.exceptions.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class PizzaDetailControllerTest {

    @Mock
    PizzaRestClient pizzaRestClient;

    @InjectMocks
    PizzaDetailController pizzaDetailController;

    @Test
    void getPizzaById_PizzaExsists_ReturnsPizzaReturnsDto() {
        //given
        var pizza = new PizzaReturnsDto(
                (long) 1, 
                "Pizza 1", 
                "More more more Mushrooms", 
                1000.0, null, null);

        doReturn(Optional.of(pizza)).when(this.pizzaRestClient).getPizzaById((long) 1);

        //when
        var result = this.pizzaDetailController.getPizzaById((long) 1);

        //then
        assertEquals(pizza, result);

        verify(this.pizzaRestClient).getPizzaById((long) 1);
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void getPizzaById_PizzaNotExsists_ThrowsNoSuchElementException() {
        //given

        //when
        var exception = assertThrows(NoSuchElementException.class, 
                () -> this.pizzaDetailController.getPizzaById((long) 1));

        //then
        assertEquals("catalogue.errors.404.not_found", exception.getMessage());

        verify(this.pizzaRestClient).getPizzaById((long) 1);
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void getPizzaPage_ReturnsPizzaPage() {
        //given

        //when
        var result = this.pizzaDetailController.getPizzaPage();

        //then
        assertEquals("/catalogue/pizza/pizza", result);

        verifyNoInteractions(this.pizzaRestClient);
    }

    @Test
    void deletePizza_PizzaExsists_RedirectToPizzaListPage() {
        //given
        var pizza = new PizzaReturnsDto((long) 1, "Pizza 1", "More more more more", 1000.0, null, null);

        //when
        var result = this.pizzaDetailController.deletePizzaById(pizza);

        //then
        assertEquals("redirect:/catalogue/pizza/list", result);

        verify(this.pizzaRestClient).deletePizzaById((long) 1);
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void getEditPizzaPage_ReturnsEditPizzaPage() {
        //given

        //when
        var result = this.pizzaDetailController.getEditPizzaPage();

        //then
        assertEquals("/catalogue/pizza/edit", result);

        verifyNoInteractions(this.pizzaRestClient);
    }

    @Test
    void editPizza_RequestIsValid_RedirectToPizzaPage() {
        //given
        var pizza = new PizzaReturnsDto(
                (long) 1, 
                "Pizza 1", 
                "More more more mushrooms", 
                1000.0, 
                null, 
                null);
        var dto = new PizzaUpdateDto(
                "Pizza 1 (изменено)",
                "More more more mushrooms (изменено)",
                1500.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        //when
        var result = this.pizzaDetailController.editPizza(pizza, dto, model, response);

        //then
        assertEquals("redirect:/catalogue/pizza/%d".formatted(pizza.getId()), result);
        
        verify(this.pizzaRestClient).editPizza((long) 1, new PizzaUpdateDto(
                "Pizza 1 (изменено)",
                "More more more mushrooms (изменено)",
                1500.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void editPizza_RequestIsInvalidLengthViolation_ReturnsPageWithErrors() {
        //given
        var pizza = new PizzaReturnsDto(
                (long) 1, 
                "Pizza 1", 
                "More more more mushrooms", 
                1000.0, 
                null, 
                null);
        var dto = new PizzaUpdateDto(
                "Piz",
                "More",
                1500.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.pizzaRestClient).editPizza((long) 1, 
                        new PizzaUpdateDto(
                            "Piz",
                            "More",
                            1500.0));

        //when
        var result = this.pizzaDetailController.editPizza(pizza, dto, model, response);

        //then
        assertEquals("/catalogue/pizza/edit", result);
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).editPizza((long) 1, new PizzaUpdateDto(
                "Piz",
                "More",
                1500.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }

    @Test
    void editPizza_RequestIsInvalidPriceNegative_ReturnsPageWithErrors() {
        //given
        var pizza = new PizzaReturnsDto(
                (long) 1,
                "Pizza 1",
                "More more more mushrooms",
                1000.0,
                null,
                null
        );
        var dto = new PizzaUpdateDto(
                "Pizza 1 (изменено)",
                "More more more mushrooms (изменено)",
                -1000.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1")))
                .when(this.pizzaRestClient).editPizza((long) 1, 
                        new PizzaUpdateDto(
                            "Pizza 1 (изменено)",
                            "More more more mushrooms (изменено)",
                            -1000.0));
        
        //when
        var result = this.pizzaDetailController.editPizza(pizza, dto, model, response);

        //then
        assertEquals("/catalogue/pizza/edit", result);
        assertEquals(List.of("Ошибка 1"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).editPizza((long) 1, 
                new PizzaUpdateDto(
                    "Pizza 1 (изменено)",
                    "More more more mushrooms (изменено)",
                    -1000.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }
    
    @Test
    void editPizza_RequestIsInvalidUniquenessViolation_ReturnsPageWithErros() {
        //given
        var pizza = new PizzaReturnsDto(
                (long) 2,
                "Pizza 2",
                "More more more mushrooms",
                1000.0,
                null,
                null);
        var dto = new PizzaUpdateDto(
                "Pizza 1",
                "More more more mushrooms (изменено)",
                1500.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1")))
                .when(this.pizzaRestClient).editPizza((long) 2, 
                        new PizzaUpdateDto(
                            "Pizza 1",
                            "More more more mushrooms (изменено)",
                            1500.0));
        
        //when
        var result = this.pizzaDetailController.editPizza(pizza, dto, model, response);

        //then
        assertEquals("/catalogue/pizza/edit", result);
        assertEquals(List.of("Ошибка 1"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.pizzaRestClient).editPizza((long) 2, 
                new PizzaUpdateDto(
                    "Pizza 1",
                    "More more more mushrooms (изменено)",
                    1500.0));
        verifyNoMoreInteractions(this.pizzaRestClient);
    }
}
