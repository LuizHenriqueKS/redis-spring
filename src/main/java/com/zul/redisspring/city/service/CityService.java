package com.zul.redisspring.city.service;

import java.util.concurrent.TimeUnit;

import com.zul.redisspring.city.client.CityClient;
import com.zul.redisspring.city.dto.City;

import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class CityService {

    @Autowired
    private CityClient cityClient;

    private final RMapCacheReactive<String, City> cityMap;

    public CityService(RedissonReactiveClient client) {
        this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, City.class));
    }

    public Mono<City> getCity(String zipCode) {
        return cityMap.get(zipCode)
                .switchIfEmpty(
                        cityClient.getCity(zipCode)
                                .doFirst(() -> log.info("Consulting city service for {}", zipCode))
                                .flatMap(
                                        city -> cityMap.fastPut(zipCode, city, 10L, TimeUnit.SECONDS)
                                                .thenReturn(city)));
    }

}
