package com.learnjava.service;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

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
                    boolean isPriceValid = priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isPriceValid);
                    return cartItem;
                })
                .filter(cartItem -> cartItem.isExpired())
                .collect(Collectors.toList());
        timeTaken();

        if (invalidPriceList.size() > 0)
            return new CheckoutResponse(CheckoutStatus.FAILURE, invalidPriceList);
        else
            return new CheckoutResponse(CheckoutStatus.SUCCESS);
    }

}
