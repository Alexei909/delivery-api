package com.feedbackservice.feedbackservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import com.feedbackservice.feedbackservice.dtos.FavouritePizzaCreateDto;
import com.feedbackservice.feedbackservice.dtos.FavouritePizzaReturnsDto;
import com.feedbackservice.feedbackservice.services.FavouritePizzaService;

@ExtendWith(MockitoExtension.class)
public class FavouritePizzaControllerTest {

    @Mock
    FavouritePizzaService favouritePizzaService;

    @InjectMocks
    FavouritePizzaController favouritePizzaController;
    
    private final UriComponentsBuilder uriComponentsBuilder = 
            UriComponentsBuilder.fromUriString("http://localhost");

    @Test
    void getAllFavouritePizza_ReturnListFavouritePizzaReturnsDto() {
        //given
        var favouritePizza = IntStream.range(0, 3)
                .mapToObj(i -> new FavouritePizzaReturnsDto((long) i, (long) 1, null, null))
                .toList();
        
        doReturn(favouritePizza)
                .when(this.favouritePizzaService).getAllFavouritePizza();

        //when
        var result = this.favouritePizzaController.getAllFavouritePizza();

        //then
        assertEquals(IntStream.range(0, 3)
                .mapToObj(i -> new FavouritePizzaReturnsDto((long) i, (long) 1, null, null))
                .toList(), result);

        verify(this.favouritePizzaService).getAllFavouritePizza();
        verifyNoMoreInteractions(this.favouritePizzaService);
    }

    @Test 
    void getAllFavouritePizzaByPizzaId_ReturnsListFavouritePizzaReturnsDto() {
        //given
        var favouritePizza = new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null);

        doReturn(List.of(favouritePizza))
                .when(this.favouritePizzaService).getAllFavouritePizzaByPizzaId((long) 1);

        //when
        var result = this.favouritePizzaController.getAllFavouritePizzaByPizzaId((long) 1);

        //then
        assertEquals(List.of(
                new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null)), result);

        verify(this.favouritePizzaService).getAllFavouritePizzaByPizzaId((long) 1);
        verifyNoMoreInteractions(this.favouritePizzaService);
    }

    @Test
    void getFavouritePizzaById_FavouritePizzaIsExsists_ReturnsFavouritePizzaReturnsDto() {
        //given
        var favouritePizza = new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null);

        doReturn(Optional.of(favouritePizza))
                .when(this.favouritePizzaService).getFavouritePizzaById((long) 1);

        //when
        var result = this.favouritePizzaController.getFavouritePizzaById((long) 1);

        //then
        assertEquals(new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null), 
                result);

        verify(this.favouritePizzaService).getFavouritePizzaById((long) 1);
        verifyNoMoreInteractions(this.favouritePizzaService);
    }

    @Test
    void getFavouritePizzaById_FavouritePizzaIsNotExsists_ThrowNoSuchElementException() {
        //given

        //when
        var exception = assertThrows(NoSuchElementException.class, 
                () -> this.favouritePizzaController.getFavouritePizzaById((long) 1));

        //then
        assertEquals("feedback.errors.404.favourite.not_found", exception.getMessage());
        verify(this.favouritePizzaService).getFavouritePizzaById((long) 1);
        verifyNoMoreInteractions(this.favouritePizzaService);
    }

    @Test
    void createFavouritePizza_RequestIsValid_ReturnsFavouritePizzaReturnsDto() throws BindException {
        //given
        var favouritePizzaCreateDto = new FavouritePizzaCreateDto((long) 1);
        var favouritePizzaReturnsDto = new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null);

        var bindingResult = new MapBindingResult(Map.of(), "dto");

        doReturn(favouritePizzaReturnsDto)
                .when(this.favouritePizzaService).createFavouritePizza(
                        new FavouritePizzaCreateDto((long) 1));
        
        //when
        var result = this.favouritePizzaController.createFavouritePizza(favouritePizzaCreateDto, 
                bindingResult, this.uriComponentsBuilder);

        //then
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("http://localhost/api/favourite-pizza/1"), result.getHeaders().getLocation());
        assertEquals(new FavouritePizzaReturnsDto((long) 1, (long) 1, null, null),
                result.getBody());

        verify(this.favouritePizzaService).createFavouritePizza(new FavouritePizzaCreateDto((long) 1));
        verifyNoMoreInteractions(this.favouritePizzaService);
    }

    @Test
    void createFavouritePizza_RequestIsInvalid_ThrowBindException() {
        //given
        var favouritePizzaCreateDto = new FavouritePizzaCreateDto();
        var bindingResult = new MapBindingResult(Map.of(), "dto");
        bindingResult.addError(
                new FieldError("dto", "pizzaId", "feedback.pizza.dto.errors.pizza-id"));

        //when
        var exception = assertThrows(BindException.class, 
                () -> this.favouritePizzaController.createFavouritePizza(favouritePizzaCreateDto, 
                        bindingResult, this.uriComponentsBuilder));
        
        //then
        assertEquals(List.of(
                new FieldError("dto", "pizzaId", "feedback.pizza.dto.errors.pizza-id")), exception.getAllErrors());
        
        verifyNoInteractions(this.favouritePizzaService);
    }
}
