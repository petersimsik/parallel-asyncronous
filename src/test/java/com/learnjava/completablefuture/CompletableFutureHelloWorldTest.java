package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    @Test
    void helloWorld() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        CompletableFuture<String> completableFuture = helloWorldFuture.helloWorld();

        //then
        completableFuture
                .thenAccept(result -> assertEquals("HELLO WORLD", result))
                .join();
    }

    @Test
    void helloWorldWithSize() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        CompletableFuture<String> completableFuture = helloWorldFuture.helloWorldWithSize();

        //then
        completableFuture
                .thenAccept(result -> assertEquals("11 - HELLO WORLD", result))
                .join();

        completableFuture
                .thenAccept(result -> assertNotEquals("11x - HELLO WORLD", result))
                .join();
    }

    @Test
    void helloWorldMultipleAsyncCalls() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        String result = helloWorldFuture.helloWorldMultipleAsyncCalls();

        //then
        assertEquals("HELLO WORLD!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        String result = helloWorldFuture.helloWorldThreeAsyncCalls();

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE!", result);
        assertNotEquals("HELLO aWORLD! HI COMPLETABLEFUTURE!", result);
    }

    @Test
    void helloWorld4AsyncCalls() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        String result = helloWorldFuture.helloWorld4AsyncCalls();

        //then
        assertEquals("HELLO WORLD! HI COMPLETABLEFUTURE! BYE!", result);
        assertNotEquals("HELLO aWORLD! HI COMPLETABLEFUTURE! BYE!", result);
    }

    @Test
    void helloWorldThenCompose() {
        //given
        CompletableFutureHelloWorld helloWorldFuture = new CompletableFutureHelloWorld(new HelloWorldService());

        //when
        startTimer();
        CompletableFuture<String> completableFuture = helloWorldFuture.helloWorldThenCompose();

        //then
        completableFuture
                .thenAccept(result -> assertEquals("hello world!", result))
                .join();
        timeTaken();
    }
}