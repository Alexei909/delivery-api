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
public class PizzaReviewsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void getAllReviews_ReturnsListPizzaReviewsReturnsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/reviews");

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
                                        "review": "Review 1",
                                        "rating": 5,
                                        "pizzaId": 1
                                    },{
                                        "id": 2,
                                        "review": "Review 2",
                                        "rating": 5,
                                        "pizzaId": 2
                                    },{
                                        "id": 3,
                                        "review": "Review 3",
                                        "rating": 5,
                                        "pizzaId": 1
                                    }]"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void getAllReviewsByPizzaId_ReturnsListPizzaReviewsReturnsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/reviews/by-pizza-id/1");

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
                                            "review": "Review 1",
                                            "rating": 5,
                                            "pizzaId": 1
                                        },{
                                            "id": 3,
                                            "review": "Review 3",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }]"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void getReviewById_ReviewIsExsists_ReturnsPizzaReviewsReturnsDto() throws Exception {
        //given 
        var requestBuilder = MockMvcRequestBuilders.get("/api/reviews/1");

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
                                            "id": 1,
                                            "review": "Review 1",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }"""));
    }

    @Test
    void getReviewById_ReviewIsNotExsists_ReturnsProblemDetails() throws Exception {
        //given 
        var requestBuilder = MockMvcRequestBuilders.get("/api/reviews/1");

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
                                            "detail": "Отзыв не найден"
                                        }"""));
    }

    @Test
    void createReview_RequestIsValid_ReturnsPizzaReviewsReturnsDto() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "review": "Review 1",
                            "rating": 1,
                            "pizzaId": 1
                        }""");
                        
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header()
                                .string(HttpHeaders.LOCATION, "http://localhost/api/reviews/1"),
                        MockMvcResultMatchers.content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "id": 1,
                                            "review": "Review 1",
                                            "rating": 1,
                                            "pizzaId": 1
                                        }
                                        """));
    }

    @Test
    void createReview_RequestIsInvalid_ReturnsProblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "rating": 0
                        }""")
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
                                                "Идентификатор пиццы не может быть пустым",
                                                "Оценка не может быть меньше 1"
                                        ]}"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void updateReview_RequestIsVAlidAndReviewIsExsists_ReturnsStatusOk() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "review": "Review (new)",
                            "rating": 5
                        }""")
                .locale(Locale.ROOT);;
        
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk()
                );
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/1"))
                .andExpectAll(
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "id": 1,
                                            "review": "Review (new)",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void updateReview_RequestIsInValidAndPizzaIsExsists_ReturnsPromblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {   
                            "review": "",
                            "rating": 6
                        }""")
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
                                                "Оценка не может быть больше 5"
                                        ]}"""));
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/1"))
                .andExpectAll(
                        MockMvcResultMatchers.content()
                                .json("""
                                        {
                                            "id": 1,
                                            "review": "Review 1",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }"""));
    }

    @Test
    void updateReview_RequestIsValidAndReviewIsNotExsists_ReturnsPromblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "review": "Review (new)",
                            "rating": 5
                        }""")
                .locale(Locale.ROOT);
        
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
                                            "detail": "Отзыв не найден"
                                        }"""));
    }

    @Test
    void updateReview_RequestIsInvalidAndReviewIsNotExsists_ReturnsProblemDetails() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {   
                            "review": "",
                            "rating": 6
                        }""")
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
                                                "Оценка не может быть больше 5"
                                        ]}"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void deleteReview_ReviewIsExsists_ReturnsStatusOk() throws Exception {
        //given 
        var requestBuilder = MockMvcRequestBuilders.delete("/api/reviews/1");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews"))
                .andExpectAll(
                        MockMvcResultMatchers.content()
                                .json("""
                                        [{
                                            "id": 2,
                                            "review": "Review 2",
                                            "rating": 5,
                                            "pizzaId": 2
                                        },{
                                            "id": 3,
                                            "review": "Review 3",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }]"""));
    }

    @Test
    @Sql({
        "../resources/sql/fill_t_pizza_reviews.sql"
    })
    void deleteReview_ReviewIsNotExsists_ReturnsStatusOk() throws Exception {
        //given 
        var requestBuilder = MockMvcRequestBuilders.delete("/api/reviews/4");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews"))
                .andExpectAll(
                        MockMvcResultMatchers.content()
                                .json("""
                                        [{
                                            "id": 1,
                                            "review": "Review 1",
                                            "rating": 5,
                                            "pizzaId": 1
                                        },{
                                            "id": 2,
                                            "review": "Review 2",
                                            "rating": 5,
                                            "pizzaId": 2
                                        },{
                                            "id": 3,
                                            "review": "Review 3",
                                            "rating": 5,
                                            "pizzaId": 1
                                        }]"""));
    }
}
