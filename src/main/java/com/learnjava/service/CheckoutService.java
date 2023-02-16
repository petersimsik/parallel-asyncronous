package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import static java.util.stream.Collectors.summingDouble;

public class CheckoutService {

    private PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkOut(Cart cart){
        startTimer();
        List<CartItem> invalidPriceList = cart.getCartItemList()
                .parallelStream()
                .map(cartItem -> {
                    log("processing item: " + cartItem);
                    boolean isPriceValid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceValid);
                    return cartItem;
                })
                .filter(cartItem -> cartItem.isExpired())
                .collect(Collectors.toList());
        timeTaken();

        if (invalidPriceList.size() > 0)
            return new CheckoutResponse(CheckoutStatus.FAILURE, invalidPriceList);

        //double finalPrice = calculateFinalPriceReduce(cart);
        double finalPrice = calculateFinalPriceUsingReduce(cart);
        log("Final price is: " + finalPrice);
        return new CheckoutResponse(CheckoutStatus.SUCCESS, finalPrice);
    }

    private double calculateFinalPrice(Cart cart) {
        return cart.getCartItemList()
                .parallelStream()
                .map(item -> item.getQuantity() * item.getRate())
                .mapToDouble(item -> item.doubleValue())
                .sum();
    }

    private double calculateFinalPriceUsingReduce(Cart cart) {
        return cart.getCartItemList()
                .parallelStream()
                .map(item -> item.getQuantity() * item.getRate())
                .reduce(0d, (x,y) -> x + y);
    }

}
