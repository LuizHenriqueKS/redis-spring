package com.zul.redisspring.weather.controller;

import com.zul.redisspring.weather.service.WeatherService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("{zip}")
    public Mono<Integer> getInfo(@PathVariable int zip) {
        return Mono.fromSupplier(() -> weatherService.getInfo(zip));
    }

}
