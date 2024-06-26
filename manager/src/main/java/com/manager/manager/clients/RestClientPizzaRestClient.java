package com.manager.manager.clients;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.manager.manager.dtos.PizzaCreateDto;
import com.manager.manager.dtos.PizzaReturnsDto;
import com.manager.manager.dtos.PizzaUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestClientPizzaRestClient implements PizzaRestClient {

    private static final ParameterizedTypeReference<List<PizzaReturnsDto>> TYPE_REFERENCE = 
            new ParameterizedTypeReference<List<PizzaReturnsDto>>() {};

    private final RestClient restClient;

    private final BadRequestExceptionHandler badRequestExceptionHandler;

    @Override
    public List<PizzaReturnsDto> getAllPizza(String filter) {
        return this.restClient.get()
                .uri("/catalogue-api/pizza?filter={filter}", filter)
                .retrieve()
                .body(TYPE_REFERENCE);
    }

    @Override
    public PizzaReturnsDto createPizza(PizzaCreateDto dto) {
        try {
            return this.restClient.post()
                    .uri("/catalogue-api/pizza")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .body(PizzaReturnsDto.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw this.badRequestExceptionHandler.getException(problemDetail);
        }
    }

    @Override
    public Optional<PizzaReturnsDto> getPizzaById(Long pizzaId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                .uri("/catalogue-api/pizza/{pizzaId}", pizzaId)
                .retrieve()
                .body(PizzaReturnsDto.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void deletePizzaById(Long pizzaId) {
        try {
            this.restClient.delete()
                .uri("/catalogue-api/pizza/{pizzaId}", pizzaId)
                .retrieve()
                .toBodilessEntity();
        } catch (NoSuchElementException exception) {
            throw new NoSuchElementException(exception);
        }
    }

    @Override
    public void editPizza(Long pizzaId, PizzaUpdateDto dto) {
        try {
            this.restClient.put()
                    .uri("/catalogue-api/pizza/{pizzaId}", pizzaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw this.badRequestExceptionHandler.getException(problemDetail);
        }
    }
}
