package com.manager.manager.config;

import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

import com.manager.manager.clients.BadRequestExceptionHandler;
import com.manager.manager.clients.RestClientPizzaRestClient;

@Configuration
public class TestBeans {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return mock(OAuth2AuthorizedClientRepository.class);
    }

   @Bean
   @Primary
    public RestClientPizzaRestClient testRestClientPizzaRestClient(
        @Value("${managerapp.services.catalogue.uri:http://localhost:54321}") String baseUri,
        BadRequestExceptionHandler badRequestExceptionHandler
    ) {
        return new RestClientPizzaRestClient(RestClient.builder()
                .baseUrl(baseUri)
                .requestFactory(new JdkClientHttpRequestFactory())
                .build(), 
                badRequestExceptionHandler);
    }   

    @Bean
    @Primary
    public BadRequestExceptionHandler testBadRequestException() {
        return mock(BadRequestExceptionHandler.class);
    }
}
