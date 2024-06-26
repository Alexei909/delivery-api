package com.feedbackservice.feedbackservice.controllers;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = true)
public class FavouritePizzaControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql({
        "../resources/sql/fill_t_favourite_pizza.sql"
    })
    void getAllFavouritePizza_ReturnsListfavouritePizzaReturnsDto() throws Exception {
        //given 
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-pizza");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        [{
                                            "id": 1,
                                            "pizzaId": 1
                                        },{
                                            "id": 2,
                                            "pizzaId": 1
                                        },{
                                            "id": 3,
                                            "pizzaId": 2
                                        }]"""));
    }

    @Test
    @Sql("../resources/sql/fill_t_favourite_pizza.sql")
    void getAllFavouritePizzaByPizzaId_ReturnsListFavouritePizzaReturnsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-pizza/by-pizza-id/1");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        [{
                                            "id": 1,
                                            "pizzaId": 1
                                        },{
                                            "id": 2,
                                            "pizzaId": 1
                                        }]"""));
    }

    @Test
    @Sql("../resources/sql/fill_t_favourite_pizza.sql")
    void getFavouritePizzaById_FavouritePizzaIsExsists_ReturnsFavouritePizzaReturnsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-pizza/2");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "id": 2,
                                            "pizzaId": 1
                                        }"""));
    }

    @Test
    void getFavouritePizzaById_FavouritePizzaIsNotExsists_ReturnsProblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-pizza/1");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "title": "Not Found",
                                            "status": 404,
                                            "detail": "Избранная пицца не найдена"
                                        }"""));
    }

    @Test
    void createFavouritePizza_RequestIsValid_ReturnsFavouritePizzaRetunsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/favourite-pizza")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "pizzaId": 1
                        }""")
                .locale(Locale.ROOT);

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header()
                                .string(HttpHeaders.LOCATION, "http://localhost/api/favourite-pizza/1"),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "id": 1,
                                            "pizzaId": 1
                                        }"""));
    }

    @Test
    void createFavouritePizza_RequestIsInvalidPizzaIdIsNull_ReturnsProblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/favourite-pizza")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {}
                        """)
                .locale(Locale.ROOT);

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "errors": [
                                                "Идентификатор пиццы не может быть пустым"
                                            ]
                                        }"""));
    }
    
    @Test
    void createFavouritePizza_RequestIsInvalidPizzaIdLessOne_ReturnsProblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/favourite-pizza")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"pizzaId": 0}
                        """)
                .locale(Locale.ROOT);

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "errors": [
                                                "Идентификатор пиццы не может быть меньше 1"
                                            ]
                                        }"""));
    }

    @Test
    @Sql("../resources/sql/fill_t_favourite_pizza.sql")
    void deleteFavouritePizza_FavouritePizzaIsExsists_ReturnsStatusOk() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.delete("/api/favourite-pizza/1");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/favourite-pizza/1"))
                .andExpectAll(
                        MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "title": "Not Found",
                                            "status": 404,
                                            "detail": "Избранная пицца не найдена"
                                        }"""));
    }

    @Test
    @Sql("../resources/sql/fill_t_favourite_pizza.sql")
    void deleteFavouritePizza_FavouritePizzaIsNotExsists_ReturnsStatusOk() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.delete("/api/favourite-pizza/4");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk());
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/favourite-pizza"))
                .andExpectAll(
                        MockMvcResultMatchers.content()
                                .json("""
                                        [{
                                            "id": 1,
                                            "pizzaId": 1
                                        },{
                                            "id": 2,
                                            "pizzaId": 1
                                        },{
                                            "id": 3,
                                            "pizzaId": 2
                                        }]"""));
    }
}
