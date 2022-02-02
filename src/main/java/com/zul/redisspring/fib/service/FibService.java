package com.zul.redisspring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FibService {

    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index, String name) {
        log.info("Calculating fibonacci number for index: {} - {}", index, name);
        return this.fib(index);
    }

    @Cacheable(value = "math:fib")
    public int getFib(int index) {
        log.info("Calculating fibonacci number for index: {}", index);
        return this.fib(index);
    }

    @CacheEvict("math:fib")
    public void clearCache(int index) {
        // log.info("Clearing fibonacci cache for index: {}", index);
    }

    @Scheduled(fixedRate = 10000)
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache() {
        // log.info("Clearing fibonacci cache for all entries");
    }

    // intentional 2^N
    private int fib(int index) {
        if (index < 2) {
            return index;
        }
        return fib(index - 1) + fib(index - 2);
    }

}
