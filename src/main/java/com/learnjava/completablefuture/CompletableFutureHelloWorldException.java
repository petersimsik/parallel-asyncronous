package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {
    private HelloWorldService helloWorldService;

    public CompletableFutureHelloWorldException(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public String helloWorldThreeAsyncCallsHandle(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            log("inside hi");
            delay(100);
            return " Hi CompletableFuture!";
        });

        String result = hello
                .handle((helloResult, exception) -> {
                    if (exception != null){
                        log("Exception is: " + exception);
                        return "";
                    } else return helloResult;
                })
                .thenCombine(world, (previous, current) -> previous + current)
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return result;
    }

    public String helloWorldThreeAsyncCallsExceptionally(){
        startTimer();

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            log("inside hi");
            delay(100);
            return " Hi CompletableFuture!";
        });

        String result = hello
                .exceptionally(exception -> {
                    log("Exception is: " + exception);
                    return "";
                })
                .thenCombine(world, (previous, current) -> previous + current)
                .thenCombine(hi, (previous, current) -> previous + current)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return result;
    }
}

