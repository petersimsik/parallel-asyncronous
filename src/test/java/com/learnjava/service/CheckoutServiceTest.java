package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.service.CheckoutService;
import com.learnjava.service.PriceValidatorService;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    PriceValidatorService priceValidatorService = new PriceValidatorService();
    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @BeforeAll
    static void getNoOfCores(){
        System.out.println("Number of cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Parallelism: " + ForkJoinPool.getCommonPoolParallelism());
    }

    @Test
    void checkOut6Items() {
        //given
        Cart cart = DataSet.createCart(6);
        //when
        CheckoutResponse checkoutResponse = checkoutService.checkOut(cart);
        //then
        assertNotNull(checkoutResponse, "Checkout respose is null");
        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus(), "Checkout status needs to be success");
        assertTrue(checkoutResponse.getFinalRate() > 0, "Final rate needs to be greater than zero.");
    }

    @Test
    void checkout13Items() {
        //given
        Cart cart = DataSet.createCart(13);
        //when
        CheckoutResponse checkoutResponse = checkoutService.checkOut(cart);
        //then
        assertNotNull(checkoutResponse, "Checkout respose is null");
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus(), "Checkout status needs to be failure");
    }

    @Test
    @DisplayName("checking performance with 25 items that means definitely we will have failure items")
    void checkout25Items() {
        //given
        Cart cart = DataSet.createCart(25);
        //when
        CheckoutResponse checkoutResponse = checkoutService.checkOut(cart);
        //then
        assertNotNull(checkoutResponse, "Checkout respose is null");
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus(), "Checkout status needs to be failure");
    }

    @Test
    @DisplayName("Modify parallelism")
    void modifyParallelism() {
        //given
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "50");
        Cart cart = DataSet.createCart(100);
        //when
        CheckoutResponse checkoutResponse = checkoutService.checkOut(cart);
        //then
        assertNotNull(checkoutResponse, "Checkout respose is null");
        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus(), "Checkout status needs to be failure");
    }
}