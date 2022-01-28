package com.zul.redisspring;

import org.junit.jupiter.api.RepeatedTest;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class RedisSpringApplicationTests {

    @Autowired
    private ReactiveStringRedisTemplate template;

    @Autowired
    private RedissonReactiveClient client;

    @RepeatedTest(3)
    void springDataRedisTest() {
        ReactiveValueOperations<String, String> valueOperations = template.opsForValue();
        long before = System.currentTimeMillis();
        valueOperations.set("key", "0").block();
        Mono<Void> mono = Flux.range(1, 500000)
                .flatMap(i -> valueOperations.increment("user:1:visit")) // incr
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    @RepeatedTest(3)
    void redissonTest() {
        RAtomicLongReactive atomicLong = client.getAtomicLong("user:2:visit");
        long before = System.currentTimeMillis();
        atomicLong.set(0).block();
        Mono<Void> mono = Flux.range(1, 500000)
                .flatMap(i -> atomicLong.incrementAndGet()) // incr
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

}
