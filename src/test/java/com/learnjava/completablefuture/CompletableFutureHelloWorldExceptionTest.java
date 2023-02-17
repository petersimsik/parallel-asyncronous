package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @Mock
    HelloWorldService helloWorldService = mock(HelloWorldService.class);

    @InjectMocks
    CompletableFutureHelloWorldException completableFutureHelloWorldException;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void helloWorldThreeAsyncCalls(boolean isExceptionThrown) {
        //given
        if (isExceptionThrown)
            when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occured"));
        else
            when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();
        //when
        String result = completableFutureHelloWorldException.helloWorldThreeAsyncCallsHandle();

        //then
        assertNotNull(result);
        if (isExceptionThrown)
            assertTrue(" WORLD! HI COMPLETABLEFUTURE!".equals(result));
        else
            assertTrue("HELLO WORLD! HI COMPLETABLEFUTURE!".equals(result));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void helloWorldThreeAsyncCallsExceptionally(boolean isExceptionThrown) {
        //given
        if (isExceptionThrown)
            when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception occured"));
        else
            when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        //when
        String result = completableFutureHelloWorldException.helloWorldThreeAsyncCallsExceptionally();

        //then
        if (isExceptionThrown)
            assertTrue(" WORLD! HI COMPLETABLEFUTURE!".equals(result));
        else
            assertTrue("HELLO WORLD! HI COMPLETABLEFUTURE!".equals(result));
    }
}