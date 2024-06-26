package com.manager.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

import com.manager.manager.clients.BadRequestExceptionHandler;
import com.manager.manager.clients.RestClientPizzaRestClient;
import com.manager.manager.security.Oauth2ClientHttpRequestInterceptor;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientPizzaRestClient pizzaRestClient(
        @Value("${managerapp.services.catalogue.uri:http://localhost:8080}") String baseUri,
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository,
        BadRequestExceptionHandler badRequestExceptionHandler,
        @Value("${managerapp.services.catalogue.registration-id:keycloak}") String registrationId,
        LoadBalancerClient loadBalancerClient
    ) {
        return new RestClientPizzaRestClient(RestClient.builder()
                .baseUrl(baseUri)
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                .requestInterceptor(new Oauth2ClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                            authorizedClientRepository), registrationId))
                .build(), 
                badRequestExceptionHandler);
    }

    @Bean
    public BadRequestExceptionHandler badRequestExceptionHandler() {
        return new BadRequestExceptionHandler();
    }
}
