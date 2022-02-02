package com.zul.redisspring.chat.service;

import java.net.URI;

import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatRoomService implements WebSocketHandler {

    @Autowired
    private RedissonReactiveClient client;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String room = getChatRoomName(session);
        RTopicReactive topic = this.client.getTopic("chat:room:" + room, StringCodec.INSTANCE);
        RListReactive<String> history = this.client.getList("chat:room:" + room + ":history", StringCodec.INSTANCE);

        // subscribe
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(msg -> System.out.println("Subscribe " + msg))
                .flatMap(msg -> history.add(msg).thenReturn(msg))
                .flatMap(topic::publish)
                .doOnError(System.out::println)
                .doFinally(e -> System.out.println("Subscriber finally " + e))
                .subscribe();
        // publisher
        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
                .doOnNext(msg -> System.out.println("Publisher " + msg))
                .startWith(history.iterator())
                .map(session::textMessage)
                .doOnError(System.out::println)
                .doFinally(e -> System.out.println("Publisher finally " + e));
        return session.send(flux);
    }

    private String getChatRoomName(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room", "default");
    }

}
