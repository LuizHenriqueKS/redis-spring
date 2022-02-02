package com.zul.redisspring.weather.service;

import java.util.stream.IntStream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class WeatherService {

    private final ExternalServiceClient client;

    @Cacheable(value = "weather", key = "#zip")
    public int getInfo(int zip) {
        return 0;
    }

    @Scheduled(fixedRate = 10000)
    public void update() {
        // log.info("Updating weather info");
        IntStream.rangeClosed(1, 5)
                .forEach(this.client::getWeatherInfo);
    }

}
