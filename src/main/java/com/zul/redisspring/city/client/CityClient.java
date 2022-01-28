package com.zul.redisspring.city.client;

import com.zul.redisspring.city.dto.City;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class CityClient {

    private final WebClient webClient;

    public CityClient(@Value("${city.service.url}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public Mono<City> getCity(final String zipCode) {
        return this.webClient.get()
                .uri("{zipCode}", zipCode)
                .retrieve()
                .bodyToMono(City.class);
    }

}
