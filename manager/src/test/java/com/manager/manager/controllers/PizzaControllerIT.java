package com.manager.manager.controllers;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.manager.manager.dtos.PizzaReturnsDto;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WireMockTest(httpPort = 54321)
public class PizzaControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void getAllPizza_ReturnsPizzaListPage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/pizza/list")
                .queryParam("filter", "pizza");

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/catalogue-api/pizza"))
                .withQueryParam("filter", WireMock.equalTo("pizza"))
                .willReturn(WireMock.ok("""
                        [
                            {"id": 1, "name": "Pizza 1", "composition": "More more more mushrooms", 
                                    "price": 1000.0, "createdAt": null, "updatedAt": null},
                            {"id": 2, "name": "Pizza 2", "composition": "More more more mushrooms", 
                                    "price": 1000.0, "createdAt": null, "updatedAt": null}
                        ]""").withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.view().name("/catalogue/pizza/list"),
                        MockMvcResultMatchers.model().attribute("filter", "pizza"),
                        MockMvcResultMatchers.model().attribute("pizza", List.of(
                                        new PizzaReturnsDto(
                                            (long) 1,
                                            "Pizza 1",
                                            "More more more mushrooms",
                                            1000.0,
                                            null,
                                            null
                                        ), new PizzaReturnsDto(
                                            (long) 2,
                                            "Pizza 2",
                                            "More more more mushrooms",
                                            1000.0,
                                            null,
                                            null))));
        
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathMatching("/catalogue-api/pizza"))
                .withQueryParam("filter", WireMock.equalTo("pizza")));
    }

    @Test
    @WithMockUser(username = "manager")
    void getAllPizza_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/pizza/list")
                .queryParam("filter", "pizza");
        
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                    MockMvcResultMatchers.status().isForbidden()
                );
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void getPizzaCreatePage_ReturnsPizzaCreatePage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/pizza/create");

        //when  
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.view().name("/catalogue/pizza/create")
                );
    }

    @Test
    @WithMockUser(username = "manager")
    void getPizzaCreatePage_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/pizza/create");

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isForbidden()   
                );
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void createPizza_RequestIsValid_RedirectToPizzaPage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue/pizza/create")
                .param("name", "Pizza 1")
                .param("composition", "More more more mushrooms")
                .param("price", "1000")
                .locale(Locale.ROOT)
                .with(csrf());

        WireMock.stubFor(WireMock.post(WireMock.urlPathMatching("/catalogue-api/pizza"))
                .withRequestBody(WireMock.equalToJson("""
                        {
                            "name": "Pizza 1",
                            "composition": "More more more Mushrooms",
                            "price": 1000
                        }"""))
                .willReturn(WireMock.created()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "Pizza 1",
                                    "composition": "More more more mushrooms",
                                    "price": 1000.0
                                }""")));
        
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andExpectAll(
                        MockMvcResultMatchers.status().is3xxRedirection(),
                        MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/catalogue/pizza/1")
                );
        
        WireMock.verify(WireMock.postRequestedFor(WireMock.urlPathMatching("/catalogue-api/pizza"))
                .withRequestBody(WireMock.equalToJson("""
                    {
                        "name": "Pizza 1",
                        "composition": "More more more Mushrooms",
                        "price": 1000
                    }""")));
    }
}
