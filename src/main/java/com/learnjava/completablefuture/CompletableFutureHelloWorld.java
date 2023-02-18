package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private HelloWorldService helloWorldService;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public CompletableFuture<String> helloWorldWithSize(){
        return CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld())
                .thenApply(String::toUpperCase)
                .thenApply(result -> result.length() + " - " + result);
    }

    public CompletableFuture<String> helloWorld(){

        return CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld())
                .thenApply(result -> result.toUpperCase());
    }

    public String helloWorldMultipleAsyncCalls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());

        String result = hello.thenCombine(world, (s1, s2) -> s1 + s2)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return result;
    }

    public String helloWorldThreeAsyncCalls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            log("inside hi");
            delay(100);
            return " Hi CompletableFuture!";
        });

        String result = hello
                .thenCombine(world, (previous, current) -> previous + current)
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return result;
    }

    public String  helloWorld4AsyncCalls(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            log("inside hi");
            delay(1000);
            return " Hi CompletableFuture!";
        });
        CompletableFuture<String> bye = CompletableFuture.supplyAsync(() -> {
            log("inside bye");
            delay(1000);
            return " Bye!";
        });

        String result = hello
                .thenCombine(world, (previous, current) -> previous + current)
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenCombine(bye, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return result;
    }

    public CompletableFuture<String> helloWorldThenCompose(){

        return CompletableFuture.supplyAsync(() -> helloWorldService.hello())
                .thenCompose((previousResult) -> helloWorldService.worldFuture(previousResult));

    }

    public String helloWorldThreeAsyncCallsCustomPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world(), executorService);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            log("inside hi");
            delay(100);
            return " Hi CompletableFuture!";
        }, executorService);

        String result = hello
                .thenCombine(world, (previous, current) -> {
                    log("inside thenCombine1");
                    return previous + current;
                })
                .thenCombine(hi, (previous, current) -> {
                    log("inside thenCombine2");
                    return previous + current;
                })
                .thenApply(s -> {
                    log("inside thenApply");
                    return s.toUpperCase();
                })
                .join();

        timeTaken();

        return result;
    }
}
