package com.zul.redisspring.weather.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import io.netty.util.internal.ThreadLocalRandom;

@Component
public class ExternalServiceClient {

    @CachePut(value = "weather", key = "#zip")
    public int getWeatherInfo(int zip) {
        return ThreadLocalRandom.current().nextInt(60, 100);
    }

}
